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

package digital.inception.application;

//~--- non-JDK imports --------------------------------------------------------

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ApplicationErrorHandler</code> class implements the application error handler.
 *
 * @author Marcus Portmann
 */
@ControllerAdvice
@SuppressWarnings("unused")
public class ApplicationErrorHandler
{
  @ExceptionHandler
  @ResponseBody
  protected ResponseEntity<ApplicationError> handle(HttpServletRequest request,
      HttpMessageNotReadableException ex)
  {
    return new ResponseEntity<>(new ApplicationError(request, HttpStatus.BAD_REQUEST,
        ex.getMostSpecificCause()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  @ResponseBody
  protected ResponseEntity<ApplicationError> handle(HttpServletRequest request, Throwable cause)
  {
    ResponseStatus annotation = AnnotatedElementUtils.findMergedAnnotation(cause.getClass(),
        ResponseStatus.class);

    if (annotation != null)
    {
      HttpStatus responseStatus = annotation.value();

      return new ResponseEntity<>(new ApplicationError(request, responseStatus, cause),
          new HttpHeaders(), responseStatus);
    }
    else
    {
      return new ResponseEntity<>(new ApplicationError(request, HttpStatus.INTERNAL_SERVER_ERROR,
          cause), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
