
package digital.inception.codes.ws.client;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * <p>Java class for GetCodeCategoryNameResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetCodeCategoryNameResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GetCodeCategoryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetCodeCategoryNameResponse", propOrder = {
    "getCodeCategoryName"
})
public class GetCodeCategoryNameResponse implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "GetCodeCategoryName")
    protected String getCodeCategoryName;

    /**
     * Gets the value of the getCodeCategoryName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetCodeCategoryName() {
        return getCodeCategoryName;
    }

    /**
     * Sets the value of the getCodeCategoryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetCodeCategoryName(String value) {
        this.getCodeCategoryName = value;
    }

}
