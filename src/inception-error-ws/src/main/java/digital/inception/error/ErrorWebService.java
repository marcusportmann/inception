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

package digital.inception.error;



import digital.inception.core.validation.InvalidArgumentException;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;



/**
 * The <b>ErrorWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "ErrorService",
    name = "IErrorService",
    targetNamespace = "http://error.inception.digital")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class ErrorWebService {

  /** The Error Service. */
  private final IErrorService errorService;

  /**
   * Constructs a new <b>ErrorWebService</b>.
   *
   * @param errorService the Error Service
   */
  public ErrorWebService(IErrorService errorService) {
    this.errorService = errorService;
  }

  /**
   * Create the new error report.
   *
   * @param errorReport the error report to create
   */
  @WebMethod(operationName = "CreateErrorReport")
  public void createErrorReport(
      @WebParam(name = "ErrorReport") @XmlElement(required = true) ErrorReport errorReport)
      throws InvalidArgumentException, ErrorServiceException {
    errorService.createErrorReport(errorReport);
  }
}
