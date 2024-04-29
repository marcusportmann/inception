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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.OffsetDateTimeAdapter;
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
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * The <b>PolicySummary</b> class holds the summary information for a policy.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A policy summary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "version", "name", "type", "lastModified"})
@XmlRootElement(name = "PolicySummary", namespace = "https://inception.digital/security")
@XmlType(
    name = "PolicySummary",
    namespace = "https://inception.digital/security",
    propOrder = {"id", "version", "name", "type", "lastModified"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "security_policies")
public class PolicySummary implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the policy. */
  @Schema(description = "The ID for the policy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Id
  @Column(name = "id", length = 100, nullable = false)
  private String id;

  /** The date and time the policy was last modified. */
  @Schema(description = "The date and time the policy was last modified")
  @JsonProperty
  @XmlElement(name = "LastModified")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "last_modified")
  private OffsetDateTime lastModified;

  /** The name of the policy. */
  @Schema(description = "The name of the policy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** The policy type. */
  @Schema(description = "The policy type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  private PolicyType type;

  /** The version of the policy. */
  @Schema(description = "The version of the policy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Version", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "version", length = 50, nullable = false)
  private String version;

  /** Constructs a new <b>PolicySummary</b>. */
  public PolicySummary() {}

  /**
   * Constructs a new <b>PolicySummary</b>.
   *
   * @param id the ID for the policy
   * @param version the version of the policy
   * @param name the name of the policy
   * @param type the policy type
   */
  public PolicySummary(String id, String version, String name, PolicyType type) {
    this.id = id;
    this.version = version;
    this.name = name;
    this.type = type;
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

    PolicySummary other = (PolicySummary) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the ID for the policy.
   *
   * @return the ID for the policy
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the date and time the policy was last modified.
   *
   * @return the date and time the policy was last modified
   */
  public OffsetDateTime getLastModified() {
    return lastModified;
  }

  /**
   * Returns the name of the policy.
   *
   * @return the name of the policy
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the policy type.
   *
   * @return the policy type
   */
  public PolicyType getType() {
    return type;
  }

  /**
   * Returns the version of the policy.
   *
   * @return the version of the policy
   */
  public String getVersion() {
    return version;
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
}
