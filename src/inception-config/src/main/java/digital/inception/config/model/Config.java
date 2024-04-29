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

package digital.inception.config.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * The <b>Config</b> class stores the key, value and description for the config.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A config")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"key", "value", "description"})
@XmlRootElement(name = "Config", namespace = "https://inception.digital/config")
@XmlType(
    name = "Config",
    namespace = "https://inception.digital/config",
    propOrder = {"key", "value", "description"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "config_config")
public class Config implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The description for the config. */
  @Schema(
      description = "The description for the config",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 100)
  @Column(name = "description", length = 100, nullable = false)
  private String description;

  /** The key for the config. */
  @Schema(description = "The key for the config", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Key", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Id
  @Column(name = "key", length = 100, nullable = false)
  private String key;

  /** The value for the config. */
  @Schema(description = "The value for the config", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(max = 16384)
  @Column(name = "value", length = 16384, nullable = false)
  private String value;

  /** Constructs a new <b>Config</b>. */
  public Config() {}

  /**
   * Constructs a new <b>Config</b>.
   *
   * @param key the key for the config
   * @param value the value for the config
   * @param description the description for the config
   */
  public Config(String key, String value, String description) {
    this.key = key;
    this.value = value;
    this.description = description;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument otherwise <b>false</b>
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

    Config other = (Config) object;

    return Objects.equals(key, other.key);
  }

  /**
   * Returns the description for the config.
   *
   * @return the description for the config
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the key for the config.
   *
   * @return the key for the config
   */
  public String getKey() {
    return key;
  }

  /**
   * Returns the value for the config.
   *
   * @return the value for the config
   */
  public String getValue() {
    return value;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (key == null) ? 0 : key.hashCode();
  }

  /**
   * Set the description for the config.
   *
   * @param description the description for the config
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the key for the config.
   *
   * @param key the key for the config
   */
  public void setKey(String key) {
    this.key = key;
  }

  /**
   * Set the value for the config.
   *
   * @param value the value for the config
   */
  public void setValue(String value) {
    this.value = value;
  }
}
