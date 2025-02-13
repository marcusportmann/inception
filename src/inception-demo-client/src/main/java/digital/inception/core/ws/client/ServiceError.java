package digital.inception.core.ws.client;

import digital.inception.core.xml.OffsetDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Java class for ServiceError complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ServiceError"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Message" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "ServiceError",
    propOrder = {"timestamp", "message"})
@XmlSeeAlso({InvalidArgumentError.class})
public class ServiceError implements Serializable {

  private static final long serialVersionUID = 1000000L;

  @XmlElement(name = "Message", required = true)
  protected String message;

  @XmlElement(name = "Timestamp", required = true, type = String.class)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  protected OffsetDateTime timestamp;

  /**
   * Gets the value of the message property.
   *
   * @return possible object is {@link String }
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets the value of the timestamp property.
   *
   * @return possible object is {@link String }
   */
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  /**
   * Sets the value of the message property.
   *
   * @param value allowed object is {@link String }
   */
  public void setMessage(String value) {
    this.message = value;
  }

  /**
   * Sets the value of the timestamp property.
   *
   * @param value allowed object is {@link String }
   */
  public void setTimestamp(OffsetDateTime value) {
    this.timestamp = value;
  }
}
