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

package digital.inception.operations.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * The {@code ExternalReference} class holds the information for an external reference.
 *
 * @author Marcus Portmann
 */
@Entity
@Table(name = "operations_external_references")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "object_type", length = 50)
@SuppressWarnings({"unused", "WeakerAccess"})
@IdClass(ExternalReferenceId.class)
public class ExternalReference implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the object the external reference is associated with. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "object_id", nullable = false)
  protected UUID objectId;

  /** The code for the external reference type. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "type", length = 50, nullable = false)
  protected String type;

  /** The value for the external reference. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Column(name = "value", length = 200, nullable = false)
  protected String value;

  /** Constructs a new {@code ExternalReference}. */
  public ExternalReference() {}

  /**
   * Constructs a new {@code ExternalReference}.
   *
   * @param type the code for the external reference type
   * @param value the value for the external reference
   */
  public ExternalReference(String type, String value) {
    this.type = type;
    this.value = value;
  }

  /**
   * Returns the ID for the object the external reference is associated with.
   *
   * @return the ID for the object the external reference is associated with
   */
  public UUID getObjectId() {
    return objectId;
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
   * Returns the value for the external reference.
   *
   * @return the value for the external reference
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the ID for the object the external reference is associated with.
   *
   * @param objectId the ID for the object the external reference is associated with
   */
  public void setObjectId(UUID objectId) {
    this.objectId = objectId;
  }

  /**
   * Sets the code for the external reference type.
   *
   * @param type the code for the external reference type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Sets the value for the external reference.
   *
   * @param value the value for the external reference
   */
  public void setValue(String value) {
    this.value = value;
  }
}
