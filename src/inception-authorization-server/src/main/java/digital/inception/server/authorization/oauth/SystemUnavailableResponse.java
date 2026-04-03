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

package digital.inception.server.authorization.oauth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import digital.inception.core.time.ApplicationClock;
import digital.inception.core.util.ISO8601Util;
import digital.inception.json.JsonUtil;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;

/**
 * The {@code SystemUnavailableResponse} class holds the information for a response returned to
 * indicated that the system encountered an error and is unable to process the OAuth2 request.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class SystemUnavailableResponse extends Response {

  /** The message. */
  private final String message;

  /** The date and time the error occurred. */
  @JsonProperty private final String timestamp;

  /** The detail. */
  @JsonProperty private String detail;

  /** The fully qualified name of the exception associated with the error. */
  @JsonProperty private String exception;

  /** The stack trace associated with the error. */
  @JsonProperty private String stackTrace;

  /** The URI for the HTTP request that resulted in the error. */
  @JsonProperty private String uri;

  /**
   * Constructs a new {@code SystemUnavailableResponse}.
   *
   * @param message the message
   */
  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public SystemUnavailableResponse(String message) {
    this(message, null);
  }

  /**
   * Constructs a new {@code SystemUnavailableResponse}.
   *
   * @param message the message
   * @param cause the cause
   */
  @JsonCreator(mode = JsonCreator.Mode.DISABLED)
  public SystemUnavailableResponse(String message, Throwable cause) {
    super(HttpStatus.INTERNAL_SERVER_ERROR);

    OffsetDateTime now = ApplicationClock.offsetNow();

    this.timestamp = ISO8601Util.fromOffsetDateTime(now);
    this.message = message;

    if (cause != null) {
      this.detail = cause.getMessage();
      this.exception = cause.getClass().getName();

      try {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        printWriter.println(cause.getMessage());
        printWriter.println();
        cause.printStackTrace(printWriter);
        printWriter.flush();

        this.stackTrace = stringWriter.toString();
      } catch (Throwable ignored) {
      }
    }
  }

  /**
   * Returns the body for the OAuth2 response.
   *
   * @return the body for the OAuth2 response
   */
  @Override
  @JsonProperty
  public String getBody() {
    try {
      return JsonUtil.getObjectMapper().writeValueAsString(this);
    } catch (Throwable e) {
      throw new RuntimeException(
          "Failed to construct the body for the system unavailable response", e);
    }
  }

  /**
   * Returns the detail.
   *
   * @return the detail
   */
  public String getDetail() {
    return detail;
  }

  /**
   * Returns the fully qualified name of the exception associated with the error.
   *
   * @return the fully qualified name of the exception associated with the error
   */
  public String getException() {
    return exception;
  }

  /**
   * Returns the message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Returns the stack trace associated with the error.
   *
   * @return the stack trace associated with the error
   */
  public String getStackTrace() {
    return stackTrace;
  }

  /**
   * Returns the date and time the error occurred.
   *
   * @return the date and time the error occurred
   */
  public String getTimestamp() {
    return timestamp;
  }

  /**
   * Returns the URI for the HTTP request that resulted in the error.
   *
   * @return the URI for the HTTP request that resulted in the error
   */
  public String getUri() {
    return uri;
  }

  /**
   * Sets the detail.
   *
   * @param detail the detail
   */
  public void setDetail(String detail) {
    this.detail = detail;
  }

  /**
   * Sets the fully qualified name of the exception associated with the error.
   *
   * @param exception the fully qualified name of the exception associated with the error
   */
  public void setException(String exception) {
    this.exception = exception;
  }

  /**
   * Sets the stack trace associated with the error.
   *
   * @param stackTrace the stack trace associated with the error
   */
  public void setStackTrace(String stackTrace) {
    this.stackTrace = stackTrace;
  }

  /**
   * Sets the URI for the HTTP request that resulted in the error.
   *
   * @param uri the URI for the HTTP request that resulted in the error
   */
  public void setUri(String uri) {
    this.uri = uri;
  }
}
