
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
 *         &lt;element name="PostResult" type="{http://www.139130.net}GsmsResponse" minOccurs="0"/>
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
    "postResult"
})
@XmlRootElement(name = "PostResponse")
public class PostResponse {

    @XmlElement(name = "PostResult")
    protected GsmsResponse postResult;

    /**
     * 获取postResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link GsmsResponse }
     *     
     */
    public GsmsResponse getPostResult() {
        return postResult;
    }

    /**
     * 设置postResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link GsmsResponse }
     *     
     */
    public void setPostResult(GsmsResponse value) {
        this.postResult = value;
    }

}
