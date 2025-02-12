package digital.inception.codes.ws.client;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Java class for GetCodeCategoryResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="GetCodeCategoryResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CodeCategory" type="{https://inception.digital/codes}CodeCategory" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "GetCodeCategoryResponse",
    propOrder = {"codeCategory"})
public class GetCodeCategoryResponse implements Serializable {

  private static final long serialVersionUID = 1000000L;

  @XmlElement(name = "CodeCategory")
  protected CodeCategory codeCategory;

  /**
   * Gets the value of the codeCategory property.
   *
   * @return possible object is {@link CodeCategory }
   */
  public CodeCategory getCodeCategory() {
    return codeCategory;
  }

  /**
   * Sets the value of the codeCategory property.
   *
   * @param value allowed object is {@link CodeCategory }
   */
  public void setCodeCategory(CodeCategory value) {
    this.codeCategory = value;
  }
}
