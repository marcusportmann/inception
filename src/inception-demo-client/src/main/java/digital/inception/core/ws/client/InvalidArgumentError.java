
package digital.inception.core.ws.client;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Java class for InvalidArgumentError complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InvalidArgumentError"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{https://inception.digital/core}ServiceError"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Parameter" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ValidationErrors"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ValidationError" type="{https://inception.digital/core}ValidationError" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvalidArgumentError", propOrder = {
    "parameter",
    "validationErrors"
})
public class InvalidArgumentError
    extends ServiceError
    implements Serializable
{

    private final static long serialVersionUID = 1000000L;
    @XmlElement(name = "Parameter", required = true)
    protected String parameter;
    @XmlElementWrapper(name = "ValidationErrors", required = true)
    @XmlElement(name = "ValidationError")
    protected List<ValidationError> validationErrors;

    /**
     * Gets the value of the parameter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParameter() {
        return parameter;
    }

    public List<ValidationError> getValidationErrors() {
        if (validationErrors == null) {
            validationErrors = new ArrayList<ValidationError>();
        }
        return validationErrors;
    }

    /**
     * Sets the value of the parameter property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setParameter(String value) {
        this.parameter = value;
    }

    public void setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

}
