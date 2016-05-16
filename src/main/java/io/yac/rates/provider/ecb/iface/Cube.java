package io.yac.rates.provider.ecb.iface;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Classe Java pour anonymous complex type.
 * <p/>
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.ecb.int/vocabulary/2002-08-01/eurofxref}Cube" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="currency" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="rate" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="time" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "cube"
})
@XmlRootElement(name = "Cube", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
public class Cube {

    @XmlElement(name = "Cube", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
    protected List<Cube> cube;
    @XmlAttribute(name = "currency")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String currency;
    @XmlAttribute(name = "rate")
    protected BigDecimal rate;
    @XmlAttribute(name = "time")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String time;

    /**
     * Gets the value of the cube property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cube property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCube().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link Cube }
     */
    public List<Cube> getCube() {
        if (cube == null) {
            cube = new ArrayList<Cube>();
        }
        return this.cube;
    }

    /**
     * Obtient la valeur de la propriété currency.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Définit la valeur de la propriété currency.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

    /**
     * Obtient la valeur de la propriété rate.
     *
     * @return possible object is
     * {@link BigDecimal }
     */
    public BigDecimal getRate() {
        return rate;
    }

    /**
     * Définit la valeur de la propriété rate.
     *
     * @param value allowed object is
     *              {@link BigDecimal }
     */
    public void setRate(BigDecimal value) {
        this.rate = value;
    }

    /**
     * Obtient la valeur de la propriété time.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTime() {
        return time;
    }

    /**
     * Définit la valeur de la propriété time.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTime(String value) {
        this.time = value;
    }

    @Override
    public String toString() {
        return "Cube{" +
                "cube=" + cube +
                ", currency='" + currency + '\'' +
                ", rate=" + rate +
                ", time='" + time + '\'' +
                '}';
    }
}
