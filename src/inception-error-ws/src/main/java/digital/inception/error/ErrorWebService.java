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

package digital.inception.error;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.StringUtil;
import digital.inception.validation.InvalidArgumentException;
import digital.inception.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;

//~--- JDK imports ------------------------------------------------------------

import java.time.LocalDateTime;

import java.util.List;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import javax.xml.bind.annotation.XmlElement;

/**
 * The <code>ErrorWebService</code> class.
 *
 * @author Marcus Portmann
 */
@WebService(serviceName = "ErrorService", name = "IErrorService",
    targetNamespace = "http://error.inception.digital")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL,
    parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@SuppressWarnings({ "unused", "WeakerAccess" })
public class ErrorWebService
{
  /* Error Service */
  @Autowired
  private IErrorService errorService;

  /* Validator */
  @Autowired
  private Validator validator;

  /**
   * Create the error report.
   *
   * @param errorReport the error report to create
   */
  @WebMethod(operationName = "CreateErrorReport")
  public void createErrorReport(@WebParam(name = "ErrorReport")
  @XmlElement(required = true) ErrorReport errorReport)
    throws InvalidArgumentException, ErrorServiceException
  {
    if (errorReport == null)
    {
      throw new InvalidArgumentException("errorReport");
    }

    Set<ConstraintViolation<ErrorReport>> constraintViolations = validator.validate(errorReport);

    if (!constraintViolations.isEmpty())
    {
      throw new InvalidArgumentException("errorReport", ValidationError.toValidationErrors(
          constraintViolations));
    }

    errorService.createErrorReport(errorReport);
  }
}
