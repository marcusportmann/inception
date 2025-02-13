package digital.inception.codes.ws.client;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Java class for UpdateCode complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="UpdateCode"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Code" type="{https://inception.digital/codes}Code"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "UpdateCode",
    propOrder = {"code"})
public class UpdateCode implements Serializable {

  private static final long serialVersionUID = 1000000L;

  @XmlElement(name = "Code", required = true)
  protected Code code;

  /**
   * Gets the value of the code property.
   *
   * @return possible object is {@link Code }
   */
  public Code getCode() {
    return code;
  }

  /**
   * Sets the value of the code property.
   *
   * @param value allowed object is {@link Code }
   */
  public void setCode(Code value) {
    this.code = value;
  }
}
