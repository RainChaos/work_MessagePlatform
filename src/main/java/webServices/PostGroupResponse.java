
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
 *         &lt;element name="PostGroupResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "postGroupResult"
})
@XmlRootElement(name = "PostGroupResponse")
public class PostGroupResponse {

    @XmlElement(name = "PostGroupResult")
    protected int postGroupResult;

    /**
     * 获取postGroupResult属性的值。
     * 
     */
    public int getPostGroupResult() {
        return postGroupResult;
    }

    /**
     * 设置postGroupResult属性的值。
     * 
     */
    public void setPostGroupResult(int value) {
        this.postGroupResult = value;
    }

}
