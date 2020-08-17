/*
 * Copyright 2020 Marcus Portmann
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
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
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
 * The <code>Organization</code> class holds the information for an organization.
 *
 * @author Marcus Portmann
 */
@Schema(description = "Organization")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name"})
@XmlRootElement(name = "Organization", namespace = "http://party.inception.digital")
@XmlType(
    name = "Organization",
    namespace = "http://party.inception.digital",
    propOrder = {"id", "name"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "party")
@SecondaryTable(
    schema = "party",
    name = "organization",
    pkJoinColumns = {@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")})
public class Organization {

  /** The type of party for the organization. */
  @JsonIgnore
  @XmlTransient
  @NotNull
  @Column(name = "type", nullable = false)
  private final PartyType type = PartyType.TENANT;

  /** The Universally Unique Identifier (UUID) uniquely identifying the organization. */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) uniquely identifying the organization",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The name of the organization. */
  @Schema(description = "The name of the organization", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(max = 100)
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /** Constructs a new <code>Organization</code>. */
  public Organization() {}

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the organization.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the organization
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the name of the organization.
   *
   * @return the name of the organization
   */
  public String getName() {
    return name;
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the organization.
   *
   * @param id the Universally Unique Identifier (UUID) uniquely identifying the organization
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the name of the organization.
   *
   * @param name the name of the organization
   */
  public void setName(String name) {
    this.name = name;
  }
}
