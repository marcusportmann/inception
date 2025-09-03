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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code LanguageProficiency} class holds the information for a language proficiency for a
 * person.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A language proficiency for a person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"language", "listenLevel", "readLevel", "speakLevel", "writeLevel"})
@XmlRootElement(name = "LanguageProficiency", namespace = "https://inception.digital/party")
@XmlType(
    name = "LanguageProficiency",
    namespace = "https://inception.digital/party",
    propOrder = {"language", "listenLevel", "readLevel", "speakLevel", "writeLevel"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_language_proficiencies")
@IdClass(LanguageProficiencyId.class)
@SuppressWarnings("unused")
public class LanguageProficiency implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ISO 639-1 alpha-2 code for the language. */
  @Schema(
      description = "The ISO 639-1 alpha-2 code for the language",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Language", required = true)
  @NotNull
  @Size(min = 2, max = 2)
  @Id
  @Column(name = "language", length = 2, nullable = false)
  private String language;

  /** The listen proficiency level for the language. */
  @Schema(
      description = "The listen proficiency level for the language",
      requiredMode = Schema.RequiredMode.REQUIRED,
      allowableValues = {"beginner", "elementary", "intermediate", "advanced", "proficient"})
  @JsonProperty(required = true)
  @XmlElement(name = "ListenLevel", required = true)
  @NotNull
  @Column(name = "listen_level", length = 15, nullable = false)
  private LanguageProficiencyLevel listenLevel;

  /** The ID for the person the language proficiency is associated with. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "person_id", nullable = false)
  private UUID personId;

  /** The read proficiency level for the language. */
  @Schema(
      description = "The read proficiency level for the language",
      requiredMode = Schema.RequiredMode.REQUIRED,
      allowableValues = {"beginner", "elementary", "intermediate", "advanced", "proficient"})
  @JsonProperty(required = true)
  @XmlElement(name = "ReadLevel", required = true)
  @NotNull
  @Column(name = "read_level", length = 15, nullable = false)
  private LanguageProficiencyLevel readLevel;

  /** The speak proficiency level for the language. */
  @Schema(
      description = "The speak proficiency level for the language",
      requiredMode = Schema.RequiredMode.REQUIRED,
      allowableValues = {"beginner", "elementary", "intermediate", "advanced", "proficient"})
  @JsonProperty(required = true)
  @XmlElement(name = "SpeakLevel", required = true)
  @NotNull
  @Column(name = "speak_level", length = 15, nullable = false)
  private LanguageProficiencyLevel speakLevel;

  /** The write proficiency level for the language. */
  @Schema(
      description = "The write proficiency level for the language",
      requiredMode = Schema.RequiredMode.REQUIRED,
      allowableValues = {"beginner", "elementary", "intermediate", "advanced", "proficient"})
  @JsonProperty(required = true)
  @XmlElement(name = "WriteLevel", required = true)
  @NotNull
  @Column(name = "write_level", length = 15, nullable = false)
  private LanguageProficiencyLevel writeLevel;

  /** Constructs a new {@code LanguageProficiency}. */
  public LanguageProficiency() {}

  /**
   * Constructs a new {@code LanguageProficiency}.
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

    LanguageProficiency other = (LanguageProficiency) object;

    return Objects.equals(personId, other.personId) && Objects.equals(language, other.language);
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
    return ((personId == null) ? 0 : personId.hashCode())
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
  @JsonBackReference("languageProficiencyReference")
  @Schema(hidden = true)
  public void setPerson(Person person) {
    if (person != null) {
      personId = person.getId();
    } else {
      personId = null;
    }
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

  /**
   * Called by the JAXB runtime an instance of this class has been completely unmarshalled, but
   * before it is added to its parent.
   *
   * @param unmarshaller the JAXB unmarshaller
   * @param parentObject the parent object
   */
  @SuppressWarnings("unused")
  private void afterUnmarshal(Unmarshaller unmarshaller, Object parentObject) {
    if (parentObject instanceof Person parent) {
      setPerson(parent);
    }
  }
}
