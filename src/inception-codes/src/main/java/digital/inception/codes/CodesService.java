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

import digital.inception.core.xml.DtdJarResolver;
import digital.inception.core.xml.XmlParserErrorHandler;
import digital.inception.core.xml.XmlUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.InputSource;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Constructor;

import java.net.URL;

import java.time.LocalDateTime;

import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * The <code>CodesService</code> class provides the Codes Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class CodesService
  implements ICodesService, InitializingBean
{
  /**
   * The path to the code provider configuration files (META-INF/code-providers.xml) on the
   * classpath.
   */
  private static final String CODE_PROVIDERS_CONFIGURATION_PATH = "META-INF/code-providers.xml";

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(CodesService.class);

  /**
   * The Spring application context.
   */
  private ApplicationContext applicationContext;

  /**
   * The Code Category Repository.
   */
  private CodeCategoryRepository codeCategoryRepository;

  /**
   * The Code Category Summary Repository.
   */
  private CodeCategorySummaryRepository codeCategorySummaryRepository;

  /**
   * The configuration information for the code providers read from the code provider configuration
   * files (META-INF/code-providers.xml) on the classpath.
   */
  private List<CodeProviderConfig> codeProviderConfigs;

  /* The code providers. */
  private List<ICodeProvider> codeProviders;

  /**
   * The Code Repository.
   */
  private CodeRepository codeRepository;

  /**
   * Constructs a new <code>CodesService</code>.
   *
   * @param applicationContext            the Spring application context
   * @param codeCategoryRepository        the Code Category Repository
   * @param codeCategorySummaryRepository the Code Category Summary Repository
   * @param codeRepository                the Code Repository
   */
  public CodesService(ApplicationContext applicationContext,
      CodeCategoryRepository codeCategoryRepository,
      CodeCategorySummaryRepository codeCategorySummaryRepository, CodeRepository codeRepository)
  {
    this.applicationContext = applicationContext;
    this.codeCategoryRepository = codeCategoryRepository;
    this.codeCategorySummaryRepository = codeCategorySummaryRepository;
    this.codeRepository = codeRepository;
  }

  /**
   * Initialize the Codes Service.
   */
  @Override
  public void afterPropertiesSet()
  {
    logger.info("Initializing the Codes Service");

    codeProviders = new ArrayList<>();

    try
    {
      // Read the codes configuration
      readCodeProviderConfigurations();

      // Initialize the code providers
      initCodeProviders();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialize the Codes Service", e);
    }
  }

  /**
   * Check whether the code category exists.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return <code>true</code> if the code category exists or <code>false</code> otherwise
   */
  @Override
  public boolean codeCategoryExists(String codeCategoryId)
    throws CodesServiceException
  {
    try
    {
      return codeCategoryRepository.existsById(codeCategoryId);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to check whether the code category (%s) exists", codeCategoryId), e);
    }
  }

  /**
   * Check whether the code exists.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category the code is associated
   *                       with
   * @param codeId         the ID used to uniquely identify the code
   *
   * @return <code>true</code> if the code exists or <code>false</code> otherwise
   */
  @Override
  public boolean codeExists(String codeCategoryId, String codeId)
    throws CodesServiceException
  {
    try
    {
      return codeRepository.existsById(new CodeId(codeCategoryId, codeId));
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to check whether the code (%s) exists for the code category (%s)", codeId,
          codeCategoryId), e);
    }
  }

  /**
   * Create the new code.
   *
   * @param code the <code>Code</code> instance containing the information for the new code
   */
  @Override
  @Transactional
  public void createCode(Code code)
    throws DuplicateCodeException, CodeCategoryNotFoundException, CodesServiceException
  {
    try
    {
      if (codeRepository.existsById(new CodeId(code.getCodeCategoryId(), code.getId())))
      {
        throw new DuplicateCodeException(code.getCodeCategoryId(), code.getId());
      }

      if (!codeCategoryRepository.existsById(code.getCodeCategoryId()))
      {
        throw new CodeCategoryNotFoundException(code.getCodeCategoryId());
      }

      codeRepository.saveAndFlush(code);
    }
    catch (DuplicateCodeException | CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to create the code (%s) for the code category (%s)", code.getName(),
          code.getCodeCategoryId()), e);
    }
  }

  /**
   * Create the new code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the information for the
   *                     new code category
   */
  @Override
  @Transactional
  public void createCodeCategory(CodeCategory codeCategory)
    throws DuplicateCodeCategoryException, CodesServiceException
  {
    try
    {
      if (codeCategoryRepository.existsById(codeCategory.getId()))
      {
        throw new DuplicateCodeCategoryException(codeCategory.getId());
      }

      if (codeCategory.getUpdated() == null)
      {
        codeCategory.setUpdated(LocalDateTime.now());
      }

      codeCategoryRepository.saveAndFlush(codeCategory);
    }
    catch (DuplicateCodeCategoryException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format("Failed to create the code category (%s)",
          codeCategory.getId()), e);
    }
  }

  /**
   * Delete the code.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category the code is associated
   *                       with
   * @param codeId         the ID uniquely identifying the code
   */
  @Override
  @Transactional
  public void deleteCode(String codeCategoryId, String codeId)
    throws CodeNotFoundException, CodesServiceException
  {
    try
    {
      CodeId id = new CodeId(codeCategoryId, codeId);

      if (!codeRepository.existsById(id))
      {
        throw new CodeNotFoundException(codeCategoryId, codeId);
      }

      codeRepository.deleteById(id);
    }
    catch (CodeNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to delete the code (%s) for the code category (%s)", codeId, codeCategoryId), e);
    }
  }

  /**
   * Delete the code category.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   */
  @Override
  @Transactional
  public void deleteCodeCategory(String codeCategoryId)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    try
    {
      if (!codeCategoryRepository.existsById(codeCategoryId))
      {
        throw new CodeCategoryNotFoundException(codeCategoryId);
      }

      codeCategoryRepository.deleteById(codeCategoryId);
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format("Failed to delete the code category (%s)",
          codeCategoryId), e);
    }
  }

  /**
   * Retrieve the code.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category the code is associated
   *                       with
   * @param codeId         the ID uniquely identifying the code
   *
   * @return the code
   */
  @Override
  public Code getCode(String codeCategoryId, String codeId)
    throws CodeNotFoundException, CodesServiceException
  {
    try
    {
      Optional<Code> code = codeRepository.findById(new CodeId(codeCategoryId, codeId));

      if (code.isPresent())
      {
        return code.get();
      }
      else
      {
        // Check if one of the registered code providers supports the code category
        for (ICodeProvider codeProvider : codeProviders)
        {
          if (codeProvider.codeCategoryExists(codeCategoryId))
          {
            return codeProvider.getCode(codeCategoryId, codeId);
          }
        }
      }

      throw new CodeNotFoundException(codeCategoryId, codeId);
    }
    catch (CodeNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to retrieve the code (%s) for the code category (%s)", codeId, codeCategoryId),
          e);
    }

  }

  /**
   * Returns all the code categories.
   *
   * @return all the code categories
   */
  @Override
  public List<CodeCategory> getCodeCategories()
    throws CodesServiceException
  {
    try
    {
      return codeCategoryRepository.findAll();
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to retrieve the code categories", e);
    }
  }

  /**
   * Retrieve the code category.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the code category
   */
  @Override
  public CodeCategory getCodeCategory(String codeCategoryId)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    try
    {
      Optional<CodeCategory> codeCategory = codeCategoryRepository.findById(codeCategoryId);

      if (codeCategory.isPresent())
      {
        return codeCategory.get();
      }

      // Check if one of the registered code providers supports the code category
      for (ICodeProvider codeProvider : codeProviders)
      {
        if (codeProvider.codeCategoryExists(codeCategoryId))
        {
          return codeProvider.getCodeCategory(codeCategoryId);
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format("Failed to retrieve the code category (%s)",
          codeCategoryId), e);
    }
  }

  /**
   * Retrieve the XML or JSON data for the code category.
   * <p/>
   * NOTE: This will also attempt to retrieve the data from the appropriate code provider that has
   * been registered with the Codes Service in the <code>META-INF/code-providers.xml</code>
   * configuration file.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the XML or JSON data for the code category
   */
  @Override
  public String getCodeCategoryData(String codeCategoryId)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    try
    {
      Optional<String> data = codeCategoryRepository.getDataById(codeCategoryId);

      if (data.isPresent())
      {
        return data.get();
      }

      // Check if one of the registered code providers supports the code category
      for (ICodeProvider codeProvider : codeProviders)
      {
        if (codeProvider.codeCategoryExists(codeCategoryId))
        {
          String codeProviderData = codeProvider.getDataForCodeCategory(codeCategoryId);

          return StringUtils.isEmpty(codeProviderData)
              ? ""
              : codeProviderData;
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to retrieve the data for the code category (%s)", codeCategoryId), e);
    }
  }

  /**
   * Retrieve the XML or JSON data for the code category using the specified parameters.
   * <p/>
   * NOTE: This will also attempt to retrieve the data from the appropriate code provider that has
   * been registered with the Codes Service in the <code>META-INF/code-providers.xml</code>
   * configuration file.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   * @param parameters     the parameters
   *
   * @return the XML or JSON data for the code category
   */
  @Override
  public String getCodeCategoryDataWithParameters(String codeCategoryId, Map<String,
      String> parameters)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    try
    {
      Optional<String> data = codeCategoryRepository.getDataById(codeCategoryId);

      if (data.isPresent())
      {
        return data.get();
      }

      // Check if one of the registered code providers supports the code category
      for (ICodeProvider codeProvider : codeProviders)
      {
        if (codeProvider.codeCategoryExists(codeCategoryId))
        {
          String codeProviderData = codeProvider.getDataForCodeCategoryWithParameters(
              codeCategoryId, parameters);

          return StringUtils.isEmpty(codeProviderData)
              ? ""
              : codeProviderData;
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to retrieve the data for the code category (%s)", codeCategoryId), e);
    }
  }

  /**
   * Returns the summaries for all the code categories.
   *
   * @return the summaries for all the code categories
   */
  @Override
  public List<CodeCategorySummary> getCodeCategorySummaries()
    throws CodesServiceException
  {
    try
    {
      return codeCategorySummaryRepository.findAll();
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to retrieve the summaries for the code categories",
          e);
    }
  }

  /**
   * Returns the date and time the code category was last updated.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the date and time the code category was last updated
   */
  @Override
  public LocalDateTime getCodeCategoryUpdated(String codeCategoryId)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    try
    {
      Optional<LocalDateTime> updated = codeCategoryRepository.getUpdatedById(codeCategoryId);

      if (updated.isPresent())
      {
        return updated.get();
      }

      // Check if one of the registered code providers supports the code category
      for (ICodeProvider codeProvider : codeProviders)
      {
        if (codeProvider.codeCategoryExists(codeCategoryId))
        {
          return codeProvider.getCodeCategoryLastUpdated(codeCategoryId);
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to retrieve the date and time the code category (%s) was last updated",
          codeCategoryId), e);
    }
  }

  /**
   * Retrieve the codes for the code category.
   * <p/>
   * NOTE: This will also attempt to retrieve the codes from the appropriate code provider that has
   * been registered with the Codes Service in the <code>META-INF/code-providers.xml</code>
   * configuration file.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the codes for the code category
   */
  @Override
  public List<Code> getCodes(String codeCategoryId)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    try
    {
      if (codeCategoryRepository.existsById(codeCategoryId))
      {
        return codeRepository.findByCodeCategoryId(codeCategoryId);
      }

      // Check if one of the registered code providers supports the code category
      for (ICodeProvider codeProvider : codeProviders)
      {
        if (codeProvider.codeCategoryExists(codeCategoryId))
        {
          return codeProvider.getCodesForCodeCategory(codeCategoryId);
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to retrieve the codes for the code category (%s)", codeCategoryId), e);
    }
  }

  /**
   * Retrieve the codes for the code category using the specified parameters.
   * <p/>
   * NOTE: This will also attempt to retrieve the codes from the appropriate code provider that has
   * been registered with the Codes Service in the <code>META-INF/code-providers.xml</code>
   * configuration file.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   * @param parameters     the parameters
   *
   * @return the codes for the code category
   */
  @Override
  public List<Code> getCodesWithParameters(String codeCategoryId, Map<String, String> parameters)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    try
    {
      if (codeCategoryRepository.existsById(codeCategoryId))
      {
        return codeRepository.findByCodeCategoryId(codeCategoryId);
      }

      // Check if one of the registered code providers supports the code category
      for (ICodeProvider codeProvider : codeProviders)
      {
        if (codeProvider.codeCategoryExists(codeCategoryId))
        {
          return codeProvider.getCodesForCodeCategoryWithParameters(codeCategoryId, parameters);
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to retrieve the codes for the code category (%s)", codeCategoryId), e);
    }
  }

  /**
   * Returns the number of code categories.
   *
   * @return the number of code categories
   */
  @Override
  public long getNumberOfCodeCategories()
    throws CodesServiceException
  {
    try
    {
      return codeCategoryRepository.count();
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to retrieve the number of code categories", e);
    }
  }

  /**
   * Returns the number of codes for the code category.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   *
   * @return the number of codes for the code category
   */
  @Override
  public long getNumberOfCodes(String codeCategoryId)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    try
    {
      if (!codeCategoryRepository.existsById(codeCategoryId))
      {
        throw new CodeCategoryNotFoundException(codeCategoryId);
      }

      return codeRepository.countByCodeCategoryId(codeCategoryId);
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to retrieve the number of codes for the code category (%s)", codeCategoryId), e);
    }

  }

  /**
   * Update the existing code.
   *
   * @param code the <code>Code</code> instance containing the updated information for the code
   *
   * @return the updated code
   */
  @Override
  public Code updateCode(Code code)
    throws CodeNotFoundException, CodesServiceException
  {
    try
    {
      if (!codeRepository.existsById(new CodeId(code.getCodeCategoryId(), code.getId())))
      {
        throw new CodeNotFoundException(code.getCodeCategoryId(), code.getId());
      }

      codeRepository.saveAndFlush(code);

      return code;
    }
    catch (CodeNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format("Failed to update the code (%s)",
          code.getId()), e);
    }

  }

  /**
   * Update the existing code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the updated information
   *                     for the code category
   *
   * @return the updated code category
   */
  @Override
  public CodeCategory updateCodeCategory(CodeCategory codeCategory)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    try
    {
      if (!codeCategoryRepository.existsById(codeCategory.getId()))
      {
        throw new CodeCategoryNotFoundException(codeCategory.getId());
      }

      codeCategory.setUpdated(LocalDateTime.now());

      codeCategoryRepository.saveAndFlush(codeCategory);

      return codeCategory;
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format("Failed to update the code category (%s)",
          codeCategory.getId()), e);
    }
  }

  /**
   * Update the XML or JSON data for the code category.
   *
   * @param codeCategoryId the ID used to uniquely identify the code category
   * @param data           the updated XML or JSON data
   */
  @Override
  @Transactional
  public void updateCodeCategoryData(String codeCategoryId, String data)
    throws CodeCategoryNotFoundException, CodesServiceException
  {
    try
    {
      if (!codeCategoryRepository.existsById(codeCategoryId))
      {
        throw new CodeCategoryNotFoundException(codeCategoryId);
      }

      codeCategoryRepository.setDataAndUpdatedById(codeCategoryId, data, LocalDateTime.now());
    }
    catch (CodeCategoryNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
          "Failed to update the data for the code category (%s)", codeCategoryId), e);
    }
  }

  /**
   * Initialize the code providers.
   */
  private void initCodeProviders()
  {
    // Initialize each code provider
    for (CodeProviderConfig codeProviderConfig : codeProviderConfigs)
    {
      try
      {
        logger.info(String.format("Initializing the code provider (%s) with class (%s)",
            codeProviderConfig.getName(), codeProviderConfig.getClassName()));

        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(
            codeProviderConfig.getClassName());

        Constructor<?> constructor = clazz.getConstructor(CodeProviderConfig.class);

        if (constructor != null)
        {
          // Create an instance of the code provider
          ICodeProvider codeProvider = (ICodeProvider) constructor.newInstance(codeProviderConfig);

          // Perform dependency injection on the code provider
          applicationContext.getAutowireCapableBeanFactory().autowireBean(codeProvider);

          codeProviders.add(codeProvider);
        }
        else
        {
          logger.error(String.format(
              "Failed to register the code provider (%s): The code provider "
              + "class does not provide a constructor with the required signature",
              codeProviderConfig.getClassName()));
        }
      }
      catch (Throwable e)
      {
        logger.error(String.format("Failed to initialize the code provider (%s) with class (%s)",
            codeProviderConfig.getName(), codeProviderConfig.getClassName()), e);
      }
    }
  }

  /**
   * Read the code provider configurations from all the <i>META-INF/code-providers.xml</i>
   * configuration files that can be found on the classpath.
   */
  private void readCodeProviderConfigurations()
    throws CodesServiceException
  {
    try
    {
      codeProviderConfigs = new ArrayList<>();

      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

      // Load the code provider configuration files from the classpath
      Enumeration<URL> codeProviderConfigurationFiles = classLoader.getResources(
          CODE_PROVIDERS_CONFIGURATION_PATH);

      while (codeProviderConfigurationFiles.hasMoreElements())
      {
        URL codeProviderConfigurationFile = codeProviderConfigurationFiles.nextElement();

        if (logger.isDebugEnabled())
        {
          logger.debug(String.format("Reading the code provider configuration file (%s)",
              codeProviderConfigurationFile.toURI().toString()));
        }

        // Retrieve a document builder instance using the factory
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        builderFactory.setValidating(true);

        // builderFactory.setNamespaceAware(true);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();

        builder.setEntityResolver(new DtdJarResolver("code-providers.dtd",
            "META-INF/code-providers.dtd"));
        builder.setErrorHandler(new XmlParserErrorHandler());

        // Parse the code providers configuration file using the document builder
        InputSource inputSource = new InputSource(codeProviderConfigurationFile.openStream());
        Document document = builder.parse(inputSource);
        Element rootElement = document.getDocumentElement();

        List<Element> codeProviderElements = XmlUtil.getChildElements(rootElement, "codeProvider");

        for (Element codeProviderElement : codeProviderElements)
        {
          // Read the handler configuration
          String name = XmlUtil.getChildElementText(codeProviderElement, "name");
          String className = XmlUtil.getChildElementText(codeProviderElement, "class");

          CodeProviderConfig codeProviderConfig = new CodeProviderConfig(name, className);

          codeProviderConfigs.add(codeProviderConfig);
        }
      }
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to read the code provider configuration files", e);
    }
  }
}
