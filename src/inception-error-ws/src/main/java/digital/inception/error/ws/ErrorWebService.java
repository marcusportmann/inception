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

package digital.inception.error.ws;

import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.xml.LocalDateAdapter;
import digital.inception.error.exception.ErrorReportNotFoundException;
import digital.inception.error.model.ErrorReport;
import digital.inception.error.model.ErrorReportSortBy;
import digital.inception.error.model.ErrorReportSummaries;
import digital.inception.error.service.ErrorService;
import digital.inception.ws.AbstractWebServiceBase;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.context.ApplicationContext;

/**
 * The {@code ErrorWebService} class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "ErrorService",
    name = "IErrorService",
    targetNamespace = "https://inception.digital/error")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class ErrorWebService extends AbstractWebServiceBase {

  /** The Error Service. */
  private final ErrorService errorService;

  /**
   * Constructs a new {@code ErrorWebService}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param errorService the Error Service
   */
  public ErrorWebService(ApplicationContext applicationContext, ErrorService errorService) {
    super(applicationContext);

    this.errorService = errorService;
  }

  /**
   * Create the error report.
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
   * Retrieve the error report.
   *
   * @param errorReportId the ID for the error report
   * @return the error report
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ErrorReportNotFoundException if the error report could not be found
   * @throws ServiceUnavailableException if the error report could not be retrieved
   */
  @WebMethod(operationName = "GetErrorReport")
  @WebResult(name = "ErrorReport")
  public ErrorReport getErrorReport(
      @WebParam(name = "ErrorReportId") @XmlElement(required = true) UUID errorReportId)
      throws InvalidArgumentException, ErrorReportNotFoundException, ServiceUnavailableException {
    return errorService.getErrorReport(errorReportId);
  }

  /**
   * Retrieve the error report summaries.
   *
   * @param filter the filter to apply to the error report summaries
   * @param fromDate the date to retrieve the error report summaries from
   * @param toDate the date to retrieve the error report summaries to
   * @param sortBy the method used to sort the error report summaries e.g. by who submitted them
   * @param sortDirection the sort direction to apply to the error report summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the error report summaries
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the error report summaries could not be retrieved
   */
  @WebMethod(operationName = "GetErrorReportSummaries")
  @WebResult(name = "ErrorReportSummaries")
  public ErrorReportSummaries getErrorReportSummaries(
      @WebParam(name = "Filter") @XmlElement String filter,
      @WebParam(name = "FromDate") @XmlElement @XmlJavaTypeAdapter(LocalDateAdapter.class)
          LocalDate fromDate,
      @WebParam(name = "ToDate") @XmlElement @XmlJavaTypeAdapter(LocalDateAdapter.class)
          LocalDate toDate,
      @WebParam(name = "SortBy") @XmlElement ErrorReportSortBy sortBy,
      @WebParam(name = "SortDirection") @XmlElement SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (pageIndex == null) {
      pageIndex = 0;
    }
    if (pageSize == null) {
      pageSize = 10;
    }

    return errorService.getErrorReportSummaries(
        filter, fromDate, toDate, sortBy, sortDirection, pageIndex, pageSize);
  }
}
