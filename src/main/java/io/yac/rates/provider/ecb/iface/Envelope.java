
package io.yac.rates.provider.ecb.iface;

import javax.xml.bind.annotation.*;


/**
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.gesmes.org/xml/2002-08-01}subject"/>
 *         &lt;element ref="{http://www.gesmes.org/xml/2002-08-01}Sender"/>
 *         &lt;element ref="{http://www.ecb.int/vocabulary/2002-08-01/eurofxref}Cube"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "subject",
    "sender",
    "cube"
})
@XmlRootElement(name = "Envelope", namespace = "http://www.gesmes.org/xml/2002-08-01")
public class Envelope {

    @XmlElement(namespace = "http://www.gesmes.org/xml/2002-08-01", required = true)
    protected String subject;
    @XmlElement(name = "Sender", namespace = "http://www.gesmes.org/xml/2002-08-01", required = true)
    protected Sender sender;
    @XmlElement(name = "Cube", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref", required = true)
    protected Cube cube;

    /**
     * Obtient la valeur de la propriété subject.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Définit la valeur de la propriété subject.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubject(String value) {
        this.subject = value;
    }

    /**
     * Obtient la valeur de la propriété sender.
     * 
     * @return
     *     possible object is
     *     {@link Sender }
     *     
     */
    public Sender getSender() {
        return sender;
    }

    /**
     * Définit la valeur de la propriété sender.
     * 
     * @param value
     *     allowed object is
     *     {@link Sender }
     *     
     */
    public void setSender(Sender value) {
        this.sender = value;
    }

    /**
     * Obtient la valeur de la propriété cube.
     * 
     * @return
     *     possible object is
     *     {@link Cube }
     *     
     */
    public Cube getCube() {
        return cube;
    }

    /**
     * Définit la valeur de la propriété cube.
     * 
     * @param value
     *     allowed object is
     *     {@link Cube }
     *     
     */
    public void setCube(Cube value) {
        this.cube = value;
    }

    @Override
    public String toString() {
        return "Envelope{" +
                "subject='" + subject + '\'' +
                ", sender=" + sender +
                ", cube=" + cube +
                '}';
    }
}
