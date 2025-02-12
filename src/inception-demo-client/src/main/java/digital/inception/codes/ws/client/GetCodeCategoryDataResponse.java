package digital.inception.codes.ws.client;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Java class for GetCodeCategoryDataResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="GetCodeCategoryDataResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CodeCategoryData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "GetCodeCategoryDataResponse",
    propOrder = {"codeCategoryData"})
public class GetCodeCategoryDataResponse implements Serializable {

  private static final long serialVersionUID = 1000000L;

  @XmlElement(name = "CodeCategoryData")
  protected String codeCategoryData;

  /**
   * Gets the value of the codeCategoryData property.
   *
   * @return possible object is {@link String }
   */
  public String getCodeCategoryData() {
    return codeCategoryData;
  }

  /**
   * Sets the value of the codeCategoryData property.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeCategoryData(String value) {
    this.codeCategoryData = value;
  }
}
