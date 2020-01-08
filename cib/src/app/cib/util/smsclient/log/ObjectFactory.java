
package app.cib.util.smsclient.log;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the app.cib.util.smsclient.log package. 
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

    private final static QName _LogSms_QNAME = new QName("http://logservice.bank.com/", "logSms");
    private final static QName _LogRequest_QNAME = new QName("http://logservice.bank.com/", "logRequest");
    private final static QName _LogSmsRequest_QNAME = new QName("http://logservice.bank.com/", "logSmsRequest");
    private final static QName _LogSmsResponse_QNAME = new QName("http://logservice.bank.com/", "logSmsResponse");
    private final static QName _LogResponse_QNAME = new QName("http://logservice.bank.com/", "logResponse");
    private final static QName _LogInfoResponse_QNAME = new QName("http://logservice.bank.com/", "logInfoResponse");
    private final static QName _LogInfo_QNAME = new QName("http://logservice.bank.com/", "logInfo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: app.cib.util.smsclient.log
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LogSmsResponse }
     * 
     */
    public LogSmsResponse createLogSmsResponse() {
        return new LogSmsResponse();
    }

    /**
     * Create an instance of {@link LogInfo }
     * 
     */
    public LogInfo createLogInfo() {
        return new LogInfo();
    }

    /**
     * Create an instance of {@link LogResponse }
     * 
     */
    public LogResponse createLogResponse() {
        return new LogResponse();
    }

    /**
     * Create an instance of {@link LogInfoResponse }
     * 
     */
    public LogInfoResponse createLogInfoResponse() {
        return new LogInfoResponse();
    }

    /**
     * Create an instance of {@link LogSms }
     * 
     */
    public LogSms createLogSms() {
        return new LogSms();
    }

    /**
     * Create an instance of {@link LogSmsRequest }
     * 
     */
    public LogSmsRequest createLogSmsRequest() {
        return new LogSmsRequest();
    }

    /**
     * Create an instance of {@link LogRequest }
     * 
     */
    public LogRequest createLogRequest() {
        return new LogRequest();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogSms }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://logservice.bank.com/", name = "logSms")
    public JAXBElement<LogSms> createLogSms(LogSms value) {
        return new JAXBElement<LogSms>(_LogSms_QNAME, LogSms.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://logservice.bank.com/", name = "logRequest")
    public JAXBElement<LogRequest> createLogRequest(LogRequest value) {
        return new JAXBElement<LogRequest>(_LogRequest_QNAME, LogRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogSmsRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://logservice.bank.com/", name = "logSmsRequest")
    public JAXBElement<LogSmsRequest> createLogSmsRequest(LogSmsRequest value) {
        return new JAXBElement<LogSmsRequest>(_LogSmsRequest_QNAME, LogSmsRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogSmsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://logservice.bank.com/", name = "logSmsResponse")
    public JAXBElement<LogSmsResponse> createLogSmsResponse(LogSmsResponse value) {
        return new JAXBElement<LogSmsResponse>(_LogSmsResponse_QNAME, LogSmsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://logservice.bank.com/", name = "logResponse")
    public JAXBElement<LogResponse> createLogResponse(LogResponse value) {
        return new JAXBElement<LogResponse>(_LogResponse_QNAME, LogResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://logservice.bank.com/", name = "logInfoResponse")
    public JAXBElement<LogInfoResponse> createLogInfoResponse(LogInfoResponse value) {
        return new JAXBElement<LogInfoResponse>(_LogInfoResponse_QNAME, LogInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://logservice.bank.com/", name = "logInfo")
    public JAXBElement<LogInfo> createLogInfo(LogInfo value) {
        return new JAXBElement<LogInfo>(_LogInfo_QNAME, LogInfo.class, null, value);
    }

}
