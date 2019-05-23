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

package digital.inception.codes;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.validation.InvalidArgumentException;
import digital.inception.validation.ValidationError;

import org.springframework.util.StringUtils;

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
 * The <code>CodesWebService</code> class.
 *
 * @author Marcus Portmann
 */
@WebService(serviceName = "CodesService", name = "ICodesService",
    targetNamespace = "http://codes.inception.digital")
@SOAPBinding
@SuppressWarnings({ "unused", "ValidExternallyBoundObject" })
public class CodesWebService
{
  /**
   * The Codes Service.
   */
  private ICodesService codesService;

  /**
   * The JSR-303 Validator.
   */
  private Validator validator;

  /**
   * Constructs a new <code>CodesRestController</code>.
   *
   * @param codesService the Codes Service
   * @param validator    the JSR-303 validator
   */
  public CodesWebService(ICodesService codesService, Validator validator)
  {
    this.codesService = codesService;
    this.validator = validator;
  }

  /**
   * Create the code.
   *
   * @param code the code to create
   */
  @WebMethod(operationName = "CreateCode")
  public void createCode(@WebParam(name = "Code")
  @XmlElement(required = true) Code code)
    throws InvalidArgumentException, DuplicateCodeException, CodeCategoryNotFoundException,
        CodesServiceException
  {
    validateCode(code);

    codesService.createCode(code);
  }

  /**
   * Create the code category.
   *
   * @param codeCategory the code category to create
   */
  @WebMethod(operationName = "CreateCodeCategory")
  public void createCodeCategory(@WebParam(name = "CodeCategory")
  @XmlElement(required = true) CodeCategory codeCategory)
    throws InvalidArgumentException, DuplicateCodeCategoryException, CodesServiceException
  {
    validateCodeCategory(codeCategory);

    codesService.createCodeCategory(codeCategory);
  }

  /**
   * Delete the code.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   */
  @WebMethod(operationName = "DeleteCode")
  public void deleteCode(@WebParam(name = "CodeCategoryId")
  @XmlElement(required = true) String codeCategoryId, @WebParam(name = "CodeId")
  @XmlElement(required = true) String codeId)
    throws InvalidArgumentException, CodeNotFoundException, CodesServiceException
  {
    if (codeCategoryId == null)
    {
      throw new InvalidArgumentException("codeCategoryId");
    }

    if (StringUtils.isEmpty(codeId))
    {
      throw new InvalidArgumentException("codeId");
    }

    codesService.deleteCode(codeCategoryId, codeId);
  }

  /**
   * Delete the code category.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   */
  @WebMethod(operationName = "DeleteCodeCategory")
  public void deleteCodeCategory(@WebParam(name = "CodeCategoryId")
  @XmlElement(required = true) String codeCategoryId)
    throws InvalidArgumentException, CodeCategoryNotFoundException, CodesServiceException
  {
    if (codeCategoryId == null)
    {
      throw new InvalidArgumentException("codeCategoryId");
    }

    codesService.deleteCodeCategory(codeCategoryId);
  }

  /**
   * Retrieve the code category.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category the code is associated
   *                       with
   * @param codeId         the ID uniquely identifying the code
   *
   * @return the code
   */
  @WebMethod(operationName = "GetCode")
  public Code getCode(@WebParam(name = "CodeCategoryId")
  @XmlElement(required = true) String codeCategoryId, @WebParam(name = "CodeId")
  @XmlElement(required = true) String codeId)
    throws InvalidArgumentException, CodeNotFoundException, CodesServiceException
  {
    if (codeCategoryId == null)
    {
      throw new InvalidArgumentException("codeCategoryId");
    }

    if (codeId == null)
    {
      throw new InvalidArgumentException("codeId");
    }

    return codesService.getCode(codeCategoryId, codeId);
  }

  /**
   * Retrieve all the code categories.
   *
   * @return all the code categories
   */
  @WebMethod(operationName = "GetCodeCategories")
  @WebResult(name = "CodeCategory")
  public List<CodeCategory> getCodeCategories()
    throws CodesServiceException
  {
    return codesService.getCodeCategories();
  }

  /**
   * Retrieve the code category.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the code category
   */
  @WebMethod(operationName = "GetCodeCategory")
  public CodeCategory getCodeCategory(@WebParam(name = "CodeCategoryId")
  @XmlElement(required = true) String codeCategoryId)
    throws InvalidArgumentException, CodeCategoryNotFoundException, CodesServiceException
  {
    if (codeCategoryId == null)
    {
      throw new InvalidArgumentException("codeCategoryId");
    }

    return codesService.getCodeCategory(codeCategoryId);
  }

  /**
   * Retrieve the codes for a code category
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the codes for the code category
   */
  @WebMethod(operationName = "GetCodeCategoryCodes")
  @WebResult(name = "Code")
  public List<Code> getCodeCategoryCodes(@WebParam(name = "CodeCategoryId")
  @XmlElement(required = true) String codeCategoryId)
    throws InvalidArgumentException, CodeCategoryNotFoundException, CodesServiceException
  {
    if (codeCategoryId == null)
    {
      throw new InvalidArgumentException("codeCategoryId");
    }

    return codesService.getCodeCategoryCodes(codeCategoryId);
  }

  /**
   * Retrieve the XML or JSON data for a code category
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the XML or JSON data for the code category
   */
  @WebMethod(operationName = "GetCodeCategoryData")
  @WebResult(name = "CodeCategoryData")
  public String getCodeCategoryData(@WebParam(name = "CodeCategoryId")
  @XmlElement(required = true) String codeCategoryId)
    throws InvalidArgumentException, CodeCategoryNotFoundException, CodesServiceException
  {
    if (codeCategoryId == null)
    {
      throw new InvalidArgumentException("codeCategoryId");
    }

    String data = codesService.getCodeCategoryData(codeCategoryId);

    return StringUtils.isEmpty(data)
        ? ""
        : data;
  }

  /**
   * Retrieve the summaries for all the code categories.
   *
   * @return the summaries for all the code categories
   */
  @WebMethod(operationName = "GetCodeCategorySummaries")
  @WebResult(name = "CodeCategorySummary")
  public List<CodeCategorySummary> getCodeCategorySummaries()
    throws CodesServiceException
  {
    return codesService.getCodeCategorySummaries();
  }

  /**
   * Returns the date and time the code category was last updated.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the date and time the code category was last updated
   */
  @WebMethod(operationName = "GetCodeCategoryUpdated")
  @WebResult(name = "CodeCategoryUpdated")
  public LocalDateTime getCodeCategoryUpdated(@WebParam(name = "CodeCategoryId")
  @XmlElement(required = true) String codeCategoryId)
    throws InvalidArgumentException, CodeCategoryNotFoundException, CodesServiceException
  {
    if (codeCategoryId == null)
    {
      throw new InvalidArgumentException("codeCategoryId");
    }

    return codesService.getCodeCategoryUpdated(codeCategoryId);
  }

  /**
   * Update the code.
   *
   * @param code the code to update
   */
  @WebMethod(operationName = "UpdateCode")
  public void updateCode(@WebParam(name = "Code")
  @XmlElement(required = true) Code code)
    throws InvalidArgumentException, CodeNotFoundException, CodesServiceException
  {
    validateCode(code);

    codesService.updateCode(code);
  }

  /**
   * Update the code category.
   *
   * @param codeCategory the code category
   */
  @WebMethod(operationName = "UpdateCodeCategory")
  public void updateCodeCategory(@WebParam(name = "CodeCategory")
  @XmlElement(required = true) CodeCategory codeCategory)
    throws InvalidArgumentException, CodeCategoryNotFoundException, CodesServiceException
  {
    validateCodeCategory(codeCategory);

    codesService.updateCodeCategory(codeCategory);
  }

  private void validateCode(Code code)
    throws InvalidArgumentException
  {
    if (code == null)
    {
      throw new InvalidArgumentException("code");
    }

    Set<ConstraintViolation<Code>> constraintViolations = validator.validate(code);

    if (!constraintViolations.isEmpty())
    {
      throw new InvalidArgumentException("code", ValidationError.toValidationErrors(
          constraintViolations));
    }
  }

  private void validateCodeCategory(CodeCategory codeCategory)
    throws InvalidArgumentException
  {
    if (codeCategory == null)
    {
      throw new InvalidArgumentException("codeCategory");
    }

    Set<ConstraintViolation<CodeCategory>> constraintViolations = validator.validate(codeCategory);

    if (!constraintViolations.isEmpty())
    {
      throw new InvalidArgumentException("codeCategory", ValidationError.toValidationErrors(
          constraintViolations));
    }
  }
}
