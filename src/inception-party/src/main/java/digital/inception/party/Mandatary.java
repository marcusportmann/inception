/*
 * Copyright 2022 Marcus Portmann
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

package digital.inception.party;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.LocalDateAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * The <b>Mandatary</b> class holds the information for a mandatary for a mandate.
 *
 * @author Marcus Portmann
 */
@Schema(name = "Mandatary", description = "A mandatary for a mandate")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"partyId", "role", "effectiveFrom", "effectiveTo"})
@XmlRootElement(name = "Mandatary", namespace = "http://inception.digital/party")
@XmlType(
    name = "Mandatary",
    namespace = "http://inception.digital/party",
    propOrder = {"partyId", "role", "effectiveFrom", "effectiveTo"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "mandataries")
@IdClass(MandataryId.class)
public class Mandatary implements Serializable {

  private static final long serialVersionUID = 1000000;

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

  /** The mandate the mandatary is associated with. */
  @Schema(hidden = true)
  @JsonBackReference
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "mandate_id")
  private Mandate mandate;

  /** The ID for the party who is the recipient of the mandate. */
  @Schema(description = "The ID for the party who is the recipient of the mandate", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "PartyId", required = true)
  @NotNull
  @Id
  @Column(name = "party_id", nullable = false)
  private UUID partyId;

  /** The code for the mandatary role. */
  @Schema(description = "The code for the mandatary role", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Role", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Column(name = "role", length = 30, nullable = false)
  private String role;

  /** Constructs a new <b>Mandatary</b>. */
  public Mandatary() {}

  /**
   * Constructs a new <b>Mandatary</b>.
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

    Mandatary other = (Mandatary) object;

    return Objects.equals(mandate, other.mandate) && Objects.equals(partyId, other.partyId);
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
   * Returns the mandate the mandatary is associated with.
   *
   * @return the mandate the mandatary is associated with
   */
  @Schema(hidden = true)
  public Mandate getMandate() {
    return mandate;
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
    return (((mandate == null) || (mandate.getId() == null)) ? 0 : mandate.getId().hashCode())
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
    this.mandate = mandate;
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
}
