/*
 * Copyright 2018 Marcus Portmann
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
import digital.inception.core.util.StringUtil;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>RestControllerError</code> class holds the information for an error returned by a
 * RESTful web service.
 *
 * @author Marcus Portmann
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "path", "timestamp", "status", "message", "detail", "exception", "stackTrace",
    "name", "validationErrors" })
@SuppressWarnings({ "unused", "WeakerAccess" })
public class RestControllerError
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The URI for the HTTP request that resulted in the error.
   */
  @JsonProperty
  private String path;

  /**
   * The date and time the error occurred.
   */
  @JsonProperty
  private LocalDateTime timestamp;

  /**
   * The HTTP status for the error.
   */
  @JsonProperty
  private String status;

  /**
   * The message.
   */
  @JsonProperty
  private String message;

  /**
   * The detail.
   */
  @JsonProperty
  private String detail;

  /**
   * The fully qualified name of the exception associated with the error.
   */
  @JsonProperty
  private String exception;

  /**
   * The stack trace associated with the error.
   */
  @JsonProperty
  private String stackTrace;

  /**
   * The name of the entity associated with the error e.g. the name of the argument or parameter.
   */
  @JsonProperty
  private String name;

  /**
   * The validation errors associated with the error.
   */
  @JsonProperty
  private List<Object> validationErrors;

  /**
   * Constructs a new <code>ApplicationError</code>.
   *
   * @param cause the exception
   */
  public RestControllerError(HttpServletRequest request, HttpStatus responseStatus, Throwable cause)
  {
    this.timestamp = LocalDateTime.now();

    ResponseStatus annotation = AnnotatedElementUtils.findMergedAnnotation(cause.getClass(),
        ResponseStatus.class);

    if (annotation != null)
    {
      // Use the HTTP response status specified through the @ResponseStatus annotation
      responseStatus = annotation.value();

      if (!StringUtil.isNullOrEmpty(annotation.reason()))
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

    this.status = responseStatus.getReasonPhrase() + " (" + responseStatus.value() + ")";

    if ((annotation == null) || (annotation.value().is5xxServerError()))
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

    this.path = request.getRequestURI();

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
   * Returns the detail.
   *
   * @return the detail
   */
  public String getDetail()
  {
    return detail;
  }

  /**
   * Returns the fully qualified name of the exception associated with the error.
   *
   * @return the fully qualified name of the exception associated with the error
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
   * Returns the name of the entity associated with the error e.g. the name of the argument or
   * parameter.
   *
   * @return the name of the entity associated with the error
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the URI for the HTTP request that resulted in the error.
   *
   * @return the URI for the HTTP request that resulted in the error
   */
  public String getPath()
  {
    return path;
  }

  /**
   * Returns the stack trace associated with the error.
   *
   * @return the stack trace associated with the error
   */
  public String getStackTrace()
  {
    return stackTrace;
  }

  /**
   * Returns the HTTP status for the error.
   *
   * @return the HTTP status for the error
   */
  public String getStatus()
  {
    return status;
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
   * Set the detail.
   *
   * @param detail the detail
   */
  public void setDetail(String detail)
  {
    this.detail = detail;
  }

  /**
   * Set the fully qualified name of the exception associated with the error.
   *
   * @param exception the fully qualified name of the exception associated with the error
   */
  public void setException(String exception)
  {
    this.exception = exception;
  }

  /**
   * Set the message.
   *
   * @param message the message
   */
  public void setMessage(String message)
  {
    this.message = message;
  }

  /**
   * Set the name of the entity associated with the error e.g. the name of the argument or
   * parameter.
   *
   * @param name the name of the entity associated with the error
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the URI for the HTTP request that resulted in the error.
   *
   * @param path the URI for the HTTP request that resulted in the error
   */
  public void setPath(String path)
  {
    this.path = path;
  }

  /**
   * Set the stack trace associated with the error.
   *
   * @param stackTrace the stack trace associated with the error
   */
  public void setStackTrace(String stackTrace)
  {
    this.stackTrace = stackTrace;
  }

  /**
   * Set the HTTP status for the error.
   *
   * @param status the HTTP status for the error
   */
  public void setStatus(String status)
  {
    this.status = status;
  }

  /**
   * Set the date and time the error occurred.
   *
   * @param timestamp the date and time the error occurred
   */
  public void setTimestamp(LocalDateTime timestamp)
  {
    this.timestamp = timestamp;
  }
}
