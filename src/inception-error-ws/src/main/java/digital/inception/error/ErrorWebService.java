/*
 * Copyright 2022 Marcus Portmann
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

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
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
    targetNamespace = "http://inception.digital/error")
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
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the error report could not be created
   */
  @WebMethod(operationName = "CreateErrorReport")
  public void createErrorReport(
      @WebParam(name = "ErrorReport") @XmlElement(required = true) ErrorReport errorReport)
      throws InvalidArgumentException, ServiceUnavailableException {
    errorService.createErrorReport(errorReport);
  }

  /**
   * Retrieve the error report summaries.
   *
   * @param filter the optional filter to apply to the error reports
   * @param sortBy the optional method used to sort the error reports e.g. by who submitted them
   * @param sortDirection the optional sort direction to apply to the error reports
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the error report summaries
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the error report summaries could not be retrieved
   */
  public ErrorReportSummaries getErrorReportSummaries(
      String filter,
      ErrorReportSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (pageIndex == null) {
      pageIndex = 0;
    }
    if (pageSize == null) {
      pageSize = 10;
    }

    return errorService.getErrorReportSummaries(filter, sortBy, sortDirection, pageIndex, pageSize);
  }
}
