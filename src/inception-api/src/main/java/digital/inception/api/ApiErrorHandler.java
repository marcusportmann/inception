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

import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.validation.InvalidArgumentException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The <b>ApiErrorHandler</b> class implements the error handler for APIs implemented as RESTful web
 * services.
 *
 * @author Marcus Portmann
 */
@ControllerAdvice
@SuppressWarnings("unused")
public class ApiErrorHandler {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ApiErrorHandler.class);

  @Value("${inception.rs.debug:#{false}}")
  private boolean debug;

  public ApiErrorHandler() {}

  @ExceptionHandler
  @ResponseBody
  protected ResponseEntity<ApiError> handle(
      HttpServletRequest request, HttpMessageNotReadableException ex) {
    return new ResponseEntity<>(
        new ApiError(request, HttpStatus.BAD_REQUEST, ex.getMostSpecificCause(), debug),
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  @ResponseBody
  protected ResponseEntity<ApiError> handle(HttpServletRequest request, Throwable cause) {
    ResponseStatus annotation =
        AnnotatedElementUtils.findMergedAnnotation(cause.getClass(), ResponseStatus.class);

    if (annotation != null) {
      HttpStatus responseStatus = annotation.value();

      return new ResponseEntity<>(
          new ApiError(request, responseStatus, cause, debug), new HttpHeaders(), responseStatus);
    } else {
      if (cause instanceof AccessDeniedException) {
        return new ResponseEntity<>(
            new ApiError(request, HttpStatus.FORBIDDEN, cause, debug),
            new HttpHeaders(),
            HttpStatus.FORBIDDEN);
      } else if (cause instanceof InvalidArgumentException) {
        return new ResponseEntity<>(
            new ApiError(request, HttpStatus.BAD_REQUEST, cause, debug),
            new HttpHeaders(),
            HttpStatus.BAD_REQUEST);
      } else if (cause instanceof ServiceUnavailableException) {
        if (debug) {
          logger.warn(
              "Failed to process the RESTful web service request for the API ("
                  + request.getRequestURI()
                  + "): "
                  + cause.getMessage(),
              cause);
        }

        return new ResponseEntity<>(
            new ApiError(request, HttpStatus.INTERNAL_SERVER_ERROR, cause, debug),
            new HttpHeaders(),
            HttpStatus.INTERNAL_SERVER_ERROR);
      } else {
        return new ResponseEntity<>(
            new ApiError(request, HttpStatus.INTERNAL_SERVER_ERROR, cause, debug),
            new HttpHeaders(),
            HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
  }
}
