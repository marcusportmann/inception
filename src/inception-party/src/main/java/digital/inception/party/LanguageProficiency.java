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
 * The <b>LanguageProficiency</b> class holds the information for a language proficiency for a
 * person.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A language proficiency for a person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"language", "listenLevel", "readLevel", "speakLevel", "writeLevel"})
@XmlRootElement(name = "LanguageProficiency", namespace = "http://inception.digital/party")
@XmlType(
    name = "LanguageProficiency",
    namespace = "http://inception.digital/party",
    propOrder = {"language", "listenLevel", "readLevel", "speakLevel", "writeLevel"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "language_proficiencies")
@IdClass(LanguageProficiencyId.class)
public class LanguageProficiency implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The date and time the language proficiency was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The ISO 639-1 alpha-2 code for the language. */
  @Schema(description = "The ISO 639-1 alpha-2 code for the language", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Language", required = true)
  @NotNull
  @Size(min = 2, max = 2)
  @Id
  @Column(name = "language", length = 2, nullable = false)
  private String language;

  /** The listen proficiency level for the language. */
  @NotNull
  @Column(name = "listen_level", length = 15, nullable = false)
  private LanguageProficiencyLevel listenLevel;

  /** The person the language proficiency is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("languageProficiencyReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "person_id")
  private Person person;

  /** The read proficiency level for the language. */
  @NotNull
  @Column(name = "read_level", length = 15, nullable = false)
  private LanguageProficiencyLevel readLevel;

  /** The speak proficiency level for the language. */
  @NotNull
  @Column(name = "speak_level", length = 15, nullable = false)
  private LanguageProficiencyLevel speakLevel;

  /** The date and time the language proficiency was last updated. */
  @JsonIgnore
  @XmlTransient
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** The write proficiency level for the language. */
  @NotNull
  @Column(name = "write_level", length = 15, nullable = false)
  private LanguageProficiencyLevel writeLevel;

  /** Constructs a new <b>LanguageProficiency</b>. */
  public LanguageProficiency() {}

  /**
   * Constructs a new <b>LanguageProficiency</b>.
   *
   * @param language the ISO 639-1 alpha-2 code for the language
   * @param listenLevel the listen proficiency level for the language.
   * @param readLevel the read proficiency level for the language.
   * @param speakLevel the speak proficiency level for the language.
   * @param writeLevel the write proficiency level for the language.
   */
  public LanguageProficiency(
      String language,
      LanguageProficiencyLevel listenLevel,
      LanguageProficiencyLevel readLevel,
      LanguageProficiencyLevel speakLevel,
      LanguageProficiencyLevel writeLevel) {
    this.language = language;
    this.listenLevel = listenLevel;
    this.readLevel = readLevel;
    this.speakLevel = speakLevel;
    this.writeLevel = writeLevel;
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

    LanguageProficiency other = (LanguageProficiency) object;

    return Objects.equals(person, other.person) && Objects.equals(language, other.language);
  }

  /**
   * Returns the date and time the language proficiency was created.
   *
   * @return the date and time the language proficiency was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the ISO 639-1 alpha-2 code for the language.
   *
   * @return the ISO 639-1 alpha-2 code for the language
   */
  public String getLanguage() {
    return language;
  }

  /**
   * Returns the listen proficiency level for the language.
   *
   * @return the listen proficiency level for the language
   */
  public LanguageProficiencyLevel getListenLevel() {
    return listenLevel;
  }

  /**
   * Returns the person the language proficiency is associated with.
   *
   * @return the person the language proficiency is associated with
   */
  @Schema(hidden = true)
  public Person getPerson() {
    return person;
  }

  /**
   * Returns the read proficiency level for the language.
   *
   * @return the read proficiency level for the language
   */
  public LanguageProficiencyLevel getReadLevel() {
    return readLevel;
  }

  /**
   * Returns the speak proficiency level for the language.
   *
   * @return the speak proficiency level for the language
   */
  public LanguageProficiencyLevel getSpeakLevel() {
    return speakLevel;
  }

  /**
   * Returns the date and time the language proficiency was last updated.
   *
   * @return the date and time the language proficiency was last updated
   */
  public LocalDateTime getUpdated() {
    return updated;
  }

  /**
   * Returns the write proficiency level for the language.
   *
   * @return the write proficiency level for the language
   */
  public LanguageProficiencyLevel getWriteLevel() {
    return writeLevel;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (((person == null) || (person.getId() == null)) ? 0 : person.getId().hashCode())
        + ((language == null) ? 0 : language.hashCode());
  }

  /**
   * Set the ISO 639-1 alpha-2 code for the language.
   *
   * @param language the ISO 639-1 alpha-2 code for the language
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Set the listen proficiency level for the language.
   *
   * @param listenLevel the listen proficiency level for the language
   */
  public void setListenLevel(LanguageProficiencyLevel listenLevel) {
    this.listenLevel = listenLevel;
  }

  /**
   * Set the person the language proficiency is associated with.
   *
   * @param person the person the language proficiency is associated with
   */
  @Schema(hidden = true)
  public void setPerson(Person person) {
    this.person = person;
  }

  /**
   * Set the read proficiency level for the language.
   *
   * @param readLevel the read proficiency level for the language
   */
  public void setReadLevel(LanguageProficiencyLevel readLevel) {
    this.readLevel = readLevel;
  }

  /**
   * Set the speak proficiency level for the language.
   *
   * @param speakLevel the speak proficiency level for the language
   */
  public void setSpeakLevel(LanguageProficiencyLevel speakLevel) {
    this.speakLevel = speakLevel;
  }

  /**
   * Set the write proficiency level for the language.
   *
   * @param writeLevel the write proficiency level for the language
   */
  public void setWriteLevel(LanguageProficiencyLevel writeLevel) {
    this.writeLevel = writeLevel;
  }
}
