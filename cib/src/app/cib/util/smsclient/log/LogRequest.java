
package app.cib.util.smsclient.log;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>logRequest complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
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
     * 获取transId属性的值。
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
     * 设置transId属性的值。
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
     * 获取logSystem属性的值。
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
     * 设置logSystem属性的值。
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
     * 获取logUser属性的值。
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
     * 设置logUser属性的值。
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
     * 获取logApplication属性的值。
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
     * 设置logApplication属性的值。
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
     * 获取logLevel属性的值。
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
     * 设置logLevel属性的值。
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
     * 获取logMessage属性的值。
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
     * 设置logMessage属性的值。
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
     * 获取logTime属性的值。
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
     * 设置logTime属性的值。
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
     * 获取sendTimes属性的值。
     * 
     */
    public int getSendTimes() {
        return sendTimes;
    }

    /**
     * 设置sendTimes属性的值。
     * 
     */
    public void setSendTimes(int value) {
        this.sendTimes = value;
    }

}
