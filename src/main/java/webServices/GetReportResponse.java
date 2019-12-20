
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
 *         &lt;element name="GetReportResult" type="{http://www.139130.net}ArrayOfMTReport" minOccurs="0"/>
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
    "getReportResult"
})
@XmlRootElement(name = "GetReportResponse")
public class GetReportResponse {

    @XmlElement(name = "GetReportResult")
    protected ArrayOfMTReport getReportResult;

    /**
     * 获取getReportResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfMTReport }
     *     
     */
    public ArrayOfMTReport getGetReportResult() {
        return getReportResult;
    }

    /**
     * 设置getReportResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfMTReport }
     *     
     */
    public void setGetReportResult(ArrayOfMTReport value) {
        this.getReportResult = value;
    }

}
