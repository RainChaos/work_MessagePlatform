
package webServices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FindResponseResult" type="{http://www.139130.net}ArrayOfMTResponse" minOccurs="0"/>
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
    "findResponseResult"
})
@XmlRootElement(name = "FindResponseResponse")
public class FindResponseResponse {

    @XmlElement(name = "FindResponseResult")
    protected ArrayOfMTResponse findResponseResult;

    /**
     * ��ȡfindResponseResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfMTResponse }
     *     
     */
    public ArrayOfMTResponse getFindResponseResult() {
        return findResponseResult;
    }

    /**
     * ����findResponseResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfMTResponse }
     *     
     */
    public void setFindResponseResult(ArrayOfMTResponse value) {
        this.findResponseResult = value;
    }

}
