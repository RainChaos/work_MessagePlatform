
package webServices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>BusinessType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="BusinessType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Priority" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="StartTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EndTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ExtendFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="bindChs" type="{http://www.139130.net}ArrayOfBindChannel" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BusinessType", propOrder = {
    "id",
    "name",
    "priority",
    "startTime",
    "endTime",
    "extendFlag",
    "state",
    "bindChs"
})
public class BusinessType {

    @XmlElement(name = "Id")
    protected int id;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "Priority")
    protected int priority;
    @XmlElement(name = "StartTime")
    protected String startTime;
    @XmlElement(name = "EndTime")
    protected String endTime;
    @XmlElement(name = "ExtendFlag")
    protected boolean extendFlag;
    protected int state;
    protected ArrayOfBindChannel bindChs;

    /**
     * 获取id属性的值。
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * 设置id属性的值。
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * 获取name属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * 获取priority属性的值。
     * 
     */
    public int getPriority() {
        return priority;
    }

    /**
     * 设置priority属性的值。
     * 
     */
    public void setPriority(int value) {
        this.priority = value;
    }

    /**
     * 获取startTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 设置startTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartTime(String value) {
        this.startTime = value;
    }

    /**
     * 获取endTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * 设置endTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndTime(String value) {
        this.endTime = value;
    }

    /**
     * 获取extendFlag属性的值。
     * 
     */
    public boolean isExtendFlag() {
        return extendFlag;
    }

    /**
     * 设置extendFlag属性的值。
     * 
     */
    public void setExtendFlag(boolean value) {
        this.extendFlag = value;
    }

    /**
     * 获取state属性的值。
     * 
     */
    public int getState() {
        return state;
    }

    /**
     * 设置state属性的值。
     * 
     */
    public void setState(int value) {
        this.state = value;
    }

    /**
     * 获取bindChs属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfBindChannel }
     *     
     */
    public ArrayOfBindChannel getBindChs() {
        return bindChs;
    }

    /**
     * 设置bindChs属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfBindChannel }
     *     
     */
    public void setBindChs(ArrayOfBindChannel value) {
        this.bindChs = value;
    }

}
