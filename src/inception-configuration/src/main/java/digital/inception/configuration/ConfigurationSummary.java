/*
 * Copyright 2021 Marcus Portmann
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

package digital.inception.configuration;

// ~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>Configuration</code> class stores the summary for a configuration.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A configuration summary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"key", "description"})
@XmlRootElement(name = "ConfigurationSummary", namespace = "http://configuration.inception.digital")
@XmlType(
    name = "ConfigurationSummary",
    namespace = "http://configuration.inception.digital",
    propOrder = {"key", "description"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "configuration", name = "configuration")
public class ConfigurationSummary implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The description for the configuration. */
  @Schema(description = "The description for the configuration", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 100)
  @Column(name = "description", nullable = false, length = 100)
  private String description;

  /** The key uniquely identifying the configuration. */
  @Schema(description = "The key uniquely identifying the configuration", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Key", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Id
  @Column(name = "key", nullable = false, length = 100)
  private String key;

  /** Constructs a new <code>ConfigurationSummary</code>. */
  public ConfigurationSummary() {}

  /**
   * Constructs a new <code>ConfigurationSummary</code>.
   *
   * @param key the key uniquely identifying the configuration
   * @param description the description for the configuration
   */
  ConfigurationSummary(String key, String description) {
    this.key = key;
    this.description = description;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <code>true</code> if this object is the same as the object argument otherwise <code>
   * false</code>
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

    ConfigurationSummary other = (ConfigurationSummary) object;

    return Objects.equals(key, other.key);
  }

  /**
   * Returns the description for the configuration.
   *
   * @return the description for the configuration
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the key uniquely identifying the configuration.
   *
   * @return the key uniquely identifying the configuration
   */
  public String getKey() {
    return key;
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
}
