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
 * Java class for CodeCategory complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CodeCategory"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LastModified" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "CodeCategory",
    propOrder = {"id", "name", "data", "lastModified"})
public class CodeCategory implements Serializable {

  private static final long serialVersionUID = 1000000L;

  @XmlElement(name = "Data")
  protected String data;

  @XmlElement(name = "Id", required = true)
  protected String id;

  @XmlElement(name = "LastModified", type = String.class)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  protected OffsetDateTime lastModified;

  @XmlElement(name = "Name", required = true)
  protected String name;

  /**
   * Gets the value of the data property.
   *
   * @return possible object is {@link String }
   */
  public String getData() {
    return data;
  }

  /**
   * Gets the value of the id property.
   *
   * @return possible object is {@link String }
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the value of the lastModified property.
   *
   * @return possible object is {@link String }
   */
  public OffsetDateTime getLastModified() {
    return lastModified;
  }

  /**
   * Gets the value of the name property.
   *
   * @return possible object is {@link String }
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the value of the data property.
   *
   * @param value allowed object is {@link String }
   */
  public void setData(String value) {
    this.data = value;
  }

  /**
   * Sets the value of the id property.
   *
   * @param value allowed object is {@link String }
   */
  public void setId(String value) {
    this.id = value;
  }

  /**
   * Sets the value of the lastModified property.
   *
   * @param value allowed object is {@link String }
   */
  public void setLastModified(OffsetDateTime value) {
    this.lastModified = value;
  }

  /**
   * Sets the value of the name property.
   *
   * @param value allowed object is {@link String }
   */
  public void setName(String value) {
    this.name = value;
  }
}
