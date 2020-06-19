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

import javax.servlet.http.HttpServletRequest;
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

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>RestControllerErrorHandler</code> class implements the error handler for RESTful web
 * services.
 *
 * @author Marcus Portmann
 */
@ControllerAdvice
@SuppressWarnings("unused")
public class RestControllerErrorHandler {

  @ExceptionHandler
  @ResponseBody
  protected ResponseEntity<RestControllerError> handle(HttpServletRequest request,
      HttpMessageNotReadableException ex) {
    return new ResponseEntity<>(new RestControllerError(request, HttpStatus.BAD_REQUEST,
        ex.getMostSpecificCause()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  @ResponseBody
  protected ResponseEntity<RestControllerError> handle(HttpServletRequest request,
      Throwable cause) {
    ResponseStatus annotation = AnnotatedElementUtils.findMergedAnnotation(cause.getClass(),
        ResponseStatus.class);

    if (annotation != null) {
      HttpStatus responseStatus = annotation.value();

      return new ResponseEntity<>(new RestControllerError(request, responseStatus, cause),
          new HttpHeaders(), responseStatus);
    } else {
      if (cause instanceof AccessDeniedException) {
        return new ResponseEntity<>(new RestControllerError(request, HttpStatus.FORBIDDEN, cause),
            new HttpHeaders(), HttpStatus.FORBIDDEN);
      } else {
        return new ResponseEntity<>(new RestControllerError(request, HttpStatus
            .INTERNAL_SERVER_ERROR, cause), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
  }
}
