
package webServices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>MessageData complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="MessageData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Phone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Content" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vipFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="customMsgID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="medias" type="{http://www.139130.net}ArrayOfMediaItems" minOccurs="0"/>
 *         &lt;element name="orgCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="msgFmt" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageData", propOrder = {
    "phone",
    "content",
    "vipFlag",
    "customMsgID",
    "medias",
    "orgCode",
    "msgFmt"
})
public class MessageData {

    @XmlElement(name = "Phone")
    protected String phone;
    @XmlElement(name = "Content")
    protected String content;
    protected boolean vipFlag;
    protected String customMsgID;
    protected ArrayOfMediaItems medias;
    protected String orgCode;
    protected int msgFmt;

    /**
     * 获取phone属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置phone属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhone(String value) {
        this.phone = value;
    }

    /**
     * 获取content属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置content属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
    }

    /**
     * 获取vipFlag属性的值。
     * 
     */
    public boolean isVipFlag() {
        return vipFlag;
    }

    /**
     * 设置vipFlag属性的值。
     * 
     */
    public void setVipFlag(boolean value) {
        this.vipFlag = value;
    }

    /**
     * 获取customMsgID属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomMsgID() {
        return customMsgID;
    }

    /**
     * 设置customMsgID属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomMsgID(String value) {
        this.customMsgID = value;
    }

    /**
     * 获取medias属性的值。
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
     * 设置medias属性的值。
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
     * 获取orgCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgCode() {
        return orgCode;
    }

    /**
     * 设置orgCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgCode(String value) {
        this.orgCode = value;
    }

    /**
     * 获取msgFmt属性的值。
     * 
     */
    public int getMsgFmt() {
        return msgFmt;
    }

    /**
     * 设置msgFmt属性的值。
     * 
     */
    public void setMsgFmt(int value) {
        this.msgFmt = value;
    }

}
