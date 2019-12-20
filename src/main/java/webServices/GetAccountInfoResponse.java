
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
 *         &lt;element name="GetAccountInfoResult" type="{http://www.139130.net}AccountInfo" minOccurs="0"/>
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
    "getAccountInfoResult"
})
@XmlRootElement(name = "GetAccountInfoResponse")
public class GetAccountInfoResponse {

    @XmlElement(name = "GetAccountInfoResult")
    protected AccountInfo getAccountInfoResult;

    /**
     * 获取getAccountInfoResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link AccountInfo }
     *     
     */
    public AccountInfo getGetAccountInfoResult() {
        return getAccountInfoResult;
    }

    /**
     * 设置getAccountInfoResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link AccountInfo }
     *     
     */
    public void setGetAccountInfoResult(AccountInfo value) {
        this.getAccountInfoResult = value;
    }

}
