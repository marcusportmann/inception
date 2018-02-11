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

package digital.inception.codes;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.util.StringUtil;
import digital.inception.validation.InvalidArgumentException;
import digital.inception.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CodesWebService</code> class.
 *
 * @author Marcus Portmann
 */
@WebService(serviceName = "CodesService", name = "ICodesService",
    targetNamespace = "http://codes.inception.digital")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL,
    parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@SuppressWarnings({ "unused", "WeakerAccess" })
public class CodesWebService
{
  /* Codes Service */
  @Autowired
  private ICodesService codesService;

  /* Validator */
  @Autowired
  private Validator validator;

  /**
   * Create a code.
   *
   * @param code the code to create
   */
  @WebMethod(operationName = "CreateCode")
  public void createCode(@WebParam(name = "Code")
  @XmlElement(required = true) Code code)
    throws InvalidArgumentException, DuplicateCodeException, CodeCategoryNotFoundException,
        CodesServiceException
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

    codesService.createCode(code);
  }

  /**
   * Create a code category.
   *
   * @param codeCategory the code category to create
   */
  @WebMethod(operationName = "CreateCodeCategory")
  public void createCodeCategory(@WebParam(name = "CodeCategory")
  @XmlElement(required = true) CodeCategory codeCategory)
    throws InvalidArgumentException, DuplicateCodeCategoryException, CodesServiceException
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

    codesService.createCodeCategory(codeCategory);
  }

  /**
   * Delete the code.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   */
  @WebMethod(operationName = "DeleteCode")
  public void deleteCode(@WebParam(name = "CodeCategoryId")
  @XmlElement(required = true) UUID codeCategoryId, @WebParam(name = "CodeId")
  @XmlElement(required = true) String codeId)
    throws InvalidArgumentException, CodeNotFoundException, CodesServiceException
  {
    if (codeCategoryId == null)
    {
      throw new InvalidArgumentException("codeCategoryId");
    }

    if (StringUtil.isNullOrEmpty(codeId))
    {
      throw new InvalidArgumentException("codeId");
    }

    codesService.deleteCode(codeCategoryId, codeId);
  }

  /**
   * Delete the code category.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   */
  @WebMethod(operationName = "DeleteCodeCategory")
  public void deleteCodeCategory(@WebParam(name = "CodeCategoryId")
  @XmlElement(required = true) UUID codeCategoryId)
    throws InvalidArgumentException, CodeCategoryNotFoundException, CodesServiceException
  {
    if (codeCategoryId == null)
    {
      throw new InvalidArgumentException("codeCategoryId");
    }

    codesService.deleteCodeCategory(codeCategoryId);
  }

  /**
   * Retrieve the code categories.
   *
   * @return the code categories
   */
  @WebMethod(operationName = "GetCodeCategories")
  @WebResult(name = "CodeCategory")
  public List<CodeCategory> getCodeCategories()
    throws CodesServiceException
  {
    return codesService.getCodeCategories();
  }

  /**
   * Retrieve the codes for a code category
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   *
   * @return the codes for the code category
   */
  @WebMethod(operationName = "GetCodeCategoryCodes")
  @WebResult(name = "Code")
  public List<Code> getCodeCategoryCodes(@WebParam(name = "CodeCategoryId")
  @XmlElement(required = true) UUID codeCategoryId)
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
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   *
   * @return the XML or JSON data for the code category
   */
  @WebMethod(operationName = "GetCodeCategoryData")
  @WebResult(name = "CodeCategoryData")
  public String getCodeCategoryData(@WebParam(name = "CodeCategoryId")
  @XmlElement(required = true) UUID codeCategoryId)
    throws InvalidArgumentException, CodeCategoryNotFoundException, CodesServiceException
  {
    if (codeCategoryId == null)
    {
      throw new InvalidArgumentException("codeCategoryId");
    }

    return StringUtil.notNull(codesService.getCodeCategoryData(codeCategoryId));
  }

  /**
   * Returns the date and time the code category was last updated.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   *
   * @return the date and time the code category was last updated
   */
  @WebMethod(operationName = "GetCodeCategoryUpdated")
  @WebResult(name = "CodeCategoryUpdated")
  public LocalDateTime getCodeCategoryUpdated(@WebParam(name = "CodeCategoryId")
  @XmlElement(required = true) UUID codeCategoryId)
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

    codesService.updateCodeCategory(codeCategory);
  }
}
