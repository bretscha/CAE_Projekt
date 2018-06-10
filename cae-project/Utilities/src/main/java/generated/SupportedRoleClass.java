package generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="RefRoleClassPath" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "value" })
public class SupportedRoleClass {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "RefRoleClassPath")
    protected String refRoleClassPath;

    /**
     * Gets the value of the value property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the refRoleClassPath property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRefRoleClassPath() {
        return refRoleClassPath;
    }

    /**
     * Sets the value of the refRoleClassPath property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRefRoleClassPath(String value) {
        this.refRoleClassPath = value;
    }

}