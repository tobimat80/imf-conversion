//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.08.11 at 01:51:42 PM MSK 
//


package org.smpte_ra.reg._2003._2012;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.smpte_ra.reg._395._2014._13._1.aaf.ApplicationPlugInObject;


/**
 * <p>Java class for ApplicationPluginObjectStrongReference complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApplicationPluginObjectStrongReference">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{http://www.smpte-ra.org/reg/395/2014/13/1/aaf}ApplicationPlugInObject"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicationPluginObjectStrongReference", propOrder = {
    "applicationPlugInObject"
})
public class ApplicationPluginObjectStrongReference {

    @XmlElement(name = "ApplicationPlugInObject", namespace = "http://www.smpte-ra.org/reg/395/2014/13/1/aaf")
    protected ApplicationPlugInObject applicationPlugInObject;

    /**
     * Gets the value of the applicationPlugInObject property.
     * 
     * @return
     *     possible object is
     *     {@link ApplicationPlugInObject }
     *     
     */
    public ApplicationPlugInObject getApplicationPlugInObject() {
        return applicationPlugInObject;
    }

    /**
     * Sets the value of the applicationPlugInObject property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApplicationPlugInObject }
     *     
     */
    public void setApplicationPlugInObject(ApplicationPlugInObject value) {
        this.applicationPlugInObject = value;
    }

}