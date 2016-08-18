//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.08.11 at 01:51:42 PM MSK 
//


package org.smpte_ra.reg._2003._2012;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ColorSitingType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ColorSitingType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="CoSiting"/>
 *     &lt;enumeration value="HorizontalMidpoint"/>
 *     &lt;enumeration value="ThreeTap"/>
 *     &lt;enumeration value="Quincunx"/>
 *     &lt;enumeration value="Rec601"/>
 *     &lt;enumeration value="LineAlternating"/>
 *     &lt;enumeration value="VerticalMidpoint"/>
 *     &lt;enumeration value="UnknownSiting"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ColorSitingType")
@XmlEnum
public enum ColorSitingType {

    @XmlEnumValue("CoSiting")
    CO_SITING("CoSiting"),
    @XmlEnumValue("HorizontalMidpoint")
    HORIZONTAL_MIDPOINT("HorizontalMidpoint"),
    @XmlEnumValue("ThreeTap")
    THREE_TAP("ThreeTap"),
    @XmlEnumValue("Quincunx")
    QUINCUNX("Quincunx"),
    @XmlEnumValue("Rec601")
    REC_601("Rec601"),
    @XmlEnumValue("LineAlternating")
    LINE_ALTERNATING("LineAlternating"),
    @XmlEnumValue("VerticalMidpoint")
    VERTICAL_MIDPOINT("VerticalMidpoint"),
    @XmlEnumValue("UnknownSiting")
    UNKNOWN_SITING("UnknownSiting");
    private final String value;

    ColorSitingType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ColorSitingType fromValue(String v) {
        for (ColorSitingType c: ColorSitingType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}