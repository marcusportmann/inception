package digital.inception.codes.ws.client;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Java class for GetCodeNameResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="GetCodeNameResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CodeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "GetCodeNameResponse",
    propOrder = {"codeName"})
public class GetCodeNameResponse implements Serializable {

  private static final long serialVersionUID = 1000000L;

  @XmlElement(name = "CodeName")
  protected String codeName;

  /**
   * Gets the value of the codeName property.
   *
   * @return possible object is {@link String }
   */
  public String getCodeName() {
    return codeName;
  }

  /**
   * Sets the value of the codeName property.
   *
   * @param value allowed object is {@link String }
   */
  public void setCodeName(String value) {
    this.codeName = value;
  }
}
