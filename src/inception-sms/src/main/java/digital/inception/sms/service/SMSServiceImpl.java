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

package digital.inception.sms.service;

import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.service.ValidationError;
import digital.inception.core.util.ServiceUtil;
import digital.inception.core.xml.XmlParserErrorHandler;
import digital.inception.core.xml.XmlUtil;
import digital.inception.sms.model.SMS;
import digital.inception.sms.model.SMSNotFoundException;
import digital.inception.sms.model.SMSStatus;
import digital.inception.sms.persistence.SMSRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.io.StringReader;
import java.net.http.HttpClient;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 * The <b>SMSServiceImpl</b> class provides the SMS Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class SMSServiceImpl implements SMSService {

  /** The maximum SMS length. */
  private static final int MAXIMUM_SMS_LENGTH = 160;

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(SMSServiceImpl.class);

  /** The SMS Portal provider. */
  private final String PROVIDER_SMS_PORTAL = "sms-portal";

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /* The name of the SMS Service instance. */
  private final String instanceName = ServiceUtil.getServiceInstanceName("SMSService");

  /** The SMS Repository. */
  private final SMSRepository smsRepository;

  /** The JSR-303 validator. */
  private final Validator validator;

  /** The web client builder. */
  private final WebClient.Builder webClientBuilder;

  /* Entity Manager */
  @PersistenceContext(unitName = "sms")
  private EntityManager entityManager;

  /** The HTTP Client. */
  private HttpClient httpClient;

  /** The maximum number of times sending will be attempted for a SMS. */
  @Value("${inception.sms.maximum-send-attempts:100}")
  private int maximumSendAttempts;

  /** The delay in milliseconds to wait before re-attempting to send a SMS. */
  @Value("${inception.sms.send-retry-delay:600000}")
  private int sendRetryDelay;

  /** The SMS Portal API endpoint. */
  @Value("${inception.sms.providers.sms-portal.api-endpoint:#{null}}")
  private String smsPortalAPIEndPoint;

  /** The SMS Portal client ID. */
  @Value("${inception.sms.providers.sms-portal.client-id:#{null}}")
  private String smsPortalClientId;

  /** The SMS Portal client secret. */
  @Value("${inception.sms.providers.sms-portal.client-secret:#{null}}")
  private String smsPortalClientSecret;

  /** The SMS provider to use. */
  @Value("${inception.sms.use-provider:#{null}}")
  private String useProvider;

  /**
   * Constructs a new <b>SMSServiceImpl</b>.
   *
   * @param applicationContext the Spring application context
   * @param validator the JSR-303 validator
   * @param webClientBuilder the web client builder
   * @param smsRepository the SMS persistence
   */
  public SMSServiceImpl(
      ApplicationContext applicationContext,
      Validator validator,
      WebClient.Builder webClientBuilder,
      SMSRepository smsRepository) {
    this.applicationContext = applicationContext;
    this.validator = validator;
    this.webClientBuilder = webClientBuilder;
    this.smsRepository = smsRepository;
  }

  @Override
  public void createSMS(SMS sms) throws InvalidArgumentException, ServiceUnavailableException {
    validateSMS(sms);

    try {
      if (sms.getId() == null) {
        sms.setId(UuidCreator.getTimeOrderedEpoch());
      }

      smsRepository.saveAndFlush(sms);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to create the SMS", e);
    }
  }

  @Override
  public void deleteSMS(UUID smsId)
      throws InvalidArgumentException, SMSNotFoundException, ServiceUnavailableException {
    if (smsId == null) {
      throw new InvalidArgumentException("smsId");
    }

    try {
      if (!smsRepository.existsById(smsId)) {
        throw new SMSNotFoundException(smsId);
      }

      smsRepository.deleteById(smsId);
    } catch (SMSNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to delete the SMS (" + smsId + ")", e);
    }
  }

  @Override
  public int getMaximumSendAttempts() {
    return maximumSendAttempts;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Optional<SMS> getNextSMSQueuedForSending() throws ServiceUnavailableException {
    try {
      OffsetDateTime lastProcessedBefore = OffsetDateTime.now();

      lastProcessedBefore = lastProcessedBefore.minus(sendRetryDelay, ChronoUnit.MILLIS);

      PageRequest pageRequest = PageRequest.of(0, 1);

      List<SMS> smss =
          smsRepository.findSMSsQueuedForSendingForWrite(lastProcessedBefore, pageRequest);

      if (!smss.isEmpty()) {
        SMS sms = smss.getFirst();

        OffsetDateTime when = OffsetDateTime.now();

        smsRepository.lockSMSForSending(sms.getId(), instanceName, when);

        entityManager.detach(sms);

        sms.setStatus(SMSStatus.SENDING);
        sms.setLockName(instanceName);
        sms.incrementSendAttempts();
        sms.setLastProcessed(when);

        return Optional.of(sms);
      } else {
        return Optional.empty();
      }

    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the next SMS that has been queued for sending from the database", e);
    }
  }

  @Override
  public int getNumberOfSMSCreditsRemaining() throws ServiceUnavailableException {
    if (PROVIDER_SMS_PORTAL.equalsIgnoreCase(useProvider)) {}

    return 0;

    //    try {
    //      APISoap myMobileAPIService = getMyMobileAPIService();
    //
    //      String apiResultXml = myMobileAPIService.creditsSTR(myMobileAPIUsername,
    // myMobileAPIPassword);
    //
    //      Element apiResultElement = parseAPIResultXML(apiResultXml);
    //
    //      Element dataElement = XmlUtil.getChildElement(apiResultElement, "data");
    //
    //      if (dataElement == null) {
    //        throw new RuntimeException("Invalid API result XML: data element not found");
    //      }
    //
    //      String credits = XmlUtil.getChildElementText(dataElement, "credits");
    //
    //      return StringUtils.isEmpty(credits)
    //          ? 0
    //          : Integer.parseInt(credits);
    //    } catch (Throwable e) {
    //      throw new ServiceUnavailableException("Failed to retrieve the number of SMS credits
    // remaining",
    // e);
    //    }
  }

  @Override
  public SMS getSMS(UUID smsId)
      throws InvalidArgumentException, SMSNotFoundException, ServiceUnavailableException {
    if (smsId == null) {
      throw new InvalidArgumentException("smsId");
    }

    try {
      Optional<SMS> smsOptional = smsRepository.findById(smsId);

      if (smsOptional.isPresent()) {
        return smsOptional.get();
      } else {
        throw new SMSNotFoundException(smsId);
      }
    } catch (SMSNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the SMS (" + smsId + ") from the database", e);
    }
  }

  @Override
  public void resetSMSLocks(SMSStatus status, SMSStatus newStatus)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (status == null) {
      throw new InvalidArgumentException("status");
    }

    if (newStatus == null) {
      throw new InvalidArgumentException("newStatus");
    }

    try {
      smsRepository.resetSMSLocks(status, newStatus, instanceName);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to reset the locks for the SMSs with the status ("
              + status
              + ") that have been locked using the lock name ("
              + instanceName
              + ")",
          e);
    }
  }

  @Override
  public void sendSMS(String mobileNumber, String message)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(mobileNumber)) {
      throw new InvalidArgumentException("mobileNumber");
    }

    if (!StringUtils.hasText(message)) {
      throw new InvalidArgumentException("message");
    }

    try {
      SMS sms = new SMS(mobileNumber, message, SMSStatus.QUEUED);

      createSMS(sms);

      applicationContext.getBean(BackgroundSMSSender.class).sendSMSs();
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to queue the SMS for the mobile number (" + mobileNumber + ") for sending", e);
    }
  }

  @Override
  public boolean sendSMSSynchronously(UUID smsId, String mobileNumber, String message)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (smsId == null) {
      throw new InvalidArgumentException("smsId");
    }

    if (!StringUtils.hasText(mobileNumber)) {
      throw new InvalidArgumentException("mobileNumber");
    }

    if (!StringUtils.hasText(message)) {
      throw new InvalidArgumentException("message");
    }

    try {
      if (!StringUtils.hasText(message)) {
        log.info("Failed to send the empty SMS message to (" + mobileNumber + ")");

        return true;
      }

      mobileNumber = formatMobileNumber(mobileNumber);

      if (message.length() > MAXIMUM_SMS_LENGTH) {
        message = message.substring(0, MAXIMUM_SMS_LENGTH);
      }

      if (log.isDebugEnabled()) {
        log.debug("Attempting to send a SMS using the mobile number (" + mobileNumber + ")");
      }

      //      if (Debug.inDebugMode()) {
      //        logger.info(
      //            "Skipping sending of SMS ("
      //                + message
      //                + ") to mobile number ("
      //                + mobileNumber
      //                + ") in DEBUG mode");
      //
      //        return true;
      //      }

      if (PROVIDER_SMS_PORTAL.equalsIgnoreCase(useProvider)) {
        String smsPortalToken = getSMSPortalToken();

        String sendXML = buildSendDataXml(smsId, mobileNumber, message);
      }

      /*
      APISoap myMobileAPIService = getMyMobileAPIService();

      String apiResultXml = myMobileAPIService.sendSTRSTR(myMobileAPIUsername, myMobileAPIPassword,
          sendXML);

      // Retrieve a document builder instance using the factory
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

      builderFactory.setValidating(false);
      builderFactory.setNamespaceAware(false);

      // Create the document builder
      DocumentBuilder builder = builderFactory.newDocumentBuilder();

      builder.setErrorHandler(new XmlParserErrorHandler());

      // Parse the XML
      InputSource inputSource = new InputSource(new StringReader(apiResultXml));
      Document document = builder.parse(inputSource);
      Element apiResultElement = document.getDocumentElement();

      if (!apiResultElement.getNodeName().equals("api_result")) {
        throw new RuntimeException("Invalid API result XML: api_result element not found");
      }

      Element callResultElement = XmlUtil.getChildElement(apiResultElement, "call_result");

      if (callResultElement == null) {
        throw new RuntimeException("Invalid API result XML: call_result element not found");
      }

      Boolean result = XmlUtil.getChildElementBoolean(callResultElement, "result");

      if (result == null) {
        throw new RuntimeException("Invalid API result XML: result element not found");
      }

      if (!result) {
        String error = XmlUtil.getChildElementText(callResultElement, "error");

        if (StringUtils.isEmpty(error)) {
          error = "UNKNOWN";
        }

        // If the SMS cannot be sent...
        if (error.equalsIgnoreCase("No data to send")) {
          return false;
        }

        throw new RuntimeException("The MyMobileAPI service returned an error: " + error);
      }

      Element sendInfoElement = XmlUtil.getChildElement(apiResultElement, "send_info");

      if (sendInfoElement == null) {
        throw new RuntimeException("Invalid API result XML: send_info element not found");
      }

      String credits = XmlUtil.getChildElementText(sendInfoElement, "credits");

      int remainingCredits = StringUtils.isEmpty(credits)
          ? 0
          : Integer.parseInt(credits);

      if (remainingCredits < 100) {
        logger.warn("There are " + remainingCredits + " SMS credits remaining");
      }

      if (logger.isDebugEnabled()) {
        logger.debug("Successfully sent a SMS using the mobile number (" + mobileNumber + ")");
      }

      */
      return true;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to send the SMS to the mobile number (" + mobileNumber + ")", e);
    }
  }

  @Override
  public void setSMSStatus(UUID smsId, SMSStatus status)
      throws InvalidArgumentException, SMSNotFoundException, ServiceUnavailableException {
    if (smsId == null) {
      throw new InvalidArgumentException("smsId");
    }

    try {
      if (!smsRepository.existsById(smsId)) {
        throw new SMSNotFoundException(smsId);
      }

      smsRepository.setSMSStatus(smsId, status);
    } catch (SMSNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to set the status for the SMS ("
              + smsId
              + ") to ("
              + status
              + ") in the database",
          e);
    }
  }

  @Override
  public void unlockSMS(UUID smsId, SMSStatus status)
      throws InvalidArgumentException, SMSNotFoundException, ServiceUnavailableException {
    if (smsId == null) {
      throw new InvalidArgumentException("smsId");
    }

    if (status == null) {
      throw new InvalidArgumentException("status");
    }

    try {
      if (!smsRepository.existsById(smsId)) {
        throw new SMSNotFoundException(smsId);
      }

      smsRepository.unlockSMS(smsId, status);
    } catch (SMSNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to unlock and set the status for the SMS ("
              + smsId
              + ") to ("
              + status
              + ") in the database",
          e);
    }
  }

  private String buildSendDataXml(UUID smsId, String mobileNumber, String message) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    Date now = new Date();

    // buffer.append("<validityperiod>").append("48").append("</validityperiod>");

    return "<senddata><settings><live>True</live>"
        + "<return_credits>True</return_credits><default_date>"
        + dateFormat.format(now)
        + "</default_date><default_time>"
        + timeFormat.format(now)
        + "</default_time>"
        + "<default_curdate>"
        + dateFormat.format(now)
        + "</default_curdate><default_curtime>"
        + timeFormat.format(now)
        + "</default_curtime><mo_forwardemail>"
        + "sms-reply@mmp.guru</mo_forwardemail>"
        + "</settings>"
        + "<entries>"
        + "<numto>"
        + mobileNumber
        + "</numto><customerid>"
        + smsId
        + "</customerid>"
        + "<data1>"
        + message
        + "</data1><type>"
        + "SMS"
        + "</type>"
        + "</entries>"
        + "</senddata>";
  }

  private String formatMobileNumber(String mobileNumber) {
    if (!StringUtils.hasText(mobileNumber)) {
      return "";
    }

    // Remove whitespace
    mobileNumber = mobileNumber.trim();
    mobileNumber = StringUtils.replace(mobileNumber, " ", "");
    mobileNumber = StringUtils.replace(mobileNumber, "\t", "");

    if (mobileNumber.length() > 30) {
      mobileNumber = mobileNumber.substring(0, 30);
    }

    if (mobileNumber.startsWith("0") && (mobileNumber.length() > 1)) {
      mobileNumber = "27" + mobileNumber.substring(1);
    }

    if (!mobileNumber.startsWith("+")) {
      mobileNumber = "+" + mobileNumber;
    }

    return mobileNumber;
  }

  private String getSMSPortalToken() throws ServiceUnavailableException {
    try {
      // WebClient webClient = WebClient.builder()

      return "";

    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the SMS Portal token", e);
    }
  }

  /*
  private APISoap getMyMobileAPIService() {
    // Retrieve the proxy for the MyMobileAPI service
    URL wsdlLocation = Thread.currentThread().getContextClassLoader().getResource(
        "META-INF/wsdl/MyMobileAPI.wsdl");

    API api = new API(wsdlLocation, new QName("http://www.mymobileapi.com/api5", "API"));

    APISoap apiSoap = api.getAPISoap();

    BindingProvider bindingProvider = ((BindingProvider) apiSoap);

    // Set the endpoint for the service
    bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
        myMobileAPIEndPoint);

    return apiSoap;
  }
  */

  private Element parseAPIResultXML(String xml) {
    try {
      // Retrieve a document builder instance using the factory
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

      builderFactory.setValidating(false);
      builderFactory.setNamespaceAware(false);

      // Create the document builder
      DocumentBuilder builder = builderFactory.newDocumentBuilder();

      builder.setErrorHandler(new XmlParserErrorHandler());

      // Parse the XML
      InputSource inputSource = new InputSource(new StringReader(xml));
      Document document = builder.parse(inputSource);
      Element apiResultElement = document.getDocumentElement();

      if (!apiResultElement.getNodeName().equals("api_result")) {
        throw new RuntimeException("Invalid API result XML: api_result element not found");
      }

      Optional<Element> callResultElementOptional =
          XmlUtil.getChildElement(apiResultElement, "call_result");

      if (callResultElementOptional.isEmpty()) {
        throw new RuntimeException("Invalid API result XML: call_result element not found");
      }

      Optional<Boolean> resultOptional =
          XmlUtil.getChildElementBoolean(callResultElementOptional.get(), "result");

      if (resultOptional.isEmpty()) {
        throw new RuntimeException("Invalid API result XML: result element not found");
      }

      if (!resultOptional.get()) {
        Optional<String> errorOptional =
            XmlUtil.getChildElementText(callResultElementOptional.get(), "error");

        throw new RuntimeException(
            "The MyMobileAPI service returned an error: " + (errorOptional.orElse("UNKNOWN")));
      }

      return apiResultElement;
    } catch (Throwable e) {
      throw new RuntimeException("Failed to parse the API result XML", e);
    }
  }

  private void validateSMS(SMS sms) throws InvalidArgumentException {
    if (sms == null) {
      throw new InvalidArgumentException("sms");
    }

    Set<ConstraintViolation<SMS>> constraintViolations = validator.validate(sms);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "sms", ValidationError.toValidationErrors(constraintViolations));
    }
  }
}
