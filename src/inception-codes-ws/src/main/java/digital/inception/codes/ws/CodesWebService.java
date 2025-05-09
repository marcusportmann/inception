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

package digital.inception.codes.ws;

import digital.inception.codes.model.Code;
import digital.inception.codes.model.CodeCategory;
import digital.inception.codes.model.CodeCategoryNotFoundException;
import digital.inception.codes.model.CodeCategorySummary;
import digital.inception.codes.model.CodeNotFoundException;
import digital.inception.codes.model.DuplicateCodeCategoryException;
import digital.inception.codes.model.DuplicateCodeException;
import digital.inception.codes.service.CodesService;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.ws.AbstractWebServiceBase;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.Date;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 * The {@code CodesWebService} class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "CodesService",
    name = "ICodesService",
    targetNamespace = "https://inception.digital/codes")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class CodesWebService extends AbstractWebServiceBase {

  /** The Codes Service. */
  private final CodesService codesService;

  /**
   * Constructs a new {@code CodesWebService}.
   *
   * @param applicationContext the Spring application context
   * @param codesService the Codes Service
   */
  public CodesWebService(ApplicationContext applicationContext, CodesService codesService) {
    super(applicationContext);

    this.codesService = codesService;
  }

  /**
   * Create the new code.
   *
   * @param code the code to create
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateCodeException if the code already exists
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the code could not be created
   */
  @WebMethod(operationName = "CreateCode")
  public void createCode(@WebParam(name = "Code") @XmlElement(required = true) Code code)
      throws InvalidArgumentException,
          DuplicateCodeException,
          CodeCategoryNotFoundException,
          ServiceUnavailableException {
    codesService.createCode(code);
  }

  /**
   * Create the new code category.
   *
   * @param codeCategory the code category to create
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateCodeCategoryException if the code category already exists
   * @throws ServiceUnavailableException if the code category could not be created
   */
  @WebMethod(operationName = "CreateCodeCategory")
  public void createCodeCategory(
      @WebParam(name = "CodeCategory") @XmlElement(required = true) CodeCategory codeCategory)
      throws InvalidArgumentException, DuplicateCodeCategoryException, ServiceUnavailableException {
    codesService.createCodeCategory(codeCategory);
  }

  /**
   * Delete the code.
   *
   * @param codeCategoryId the ID for the code category
   * @param codeId the ID for the code
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeNotFoundException if the code could not be found
   * @throws ServiceUnavailableException if the code could not be deleted
   */
  @WebMethod(operationName = "DeleteCode")
  public void deleteCode(
      @WebParam(name = "CodeCategoryId") @XmlElement(required = true) String codeCategoryId,
      @WebParam(name = "CodeId") @XmlElement(required = true) String codeId)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException {
    codesService.deleteCode(codeCategoryId, codeId);
  }

  /**
   * Delete the code category.
   *
   * @param codeCategoryId the ID for the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the code category could not be deleted
   */
  @WebMethod(operationName = "DeleteCodeCategory")
  public void deleteCodeCategory(
      @WebParam(name = "CodeCategoryId") @XmlElement(required = true) String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    codesService.deleteCodeCategory(codeCategoryId);
  }

  /**
   * Retrieve the code category.
   *
   * @param codeCategoryId the ID for the code category the code is associated with
   * @param codeId the ID for the code
   * @return the code
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeNotFoundException if the code could not be found
   * @throws ServiceUnavailableException if the code could not be retrieved
   */
  @WebMethod(operationName = "GetCode")
  @WebResult(name = "Code")
  public Code getCode(
      @WebParam(name = "CodeCategoryId") @XmlElement(required = true) String codeCategoryId,
      @WebParam(name = "CodeId") @XmlElement(required = true) String codeId)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException {
    return codesService.getCode(codeCategoryId, codeId);
  }

  /**
   * Retrieve all the code categories.
   *
   * @return all the code categories
   * @throws ServiceUnavailableException if the code categories could not be retrieved
   */
  @WebMethod(operationName = "GetCodeCategories")
  @WebResult(name = "CodeCategory")
  public List<CodeCategory> getCodeCategories() throws ServiceUnavailableException {
    return codesService.getCodeCategories();
  }

  /**
   * Retrieve the code category.
   *
   * @param codeCategoryId the ID for the code category
   * @return the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the code category could not be retrieved
   */
  @WebMethod(operationName = "GetCodeCategory")
  @WebResult(name = "CodeCategory")
  public CodeCategory getCodeCategory(
      @WebParam(name = "CodeCategoryId") @XmlElement(required = true) String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    return codesService.getCodeCategory(codeCategoryId);
  }

  /**
   * Retrieve the XML or JSON data for a code category
   *
   * @param codeCategoryId the ID for the code category
   * @return the XML or JSON data for the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the code category data could not be retrieved
   */
  @WebMethod(operationName = "GetCodeCategoryData")
  @WebResult(name = "CodeCategoryData")
  public String getCodeCategoryData(
      @WebParam(name = "CodeCategoryId") @XmlElement(required = true) String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    String data = codesService.getCodeCategoryData(codeCategoryId);

    return StringUtils.hasText(data) ? data : "";
  }

  /**
   * Returns the date and time the code category was last modified.
   *
   * @param codeCategoryId the ID for the code category
   * @return the date and time the code category was last modified
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the date and time the code category was last modified
   *     could not be retrieved
   */
  @WebMethod(operationName = "GetCodeCategoryLastModified")
  @WebResult(name = "CodeCategoryLastModified")
  public Date getCodeCategoryLastModified(
      @WebParam(name = "CodeCategoryId") @XmlElement(required = true) String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    return Date.from(codesService.getCodeCategoryLastModified(codeCategoryId).toInstant());
  }

  /**
   * Retrieve the name of the code category
   *
   * @param codeCategoryId the ID for the code category
   * @return the name of the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the name of the code category could not be retrieved
   */
  @WebMethod(operationName = "GetCodeCategoryName")
  @WebResult(name = "GetCodeCategoryName")
  public String getCodeCategoryName(
      @WebParam(name = "CodeCategoryId") @XmlElement(required = true) String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    return codesService.getCodeCategoryName(codeCategoryId);
  }

  /**
   * Retrieve the summaries for all the code categories.
   *
   * @return the summaries for all the code categories
   * @throws ServiceUnavailableException if the code category summaries could not be retrieved
   */
  @WebMethod(operationName = "GetCodeCategorySummaries")
  @WebResult(name = "CodeCategorySummary")
  public List<CodeCategorySummary> getCodeCategorySummaries() throws ServiceUnavailableException {
    return codesService.getCodeCategorySummaries();
  }

  /**
   * Retrieve the name of the code.
   *
   * @param codeCategoryId the ID for the code category the code is associated with
   * @param codeId the ID for the code
   * @return the name of the code
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeNotFoundException if the code could not be found
   * @throws ServiceUnavailableException if the name of the code could not be retrieved
   */
  @WebMethod(operationName = "GetCodeName")
  @WebResult(name = "CodeName")
  public String getCodeName(
      @WebParam(name = "CodeCategoryId") @XmlElement(required = true) String codeCategoryId,
      @WebParam(name = "CodeId") @XmlElement(required = true) String codeId)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException {
    return codesService.getCodeName(codeCategoryId, codeId);
  }

  /**
   * Retrieve the codes for a code category
   *
   * @param codeCategoryId the ID for the code category
   * @return the codes for the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the codes for the code category could not be retrieved
   */
  @WebMethod(operationName = "GetCodes")
  @WebResult(name = "Code")
  public List<Code> getCodesForCodeCategory(
      @WebParam(name = "CodeCategoryId") @XmlElement(required = true) String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    return codesService.getCodesForCodeCategory(codeCategoryId);
  }

  /**
   * Update the code.
   *
   * @param code the code to update
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeNotFoundException if the code could not be found
   * @throws ServiceUnavailableException if the code could not be updated
   */
  @WebMethod(operationName = "UpdateCode")
  public void updateCode(@WebParam(name = "Code") @XmlElement(required = true) Code code)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException {
    codesService.updateCode(code);
  }

  /**
   * Update the code category.
   *
   * @param codeCategory the code category
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the code category could not be updated
   */
  @WebMethod(operationName = "UpdateCodeCategory")
  public void updateCodeCategory(
      @WebParam(name = "CodeCategory") @XmlElement(required = true) CodeCategory codeCategory)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    codesService.updateCodeCategory(codeCategory);
  }
}
