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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * The <b>DocumentSummary</b> class holds the summary information for a document.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A document")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "definitionId", "fileType", "name", "hash"})
@XmlRootElement(name = "DocumentSummary", namespace = "https://inception.digital/operations")
@XmlType(
    name = "DocumentSummary",
    namespace = "https://inception.digital/operations",
    propOrder = {"id", "definitionId", "fileType", "name", "hash"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "operations_documents")
@SuppressWarnings({"unused", "WeakerAccess"})
public class DocumentSummary implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the document definition the document is associated with. */
  @Schema(
      description = "The ID for the document definition the document is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DefinitionId", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "definition_id", length = 50, nullable = false)
  private String definitionId;

  /** The ID for the document. */
  @Schema(description = "The ID for the document", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** Constructs a new <b>DocumentSummary</b>. */
  public DocumentSummary() {}

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

    DocumentSummary other = (DocumentSummary) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the ID for the document definition the document is associated with.
   *
   * @return the ID for the document definition the document is associated with
   */
  public String getDefinitionId() {
    return definitionId;
  }

  /**
   * Returns the ID for the document.
   *
   * @return the ID for the document
   */
  public UUID getId() {
    return id;
  }

  /**
   * Set the ID for the document definition the document is associated with.
   *
   * @param definitionId the ID for the document definition the document is associated with
   */
  public void setDefinitionId(String definitionId) {
    this.definitionId = definitionId;
  }

  /**
   * Set the ID for the document.
   *
   * @param id the ID for the document
   */
  public void setId(UUID id) {
    this.id = id;
  }
}
