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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.f4b6a3.uuid.UuidCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.hibernate.annotations.CreationTimestamp;

/**
 * The <b>PartyHistory</b> class holds the information for a historical view of a party.
 *
 * <p>Changes to parties are recorded as party history records, which include all the data for the
 * party serialized as a JSON data structure.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A historical view of a party")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "partyId", "data"})
@XmlRootElement(name = "PartyHistory", namespace = "http://inception.digital/party")
@XmlType(
    name = "PartyHistory",
    namespace = "http://inception.digital/party",
    propOrder = {"id", "partyId", "data"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "party_history")
public class PartyHistory implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The date and time the party history was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The JSON data for the party representing a historical view of the party. */
  @Schema(
      description = "The JSON data for the party representing a historical view of the party",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Data", required = true)
  @NotNull
  @Column(name = "data", nullable = false)
  private String data;

  /** The Universally Unique Identifier (UUID) for the party history. */
  @Schema(
      description = "The Universally Unique Identifier (UUID) for the party history",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /**
   * The Universally Unique Identifier (UUID) for the party the party history is associated with.
   */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) for the party the party history is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "PartyId", required = true)
  @NotNull
  @Column(name = "party_id", nullable = false)
  private UUID partyId;

  /** Constructs a new <b>PartyHistory</b>. */
  public PartyHistory() {}

  /**
   * Constructs a new <b>PartyHistory</b>.
   *
   * @param partyId the Universally Unique Identifier (UUID) for the party the party history is
   *     associated with
   * @param data the JSON data for the party representing a historical view of the party
   */
  public PartyHistory(UUID partyId, String data) {
    this.id = UuidCreator.getShortPrefixComb();
    this.partyId = partyId;
    this.data = data;
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

    PartyHistory other = (PartyHistory) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the date and time the party history was created.
   *
   * @return the date and time the party history was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the JSON data for the party representing a historical view of the party.
   *
   * @return the JSON data for the party representing a historical view of the party
   */
  public String getData() {
    return data;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the party history.
   *
   * @return the Universally Unique Identifier (UUID) for the party history
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the party the party history is associated
   * with.
   *
   * @return the Universally Unique Identifier (UUID) for the party the party history is associated
   *     with
   */
  public UUID getPartyId() {
    return partyId;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((id == null) ? 0 : id.hashCode());
  }

  /**
   * Set the JSON data for the party representing a historical view of the party.
   *
   * @param data the JSON data for the party representing a historical view of the party
   */
  public void setData(String data) {
    this.data = data;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the party history.
   *
   * @param id the Universally Unique Identifier (UUID) for the party history
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the party the party history is associated
   * with.
   *
   * @param partyId the Universally Unique Identifier (UUID) for the party the party history is
   *     associated with
   */
  public void setPartyId(UUID partyId) {
    this.partyId = partyId;
  }
}
