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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
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

/**
 * The <b>Skill</b> class holds the information for a skill possessed by a person.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A skill possessed by a person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "level"})
@XmlRootElement(name = "Skill", namespace = "http://inception.digital/party")
@XmlType(
    name = "Skill",
    namespace = "http://inception.digital/party",
    propOrder = {"type", "level"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "skills")
@IdClass(SkillId.class)
public class Skill implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The proficiency level for the skill. */
  @Schema(
      description = "The proficiency level for the skill",
      required = true,
      allowableValues = {"novice", "advanced_beginner", "competence", "proficient", "expert"})
  @JsonProperty(required = true)
  @XmlElement(name = "Level", required = true)
  @NotNull
  @Column(name = "level", length = 20, nullable = false)
  private SkillProficiencyLevel level;

  /** The person the skill is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("skillReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "person_id")
  private Person person;

  /** The code for the skill type. */
  @Schema(description = "The code for the skill type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "type", length = 30, nullable = false)
  private String type;

  /** Constructs a new <b>Skill</b>. */
  public Skill() {}

  /**
   * Constructs a new <b>Skill</b>.
   *
   * @param type the skill type
   * @param level the proficiency level for the skill
   */
  public Skill(String type, SkillProficiencyLevel level) {
    this.type = type;
    this.level = level;
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

    Skill other = (Skill) object;

    return Objects.equals(person, other.person) && Objects.equals(type, other.type);
  }

  /**
   * Returns the proficiency level for the skill.
   *
   * @return the proficiency level for the skill
   */
  public SkillProficiencyLevel getLevel() {
    return level;
  }

  /**
   * Returns the person the skill is associated with.
   *
   * @return the person the skill is associated with
   */
  @Schema(hidden = true)
  public Person getPerson() {
    return person;
  }

  /**
   * Returns the code for the skill type.
   *
   * @return the code for the skill type
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
    return (((person == null) || (person.getId() == null)) ? 0 : person.getId().hashCode())
        + ((type == null) ? 0 : type.hashCode());
  }

  /**
   * Set the proficiency level for the skill.
   *
   * @param level the proficiency level for the skill
   */
  public void setLevel(SkillProficiencyLevel level) {
    this.level = level;
  }

  /**
   * Set the person the skill is associated with.
   *
   * @param person the party the skill is associated with
   */
  @Schema(hidden = true)
  public void setPerson(Person person) {
    this.person = person;
  }

  /**
   * Set the code for the skill type.
   *
   * @param type the code for the skill type
   */
  public void setType(String type) {
    this.type = type;
  }
}
