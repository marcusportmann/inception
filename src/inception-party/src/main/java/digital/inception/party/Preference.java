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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The <b>Preference</b> class holds the information for a preference.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A preference")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "value"})
@XmlRootElement(name = "Preference", namespace = "http://inception.digital/party")
@XmlType(
    name = "Preference",
    namespace = "http://inception.digital/party",
    propOrder = {"type", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "preferences")
@IdClass(PreferenceId.class)
public class Preference implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The date and time the preference was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The party the preference is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("preferenceReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private PartyBase party;

  /** The code for the preference type. */
  @Schema(description = "The code for the preference type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "type", length = 30, nullable = false)
  private String type;

  /** The date and time the preference was last updated. */
  @JsonIgnore
  @XmlTransient
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** The value for the preference. */
  @Schema(description = "The value for the preference", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(min = 1, max = 200)
  @Column(name = "value", length = 200, nullable = false)
  private String value;

  /** Constructs a new <b>Preference</b>. */
  public Preference() {}

  /**
   * Constructs a new <b>Preference</b>.
   *
   * @param type the preference type
   * @param value the value for the preference
   */
  public Preference(String type, String value) {
    this.type = type;
    this.value = value;
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

    Preference other = (Preference) object;

    return Objects.equals(party, other.party) && Objects.equals(type, other.type);
  }

  /**
   * Returns the date and time the preference was created.
   *
   * @return the date and time the preference was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the party the preference is associated with.
   *
   * @return the party the preference is associated with
   */
  @Schema(hidden = true)
  public PartyBase getParty() {
    return party;
  }

  /**
   * Returns the code for the preference type.
   *
   * @return the code for the preference type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the date and time the preference was last updated.
   *
   * @return the date and time the preference was last updated
   */
  public LocalDateTime getUpdated() {
    return updated;
  }

  /**
   * Returns the value for the preference.
   *
   * @return the value for the preference
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
    return (((party == null) || (party.getId() == null)) ? 0 : party.getId().hashCode())
        + ((type == null) ? 0 : type.hashCode());
  }

  /**
   * Set the party the preference is associated with.
   *
   * @param party the party the preference is associated with
   */
  @Schema(hidden = true)
  public void setParty(PartyBase party) {
    this.party = party;
  }

  /**
   * Set the code for the preference type.
   *
   * @param type the code for the preference type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Set the value for the preference.
   *
   * @param value the value for the preference
   */
  public void setValue(String value) {
    this.value = value;
  }
}
