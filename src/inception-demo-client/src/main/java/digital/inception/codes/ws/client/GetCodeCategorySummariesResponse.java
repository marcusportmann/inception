
package digital.inception.codes.ws.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetCodeCategorySummariesResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetCodeCategorySummariesResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CodeCategorySummary" type="{http://inception.digital/codes}CodeCategorySummary" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetCodeCategorySummariesResponse", propOrder = {
    "codeCategorySummary"
})
public class GetCodeCategorySummariesResponse implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "CodeCategorySummary")
    protected List<CodeCategorySummary> codeCategorySummary;

    /**
     * Gets the value of the codeCategorySummary property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the codeCategorySummary property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCodeCategorySummary().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodeCategorySummary }
     * 
     * 
     */
    public List<CodeCategorySummary> getCodeCategorySummary() {
        if (codeCategorySummary == null) {
            codeCategorySummary = new ArrayList<CodeCategorySummary>();
        }
        return this.codeCategorySummary;
    }

}
