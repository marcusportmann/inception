
package digital.inception.codes.ws.client;

import digital.inception.core.xml.OffsetDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * <p>Java class for GetCodeCategoryLastModifiedResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetCodeCategoryLastModifiedResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CodeCategoryLastModified" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetCodeCategoryLastModifiedResponse", propOrder = {
    "codeCategoryLastModified"
})
public class GetCodeCategoryLastModifiedResponse implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "CodeCategoryLastModified", type = String.class)
    @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
    @XmlSchemaType(name = "dateTime")
    protected OffsetDateTime codeCategoryLastModified;

    /**
     * Gets the value of the codeCategoryLastModified property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public OffsetDateTime getCodeCategoryLastModified() {
        return codeCategoryLastModified;
    }

    /**
     * Sets the value of the codeCategoryLastModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeCategoryLastModified(OffsetDateTime value) {
        this.codeCategoryLastModified = value;
    }

}
