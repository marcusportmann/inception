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

package digital.inception.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.service.ValidationError;
import digital.inception.core.xml.LocalDateTimeAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * The <b>ProblemDetails</b> class holds the information for a Problem Details Object as defined in
 * RFC 7807.
 *
 * <p>It contains a number of additional members, beyond those defined by the specification, which
 * hold information related to validation errors and support debugging.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A problem details object, as defined by RFC 7807, which holds the information for an error returned by an API")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "timestamp",
  "type",
  "title",
  "status",
  "detail",
  "parameter",
  "validationErrors",
  "stackTrace"
})
public class ProblemDetails {

  /** The human-readable explanation specific to this occurrence of the problem. */
  @Schema(
      description = "The human-readable explanation specific to this occurrence of the problem",
      required = true)
  @JsonProperty(required = true)
  private String detail;

  /** The optional name of the parameter associated with the problem. */
  @Schema(description = "The optional name of the parameter associated with the problem")
  @JsonProperty
  private String parameter;

  /** The optional stack trace generated by the origin server for the problem. */
  @Schema(description = "The optional stack trace generated by the origin server for the problem")
  @JsonProperty
  private String stackTrace;

  /** The HTTP status code generated by the origin server for this occurrence of the problem. */
  @Schema(
      description =
          "The HTTP status code generated by the origin server for this occurrence of the problem",
      required = true)
  @JsonProperty(required = true)
  private int status;

  /** The date and time the problem occurred. */
  @Schema(description = "The date and time the problem occurred", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Timestamp", required = true)
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  private LocalDateTime timestamp;

  /**
   * The short, human-readable summary of the problem type; it should not change from occurrence to
   * occurrence of the problem, except for purposes of localization.
   */
  @Schema(
      description =
          "The short, human-readable summary of the problem type; it should not change from occurrence to occurrence of the problem, except for purposes of localization",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Title", required = true)
  private String title;

  /** The URI reference that identifies the problem type. */
  @Schema(description = "The URI reference that identifies the problem type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  private String type;

  /** The optional validation errors associated with the problem. */
  @Schema(description = "The optional validation errors associated with the problem")
  @JsonProperty
  private List<ValidationError> validationErrors;

  /** Constructs a new <b>ProblemDetails</b>. */
  public ProblemDetails() {}

  /**
   * Returns the human-readable explanation specific to this occurrence of the problem.
   *
   * @return the human-readable explanation specific to this occurrence of the problem
   */
  public String getDetail() {
    return detail;
  }

  /**
   * Returns the optional name of the parameter associated with the problem.
   *
   * @return the optional name of the parameter associated with the problem
   */
  public String getParameter() {
    return parameter;
  }

  /**
   * Returns the optional stack trace generated by the origin server for the problem.
   *
   * @return the optional stack trace generated by the origin server for the problem
   */
  public String getStackTrace() {
    return stackTrace;
  }

  /**
   * Returns the HTTP status code generated by the origin server for this occurrence of the problem.
   *
   * @return the HTTP status code generated by the origin server for this occurrence of the problem
   */
  public int getStatus() {
    return status;
  }

  /**
   * Returns the date and time the problem occurred.
   *
   * @return the date and time the problem occurred
   */
  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  /**
   * Returns the short, human-readable summary of the problem type.
   *
   * @return the short, human-readable summary of the problem type
   */
  public String getTitle() {
    return title;
  }

  /**
   * Returns the URI reference that identifies the problem type.
   *
   * @return the URI reference that identifies the problem type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the optional validation errors associated with the problem.
   *
   * @return the optional validation errors associated with the problem
   */
  public List<ValidationError> getValidationErrors() {
    return validationErrors;
  }

  /**
   * Set the human-readable explanation specific to this occurrence of the problem.
   *
   * @param detail the human-readable explanation specific to this occurrence of the problem
   */
  public void setDetail(String detail) {
    this.detail = detail;
  }

  /**
   * Set the name of the parameter associated with the problem.
   *
   * @param parameter the name of the parameter associated with the problem
   */
  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  /**
   * Set the stack trace generated by the origin server for the problem.
   *
   * @param stackTrace the stack trace generated by the origin server for the problem
   */
  public void setStackTrace(String stackTrace) {
    this.stackTrace = stackTrace;
  }

  /**
   * Set the HTTP status code generated by the origin server for this occurrence of the problem.
   *
   * @param status the HTTP status code generated by the origin server for this occurrence of the
   *     problem
   */
  public void setStatus(int status) {
    this.status = status;
  }

  /**
   * Set the date and time the problem occurred.
   *
   * @param timestamp the date and time the problem occurred
   */
  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Set the short, human-readable summary of the problem type.
   *
   * @param title the short, human-readable summary of the problem type
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Set the URI reference that identifies the problem type.
   *
   * @param type the URI reference that identifies the problem type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Set the validation errors associated with the problem.
   *
   * @param validationErrors the validation errors associated with the problem
   */
  public void setValidationErrors(List<ValidationError> validationErrors) {
    this.validationErrors = validationErrors;
  }
}
