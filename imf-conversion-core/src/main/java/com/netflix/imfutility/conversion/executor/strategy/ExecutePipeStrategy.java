package com.netflix.imfutility.conversion.executor.strategy;

import com.netflix.imfutility.conversion.executor.ExecutionException;
import com.netflix.imfutility.conversion.executor.ExternalProcess;
import com.netflix.imfutility.conversion.executor.OutputRedirect;
import com.netflix.imfutility.conversion.executor.ProcessStarter;
import com.netflix.imfutility.conversion.templateParameter.context.TemplateParameterContextProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Execute all operations in a pipeline.
 * <p>
 * Example of the input {@link PipeOperationInfo}:
 * <ul>
 * <li>cycleOperations: {{cycle11, cycle12, cycle13}, {cycle21, cycle22}},</li>
 * <li>pipeOperations: {pipe1, pipe2, pipe3}.</li>
 * <li>Execution order:
 * <ol>
 * <li>start pipe1, pipe2, pipe3;</li>
 * <li>start cycle11, cycle12, cycle13</li>
 * <li>create a pipeline:  cycle11 -> cycle12 -> cycle13 -> pipe1 -> pipe2 -> pipe3</li>
 * <li>wait until the first operation (cycle11) is finished</li>
 * <li>finish cycle 12 and cycle 13 (pipe1, pipe2 and pipe3 are still running)</li>
 * <li>start cycle21, cycle22</li>
 * <li>create a pipeline:  cycle21 -> cycle22 -> pipe1 -> pipe2 -> pipe3</li>
 * <li>wait until the first operation (cycle21) is finished</li>
 * <li>finish cycle 22</li>
 * <li>finish pipe1, pipe2 and pipe3</li>
 * </ol>
 * </li>
 * </ul>
 * </p>
 */
public class ExecutePipeStrategy extends AbstractExecuteStrategy {

    public ExecutePipeStrategy(TemplateParameterContextProvider contextProvider, ProcessStarter processStarter) {
        super(contextProvider, processStarter);
    }

    public void execute(PipeOperationInfo operations) throws IOException {
        List<ExternalProcess> tailProcesses = new ArrayList<>();

        try {
            // 1. start all tailing operation
            startTailProcesses(operations, tailProcesses);

            // 2. start all cycle processes in a sequence subsequently in pipelines
            if (operations.getCycleOperations().isEmpty()) {
                processNonCycle(tailProcesses);
            } else {
                processCycle(operations, tailProcesses);
            }
        } finally {
            // 3. close all tail processes.
            tailProcesses.forEach(ExternalProcess::finishClose);
        }
    }

    private void startTailProcesses(PipeOperationInfo operations, List<ExternalProcess> tailProcesses) throws IOException {
        int i = 0;
        for (OperationInfo tailOperation : operations.getTailOperations()) {
            // output can be safely re-directed to stderr's log, if it's the last element in the pipeline.
            // otherwise output must not be re-directed, as it goes to the next element in the pipeline.
            OutputRedirect outputRedirect = i < operations.getTailOperations().size() - 1 ? OutputRedirect.PIPE : OutputRedirect.ERR_LOG;
            i++;
            tailProcesses.add(startProcess(tailOperation, outputRedirect));
        }
    }

    private void processNonCycle(List<ExternalProcess> pipeline) {
        pipe(pipeline);
    }

    private void processCycle(PipeOperationInfo operations, List<ExternalProcess> tailProcesses) throws IOException {
        for (List<OperationInfo> cyclePipeOperations : operations.getCycleOperations()) {
            processCyclePipe(cyclePipeOperations, tailProcesses);
        }
    }

    private void processCyclePipe(List<OperationInfo> cyclePipeOperations, List<ExternalProcess> tailProcesses) throws IOException {
        List<ExternalProcess> headProcesses = new ArrayList<>();
        try {
            for (OperationInfo headOperation : cyclePipeOperations) {
                // output must not be re-directed, as it goes to the next element in the pipeline.
                headProcesses.add(startProcess(headOperation, OutputRedirect.PIPE));
            }
            pipe(headProcesses, tailProcesses);
        } finally {
            headProcesses.forEach(ExternalProcess::finishClose);
        }
    }

    private void pipe(List<ExternalProcess> head, List<ExternalProcess> tail) {
        List<ExternalProcess> pipeline = new ArrayList<>(head);
        pipeline.addAll(tail);
        pipe(pipeline);
    }

    private void pipe(ExternalProcess firstProc, List<ExternalProcess> tail) {
        List<ExternalProcess> pipeline = new ArrayList<>();
        pipeline.add(firstProc);
        pipeline.addAll(tail);
        pipe(pipeline);
    }

    private void pipe(List<ExternalProcess> pipeline) {
        // 1. start a new thread to copy input - output in a pipeline
        ExternalProcess p1;
        ExternalProcess p2;
        for (int i = 0; i < pipeline.size(); i++) {
            p1 = pipeline.get(i);
            if (i + 1 < pipeline.size()) {
                p2 = pipeline.get(i + 1);
                new Thread(new Piper(p1, p2)).start();
            }
        }

        // 2. Wait for the first process in chain
        ExternalProcess firstProcess = pipeline.get(0);
        firstProcess.finishWaitFor();
    }

    private static class Piper implements Runnable {

        private static final int BUF_SIZE = 512;

        private final ExternalProcess inputProcess;
        private final ExternalProcess outputProcess;

        public Piper(ExternalProcess inputProcess, ExternalProcess outputProcess) {
            this.inputProcess = inputProcess;
            this.outputProcess = outputProcess;
        }

        public void run() {
            // don't use buffered streams!
            InputStream input = inputProcess.getProcess().getInputStream(); //new BufferedInputStream(input);
            OutputStream output = outputProcess.getProcess().getOutputStream(); //new BufferedOutputStream(output);
            try {
                byte[] b = new byte[BUF_SIZE];
                int read = 1;
                while (read > -1) {
                    read = input.read(b, 0, b.length);
                    if (read > -1) {
                        output.write(b, 0, read);
                    }
                }
            } catch (IOException e) {
                throw new ExecutionException(
                        String.format("Broken pipe. Input process: %s. Output Process: %s", inputProcess.toString(), outputProcess.toString()),
                        e);
            }
        }

    }
}