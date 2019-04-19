/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.rs;

//~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.springframework.util.StringUtils;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;

import java.lang.reflect.Method;

import java.time.LocalDateTime;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * The <code>RestControllerError</code> class holds the information for an error returned by a
 * RESTful web service.
 *
 * @author Marcus Portmann
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "uri", "timestamp", "status", "statusText", "message", "detail", "exception",
    "stackTrace", "name", "validationErrors" })
@SuppressWarnings({ "unused", "WeakerAccess" })
public class RestControllerError
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The optional detail.
   */
  @JsonProperty
  private String detail;

  /**
   * The optional fully qualified name of the exception associated with the error.
   */
  @JsonProperty
  private String exception;

  /**
   * The message.
   */
  @JsonProperty
  private String message;

  /**
   * The optional name of the entity associated with the error e.g. the name of the argument or
   * parameter.
   */
  @JsonProperty
  private String name;

  /**
   * The optional stack trace associated with the error.
   */
  @JsonProperty
  private String stackTrace;

  /**
   * The HTTP status-code for the error.
   */
  @JsonProperty
  private int status;

  /**
   * The HTTP reason-phrase for the HTTP status-code for the error.
   */
  @JsonProperty
  private String statusText;

  /**
   * The date and time the error occurred.
   */
  @JsonProperty
  private LocalDateTime timestamp;

  /**
   * The URI for the HTTP request that resulted in the error.
   */
  @JsonProperty
  private String uri;

  /**
   * The optional validation errors associated with the error.
   */
  @JsonProperty
  private List<Object> validationErrors;

  /**
   * Constructs a new <code>ApplicationError</code>.
   *
   * @param request        the HTTP servlet request
   * @param responseStatus the HTTP response status
   * @param cause          the exception
   */
  @SuppressWarnings("unchecked")
  public RestControllerError(HttpServletRequest request, HttpStatus responseStatus, Throwable cause)
  {
    this.timestamp = LocalDateTime.now();

    ResponseStatus annotation = AnnotatedElementUtils.findMergedAnnotation(cause.getClass(),
        ResponseStatus.class);

    if (annotation != null)
    {
      // Use the HTTP response status specified through the @ResponseStatus annotation
      responseStatus = annotation.value();

      if (!StringUtils.isEmpty(annotation.reason()))
      {
        this.message = annotation.reason();
        this.detail = cause.getMessage();
      }
      else
      {
        this.message = cause.getMessage();
      }
    }
    else
    {
      this.message = cause.getMessage();
    }

    if (cause instanceof org.springframework.security.access.AccessDeniedException)
    {
      responseStatus = HttpStatus.FORBIDDEN;
    }
    else if ((annotation == null) || (annotation.value().is5xxServerError()))
    {
      this.exception = cause.getClass().getName();

      try
      {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);

        pw.println(cause.getMessage());
        pw.println();

        cause.printStackTrace(pw);

        pw.flush();

        this.stackTrace = baos.toString();
      }
      catch (Throwable ignored) {}
    }

    this.uri = request.getRequestURI();

    this.status = responseStatus.value();

    this.statusText = responseStatus.getReasonPhrase();

    try
    {
      if (cause.getClass().getName().equals(
          "digital.inception.validation.InvalidArgumentException"))
      {
        Method getNameMethod = cause.getClass().getMethod("getName");

        if (getNameMethod != null)
        {
          this.name = (String) getNameMethod.invoke(cause);
        }

        Method getValidationErrorsMethod = cause.getClass().getMethod("getValidationErrors");

        if (getValidationErrorsMethod != null)
        {
          validationErrors = (List<Object>) getValidationErrorsMethod.invoke(cause);
        }
      }
    }
    catch (Throwable ignored) {}
  }

  /**
   * Returns the optional detail.
   *
   * @return the optional detail
   */
  public String getDetail()
  {
    return detail;
  }

  /**
   * Returns the optional fully qualified name of the exception associated with the error.
   *
   * @return the optional fully qualified name of the exception associated with the error
   */
  public String getException()
  {
    return exception;
  }

  /**
   * Returns the message.
   *
   * @return the message
   */
  public String getMessage()
  {
    return message;
  }

  /**
   * Returns the optional name of the entity associated with the error e.g. the name of the argument
   * or parameter.
   *
   * @return the optional name of the entity associated with the error
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the optional stack trace associated with the error.
   *
   * @return the optional stack trace associated with the error
   */
  public String getStackTrace()
  {
    return stackTrace;
  }

  /**
   * Returns the HTTP status-code for the error.
   *
   * @return the HTTP status-code for the error
   */
  public int getStatus()
  {
    return status;
  }

  /**
   * Returns the HTTP reason-phrase for the HTTP status-code for the error.
   *
   * @return the HTTP reason-phrase for the HTTP status-code for the error
   */
  public String getStatusText()
  {
    return statusText;
  }

  /**
   * Returns the date and time the error occurred.
   *
   * @return the date and time the error occurred
   */
  public LocalDateTime getTimestamp()
  {
    return timestamp;
  }

  /**
   * Returns the URI for the HTTP request that resulted in the error.
   *
   * @return the URI for the HTTP request that resulted in the error
   */
  public String getURI()
  {
    return uri;
  }
}
