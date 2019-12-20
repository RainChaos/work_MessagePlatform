
package webServices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>MTPacks complex type?? Java ????
 * 
 * <p>????????????????????????????????????????
 * 
 * <pre>
 * &lt;complexType name="MTPacks">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="uuid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="batchID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="batchName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sendType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="msgType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="medias" type="{http://www.139130.net}ArrayOfMediaItems" minOccurs="0"/>
 *         &lt;element name="msgs" type="{http://www.139130.net}ArrayOfMessageData" minOccurs="0"/>
 *         &lt;element name="bizType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="distinctFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="scheduleTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="remark" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customNum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deadline" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="templateNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MTPacks", propOrder = {
    "uuid",
    "batchID",
    "batchName",
    "sendType",
    "msgType",
    "medias",
    "msgs",
    "bizType",
    "distinctFlag",
    "scheduleTime",
    "remark",
    "customNum",
    "deadline",
    "templateNo"
})
public class MTPacks {

    @XmlElement(required = true)
    protected String uuid;
    @XmlElement(required = true)
    protected String batchID;
    protected String batchName;
    protected int sendType;
    protected int msgType;
    protected ArrayOfMediaItems medias;
    protected ArrayOfMessageData msgs;
    protected int bizType;
    protected boolean distinctFlag;
    protected String scheduleTime;
    protected String remark;
    protected String customNum;
    protected String deadline;
    protected String templateNo;

    /**
     * ????uuid??????????
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * ????uuid??????????
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUuid(String value) {
        this.uuid = value;
    }

    /**
     * ????batchID??????????
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBatchID() {
        return batchID;
    }

    /**
     * ????batchID??????????
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBatchID(String value) {
        this.batchID = value;
    }

    /**
     * ????batchName??????????
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBatchName() {
        return batchName;
    }

    /**
     * ????batchName??????????
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBatchName(String value) {
        this.batchName = value;
    }

    /**
     * ????sendType??????????
     * 
     */
    public int getSendType() {
        return sendType;
    }

    /**
     * ????sendType??????????
     * 
     */
    public void setSendType(int value) {
        this.sendType = value;
    }

    /**
     * ????msgType??????????
     * 
     */
    public int getMsgType() {
        return msgType;
    }

    /**
     * ????msgType??????????
     * 
     */
    public void setMsgType(int value) {
        this.msgType = value;
    }

    /**
     * ????medias??????????
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfMediaItems }
     *     
     */
    public ArrayOfMediaItems getMedias() {
        return medias;
    }

    /**
     * ????medias??????????
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfMediaItems }
     *     
     */
    public void setMedias(ArrayOfMediaItems value) {
        this.medias = value;
    }

    /**
     * ????msgs??????????
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfMessageData }
     *     
     */
    public ArrayOfMessageData getMsgs() {
        return msgs;
    }

    /**
     * ????msgs??????????
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfMessageData }
     *     
     */
    public void setMsgs(ArrayOfMessageData value) {
        this.msgs = value;
    }

    /**
     * ????bizType??????????
     * 
     */
    public int getBizType() {
        return bizType;
    }

    /**
     * ????bizType??????????
     * 
     */
    public void setBizType(int value) {
        this.bizType = value;
    }

    /**
     * ????distinctFlag??????????
     * 
     */
    public boolean isDistinctFlag() {
        return distinctFlag;
    }

    /**
     * ????distinctFlag??????????
     * 
     */
    public void setDistinctFlag(boolean value) {
        this.distinctFlag = value;
    }

    /**
     * ????scheduleTime??????????
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScheduleTime() {
        return scheduleTime;
    }

    /**
     * ????scheduleTime??????????
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScheduleTime(String value) {
        this.scheduleTime = value;
    }

    /**
     * ????remark??????????
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemark() {
        return remark;
    }

    /**
     * ????remark??????????
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemark(String value) {
        this.remark = value;
    }

    /**
     * ????customNum??????????
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomNum() {
        return customNum;
    }

    /**
     * ????customNum??????????
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomNum(String value) {
        this.customNum = value;
    }

    /**
     * ????deadline??????????
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeadline() {
        return deadline;
    }

    /**
     * ????deadline??????????
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeadline(String value) {
        this.deadline = value;
    }

    /**
     * ????templateNo??????????
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemplateNo() {
        return templateNo;
    }

    /**
     * ????templateNo??????????
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemplateNo(String value) {
        this.templateNo = value;
    }

}
