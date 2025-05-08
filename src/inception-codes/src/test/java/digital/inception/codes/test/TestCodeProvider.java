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

package digital.inception.codes.test;

import digital.inception.codes.model.Code;
import digital.inception.codes.model.CodeCategory;
import digital.inception.codes.model.CodeCategoryNotFoundException;
import digital.inception.codes.model.CodeNotFoundException;
import digital.inception.codes.model.CodeProvider;
import digital.inception.codes.model.CodeProviderConfig;
import digital.inception.codes.model.CodeProviderException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The {@code TestCodeProvider} class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class TestCodeProvider implements CodeProvider {

  private final CodeCategory codeCategory;

  private final List<Code> codes;

  /**
   * Creates a new {@code TestCodeProvider} instance.
   *
   * @param codeProviderConfig the configuration for the code provider
   */
  public TestCodeProvider(CodeProviderConfig codeProviderConfig) {
    codeCategory =
        new CodeCategory("TestCodeCategory", "Test Code Category Name", "Test Code Category Data");

    codes = new ArrayList<>();

    codes.add(new Code("TestCode1", codeCategory.getId(), "Test Code Name 1", "Test Code Value 1"));
    codes.add(new Code("TestCode2", codeCategory.getId(), "Test Code Name 2", "Test Code Value 2"));
    codes.add(new Code("TestCode3", codeCategory.getId(), "Test Code Name 3", "Test Code Value 3"));
  }

  @Override
  public boolean codeCategoryExists(String codeCategoryId) throws CodeProviderException {
    try {
      return codeCategory.getId().equals(codeCategoryId);
    } catch (Throwable e) {
      throw new CodeProviderException(
          "Failed to check whether the code provider supports the code category ("
              + codeCategoryId
              + ")",
          e);
    }
  }

  @Override
  public boolean codeExists(String codeCategoryId, String codeId) throws CodeProviderException {
    try {
      if (codeCategory.getId().equals(codeCategoryId)) {
        for (Code code : codes) {
          if (code.getId().equals(codeId)) {
            return true;
          }
        }
      }

      return false;
    } catch (Throwable e) {
      throw new CodeProviderException(
          "Failed to check whether the code ("
              + codeId
              + ") exists for the code category ("
              + codeCategoryId
              + ")",
          e);
    }
  }

  @Override
  public Code getCode(String codeCategoryId, String codeId)
      throws CodeNotFoundException, CodeProviderException {
    try {
      if (codeCategory.getId().equals(codeCategoryId)) {
        for (Code code : codes) {
          if (code.getId().equals(codeId)) {
            return code;
          }
        }
      }
    } catch (Throwable e) {
      throw new CodeProviderException(
          "Failed to retrieve the code ("
              + codeId
              + ") for the code category ("
              + codeCategoryId
              + ")",
          e);
    }

    throw new CodeNotFoundException(codeCategoryId, codeId);
  }

  @Override
  public List<CodeCategory> getCodeCategories() throws CodeProviderException {
    try {
      List<CodeCategory> codeCategories = new ArrayList<>();

      codeCategories.add(codeCategory);

      return codeCategories;
    } catch (Throwable e) {
      throw new CodeProviderException("Failed to retrieve code categories", e);
    }
  }

  @Override
  public CodeCategory getCodeCategory(String codeCategoryId)
      throws CodeCategoryNotFoundException, CodeProviderException {
    try {
      if (codeCategory.getId().equals(codeCategoryId)) {
        return codeCategory;
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodeProviderException(
          "Failed to retrieve the code category (" + codeCategoryId + ")", e);
    }
  }

  @Override
  public String getCodeCategoryData(String codeCategoryId)
      throws CodeCategoryNotFoundException, CodeProviderException {
    try {
      if (codeCategory.getId().equals(codeCategoryId)) {
        return "<codes><code name=\"name1\" value=\"value1\"/></codes>";
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodeProviderException(
          "Failed to retrieve the data for the code category (" + codeCategoryId + ")", e);
    }
  }

  @Override
  public String getCodeCategoryDataWithParameters(
      String codeCategoryId, Map<String, String> parameters)
      throws CodeCategoryNotFoundException, CodeProviderException {
    return getCodeCategoryData(codeCategoryId);
  }

  @Override
  public OffsetDateTime getCodeCategoryLastModified(String codeCategoryId)
      throws CodeCategoryNotFoundException, CodeProviderException {
    try {
      if (codeCategory.getId().equals(codeCategoryId)) {
        return codeCategory.getLastModified();
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodeProviderException(
          "Failed to retrieve the date and time the code category ("
              + codeCategoryId
              + ") was last modified",
          e);
    }
  }

  @Override
  public String getCodeCategoryName(String codeCategoryId)
      throws CodeCategoryNotFoundException, CodeProviderException {
    try {
      if (codeCategory.getId().equals(codeCategoryId)) {
        return codeCategory.getName();
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodeProviderException(
          "Failed to retrieve the code category name (" + codeCategoryId + ")", e);
    }
  }

  @Override
  public String getCodeName(String codeCategoryId, String codeId)
      throws CodeNotFoundException, CodeProviderException {
    try {
      if (codeCategory.getId().equals(codeCategoryId)) {
        for (Code code : codes) {
          if (code.getId().equals(codeId)) {
            return code.getName();
          }
        }
      }
    } catch (Throwable e) {
      throw new CodeProviderException(
          "Failed to retrieve the code ("
              + codeId
              + ") for the code category ("
              + codeCategoryId
              + ")",
          e);
    }

    throw new CodeNotFoundException(codeCategoryId, codeId);
  }

  @Override
  public List<Code> getCodesForCodeCategory(String codeCategoryId)
      throws CodeCategoryNotFoundException, CodeProviderException {
    try {
      if (codeCategory.getId().equals(codeCategoryId)) {
        return codes;
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodeProviderException(
          "Failed to retrieve the codes for the code category (" + codeCategoryId + ")", e);
    }
  }

  @Override
  public List<Code> getCodesForCodeCategoryWithParameters(
      String codeCategoryId, Map<String, String> parameters)
      throws CodeCategoryNotFoundException, CodeProviderException {
    return getCodesForCodeCategory(codeCategoryId);
  }
}
