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
 * The <code>Title</code> class holds the information for a possible title.
 *
 * @author Marcus Portmann
 */
@Schema(description = "Title")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "locale", "sortIndex", "name", "", "description"})
@XmlRootElement(name = "Title", namespace = "http://reference.inception.digital")
@XmlType(
    name = "Title",
    namespace = "http://reference.inception.digital",
    propOrder = {"code", "locale", "sortIndex", "name", "", "description"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "reference", name = "titles")
@IdClass(TitleId.class)
public class Title {

  /** The abbreviation for the title. */
  @Schema(description = "The abbreviation for the title", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Abbreviation", required = true)
  @NotNull
  @Size(min = 1, max = 20)
  @Column(name = "abbreviation", nullable = false)
  private String abbreviation;

  /** The code for the title. */
  @Schema(description = "The code for the title", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Code", required = true)
  @NotNull
  @Size(min = 1, max = 10)
  @Id
  @Column(name = "code", nullable = false)
  private String code;

  /** The description for the title. */
  @Schema(description = "The description for the title", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 200)
  @Column(name = "description", nullable = false)
  private String description;

  /** The Unicode locale identifier for the title. */
  @Schema(description = "The Unicode locale identifier for the title", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Locale", required = true)
  @NotNull
  @Size(min = 2, max = 10)
  @Id
  @Column(name = "locale", nullable = false)
  private String locale;

  /** The name of the title. */
  @Schema(description = "The name of the title", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "name", nullable = false)
  private String name;

  /** The sort index for the title. */
  @Schema(description = "The sort index for the title", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SortIndex", required = true)
  @NotNull
  @Column(name = "sort_index", nullable = false)
  private Integer sortIndex;

  /** Constructs a new <code>Title</code>. */
  public Title() {}

  /**
   * Returns the abbreviation for the title.
   *
   * @return the abbreviation for the title
   */
  public String getAbbreviation() {
    return abbreviation;
  }

  /**
   * Returns the code for the title.
   *
   * @return the code for the title
   */
  public String getCode() {
    return code;
  }

  /**
   * Returns the description for the title.
   *
   * @return the description for the title
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the Unicode locale identifier for the title.
   *
   * @return the Unicode locale identifier for the title
   */
  public String getLocale() {
    return locale;
  }

  /**
   * Returns the name of the title.
   *
   * @return the name of the title
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the sort index for the title.
   *
   * @return the sort index for the title
   */
  public Integer getSortIndex() {
    return sortIndex;
  }

  /**
   * Set the abbreviation for the title.
   *
   * @param abbreviation the abbreviation for the title
   */
  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  /**
   * Set the code for the title.
   *
   * @param code the code for the title
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Set the description for the title.
   *
   * @param description the description for the title
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the Unicode locale identifier for the title.
   *
   * @param localeId the Unicode locale identifier for the title
   */
  public void setLocale(String localeId) {
    this.locale = localeId;
  }

  /**
   * Set the name of the title.
   *
   * @param name the name of the title
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the sort index for the title.
   *
   * @param sortIndex the sort index for the title
   */
  public void setSortIndex(Integer sortIndex) {
    this.sortIndex = sortIndex;
  }
}
