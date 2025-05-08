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

package digital.inception.mail.model;

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

/**
 * The {@code MailTemplateSummary} class holds the summary information for a mail template.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A mail template summary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name", "contentType"})
@XmlRootElement(name = "MailTemplateSummary", namespace = "https://inception.digital/mail")
@XmlType(
    name = "MailTemplateSummary",
    namespace = "https://inception.digital/mail",
    propOrder = {"id", "name", "contentType"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "mail_mail_templates")
@SuppressWarnings({"unused"})
public class MailTemplateSummary implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The content type for the mail template. */
  @Schema(
      description = "The content type for the mail template",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ContentType", required = true)
  @NotNull
  @Column(name = "content_type", length = 50, nullable = false)
  private MailTemplateContentType contentType;

  /** The ID for the mail template. */
  @Schema(description = "The ID for the mail template", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Id
  @Column(name = "id", length = 100, nullable = false)
  private String id;

  /** The name of the mail template. */
  @Schema(
      description = "The name of the mail template",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "name", length = 100, nullable = false)
  private String name;

  /** Creates a new {@code MailTemplateSummary} instance. */
  public MailTemplateSummary() {}

  /**
   * Creates a new {@code MailTemplateSummary} instance.
   *
   * @param id the ID for the mail template
   * @param name the name of the mail template
   * @param contentType the content type for the mail template
   */
  public MailTemplateSummary(String id, String name, MailTemplateContentType contentType) {
    this.id = id;
    this.name = name;
    this.contentType = contentType;
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

    MailTemplateSummary other = (MailTemplateSummary) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the content type for the mail template.
   *
   * @return the content type for the mail template
   */
  public MailTemplateContentType getContentType() {
    return contentType;
  }

  /**
   * Returns the ID for the mail template.
   *
   * @return the ID for the mail template
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the name of the mail template.
   *
   * @return the name of the mail template
   */
  public String getName() {
    return name;
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
   * Returns a string representation of the object.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString() {
    return "MailTemplateSummary {id=\""
        + getId()
        + "\", name=\""
        + getName()
        + "\", contentType=\""
        + getContentType()
        + "\"}";
  }
}
