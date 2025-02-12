
package digital.inception.codes.ws.client;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DeleteCode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DeleteCode"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CodeCategoryId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CodeId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeleteCode", propOrder = {
    "codeCategoryId",
    "codeId"
})
public class DeleteCode implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "CodeCategoryId", required = true)
    protected String codeCategoryId;
    @XmlElement(name = "CodeId", required = true)
    protected String codeId;

    /**
     * Gets the value of the codeCategoryId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeCategoryId() {
        return codeCategoryId;
    }

    /**
     * Sets the value of the codeCategoryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeCategoryId(String value) {
        this.codeCategoryId = value;
    }

    /**
     * Gets the value of the codeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeId() {
        return codeId;
    }

    /**
     * Sets the value of the codeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeId(String value) {
        this.codeId = value;
    }

}
