
package webServices;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfMOMsg complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfMOMsg">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MOMsg" type="{http://www.139130.net}MOMsg" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfMOMsg", propOrder = {
    "moMsg"
})
public class ArrayOfMOMsg {

    @XmlElement(name = "MOMsg", nillable = true)
    protected List<MOMsg> moMsg;

    /**
     * Gets the value of the moMsg property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the moMsg property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMOMsg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MOMsg }
     * 
     * 
     */
    public List<MOMsg> getMOMsg() {
        if (moMsg == null) {
            moMsg = new ArrayList<MOMsg>();
        }
        return this.moMsg;
    }

}
