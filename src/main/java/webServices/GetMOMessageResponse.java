
package webServices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetMOMessageResult" type="{http://www.139130.net}ArrayOfMOMsg" minOccurs="0"/>
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
    "getMOMessageResult"
})
@XmlRootElement(name = "GetMOMessageResponse")
public class GetMOMessageResponse {

    @XmlElement(name = "GetMOMessageResult")
    protected ArrayOfMOMsg getMOMessageResult;

    /**
     * 获取getMOMessageResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfMOMsg }
     *     
     */
    public ArrayOfMOMsg getGetMOMessageResult() {
        return getMOMessageResult;
    }

    /**
     * 设置getMOMessageResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfMOMsg }
     *     
     */
    public void setGetMOMessageResult(ArrayOfMOMsg value) {
        this.getMOMessageResult = value;
    }

}
