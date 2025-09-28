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

package digital.inception.party.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code ExternalReference} class holds the information for an external reference for an
 * organization or person.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An external reference for an organization or person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "value"})
@XmlRootElement(name = "ExternalReference", namespace = "https://inception.digital/party")
@XmlType(
    name = "ExternalReference",
    namespace = "https://inception.digital/party",
    propOrder = {"type", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_external_references")
@IdClass(ExternalReferenceId.class)
public class ExternalReference implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the party the external reference is associated with. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "party_id", nullable = false)
  private UUID partyId;

  /** The code for the external reference type. */
  @Schema(
      description = "The code for the external reference type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "type", length = 50, nullable = false)
  private String type;

  /** The value for the external reference. */
  @Schema(
      description = "The value for the external reference",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "value", length = 100, nullable = false)
  private String value;

  /** Constructs a new {@code ExternalReference}. */
  public ExternalReference() {}

  /**
   * Constructs a new {@code ExternalReference}.
   *
   * @param type the code for the external reference type
   * @param value the value for the external reference
   */
  public ExternalReference(String type, String value) {
    this.type = type;
    this.value = value;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument, otherwise {@code false}
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

    ExternalReference other = (ExternalReference) object;

    return Objects.equals(partyId, other.partyId) && Objects.equals(type, other.type);
  }

  /**
   * Returns the code for the external reference type.
   *
   * @return the code for the external reference type
   */
  public String getType() {
    return type;
  }

  /**
   * The value for the external reference.
   *
   * @return the value for the external reference
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
    return ((partyId == null) ? 0 : partyId.hashCode()) + ((type == null) ? 0 : type.hashCode());
  }

  /**
   * Set the party the external reference is associated with.
   *
   * @param party the party the external reference is associated with
   */
  @JsonBackReference("externalReferenceReference")
  @Schema(hidden = true)
  public void setParty(PartyBase party) {
    if (party != null) {
      this.partyId = party.getId();
    } else {
      this.partyId = null;
    }
  }

  /**
   * Set the code for the external reference type.
   *
   * @param type the code for the external reference type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Set the value for the external reference.
   *
   * @param value the value for the external reference
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Called by the JAXB runtime when an instance of this class has been completely unmarshalled, but
   * before it is added to its parent.
   *
   * @param unmarshaller the JAXB unmarshaller
   * @param parentObject the parent object
   */
  @SuppressWarnings("unused")
  private void afterUnmarshal(Unmarshaller unmarshaller, Object parentObject) {
    if (parentObject instanceof PartyBase parent) {
      setParty(parent);
    }
  }
}
