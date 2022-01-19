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

package digital.inception.party;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * The <b>Mandatary</b> class holds the information for a mandatary for a mandate.
 *
 * @author Marcus Portmann
 */
@Schema(name = "Mandatary", description = "A mandatary for a mandate")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"partyId", "type"})
@XmlRootElement(name = "Mandatary", namespace = "http://inception.digital/party")
@XmlType(
    name = "Mandatary",
    namespace = "http://inception.digital/party",
    propOrder = {"partyId", "type"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "mandataries")
@IdClass(MandataryId.class)
public class Mandatary implements Serializable {

  private static final long serialVersionUID = 1000000;

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

  /** The code for the mandatary type. */
  @Schema(description = "The code for the mandatary type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Column(name = "type", length = 30, nullable = false)
  private String type;

  /** Constructs a new <b>Mandatary</b>. */
  public Mandatary() {}

  /**
   * Constructs a new <b>Mandatary</b>.
   *
   * @param partyId the ID for the party who is the recipient of the mandate
   * @param type the code for the mandatary type
   */
  public Mandatary(UUID partyId, String type) {
    this.partyId = partyId;
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

    Mandatary other = (Mandatary) object;

    return Objects.equals(mandate, other.mandate) && Objects.equals(partyId, other.partyId);
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
   * Returns the code for the mandatary type.
   *
   * @return the code for the mandatary type
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
    return (((mandate == null) || (mandate.getId() == null)) ? 0 : mandate.getId().hashCode())
        + ((partyId == null) ? 0 : partyId.hashCode());
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
   * Set the code for the mandatary type.
   *
   * @param type the code for the mandatary type
   */
  public void setType(String type) {
    this.type = type;
  }
}
