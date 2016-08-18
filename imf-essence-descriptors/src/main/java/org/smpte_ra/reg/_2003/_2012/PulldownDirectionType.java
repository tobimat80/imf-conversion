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
 * <p>Java class for PulldownDirectionType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PulldownDirectionType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="TapeToFilmSpeed"/>
 *     &lt;enumeration value="FilmToTapeSpeed"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PulldownDirectionType")
@XmlEnum
public enum PulldownDirectionType {

    @XmlEnumValue("TapeToFilmSpeed")
    TAPE_TO_FILM_SPEED("TapeToFilmSpeed"),
    @XmlEnumValue("FilmToTapeSpeed")
    FILM_TO_TAPE_SPEED("FilmToTapeSpeed");
    private final String value;

    PulldownDirectionType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PulldownDirectionType fromValue(String v) {
        for (PulldownDirectionType c: PulldownDirectionType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}