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
 *       &lt;attribute name="Name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="RefPartnerSideA" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="RefPartnerSideB" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "value" })
public class InternalLink {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "Name")
    protected String name;
    @XmlAttribute(name = "RefPartnerSideA")
    protected String refPartnerSideA;
    @XmlAttribute(name = "RefPartnerSideB")
    protected String refPartnerSideB;
    @XmlAttribute(name = "ID")
    protected String id;

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
     * Gets the value of the name property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the refPartnerSideA property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRefPartnerSideA() {
        return refPartnerSideA;
    }

    /**
     * Sets the value of the refPartnerSideA property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRefPartnerSideA(String value) {
        this.refPartnerSideA = value;
    }

    /**
     * Gets the value of the refPartnerSideB property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRefPartnerSideB() {
        return refPartnerSideB;
    }

    /**
     * Sets the value of the refPartnerSideB property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRefPartnerSideB(String value) {
        this.refPartnerSideB = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setID(String value) {
        this.id = value;
    }

}