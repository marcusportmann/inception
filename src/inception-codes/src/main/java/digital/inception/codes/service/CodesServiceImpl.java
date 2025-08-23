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

package digital.inception.codes.service;

import digital.inception.codes.exception.CodeCategoryNotFoundException;
import digital.inception.codes.exception.CodeNotFoundException;
import digital.inception.codes.exception.DuplicateCodeCategoryException;
import digital.inception.codes.exception.DuplicateCodeException;
import digital.inception.codes.model.Code;
import digital.inception.codes.model.CodeCategory;
import digital.inception.codes.model.CodeCategorySummary;
import digital.inception.codes.model.CodeId;
import digital.inception.codes.model.CodeProvider;
import digital.inception.codes.model.CodeProviderConfig;
import digital.inception.codes.persistence.jpa.CodeCategoryRepository;
import digital.inception.codes.persistence.jpa.CodeCategorySummaryRepository;
import digital.inception.codes.persistence.jpa.CodeRepository;
import digital.inception.core.exception.InvalidArgumentException;
import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.service.AbstractServiceBase;
import digital.inception.core.xml.DtdJarResolver;
import digital.inception.core.xml.XmlParserErrorHandler;
import digital.inception.core.xml.XmlUtil;
import jakarta.annotation.PostConstruct;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 * The {@code CodesServiceImpl} class provides the Codes Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class CodesServiceImpl extends AbstractServiceBase implements CodesService {

  /**
   * The path to the code provider configuration files (META-INF/code-providers.xml) on the
   * classpath.
   */
  private static final String CODE_PROVIDERS_CONFIGURATION_PATH = "META-INF/code-providers.xml";

  /** The Code Category Repository. */
  private final CodeCategoryRepository codeCategoryRepository;

  /** The Code Category Summary Repository. */
  private final CodeCategorySummaryRepository codeCategorySummaryRepository;

  /** The Code Repository. */
  private final CodeRepository codeRepository;

  /**
   * The configuration information for the code providers read from the code provider configuration
   * files (META-INF/code-providers.xml) on the classpath.
   */
  private List<CodeProviderConfig> codeProviderConfigs;

  /* The code providers. */
  private List<CodeProvider> codeProviders;

  /**
   * Constructs a new {@code CodesServiceImpl}.
   *
   * @param applicationContext the Spring application context
   * @param codeCategoryRepository the Code Category Repository
   * @param codeCategorySummaryRepository the Code Category Summary Repository
   * @param codeRepository the Code Repository
   */
  public CodesServiceImpl(
      ApplicationContext applicationContext,
      CodeCategoryRepository codeCategoryRepository,
      CodeCategorySummaryRepository codeCategorySummaryRepository,
      CodeRepository codeRepository) {
    super(applicationContext);

    this.codeCategoryRepository = codeCategoryRepository;
    this.codeCategorySummaryRepository = codeCategorySummaryRepository;
    this.codeRepository = codeRepository;
  }

  @Override
  public boolean codeCategoryExists(String codeCategoryId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    try {
      return codeCategoryRepository.existsById(codeCategoryId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the code category (" + codeCategoryId + ") exists", e);
    }
  }

  @Override
  public boolean codeExists(String codeCategoryId, String codeId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    if (!StringUtils.hasText(codeId)) {
      throw new InvalidArgumentException("codeId");
    }

    try {
      return codeRepository.existsById(new CodeId(codeCategoryId, codeId));
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the code ("
              + codeId
              + ") exists for the code category ("
              + codeCategoryId
              + ")",
          e);
    }
  }

  @Override
  public void createCode(Code code)
      throws InvalidArgumentException,
          DuplicateCodeException,
          CodeCategoryNotFoundException,
          ServiceUnavailableException {
    validateArgument("code", code);

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
      throw new ServiceUnavailableException(
          "Failed to create the code ("
              + code.getName()
              + ") for the code category ("
              + code.getCodeCategoryId()
              + ")",
          e);
    }
  }

  @Override
  public void createCodeCategory(CodeCategory codeCategory)
      throws InvalidArgumentException, DuplicateCodeCategoryException, ServiceUnavailableException {
    validateArgument("codeCategory", codeCategory);

    try {
      if (codeCategoryRepository.existsById(codeCategory.getId())) {
        throw new DuplicateCodeCategoryException(codeCategory.getId());
      }

      codeCategoryRepository.saveAndFlush(codeCategory);
    } catch (DuplicateCodeCategoryException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the code category (" + codeCategory.getId() + ")", e);
    }
  }

  @Override
  public void deleteCode(String codeCategoryId, String codeId)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    if (!StringUtils.hasText(codeId)) {
      throw new InvalidArgumentException("codeId");
    }

    try {
      CodeId id = new CodeId(codeCategoryId, codeId);

      if (!codeRepository.existsById(id)) {
        throw new CodeNotFoundException(codeCategoryId, codeId);
      }

      codeRepository.deleteByCodeCategoryIdAndId(codeCategoryId, codeId);
    } catch (CodeNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the code ("
              + codeId
              + ") for the code category ("
              + codeCategoryId
              + ")",
          e);
    }
  }

  @Override
  public void deleteCodeCategory(String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    try {
      if (!codeCategoryRepository.existsById(codeCategoryId)) {
        throw new CodeCategoryNotFoundException(codeCategoryId);
      }

      codeCategoryRepository.deleteById(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the code category (" + codeCategoryId + ")", e);
    }
  }

  @Override
  public Code getCode(String codeCategoryId, String codeId)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    if (!StringUtils.hasText(codeId)) {
      throw new InvalidArgumentException("codeId");
    }

    try {
      Optional<Code> codeOptional = codeRepository.findById(new CodeId(codeCategoryId, codeId));

      if (codeOptional.isPresent()) {
        return codeOptional.get();
      } else {
        // Check if one of the registered code providers supports the code category
        for (CodeProvider codeProvider : codeProviders) {
          if (codeProvider.codeCategoryExists(codeCategoryId)) {
            return codeProvider.getCode(codeCategoryId, codeId);
          }
        }
      }

      throw new CodeNotFoundException(codeCategoryId, codeId);
    } catch (CodeNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the code ("
              + codeId
              + ") for the code category ("
              + codeCategoryId
              + ")",
          e);
    }
  }

  @Override
  public List<CodeCategory> getCodeCategories() throws ServiceUnavailableException {
    try {
      return codeCategoryRepository.findAllByOrderByNameAsc();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the code categories", e);
    }
  }

  @Override
  public CodeCategory getCodeCategory(String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    try {
      Optional<CodeCategory> codeCategoryOptional = codeCategoryRepository.findById(codeCategoryId);

      if (codeCategoryOptional.isPresent()) {
        return codeCategoryOptional.get();
      }

      // Check if one of the registered code providers supports the code category
      for (CodeProvider codeProvider : codeProviders) {
        if (codeProvider.codeCategoryExists(codeCategoryId)) {
          return codeProvider.getCodeCategory(codeCategoryId);
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the code category (" + codeCategoryId + ")", e);
    }
  }

  @Override
  public String getCodeCategoryData(String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    try {
      Optional<String> dataOptional = codeCategoryRepository.findDataById(codeCategoryId);

      if (dataOptional.isPresent()) {
        return dataOptional.get();
      }

      // Check if one of the registered code providers supports the code category
      for (CodeProvider codeProvider : codeProviders) {
        if (codeProvider.codeCategoryExists(codeCategoryId)) {
          String codeProviderData = codeProvider.getCodeCategoryData(codeCategoryId);

          return StringUtils.hasText(codeProviderData) ? codeProviderData : "";
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the data for the code category (" + codeCategoryId + ")", e);
    }
  }

  @Override
  public String getCodeCategoryDataWithParameters(
      String codeCategoryId, Map<String, String> parameters)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    try {
      Optional<String> dataOptional = codeCategoryRepository.findDataById(codeCategoryId);

      if (dataOptional.isPresent()) {
        return dataOptional.get();
      }

      // Check if one of the registered code providers supports the code category
      for (CodeProvider codeProvider : codeProviders) {
        if (codeProvider.codeCategoryExists(codeCategoryId)) {
          String codeProviderData =
              codeProvider.getCodeCategoryDataWithParameters(codeCategoryId, parameters);

          return StringUtils.hasText(codeProviderData) ? codeProviderData : "";
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the data for the code category (" + codeCategoryId + ")", e);
    }
  }

  @Override
  public OffsetDateTime getCodeCategoryLastModified(String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    try {
      Optional<OffsetDateTime> lastModifiedOptional =
          codeCategoryRepository.findLastModifiedById(codeCategoryId);

      if (lastModifiedOptional.isPresent()) {
        return lastModifiedOptional.get();
      }

      // Check if one of the registered code providers supports the code category
      for (CodeProvider codeProvider : codeProviders) {
        if (codeProvider.codeCategoryExists(codeCategoryId)) {
          return codeProvider.getCodeCategoryLastModified(codeCategoryId);
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the date and time the code category ("
              + codeCategoryId
              + ") was last updated",
          e);
    }
  }

  @Override
  public String getCodeCategoryName(String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    try {
      Optional<String> nameOptional = codeCategoryRepository.findNameById(codeCategoryId);

      if (nameOptional.isPresent()) {
        return nameOptional.get();
      }

      // Check if one of the registered code providers supports the code category
      for (CodeProvider codeProvider : codeProviders) {
        if (codeProvider.codeCategoryExists(codeCategoryId)) {
          return codeProvider.getCodeCategoryName(codeCategoryId);
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the name of the code category (" + codeCategoryId + ")", e);
    }
  }

  @Override
  public List<CodeCategorySummary> getCodeCategorySummaries() throws ServiceUnavailableException {
    try {
      return codeCategorySummaryRepository.findAllByOrderByNameAsc();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the summaries for the code categories", e);
    }
  }

  @Override
  public String getCodeName(String codeCategoryId, String codeId)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    if (!StringUtils.hasText(codeId)) {
      throw new InvalidArgumentException("codeId");
    }

    try {
      Optional<String> nameOptional =
          codeRepository.findNameByCodeCategoryIdAndId(codeCategoryId, codeId);

      if (nameOptional.isPresent()) {
        return nameOptional.get();
      } else {
        // Check if one of the registered code providers supports the code category
        for (CodeProvider codeProvider : codeProviders) {
          if (codeProvider.codeCategoryExists(codeCategoryId)) {
            return codeProvider.getCodeName(codeCategoryId, codeId);
          }
        }
      }

      throw new CodeNotFoundException(codeCategoryId, codeId);
    } catch (CodeNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
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
   * <p>This will also attempt to retrieve the codes from the appropriate code provider that has
   * been registered with the Codes Service in the {@code META-INF/code-providers.xml} configuration
   * file.
   *
   * @param codeCategoryId the ID for the code category
   * @return the codes for the code category
   */
  @Override
  public List<Code> getCodesForCodeCategory(String codeCategoryId)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    try {
      if (codeCategoryRepository.existsById(codeCategoryId)) {
        return codeRepository.findByCodeCategoryId(codeCategoryId);
      }

      // Check if one of the registered code providers supports the code category
      for (CodeProvider codeProvider : codeProviders) {
        if (codeProvider.codeCategoryExists(codeCategoryId)) {
          return codeProvider.getCodesForCodeCategory(codeCategoryId);
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the codes for the code category (" + codeCategoryId + ")", e);
    }
  }

  /**
   * Retrieve the codes for the code category using the specified parameters.
   *
   * <p>This will also attempt to retrieve the codes from the appropriate code provider that has
   * been registered with the Codes Service in the {@code META-INF/code-providers.xml} configuration
   * file.
   *
   * @param codeCategoryId the ID for the code category
   * @param parameters the parameters
   * @return the codes for the code category
   */
  @Override
  public List<Code> getCodesForCodeCategoryWithParameters(
      String codeCategoryId, Map<String, String> parameters)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    try {
      if (codeCategoryRepository.existsById(codeCategoryId)) {
        return codeRepository.findByCodeCategoryId(codeCategoryId);
      }

      // Check if one of the registered code providers supports the code category
      for (CodeProvider codeProvider : codeProviders) {
        if (codeProvider.codeCategoryExists(codeCategoryId)) {
          return codeProvider.getCodesForCodeCategoryWithParameters(codeCategoryId, parameters);
        }
      }

      throw new CodeCategoryNotFoundException(codeCategoryId);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the codes for the code category (" + codeCategoryId + ")", e);
    }
  }

  /** Initialize the Codes Service. */
  @PostConstruct
  public void init() {
    log.info("Initializing the Codes Service");

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
   * Update the existing code.
   *
   * @param code the {@code Code} instance containing the updated information for the code
   */
  @Override
  public void updateCode(Code code)
      throws InvalidArgumentException, CodeNotFoundException, ServiceUnavailableException {
    validateArgument("code", code);

    try {
      if (!codeRepository.existsById(new CodeId(code.getCodeCategoryId(), code.getId()))) {
        throw new CodeNotFoundException(code.getCodeCategoryId(), code.getId());
      }

      codeRepository.saveAndFlush(code);
    } catch (CodeNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to update the code (" + code.getId() + ")", e);
    }
  }

  /**
   * Update the existing code category.
   *
   * @param codeCategory the {@code CodeCategory} instance containing the updated information for
   *     the code category
   */
  @Override
  public void updateCodeCategory(CodeCategory codeCategory)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    validateArgument("codeCategory", codeCategory);

    try {
      if (!codeCategoryRepository.existsById(codeCategory.getId())) {
        throw new CodeCategoryNotFoundException(codeCategory.getId());
      }

      codeCategoryRepository.saveAndFlush(codeCategory);
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the code category (" + codeCategory.getId() + ")", e);
    }
  }

  /**
   * Update the XML or JSON data for the code category.
   *
   * @param codeCategoryId the ID for the code category
   * @param data the updated XML or JSON data
   */
  @Override
  public void updateCodeCategoryData(String codeCategoryId, String data)
      throws InvalidArgumentException, CodeCategoryNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(codeCategoryId)) {
      throw new InvalidArgumentException("codeCategoryId");
    }

    try {
      if (!codeCategoryRepository.existsById(codeCategoryId)) {
        throw new CodeCategoryNotFoundException(codeCategoryId);
      }

      codeCategoryRepository.setDataAndLastModifiedById(codeCategoryId, data, OffsetDateTime.now());
    } catch (CodeCategoryNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the data for the code category (" + codeCategoryId + ")", e);
    }
  }

  /** Initialize the code providers. */
  private void initCodeProviders() {
    // Initialize each code provider
    for (CodeProviderConfig codeProviderConfig : codeProviderConfigs) {
      try {
        log.info(
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
          CodeProvider codeProvider = (CodeProvider) constructor.newInstance(codeProviderConfig);

          // Perform dependency injection on the code provider
          getApplicationContext().getAutowireCapableBeanFactory().autowireBean(codeProvider);

          codeProviders.add(codeProvider);
        } else {
          log.error(
              "Failed to register the code provider ("
                  + codeProviderConfig.getClassName()
                  + "): The code provider class does not provide a constructor with the required "
                  + "signature");
        }
      } catch (Throwable e) {
        log.error(
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
  private void readCodeProviderConfigurations() throws ServiceUnavailableException {
    try {
      codeProviderConfigs = new ArrayList<>();

      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

      // Load the code provider configuration files from the classpath
      Enumeration<URL> codeProviderConfigurationFiles =
          classLoader.getResources(CODE_PROVIDERS_CONFIGURATION_PATH);

      while (codeProviderConfigurationFiles.hasMoreElements()) {
        URL codeProviderConfigurationFile = codeProviderConfigurationFiles.nextElement();

        if (log.isDebugEnabled()) {
          log.debug(
              "Reading the code provider configuration file ("
                  + codeProviderConfigurationFile.toURI()
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
          Optional<String> nameOptional = XmlUtil.getChildElementText(codeProviderElement, "name");
          Optional<String> classNameOptional =
              XmlUtil.getChildElementText(codeProviderElement, "class");

          if (nameOptional.isPresent() && classNameOptional.isPresent()) {
            CodeProviderConfig codeProviderConfig =
                new CodeProviderConfig(nameOptional.get(), classNameOptional.get());

            codeProviderConfigs.add(codeProviderConfig);
          }
        }
      }
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to read the code provider configuration files", e);
    }
  }
}
