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

package digital.inception.reference;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The <code>SourceOfFunds</code> class holds the information for a possible source of funds.
 *
 * @author Marcus Portmann
 */
@Schema(description = "SourceOfFunds")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "localeId", "sortIndex", "name", "description"})
@XmlRootElement(name = "SourceOfFunds", namespace = "http://reference.inception.digital")
@XmlType(
    name = "SourceOfFunds",
    namespace = "http://reference.inception.digital",
    propOrder = {"code", "localeId", "sortIndex", "name", "description"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "reference", name = "sources_of_funds")
@IdClass(SourceOfFundsId.class)
public class SourceOfFunds {

  /** The code for the source of funds. */
  @Schema(description = "The code for the source of funds", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 10)
  @Id
  @Column(name = "code", nullable = false)
  private String code;

  /** The description for the source of funds. */
  @Schema(description = "The description for the source of funds", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", nullable = false)
  private String description;

  /** The Unicode locale identifier for the source of funds. */
  @Schema(description = "The Unicode locale identifier for the source of funds", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "LocaleId", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale_id", nullable = false)
  private String localeId;

  /** The name of the source of funds. */
  @Schema(description = "The name of the source of funds", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", nullable = false)
  private String name;

  /** The sort index for the source of funds. */
  @Schema(description = "The sort index for the source of funds", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SortIndex", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortIndex;

  /** Constructs a new <code>SourceOfFunds</code>. */
  public SourceOfFunds() {}

  /**
   * Returns the code for the source of funds.
   *
   * @return the code for the source of funds
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the source of funds.
   *
   * @return the description for the source of funds
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the source of funds.
   *
   * @return the Unicode locale identifier for the source of funds
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Returns the name of the source of funds.
   *
   * @return the name of the source of funds
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the sort index for the source of funds.
   *
   * @return the sort index for the source of funds
   */
  public Integer getSortIndex() {
    return sortIndex;
  }

  /**
   * Set the code for the source of funds.
   *
   * @param code the code for the source of funds
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the description for the source of funds.
   *
   * @param description the description for the source of funds
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the Unicode locale identifier for the source of funds.
   *
   * @param localeId the Unicode locale identifier for the source of funds
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Set the name of the source of funds.
   *
   * @param name the name of the source of funds
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the sort index for the source of funds.
   *
   * @param sortIndex the sort index for the source of funds
   */
  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
  }
}
