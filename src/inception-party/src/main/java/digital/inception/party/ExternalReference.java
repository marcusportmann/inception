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
import com.github.f4b6a3.uuid.UuidCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
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
 * The <b>ExternalReference</b> class holds the information for an external reference for an
 * organization or person.
 *
 * <p>The primary key for the external reference entity (ID) is a surrogate key to support the
 * management of related data in one or more external stores, e.g. an image of a supporting document
 * for the external reference stored in an enterprise content management repository. This approach
 * allows an entity to be modified without impacting the related data's referential integrity, for
 * example, when correcting an error that occurred during the initial capture of the information for
 * an external reference.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An external reference for an organization or person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "type", "value"})
@XmlRootElement(name = "ExternalReference", namespace = "http://inception.digital/party")
@XmlType(
    name = "ExternalReference",
    namespace = "http://inception.digital/party",
    propOrder = {"id", "type", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "external_references")
public class ExternalReference implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The date and time the external reference was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The Universally Unique Identifier (UUID) for the external reference. */
  @Schema(
      description = "The Universally Unique Identifier (UUID) for the external reference",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The party the external reference is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("externalReferenceReference")
  @XmlTransient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private PartyBase party;

  /** The code for the external reference type. */
  @Schema(description = "The code for the external reference type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Column(name = "type", length = 30, nullable = false)
  private String type;

  /** The date and time the external reference was last updated. */
  @JsonIgnore
  @XmlTransient
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** The value for the external reference. */
  @Schema(description = "The value for the external reference", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "value", length = 100, nullable = false)
  private String value;

  /** Constructs a new <b>ExternalReference</b>. */
  public ExternalReference() {}

  /**
   * Constructs a new <b>ExternalReference</b>.
   *
   * @param type the code for the external reference type
   * @param value the value for the external reference
   */
  public ExternalReference(String type, String value) {
    this.id = UuidCreator.getShortPrefixComb();
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

    ExternalReference other = (ExternalReference) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the date and time the external reference was created.
   *
   * @return the date and time the external reference was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the external reference.
   *
   * @return the Universally Unique Identifier (UUID) for the external reference
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the party the external reference is associated with.
   *
   * @return the party the external reference is associated with
   */
  @Schema(hidden = true)
  public PartyBase getParty() {
    return party;
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
   * Returns the date and time the external reference was last updated.
   *
   * @return the date and time the external reference was last updated
   */
  public LocalDateTime getUpdated() {
    return updated;
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
    return (id == null) ? 0 : id.hashCode();
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the external reference.
   *
   * @param id the Universally Unique Identifier (UUID) for the external reference
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the party the external reference is associated with.
   *
   * @param party the party the external reference is associated with
   */
  @Schema(hidden = true)
  public void setParty(PartyBase party) {
    this.party = party;
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
}
