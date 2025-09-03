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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.LocalDateAdapter;
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
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code Mandatary} class holds the information for a mandatary for a mandate.
 *
 * @author Marcus Portmann
 */
@Schema(name = "Mandatary", description = "A mandatary for a mandate")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"partyId", "role", "effectiveFrom", "effectiveTo"})
@XmlRootElement(name = "Mandatary", namespace = "https://inception.digital/party")
@XmlType(
    name = "Mandatary",
    namespace = "https://inception.digital/party",
    propOrder = {"partyId", "role", "effectiveFrom", "effectiveTo"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_mandataries")
@IdClass(MandataryId.class)
public class Mandatary implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The date the mandatary is effective from. */
  @Schema(description = "The date the mandatary is effective from")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "EffectiveFrom")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_from")
  private LocalDate effectiveFrom;

  /** The date the mandatary is effective to. */
  @Schema(description = "The date the mandatary is effective to")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "EffectiveTo")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_to")
  private LocalDate effectiveTo;

  /** The ID for the mandate the mandatary is associated with. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "mandate_id", nullable = false)
  private UUID mandateId;

  /** The ID for the party who is the recipient of the mandate. */
  @Schema(
      description = "The ID for the party who is the recipient of the mandate",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "PartyId", required = true)
  @NotNull
  @Id
  @Column(name = "party_id", nullable = false)
  private UUID partyId;

  /** The code for the mandatary role. */
  @Schema(
      description = "The code for the mandatary role",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Role", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "role", length = 50, nullable = false)
  private String role;

  /** Constructs a new {@code Mandatary}. */
  public Mandatary() {}

  /**
   * Constructs a new {@code Mandatary}.
   *
   * @param partyId the ID for the party who is the recipient of the mandate
   * @param role the code for the mandatary role
   */
  public Mandatary(UUID partyId, String role) {
    this.partyId = partyId;
    this.role = role;
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

    Mandatary other = (Mandatary) object;

    return Objects.equals(mandateId, other.mandateId) && Objects.equals(partyId, other.partyId);
  }

  /**
   * Returns the date the mandatary is effective from.
   *
   * @return the date the mandatary is effective from
   */
  public LocalDate getEffectiveFrom() {
    return effectiveFrom;
  }

  /**
   * Returns the date the mandatary is effective to.
   *
   * @return the date the mandatary is effective to
   */
  public LocalDate getEffectiveTo() {
    return effectiveTo;
  }

  /**
   * Returns the ID for the party who is the recipient of the mandate.
   *
   * @return the ID for the party who is the recipient of the mandate
   */
  public UUID getPartyId() {
    return partyId;
  }

  /**
   * Returns the code for the mandatary role.
   *
   * @return the code for the mandatary role
   */
  public String getRole() {
    return role;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((mandateId == null) ? 0 : mandateId.hashCode())
        + ((partyId == null) ? 0 : partyId.hashCode());
  }

  /**
   * Set the date the mandatary is effective from.
   *
   * @param effectiveFrom the date the mandatary is effective from
   */
  public void setEffectiveFrom(LocalDate effectiveFrom) {
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Set the date the mandatary is effective to.
   *
   * @param effectiveTo the date the mandatary is effective to
   */
  public void setEffectiveTo(LocalDate effectiveTo) {
    this.effectiveTo = effectiveTo;
  }

  /**
   * Set the mandate the mandatary is associated with.
   *
   * @param mandate the mandate the mandatary is associated with
   */
  @Schema(hidden = true)
  public void setMandate(Mandate mandate) {
    if (mandate != null) {
      this.mandateId = mandate.getId();
    } else {
      this.mandateId = null;
    }
  }

  /**
   * Set the ID for the party who is the recipient of the mandate.
   *
   * @param partyId the ID for the party who is the recipient of the mandate
   */
  public void setPartyId(UUID partyId) {
    this.partyId = partyId;
  }

  /**
   * Set the code for the mandatary role.
   *
   * @param role the code for the mandatary role
   */
  public void setRole(String role) {
    this.role = role;
  }


  /**
   * Called by the JAXB runtime an instance of this class has been completely unmarshalled, but
   * before it is added to its parent.
   *
   * @param unmarshaller the JAXB unmarshaller
   * @param parentObject the parent object
   */
  @SuppressWarnings("unused")
  private void afterUnmarshal(Unmarshaller unmarshaller, Object parentObject) {
    if (parentObject instanceof Mandate parent) {
      setMandate(parent);
    }
  }
}
