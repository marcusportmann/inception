/*
 * Copyright Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package digital.inception.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.DtdJarResolver;
import digital.inception.core.xml.XmlParserErrorHandler;
import digital.inception.core.xml.XmlUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.ByteArrayInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * The <b>UserDirectory</b> class holds the information for a user directory.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A user directory")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "type", "name", "parameters"})
@XmlRootElement(name = "UserDirectory", namespace = "https://inception.digital/security")
@XmlType(
    name = "UserDirectory",
    namespace = "https://inception.digital/security",
    propOrder = {"id", "type", "name", "parameters"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "security_user_directories")
@SuppressWarnings({"unused"})
public class UserDirectory implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the user directory. */
  @Schema(
      description = "The ID for the user directory",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The name of the user directory. */
  @Schema(
      description = "The name of the user directory",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The parameters for the user directory. */
  @Schema(
      description = "The parameters for the user directory",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Parameters", required = true)
  @XmlElement(name = "Parameter", required = true)
  @Valid
  @Transient
  private List<UserDirectoryParameter> parameters = new ArrayList<>();

  /** The tenants the user directory is associated with. */
  @JsonIgnore
  @XmlTransient
  @ManyToMany(mappedBy = "userDirectories")
  private List<Tenant> tenants = new ArrayList<>();

  /** The code for the user directory type. */
  @Schema(
      description = "The code for the user directory type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "type", length = 100, nullable = false)
  private String type;

  /** Constructs a new <b>UserDirectory</b>. */
  public UserDirectory() {}

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument, otherwise <b>false</b>
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (getClass() != object.getClass()) {
      return false;
    }

    UserDirectory other = (UserDirectory) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the XML configuration data for the user directory.
   *
   * @return the XML configuration data for the user directory
   */
  @JsonIgnore
  @XmlTransient
  @Access(AccessType.PROPERTY)
  @Column(name = "configuration", length = 16384, nullable = false)
  public String getConfiguration() {
    StringBuilder buffer = new StringBuilder();

    buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    buffer.append(
        "<!DOCTYPE userDirectory SYSTEM \"UserDirectoryConfiguration.dtd\"><userDirectory>");

    for (UserDirectoryParameter parameter : parameters) {
      buffer.append("<parameter>");
      buffer.append("<name>").append(parameter.getName()).append("</name>");
      buffer
          .append("<value>")
          .append(StringUtils.hasText(parameter.getValue()) ? parameter.getValue() : "")
          .append("</value>");
      buffer.append("</parameter>");
    }

    buffer.append("</userDirectory>");

    return buffer.toString();
  }

  /**
   * Returns the ID for the user directory.
   *
   * @return the ID for the user directory
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the name of the user directory.
   *
   * @return the name of the user directory
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the parameters for the user directory.
   *
   * @return the parameters for the user directory
   */
  public List<UserDirectoryParameter> getParameters() {
    return parameters;
  }

  /**
   * Returns the tenants the user directory is associated with.
   *
   * @return the tenants the user directory is associated with
   */
  public List<Tenant> getTenants() {
    return tenants;
  }

  /**
   * Returns the code for the user directory type.
   *
   * @return the code for the user directory type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
  }

  /**
   * Set the XML configuration data for the user directory.
   *
   * @param configuration the XML configuration data for the user directory
   * @throws InvalidConfigurationException if the XML configuration data could not be set for the
   *     user directory
   */
  public void setConfiguration(String configuration) throws InvalidConfigurationException {
    try {
      // Parse the XML configuration data
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

      builderFactory.setValidating(true);

      DocumentBuilder builder = builderFactory.newDocumentBuilder();

      builder.setEntityResolver(
          new DtdJarResolver(
              "UserDirectoryConfiguration.dtd", "META-INF/UserDirectoryConfiguration.dtd"));
      builder.setErrorHandler(new XmlParserErrorHandler());

      InputSource inputSource = new InputSource(new ByteArrayInputStream(configuration.getBytes()));
      Document document = builder.parse(inputSource);

      Element rootElement = document.getDocumentElement();

      // Read the user directory parameters configuration
      parameters = new ArrayList<>();

      NodeList parameterElements = rootElement.getElementsByTagName("parameter");

      for (int i = 0; i < parameterElements.getLength(); i++) {
        Element parameterElement = (Element) parameterElements.item(i);

        Optional<String> nameOptional = XmlUtil.getChildElementText(parameterElement, "name");
        Optional<String> valueOptional = XmlUtil.getChildElementText(parameterElement, "value");

        if (nameOptional.isPresent() && valueOptional.isPresent()) {
          parameters.add(new UserDirectoryParameter(nameOptional.get(), valueOptional.get()));
        }
      }
    } catch (Throwable e) {
      throw new InvalidConfigurationException(
          "Failed to parse the XML configuration data for the user directory", e);
    }
  }

  /**
   * Set the ID for the user directory.
   *
   * @param id the ID for the user directory
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the name of the user directory.
   *
   * @param name the name of the user directory
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the parameters for the user directory.
   *
   * @param parameters the parameters for the user directory
   */
  public void setParameters(List<UserDirectoryParameter> parameters) {
    this.parameters = parameters;
  }

  /**
   * Set the tenants the user directory is associated with.
   *
   * @param tenants the tenants the user directory is associated with
   */
  public void setTenants(List<Tenant> tenants) {
    this.tenants = tenants;
  }

  /**
   * Set the code for the user directory type.
   *
   * @param type the code for the user directory type
   */
  public void setType(String type) {
    this.type = type;
  }
}
