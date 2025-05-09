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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import digital.inception.core.util.ISO8601Util;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

/**
 * The {@code SystemUnavailableResponse} class holds the information for a response returned to
 * indicated that the system encountered an error and is unable to process the OAuth2 request.
 *
 * @author Marcus Portmann
 */
public class SystemUnavailableResponse extends Response {

  /** The message. */
  private final String message;

  /** The date and time the error occurred. */
  private final OffsetDateTime timestamp;

  /** The detail. */
  private String detail;

  /** The fully qualified name of the exception associated with the error. */
  private String exception;

  /** The stack trace associated with the error. */
  private String stackTrace;

  /** The URI for the HTTP request that resulted in the error. */
  @JsonProperty private String uri;

  /**
   * Constructs a new {@code SystemUnavailableResponse}.
   *
   * @param message the message
   */
  public SystemUnavailableResponse(String message) {
    this(message, null);
  }

  /**
   * Constructs a new {@code SystemUnavailableResponse}.
   *
   * @param message the message
   * @param cause the cause
   */
  public SystemUnavailableResponse(String message, Throwable cause) {
    super(HttpStatus.INTERNAL_SERVER_ERROR);

    this.timestamp = OffsetDateTime.now();

    this.message = message;

    if (cause != null) {
      this.detail = cause.getMessage();

      this.exception = cause.getClass().getName();

      try {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);

        pw.println(cause.getMessage());
        pw.println();

        cause.printStackTrace(pw);

        pw.flush();

        this.stackTrace = baos.toString();
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
  public String getBody() {
    try {
      JsonFactory jsonFactory = new JsonFactory();

      StringWriter stringWriter = new StringWriter();

      JsonGenerator jsonGenerator = jsonFactory.createGenerator(stringWriter);

      jsonGenerator.writeStartObject();
      jsonGenerator.writeStringField("timestamp", ISO8601Util.fromOffsetDateTime(timestamp));
      jsonGenerator.writeStringField("message", message);
      if (StringUtils.hasText(detail)) {
        jsonGenerator.writeStringField("detail", detail);
      }
      if (StringUtils.hasText(exception)) {
        jsonGenerator.writeStringField("exception", exception);
      }
      if (StringUtils.hasText(stackTrace)) {
        jsonGenerator.writeStringField("exception", stackTrace);
      }

      jsonGenerator.writeEndObject();

      jsonGenerator.close();

      return stringWriter.getBuffer().toString();
    } catch (Throwable e) {
      throw new RuntimeException(
          "Failed to construct the body for the system unavailable response", e);
    }
  }
}
