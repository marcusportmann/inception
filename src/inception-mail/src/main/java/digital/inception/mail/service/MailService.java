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

package digital.inception.mail.service;

import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.service.ValidationError;
import digital.inception.mail.model.DuplicateMailTemplateException;
import digital.inception.mail.model.MailTemplate;
import digital.inception.mail.model.MailTemplateContentType;
import digital.inception.mail.model.MailTemplateNotFoundException;
import digital.inception.mail.model.MailTemplateSummary;
import digital.inception.mail.persistence.MailTemplateRepository;
import digital.inception.mail.persistence.MailTemplateSummaryRepository;
import freemarker.cache.TemplateLoader;
import freemarker.cache.TemplateLookupContext;
import freemarker.cache.TemplateLookupResult;
import freemarker.cache.TemplateLookupStrategy;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The <b>MailService</b> class provides the Mail Service implementation.
 *
 * @author Marcus Portmann
 */

@Service
@SuppressWarnings({"unused"})
public class MailService implements IMailService {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(MailService.class);

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** The Apache FreeMarker configuration., */
  private final Configuration freeMarkerConfiguration;

  /** The Mail Template Repository. */
  private final MailTemplateRepository mailTemplateRepository;

  /** The Mail Template Summary Repository. */
  private final MailTemplateSummaryRepository mailTemplateSummaryRepository;

  /** The JSR-380 validator. */
  private final Validator validator;

  /** The Java mail sender. */
  private JavaMailSender javaMailSender;

  /**
   * Constructs a new <b>MailService</b>.
   *
   * @param applicationContext the Spring application context
   * @param validator the JSR-380 validator
   * @param mailTemplateRepository the Mail Template Repository
   * @param mailTemplateSummaryRepository the Mail Template Summary Repository
   */
  public MailService(
      ApplicationContext applicationContext,
      Validator validator,
      MailTemplateRepository mailTemplateRepository,
      MailTemplateSummaryRepository mailTemplateSummaryRepository) {
    this.applicationContext = applicationContext;
    this.validator = validator;
    this.mailTemplateRepository = mailTemplateRepository;
    this.mailTemplateSummaryRepository = mailTemplateSummaryRepository;

    this.freeMarkerConfiguration = new Configuration(Configuration.VERSION_2_3_29);
    this.freeMarkerConfiguration.setTemplateLoader(new FreeMarkerTemplateLoader(this));
    this.freeMarkerConfiguration.setTemplateLookupStrategy(
        new TemplateLookupStrategy() {
          @Override
          public TemplateLookupResult lookup(TemplateLookupContext templateLookupContext)
              throws IOException {
            return templateLookupContext.lookupWithAcquisitionStrategy(
                templateLookupContext.getTemplateName());
          }

          @Override
          public String toString() {
            return "MailServiceLookupStrategy";
          }
        });
  }

  @Override
  @CachePut(cacheNames = "mailTemplates", key = "#mailTemplate.id")
  public MailTemplate createMailTemplate(MailTemplate mailTemplate)
      throws InvalidArgumentException, DuplicateMailTemplateException, ServiceUnavailableException {
    validateMailTemplate(mailTemplate);

    try {
      if (mailTemplateRepository.existsById(mailTemplate.getId())) {
        throw new DuplicateMailTemplateException(mailTemplate.getId());
      }

      return mailTemplateRepository.saveAndFlush(mailTemplate);
    } catch (DuplicateMailTemplateException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the mail template (" + mailTemplate.getId() + ")", e);
    }
  }

  @Override
  @CacheEvict(cacheNames = "mailTemplates", key = "#mailTemplateId")
  public void deleteMailTemplate(String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(mailTemplateId)) {
      throw new InvalidArgumentException("mailTemplateId");
    }

    try {
      if (!mailTemplateRepository.existsById(mailTemplateId)) {
        throw new MailTemplateNotFoundException(mailTemplateId);
      }

      mailTemplateRepository.deleteById(mailTemplateId);

      /*
       * Clear the FreeMarker template cache. This could be optimized in future but given that
       * message templates are not updated on a continuous or even regular basis this is probably
       * sufficient for now.
       */
      freeMarkerConfiguration.clearTemplateCache();
    } catch (MailTemplateNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the mail template (" + mailTemplateId + ")", e);
    }
  }

  @Override
  @Cacheable(cacheNames = "mailTemplates", key = "#mailTemplateId")
  public MailTemplate getMailTemplate(String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(mailTemplateId)) {
      throw new InvalidArgumentException("mailTemplateId");
    }

    try {
      Optional<MailTemplate> mailTemplateOptional = mailTemplateRepository.findById(mailTemplateId);

      if (mailTemplateOptional.isPresent()) {
        return mailTemplateOptional.get();
      } else {
        throw new MailTemplateNotFoundException(mailTemplateId);
      }
    } catch (MailTemplateNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the mail template (" + mailTemplateId + ")", e);
    }
  }

  @Override
  public OffsetDateTime getMailTemplateLastModified(String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(mailTemplateId)) {
      throw new InvalidArgumentException("mailTemplateId");
    }

    try {
      Optional<OffsetDateTime> updatedOptional =
          mailTemplateRepository.getLastModifiedById(mailTemplateId);

      if (updatedOptional.isPresent()) {
        return updatedOptional.get();
      }

      throw new MailTemplateNotFoundException(mailTemplateId);
    } catch (MailTemplateNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the date and time the mail template ("
              + mailTemplateId
              + ") was last modified",
          e);
    }
  }

  @Override
  public String getMailTemplateName(String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(mailTemplateId)) {
      throw new InvalidArgumentException("mailTemplateId");
    }

    try {
      Optional<String> nameOptional = mailTemplateRepository.getNameById(mailTemplateId);

      if (nameOptional.isPresent()) {
        return nameOptional.get();
      }

      throw new MailTemplateNotFoundException(mailTemplateId);
    } catch (MailTemplateNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the name of the mail template (" + mailTemplateId + ")", e);
    }
  }

  @Override
  public List<MailTemplateSummary> getMailTemplateSummaries() throws ServiceUnavailableException {
    try {
      return mailTemplateSummaryRepository.findAllByOrderByNameAsc();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the summaries for the mail templates", e);
    }
  }

  @Override
  public MailTemplateSummary getMailTemplateSummary(String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(mailTemplateId)) {
      throw new InvalidArgumentException("mailTemplateId");
    }

    try {
      Optional<MailTemplateSummary> mailTemplateSummaryOptional =
          mailTemplateSummaryRepository.findById(mailTemplateId);

      if (mailTemplateSummaryOptional.isPresent()) {
        return mailTemplateSummaryOptional.get();
      } else {
        throw new MailTemplateNotFoundException(mailTemplateId);
      }
    } catch (MailTemplateNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the summary for the mail template (" + mailTemplateId + ")", e);
    }
  }

  @Override
  public List<MailTemplate> getMailTemplates() throws ServiceUnavailableException {
    try {
      return mailTemplateRepository.findAllByOrderByNameAsc();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the mail templates", e);
    }
  }

  /** Initialize the Mail Service. */
  @PostConstruct
  public void init() {
    try {
      javaMailSender = applicationContext.getBean(JavaMailSender.class);
    } catch (NoSuchBeanDefinitionException ignored) {
      log.warn("No JavaMailSender implementation found");
    }
  }

  @Override
  public boolean mailTemplateExists(String mailTemplateId)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(mailTemplateId)) {
      throw new InvalidArgumentException("mailTemplateId");
    }

    try {
      return mailTemplateRepository.existsById(mailTemplateId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the mail template (" + mailTemplateId + ") exists", e);
    }
  }

  @Override
  public String processMailTemplate(String mailTemplateId, Map<String, String> templateParameters)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(mailTemplateId)) {
      throw new InvalidArgumentException("mailTemplateId");
    }

    if (templateParameters == null) {
      throw new InvalidArgumentException("templateParameters");
    }

    try {
      Template template = freeMarkerConfiguration.getTemplate(mailTemplateId);

      StringWriter sw = new StringWriter();
      template.process(templateParameters, sw);

      return sw.toString();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to process the mail template (" + mailTemplateId + ")", e);
    }
  }

  @Override
  public void sendMail(
      List<String> to,
      String subject,
      String from,
      String fromName,
      String mailTemplateId,
      Map<String, String> mailTemplateParameters)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException {
    if (to == null) {
      throw new InvalidArgumentException("to");
    }

    if (!StringUtils.hasText(subject)) {
      throw new InvalidArgumentException("subject");
    }

    if (!StringUtils.hasText(from)) {
      throw new InvalidArgumentException("from");
    }

    if (!StringUtils.hasText(fromName)) {
      throw new InvalidArgumentException("fromName");
    }

    if (!StringUtils.hasText(mailTemplateId)) {
      throw new InvalidArgumentException("mailTemplateId");
    }

    if (mailTemplateParameters == null) {
      throw new InvalidArgumentException("mailTemplateParameters");
    }

    try {
      // Retrieve the mail template
      Optional<MailTemplate> mailTemplateOptional = mailTemplateRepository.findById(mailTemplateId);

      if (mailTemplateOptional.isEmpty()) {
        throw new MailTemplateNotFoundException(mailTemplateId);
      }

      MailTemplate mailTemplate = mailTemplateOptional.get();

      if (javaMailSender != null) {
        // Send the email message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(new InternetAddress(from, fromName));

        helper.setTo(to.toArray(new String[0]));

        helper.setSubject(subject);

        helper.setText(
            processMailTemplate(mailTemplate.getId(), mailTemplateParameters),
            mailTemplate.getContentType() == MailTemplateContentType.HTML);

        javaMailSender.send(helper.getMimeMessage());
      } else {
        throw new ServiceUnavailableException("No JavaMailSender bean has been configured");
      }
    } catch (MailTemplateNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to send the mail", e);
    }
  }

  @Override
  @CachePut(cacheNames = "mailTemplates", key = "#mailTemplate.id")
  public MailTemplate updateMailTemplate(MailTemplate mailTemplate)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException {
    validateMailTemplate(mailTemplate);

    try {
      if (!mailTemplateRepository.existsById(mailTemplate.getId())) {
        throw new MailTemplateNotFoundException(mailTemplate.getId());
      }

      mailTemplateRepository.saveAndFlush(mailTemplate);

      /*
       * Clear the FreeMarker template cache. This could be optimized in future but given that
       * message templates are not updated on a continuous or even regular basis this is probably
       * sufficient for now.
       */
      freeMarkerConfiguration.clearTemplateCache();

      return mailTemplate;
    } catch (MailTemplateNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the mail template (" + mailTemplate.getId() + ")", e);
    }
  }

  private void validateMailTemplate(MailTemplate mailTemplate) throws InvalidArgumentException {
    if (mailTemplate == null) {
      throw new InvalidArgumentException("mailTemplate");
    }

    Set<ConstraintViolation<MailTemplate>> constraintViolations = validator.validate(mailTemplate);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "mailTemplate", ValidationError.toValidationErrors(constraintViolations));
    }
  }

  /**
   * The <b>FreeMarkerTemplateLoader</b> class implements the Apache FreeMarker template loader.
   *
   * @author Marcus Portmann
   */
  public static class FreeMarkerTemplateLoader implements TemplateLoader {

    /** The Mail Service. */
    private final IMailService mailService;

    /**
     * Constructs a new <b>FreeMarkerTemplateLoader</b>.
     *
     * @param mailService the Mail Service
     */
    public FreeMarkerTemplateLoader(IMailService mailService) {
      this.mailService = mailService;
    }

    /**
     * Closes the template source, releasing any resources held that are only required for reading
     * the template and/or its metadata. This is the last method that is called by the {@link
     * freemarker.cache.TemplateCache} for a template source, except that {@link
     * Object#equals(Object)} is might called later too. {@link freemarker.cache.TemplateCache}
     * ensures that this method will be called on every object that is returned from {@link
     * #findTemplateSource(String)}.
     *
     * @param templateSource the template source that should be closed.
     */
    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {}

    /**
     * Finds the template in the backing storage and returns an object that identifies the storage
     * location where the template can be loaded from. See the return value for more information.
     *
     * @param name The name of the template, already localized and normalized by the {@link
     *     freemarker.cache.TemplateCache cache}.
     * @return An object representing the template source, which can be supplied in subsequent calls
     *     to {@link #getLastModified(Object)} and {@link #getReader(Object, String)}, when those
     *     are called on the same {@link TemplateLoader}. {@code null} must be returned if the
     *     source for the template doesn't exist
     */
    @Override
    public Object findTemplateSource(String name) throws IOException {
      try {
        MailTemplate mailTemplate = mailService.getMailTemplate(name);

        return mailTemplate.getId();
      } catch (MailTemplateNotFoundException e) {
        return null;
      } catch (Throwable e) {
        throw new IOException("Failed to find the template source (" + name + ")", e);
      }
    }

    /**
     * Returns the time of last modification of the specified template source. This method is called
     * after <b>findTemplateSource()</b>.
     *
     * @param templateSource an object representing a template source, obtained through a prior call
     *     to {@link #findTemplateSource(String)}
     * @return the time of last modification of the specified template source, or -1 if the time is
     *     not known.
     */
    @Override
    public long getLastModified(Object templateSource) {
      try {
        if (templateSource instanceof String) {
          MailTemplate mailTemplate = mailService.getMailTemplate((String) templateSource);

          OffsetDateTime lastModified = mailTemplate.getLastModified();

          if (lastModified != null) {
            return lastModified.toEpochSecond();
          } else {
            return -1;
          }
        }
      } catch (Throwable ignored) {
      }

      return -1;
    }

    /**
     * Returns the character stream of a template represented by the specified template source. This
     * method is possibly called for multiple times for the same template source object, and it must
     * always return a {@link Reader} that reads the template from its beginning. Before this method
     * is called for the second time (or later), its caller must close the previously returned
     * {@link Reader}, and it must not use it anymore. That is, this method is not required to
     * support multiple concurrent readers for the same source {@code templateSource} object.
     *
     * <p>Typically, this method is called if the template is missing from the cache, or if after
     * calling {@link #findTemplateSource(String)} and {@link #getLastModified(Object)} it was
     * determined that the cached copy of the template is stale. Then, if it turns out that the
     * {@code encoding} parameter used doesn't match the actual template content (based on the
     * {@code #ftl encoding=...} header), this method will be called for a second time with the
     * correct {@code encoding} parameter value.
     *
     * @param templateSource an object representing a template source, obtained through a prior call
     *     to {@link #findTemplateSource(String)}. This must be an object on which {@link
     *     TemplateLoader#closeTemplateSource(Object)} wasn't applied yet.
     * @param encoding the character encoding used to translate source bytes to characters. Some
     *     loaders may not have access to the byte representation of the template stream, and
     *     instead directly obtain a character stream. These loaders should ignore the encoding
     *     parameter.
     * @return A {@link Reader} representing the template character stream. It's the responsibility
     *     of the caller (which is {@link freemarker.cache.TemplateCache} usually) to {@code
     *     close()} it. The {@link Reader} is not required to work after the {@code templateSource}
     *     was closed ({@link #closeTemplateSource(Object)}).
     */
    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
      try {
        if (templateSource instanceof String) {
          MailTemplate mailTemplate = mailService.getMailTemplate((String) templateSource);

          return new StringReader(new String(mailTemplate.getTemplate(), StandardCharsets.UTF_8));
        } else {
          throw new RuntimeException("Invalid template source (" + templateSource + ")");
        }
      } catch (Throwable e) {
        throw new IOException(
            "Failed to retrieve the character stream for the template (" + templateSource + ")", e);
      }
    }
  }
}
