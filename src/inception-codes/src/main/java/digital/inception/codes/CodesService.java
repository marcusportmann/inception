/*
 * Copyright 2020 Marcus Portmann
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

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.xml.DtdJarResolver;
import digital.inception.core.xml.XmlParserErrorHandler;
import digital.inception.core.xml.XmlUtil;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CodesService</code> class provides the Codes Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class CodesService implements ICodesService, InitializingBean {

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
  private final ApplicationContext applicationContext;

  /**
   * The Code Category Repository.
   */
  private final CodeCategoryRepository codeCategoryRepository;

  /**
   * The Code Category Summary Repository.
   */
  private final CodeCategorySummaryRepository codeCategorySummaryRepository;

  /**
   * The Code Repository.
   */
  private final CodeRepository codeRepository;

  /**
   * The configuration information for the code providers read from the code provider configuration
   * files (META-INF/code-providers.xml) on the classpath.
   */
  private List<CodeProviderConfig> codeProviderConfigs;

  /* The code providers. */
  private List<ICodeProvider> codeProviders;

  /**
   * Constructs a new <code>CodesService</code>.
   *
   * @param applicationContext            the Spring application context
   * @param codeCategoryRepository        the Code Category Repository
   * @param codeCategorySummaryRepository the Code Category Summary Repository
   * @param codeRepository                the Code Repository
   */
  public CodesService(
      ApplicationContext applicationContext,
      CodeCategoryRepository codeCategoryRepository,
      CodeCategorySummaryRepository codeCategorySummaryRepository,
      CodeRepository codeRepository) {
    this.applicationContext = applicationContext;
    this.codeCategoryRepository = codeCategoryRepository;
    this.codeCategorySummaryRepository = codeCategorySummaryRepository;
    this.codeRepository = codeRepository;
  }

  /**
   * Initialize the Codes Service.
   */
  @Override
  public void afterPropertiesSet() {
    logger.info("Initializing the Codes Service");

    codeProviders = new ArrayList<>();

    try {
      // Read the codes configuration
      readCodeProviderConfigurations();

      // Initialize the code providers
      initCodeProviders();
    } catch (Throwable e) {
      throw new RuntimeException("Failed to initialize the Codes Service", e);
    }
  }

  /**
   * Check whether the code category exists.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   *
   * @return <code>true</code> if the code category exists or <code>false</code> otherwise
   */
  @Override
  public boolean codeCategoryExists(String codeCategoryId) throws CodesServiceException {
    try {
      return codeCategoryRepository.existsById(codeCategoryId);
    } catch (Throwable e) {
      throw new CodesServiceException(
          "Failed to check whether the code category (" + codeCategoryId + ") exists", e);
    }
  }

  /**
   * Check whether the code exists.
   *
   * @param codeCategoryId the ID uniquely identifying the code category the code is associated
   *                       with
   * @param codeId         the ID uniquely identifying the code
   *
   * @return <code>true</code> if the code exists or <code>false</code> otherwise
   */
  @Override
  public boolean codeExists(String codeCategoryId, String codeId) throws CodesServiceException {
    try {
      return codeRepository.existsById(new CodeId(codeCategoryId, codeId));
    } catch (Throwable e) {
      throw new CodesServiceException(
          "Failed to check whether the code ("
              + codeId
              + ") exists for the code category ("
              + codeCategoryId
              + ")",
          e);
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
      throws DuplicateCodeException, CodeCategoryNotFoundException, CodesServiceException {
    try {
      if (codeRepository.existsById(new CodeId(code.getCodeCategoryId(), code.getId()))) {
        throw new DuplicateCodeException(code.getCodeCategoryId(), code.getId());
      }

      if (!codeCategoryRepository.existsById(code.getCodeCategoryId())) {
        throw new CodeCategoryNotFoundException(code.getCodeCategoryId());
      }

      codeRepository.saveAndFlush(code);
    } catch (DuplicateCodeException | CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodesServiceException(
          "Failed to create the code ("
              + code.getName()
              + ") for the code category ("
              + code.getCodeCategoryId()
              + ")",
          e);
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
      throws DuplicateCodeCategoryException, CodesServiceException {
    try {
      if (codeCategoryRepository.existsById(codeCategory.getId())) {
        throw new DuplicateCodeCategoryException(codeCategory.getId());
      }

      if (codeCategory.getUpdated() == null) {
        codeCategory.setUpdated(LocalDateTime.now());
      }

      codeCategoryRepository.saveAndFlush(codeCategory);
    } catch (DuplicateCodeCategoryException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodesServiceException(
          "Failed to create the code category (" + codeCategory.getId() + ")", e);
    }
  }

  /**
   * Delete the code.
   *
   * @param codeCategoryId the ID uniquely identifying the code category the code is associated
   *                       with
   * @param codeId         the ID uniquely identifying the code
   */
  @Override
  @Transactional
  public void deleteCode(String codeCategoryId, String codeId)
      throws CodeNotFoundException, CodesServiceException {
    try {
      CodeId id = new CodeId(codeCategoryId, codeId);

      if (!codeRepository.existsById(id)) {
        throw new CodeNotFoundException(codeCategoryId, codeId);
      }

      codeRepository.deleteById(codeCategoryId, codeId);
    } catch (CodeNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodesServiceException(
          "Failed to delete the code ("
              + codeId
              + ") for the code category ("
              + codeCategoryId
              + ")",
          e);
    }
  }

  /**
   * Delete the code category.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   */
  @Override
  @Transactional
  public void deleteCodeCategory(String codeCategoryId)
      throws CodeCategoryNotFoundException, CodesServiceException {
    try {
      if (!codeCategoryRepository.existsById(codeCategoryId)) {
        throw new CodeCategoryNotFoundException(codeCategoryId);
      }

      codeCategoryRepository.deleteById(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodesServiceException(
          "Failed to delete the code category (" + codeCategoryId + ")", e);
    }
  }

  /**
   * Retrieve the code.
   *
   * @param codeCategoryId the ID uniquely identifying the code category the code is associated
   *                       with
   * @param codeId         the ID uniquely identifying the code
   *
   * @return the code
   */
  @Override
  public Code getCode(String codeCategoryId, String codeId)
      throws CodeNotFoundException, CodesServiceException {
    try {
      Optional<Code> codeOptional = codeRepository.findById(new CodeId(codeCategoryId, codeId));

      if (codeOptional.isPresent()) {
        return codeOptional.get();
      } else {
        // Check if one of the registered code providers supports the code category
        for (ICodeProvider codeProvider : codeProviders) {
          if (codeProvider.codeCategoryExists(codeCategoryId)) {
            return codeProvider.getCode(codeCategoryId, codeId);
          }
        }
      }

      throw new CodeNotFoundException(codeCategoryId, codeId);
    } catch (CodeNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodesServiceException(
          "Failed to retrieve the code ("
              + codeId
              + ") for the code category ("
              + codeCategoryId
              + ")",
          e);
    }
  }

  /**
   * Returns all the code categories.
   *
   * @return all the code categories
   */
  @Override
  public List<CodeCategory> getCodeCategories() throws CodesServiceException {
    try {
      return codeCategoryRepository.findAll();
    } catch (Throwable e) {
      throw new CodesServiceException("Failed to retrieve the code categories", e);
    }
  }

  /**
   * Retrieve the code category.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   *
   * @return the code category
   */
  @Override
  public CodeCategory getCodeCategory(String codeCategoryId)
      throws CodeCategoryNotFoundException, CodesServiceException {
    try {
      Optional<CodeCategory> codeCategoryOptional = codeCategoryRepository.findById(codeCategoryId);

      if (codeCategoryOptional.isPresent()) {
        return codeCategoryOptional.get();
      }

      // Check if one of the registered code providers supports the code category
      for (ICodeProvider codeProvider : codeProviders) {
        if (codeProvider.codeCategoryExists(codeCategoryId)) {
          return codeProvider.getCodeCategory(codeCategoryId);
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodesServiceException(
          "Failed to retrieve the code category (" + codeCategoryId + ")", e);
    }
  }

  /**
   * Retrieve the XML or JSON data for the code category.
   *
   * <p>NOTE: This will also attempt to retrieve the data from the appropriate code provider that
   * has been registered with the Codes Service in the <code>META-INF/code-providers.xml</code>
   * configuration file.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   *
   * @return the XML or JSON data for the code category
   */
  @Override
  public String getCodeCategoryData(String codeCategoryId)
      throws CodeCategoryNotFoundException, CodesServiceException {
    try {
      Optional<String> dataOptional = codeCategoryRepository.getDataById(codeCategoryId);

      if (dataOptional.isPresent()) {
        return dataOptional.get();
      }

      // Check if one of the registered code providers supports the code category
      for (ICodeProvider codeProvider : codeProviders) {
        if (codeProvider.codeCategoryExists(codeCategoryId)) {
          String codeProviderData = codeProvider.getCodeCategoryData(codeCategoryId);

          return StringUtils.isEmpty(codeProviderData) ? "" : codeProviderData;
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodesServiceException(
          "Failed to retrieve the data for the code category (" + codeCategoryId + ")", e);
    }
  }

  /**
   * Retrieve the XML or JSON data for the code category using the specified parameters.
   *
   * <p>NOTE: This will also attempt to retrieve the data from the appropriate code provider that
   * has been registered with the Codes Service in the <code>META-INF/code-providers.xml</code>
   * configuration file.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   * @param parameters     the parameters
   *
   * @return the XML or JSON data for the code category
   */
  @Override
  public String getCodeCategoryDataWithParameters(
      String codeCategoryId, Map<String, String> parameters)
      throws CodeCategoryNotFoundException, CodesServiceException {
    try {
      Optional<String> dataOptional = codeCategoryRepository.getDataById(codeCategoryId);

      if (dataOptional.isPresent()) {
        return dataOptional.get();
      }

      // Check if one of the registered code providers supports the code category
      for (ICodeProvider codeProvider : codeProviders) {
        if (codeProvider.codeCategoryExists(codeCategoryId)) {
          String codeProviderData =
              codeProvider.getCodeCategoryDataWithParameters(codeCategoryId, parameters);

          return StringUtils.isEmpty(codeProviderData) ? "" : codeProviderData;
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodesServiceException(
          "Failed to retrieve the data for the code category (" + codeCategoryId + ")", e);
    }
  }

  /**
   * Retrieve the name of the code category.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   *
   * @return the name of the code category
   */
  @Override
  public String getCodeCategoryName(String codeCategoryId)
      throws CodeCategoryNotFoundException, CodesServiceException {
    try {
      Optional<String> nameOptional = codeCategoryRepository.getNameById(codeCategoryId);

      if (nameOptional.isPresent()) {
        return nameOptional.get();
      }

      // Check if one of the registered code providers supports the code category
      for (ICodeProvider codeProvider : codeProviders) {
        if (codeProvider.codeCategoryExists(codeCategoryId)) {
          return codeProvider.getCodeCategoryName(codeCategoryId);
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodesServiceException(
          "Failed to retrieve the name for the code category (" + codeCategoryId + ")", e);
    }
  }

  /**
   * Returns the summaries for all the code categories.
   *
   * @return the summaries for all the code categories
   */
  @Override
  public List<CodeCategorySummary> getCodeCategorySummaries() throws CodesServiceException {
    try {
      return codeCategorySummaryRepository.findAll();
    } catch (Throwable e) {
      throw new CodesServiceException(
          "Failed to retrieve the summaries for the code categories", e);
    }
  }

  /**
   * Returns the date and time the code category was last updated.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   *
   * @return the date and time the code category was last updated
   */
  @Override
  public LocalDateTime getCodeCategoryUpdated(String codeCategoryId)
      throws CodeCategoryNotFoundException, CodesServiceException {
    try {
      Optional<LocalDateTime> updatedOptional =
          codeCategoryRepository.getUpdatedById(codeCategoryId);

      if (updatedOptional.isPresent()) {
        return updatedOptional.get();
      }

      // Check if one of the registered code providers supports the code category
      for (ICodeProvider codeProvider : codeProviders) {
        if (codeProvider.codeCategoryExists(codeCategoryId)) {
          return codeProvider.getCodeCategoryLastUpdated(codeCategoryId);
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodesServiceException(
          "Failed to retrieve the date and time the code category ("
              + codeCategoryId
              + ") was last updated",
          e);
    }
  }

  /**
   * Retrieve the name of the code.
   *
   * @param codeCategoryId the ID uniquely identifying the code category the code is associated
   *                       with
   * @param codeId         the ID uniquely identifying the code
   *
   * @return the name of the code
   */
  @Override
  public String getCodeName(String codeCategoryId, String codeId)
      throws CodeNotFoundException, CodesServiceException {
    try {
      Optional<String> nameOptional = codeRepository.getNameById(codeCategoryId, codeId);

      if (nameOptional.isPresent()) {
        return nameOptional.get();
      } else {
        // Check if one of the registered code providers supports the code category
        for (ICodeProvider codeProvider : codeProviders) {
          if (codeProvider.codeCategoryExists(codeCategoryId)) {
            return codeProvider.getCodeName(codeCategoryId, codeId);
          }
        }
      }

      throw new CodeNotFoundException(codeCategoryId, codeId);
    } catch (CodeNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodesServiceException(
          "Failed to retrieve the name of the code ("
              + codeId
              + ") for the code category ("
              + codeCategoryId
              + ")",
          e);
    }
  }

  /**
   * Retrieve the codes for the code category.
   *
   * <p>NOTE: This will also attempt to retrieve the codes from the appropriate code provider that
   * has been registered with the Codes Service in the <code>META-INF/code-providers.xml</code>
   * configuration file.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   *
   * @return the codes for the code category
   */
  @Override
  public List<Code> getCodesForCodeCategory(String codeCategoryId)
      throws CodeCategoryNotFoundException, CodesServiceException {
    try {
      if (codeCategoryRepository.existsById(codeCategoryId)) {
        return codeRepository.findByCodeCategoryId(codeCategoryId);
      }

      // Check if one of the registered code providers supports the code category
      for (ICodeProvider codeProvider : codeProviders) {
        if (codeProvider.codeCategoryExists(codeCategoryId)) {
          return codeProvider.getCodesForCodeCategory(codeCategoryId);
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodesServiceException(
          "Failed to retrieve the codes for the code category (" + codeCategoryId + ")", e);
    }
  }

  /**
   * Retrieve the codes for the code category using the specified parameters.
   *
   * <p>NOTE: This will also attempt to retrieve the codes from the appropriate code provider that
   * has been registered with the Codes Service in the <code>META-INF/code-providers.xml</code>
   * configuration file.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   * @param parameters     the parameters
   *
   * @return the codes for the code category
   */
  @Override
  public List<Code> getCodesForCodeCategoryWithParameters(
      String codeCategoryId, Map<String, String> parameters)
      throws CodeCategoryNotFoundException, CodesServiceException {
    try {
      if (codeCategoryRepository.existsById(codeCategoryId)) {
        return codeRepository.findByCodeCategoryId(codeCategoryId);
      }

      // Check if one of the registered code providers supports the code category
      for (ICodeProvider codeProvider : codeProviders) {
        if (codeProvider.codeCategoryExists(codeCategoryId)) {
          return codeProvider.getCodesForCodeCategoryWithParameters(codeCategoryId, parameters);
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodesServiceException(
          "Failed to retrieve the codes for the code category (" + codeCategoryId + ")", e);
    }
  }

  /**
   * Update the existing code.
   *
   * @param code the <code>Code</code> instance containing the updated information for the code
   */
  @Override
  public void updateCode(Code code) throws CodeNotFoundException, CodesServiceException {
    try {
      if (!codeRepository.existsById(new CodeId(code.getCodeCategoryId(), code.getId()))) {
        throw new CodeNotFoundException(code.getCodeCategoryId(), code.getId());
      }

      codeRepository.saveAndFlush(code);
    } catch (CodeNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodesServiceException("Failed to update the code (" + code.getId() + ")", e);
    }
  }

  /**
   * Update the existing code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the updated information
   *                     for the code category
   */
  @Override
  public void updateCodeCategory(CodeCategory codeCategory)
      throws CodeCategoryNotFoundException, CodesServiceException {
    try {
      if (!codeCategoryRepository.existsById(codeCategory.getId())) {
        throw new CodeCategoryNotFoundException(codeCategory.getId());
      }

      codeCategory.setUpdated(LocalDateTime.now());

      codeCategoryRepository.saveAndFlush(codeCategory);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodesServiceException(
          "Failed to update the code category (" + codeCategory.getId() + ")", e);
    }
  }

  /**
   * Update the XML or JSON data for the code category.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   * @param data           the updated XML or JSON data
   */
  @Override
  @Transactional
  public void updateCodeCategoryData(String codeCategoryId, String data)
      throws CodeCategoryNotFoundException, CodesServiceException {
    try {
      if (!codeCategoryRepository.existsById(codeCategoryId)) {
        throw new CodeCategoryNotFoundException(codeCategoryId);
      }

      codeCategoryRepository.setDataAndUpdatedById(codeCategoryId, data, LocalDateTime.now());
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new CodesServiceException(
          "Failed to update the data for the code category (" + codeCategoryId + ")", e);
    }
  }

  /**
   * Initialize the code providers.
   */
  private void initCodeProviders() {
    // Initialize each code provider
    for (CodeProviderConfig codeProviderConfig : codeProviderConfigs) {
      try {
        logger.info(
            "Initializing the code provider ("
                + codeProviderConfig.getName()
                + ") with class ("
                + codeProviderConfig.getClassName()
                + ")");

        Class<?> clazz =
            Thread.currentThread()
                .getContextClassLoader()
                .loadClass(codeProviderConfig.getClassName());

        Constructor<?> constructor;

        try {
          constructor = clazz.getConstructor(CodeProviderConfig.class);
        } catch (NoSuchMethodException e) {
          constructor = null;
        }

        if (constructor != null) {
          // Create an instance of the code provider
          ICodeProvider codeProvider = (ICodeProvider) constructor.newInstance(codeProviderConfig);

          // Perform dependency injection on the code provider
          applicationContext.getAutowireCapableBeanFactory().autowireBean(codeProvider);

          codeProviders.add(codeProvider);
        } else {
          logger.error(
              "Failed to register the code provider ("
                  + codeProviderConfig.getClassName()
                  + "): The code provider class does not provide a constructor with the required "
                  + "signature");
        }
      } catch (Throwable e) {
        logger.error(
            "Failed to initialize the code provider ("
                + codeProviderConfig.getName()
                + ") with class ("
                + codeProviderConfig.getClassName()
                + ")",
            e);
      }
    }
  }

  /**
   * Read the code provider configurations from all the <i>META-INF/code-providers.xml</i>
   * configuration files that can be found on the classpath.
   */
  private void readCodeProviderConfigurations() throws CodesServiceException {
    try {
      codeProviderConfigs = new ArrayList<>();

      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

      // Load the code provider configuration files from the classpath
      Enumeration<URL> codeProviderConfigurationFiles =
          classLoader.getResources(CODE_PROVIDERS_CONFIGURATION_PATH);

      while (codeProviderConfigurationFiles.hasMoreElements()) {
        URL codeProviderConfigurationFile = codeProviderConfigurationFiles.nextElement();

        if (logger.isDebugEnabled()) {
          logger.debug(
              "Reading the code provider configuration file ("
                  + codeProviderConfigurationFile.toURI().toString()
                  + ")");
        }

        // Retrieve a document builder instance using the factory
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        builderFactory.setValidating(true);

        // builderFactory.setNamespaceAware(true);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();

        builder.setEntityResolver(
            new DtdJarResolver("code-providers.dtd", "META-INF/code-providers.dtd"));
        builder.setErrorHandler(new XmlParserErrorHandler());

        // Parse the code providers configuration file using the document builder
        InputSource inputSource = new InputSource(codeProviderConfigurationFile.openStream());
        Document document = builder.parse(inputSource);
        Element rootElement = document.getDocumentElement();

        List<Element> codeProviderElements = XmlUtil.getChildElements(rootElement, "codeProvider");

        for (Element codeProviderElement : codeProviderElements) {
          // Read the handler configuration
          String name = XmlUtil.getChildElementText(codeProviderElement, "name");
          String className = XmlUtil.getChildElementText(codeProviderElement, "class");

          CodeProviderConfig codeProviderConfig = new CodeProviderConfig(name, className);

          codeProviderConfigs.add(codeProviderConfig);
        }
      }
    } catch (Throwable e) {
      throw new CodesServiceException("Failed to read the code provider configuration files", e);
    }
  }
}
