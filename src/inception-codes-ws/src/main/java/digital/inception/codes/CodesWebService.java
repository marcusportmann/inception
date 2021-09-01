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

package digital.inception.codes;

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import org.springframework.util.StringUtils;

/**
 * The <b>CodesWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "CodesService",
    name = "ICodesService",
    targetNamespace = "http://inception.digital/codes")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class CodesWebService {

  /** The Codes Service. */
  private final ICodesService codesService;

  /**
   * Constructs a new <b>CodesRestController</b>.
   *
   * @param codesService the Codes Service
   */
  public CodesWebService(ICodesService codesService) {
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
      throws InvalidArgumentException, DuplicateCodeException, CodeCategoryNotFoundException,
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
   * Returns the date and time the code category was last updated.
   *
   * @param codeCategoryId the ID for the code category
   * @return the date and time the code category was last updated
   * @throws InvalidArgumentException if an argument is invalid
   * @throws CodeCategoryNotFoundException if the code category could not be found
   * @throws ServiceUnavailableException if the date and time the code category was last updated
   *     could not be retrieved
   */
  @WebMethod(operationName = "GetCodeCategoryUpdated")
  @WebResult(name = "CodeCategoryUpdated")
  public Date getCodeCategoryUpdated(
      @WebParam(name = "CodeCategoryId") @XmlElement(required = true) String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    return Date.from(
        codesService
            .getCodeCategoryUpdated(codeCategoryId)
            .atZone(ZoneId.systemDefault())
            .toInstant());
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
