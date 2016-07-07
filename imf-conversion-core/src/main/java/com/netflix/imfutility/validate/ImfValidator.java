/**
 * Copyright (C) 2016 Netflix, Inc.
 *
 *     This file is part of IMF Conversion Utility.
 *
 *     IMF Conversion Utility is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     IMF Conversion Utility is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with IMF Conversion Utility.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.netflix.imfutility.validate;

import com.netflix.imfutility.ConversionException;
import com.netflix.imfutility.CoreConstants;
import com.netflix.imfutility.conversion.executor.strategy.ExecuteStrategyFactory;
import com.netflix.imfutility.conversion.executor.strategy.OperationInfo;
import com.netflix.imfutility.conversion.templateParameter.ContextInfo;
import com.netflix.imfutility.conversion.templateParameter.context.TemplateParameterContextProvider;
import com.netflix.imfutility.conversion.templateParameter.context.parameters.DynamicContextParameters;
import com.netflix.imfutility.generated.conversion.ImfValidationType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Performs validation of the input IMF package (IMP) and CPL.
 * <ul>
 * <li>Validation is performed by a separate external tool.</li>
 * <li>The validation external tool command is specified in conversion.xml</li>
 * <li>By default, validation is done by a wrapper on Netflix Photon library.</li>
 * <li>It's possible to set a custom validation tool using config.xml.</li>
 * <li>The validation tool expect IMP, CPL, working dir and output XML file name parameters.</li>
 * <li>The result of the validation is stored in the specified output XML file.</li>
 * <li>The current class executes the validation commands and parses the output XML.</li>
 * <li>If there are validation exceptions - it throws {@link ImfValidationException} containing all found errors.</li>
 * </ul>
 */
public class ImfValidator {

    private final TemplateParameterContextProvider contextProvider;
    private final ExecuteStrategyFactory executeStrategyFactory;

    public ImfValidator(TemplateParameterContextProvider contextProvider, ExecuteStrategyFactory executeStrategyFactory) {
        this.contextProvider = contextProvider;
        this.executeStrategyFactory = executeStrategyFactory;
    }

    /**
     * Performs validation of the given IMP and CPL.
     *
     * @throws ImfValidationException an exception thrown if validation errors are found
     * @throws IOException
     */
    public void validate() throws ImfValidationException, IOException {
        executeValidationCommand();
        analyzeResult();
    }

    private void executeValidationCommand() throws IOException {
        ImfValidationType imfValidationCommand = contextProvider.getConversionProvider().getConversion().getImfValidation();
        OperationInfo operationInfo = new OperationInfo(
                imfValidationCommand.getValue(), imfValidationCommand.getClass().getSimpleName(), ContextInfo.EMPTY);
        executeStrategyFactory.createExecuteOnceStrategy(contextProvider).execute(operationInfo);
    }

    private void analyzeResult() throws ImfValidationException, IOException {
        String errorFileName = contextProvider.getDynamicContext().getParameterValueAsString(
                DynamicContextParameters.OUTPUT_VALIDATION_FILE);
        File errorFile = new File(contextProvider.getWorkingDir(), errorFileName);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(errorFile);
            NodeList errorNodes = doc.getElementsByTagName(CoreConstants.VALIDATION_OUTPUT_XML_ERROR_TAG);

            if (errorNodes == null || errorNodes.getLength() == 0) {
                // OK! no errors
                return;
            }

            List<String> errors = new ArrayList<>();
            for (int i = 0; i < errorNodes.getLength(); i++) {
                Element errorNode = (Element) errorNodes.item(i);
                if (errorNode.getFirstChild() != null) {
                    errors.add(errorNode.getFirstChild().getNodeValue());
                }
            }
            throw new ImfValidationException(errors);

        } catch (ParserConfigurationException | SAXException e) {
            throw new ConversionException("Can not read result of IMF validation", e);
        }
    }

}
