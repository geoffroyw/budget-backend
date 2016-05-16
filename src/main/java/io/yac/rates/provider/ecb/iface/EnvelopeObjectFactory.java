
package io.yac.rates.provider.ecb.iface;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the io.yac.rates.iface.ecb.v2 package. 
 * <p>An EnvelopeObjectFactory allows you to programatically
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class EnvelopeObjectFactory {

    private final static QName _Name_QNAME = new QName("http://www.gesmes.org/xml/2002-08-01", "name");
    private final static QName _Subject_QNAME = new QName("http://www.gesmes.org/xml/2002-08-01", "subject");

    /**
     * Create a new EnvelopeObjectFactory that can be used to create new instances of schema derived classes for package: io.yac.rates.iface.ecb.v2
     * 
     */
    public EnvelopeObjectFactory() {
    }

    /**
     * Create an instance of {@link Cube }
     * 
     */
    public Cube createCube() {
        return new Cube();
    }

    /**
     * Create an instance of {@link Sender }
     * 
     */
    public Sender createSender() {
        return new Sender();
    }

    /**
     * Create an instance of {@link Envelope }
     * 
     */
    public Envelope createEnvelope() {
        return new Envelope();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.gesmes.org/xml/2002-08-01", name = "name")
    public JAXBElement<String> createName(String value) {
        return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.gesmes.org/xml/2002-08-01", name = "subject")
    public JAXBElement<String> createSubject(String value) {
        return new JAXBElement<String>(_Subject_QNAME, String.class, null, value);
    }

}
