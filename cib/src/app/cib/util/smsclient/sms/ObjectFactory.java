
package app.cib.util.smsclient.sms;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the app.cib.util.smsclient.sms package. 
 * <p>An ObjectFactory allows you to programatically 
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
public class ObjectFactory {

    private final static QName _SendSms_QNAME = new QName("http://smsservice.bank.com/", "sendSms");
    private final static QName _SmsRequest_QNAME = new QName("http://smsservice.bank.com/", "smsRequest");
    private final static QName _SendSmsResponse_QNAME = new QName("http://smsservice.bank.com/", "sendSmsResponse");
    private final static QName _SmsResponse_QNAME = new QName("http://smsservice.bank.com/", "smsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: app.cib.util.smsclient.sms
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SmsResponse }
     * 
     */
    public SmsResponse createSmsResponse() {
        return new SmsResponse();
    }

    /**
     * Create an instance of {@link SendSmsResponse }
     * 
     */
    public SendSmsResponse createSendSmsResponse() {
        return new SendSmsResponse();
    }

    /**
     * Create an instance of {@link SmsRequest }
     * 
     */
    public SmsRequest createSmsRequest() {
        return new SmsRequest();
    }

    /**
     * Create an instance of {@link SendSms }
     * 
     */
    public SendSms createSendSms() {
        return new SendSms();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendSms }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smsservice.bank.com/", name = "sendSms")
    public JAXBElement<SendSms> createSendSms(SendSms value) {
        return new JAXBElement<SendSms>(_SendSms_QNAME, SendSms.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SmsRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smsservice.bank.com/", name = "smsRequest")
    public JAXBElement<SmsRequest> createSmsRequest(SmsRequest value) {
        return new JAXBElement<SmsRequest>(_SmsRequest_QNAME, SmsRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendSmsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smsservice.bank.com/", name = "sendSmsResponse")
    public JAXBElement<SendSmsResponse> createSendSmsResponse(SendSmsResponse value) {
        return new JAXBElement<SendSmsResponse>(_SendSmsResponse_QNAME, SendSmsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SmsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://smsservice.bank.com/", name = "smsResponse")
    public JAXBElement<SmsResponse> createSmsResponse(SmsResponse value) {
        return new JAXBElement<SmsResponse>(_SmsResponse_QNAME, SmsResponse.class, null, value);
    }

}
