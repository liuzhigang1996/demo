
package app.cib.util.smsclient.log;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>logRequest complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="logRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TransId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LogSystem" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LogUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LogApplication" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LogLevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LogMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LogTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SendTimes" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "logRequest", propOrder = {
    "transId",
    "logSystem",
    "logUser",
    "logApplication",
    "logLevel",
    "logMessage",
    "logTime",
    "sendTimes"
})
public class LogRequest {

    @XmlElement(name = "TransId")
    protected String transId;
    @XmlElement(name = "LogSystem")
    protected String logSystem;
    @XmlElement(name = "LogUser")
    protected String logUser;
    @XmlElement(name = "LogApplication")
    protected String logApplication;
    @XmlElement(name = "LogLevel")
    protected String logLevel;
    @XmlElement(name = "LogMessage")
    protected String logMessage;
    @XmlElement(name = "LogTime")
    protected String logTime;
    @XmlElement(name = "SendTimes")
    protected int sendTimes;

    /**
     * ��ȡtransId���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransId() {
        return transId;
    }

    /**
     * ����transId���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransId(String value) {
        this.transId = value;
    }

    /**
     * ��ȡlogSystem���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogSystem() {
        return logSystem;
    }

    /**
     * ����logSystem���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogSystem(String value) {
        this.logSystem = value;
    }

    /**
     * ��ȡlogUser���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogUser() {
        return logUser;
    }

    /**
     * ����logUser���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogUser(String value) {
        this.logUser = value;
    }

    /**
     * ��ȡlogApplication���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogApplication() {
        return logApplication;
    }

    /**
     * ����logApplication���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogApplication(String value) {
        this.logApplication = value;
    }

    /**
     * ��ȡlogLevel���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogLevel() {
        return logLevel;
    }

    /**
     * ����logLevel���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogLevel(String value) {
        this.logLevel = value;
    }

    /**
     * ��ȡlogMessage���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogMessage() {
        return logMessage;
    }

    /**
     * ����logMessage���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogMessage(String value) {
        this.logMessage = value;
    }

    /**
     * ��ȡlogTime���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogTime() {
        return logTime;
    }

    /**
     * ����logTime���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogTime(String value) {
        this.logTime = value;
    }

    /**
     * ��ȡsendTimes���Ե�ֵ��
     * 
     */
    public int getSendTimes() {
        return sendTimes;
    }

    /**
     * ����sendTimes���Ե�ֵ��
     * 
     */
    public void setSendTimes(int value) {
        this.sendTimes = value;
    }

}
