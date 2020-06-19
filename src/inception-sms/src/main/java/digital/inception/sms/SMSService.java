/*
 * Copyright 2017 Marcus Portmann
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

package digital.inception.sms;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.Debug;
import digital.inception.core.util.ServiceUtil;
import digital.inception.core.xml.XmlParserErrorHandler;
import digital.inception.core.xml.XmlUtil;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SMSService</code> class provides the SMS Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class SMSService implements ISMSService {

  /** The maximum SMS length. */
  private static final int MAXIMUM_SMS_LENGTH = 160;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SMSService.class);

  /** The Spring application context. */
  private ApplicationContext applicationContext;

  /* Entity Manager */
  @PersistenceContext(unitName = "applicationPersistenceUnit")
  private EntityManager entityManager;

  /* The name of the SMS Service instance. */
  private String instanceName = ServiceUtil.getServiceInstanceName("SMSService");

  /** The maximum number of times sending will be attempted for a SMS. */
  @Value("${application.sms.maximumSendAttempts:100}")
  private int maximumSendAttempts;

  /** The MyMobileAPI endpoint. */
  @Value("${application.sms.myMobileAPIEndPoint:#{null}}")
  private String myMobileAPIEndPoint;

  /** The MyMobileAPI password. */
  @Value("${application.sms.myMobileAPIPassword:#{null}}")
  private String myMobileAPIPassword;

  /** The MyMobileAPI username. */
  @Value("${application.sms.myMobileAPIUsername:#{null}}")
  private String myMobileAPIUsername;

  /** The delay in milliseconds to wait before re-attempting to send a SMS. */
  @Value("${application.sms.sendRetryDelay:600000}")
  private int sendRetryDelay;

  /** The SMS Repository. */
  private SMSRepository smsRepository;

  /**
   * Constructs a new <code>SMSService</code>.
   *
   * @param applicationContext the Spring application context
   */
  public SMSService(ApplicationContext applicationContext, SMSRepository smsRepository) {
    this.applicationContext = applicationContext;
    this.smsRepository = smsRepository;
  }

  /**
   * Create the SMS.
   *
   * @param sms the <code>SMS</code> instance containing the information for the SMS
   */
  @Override
  @Transactional
  public void createSMS(SMS sms) throws SMSServiceException {
    try {
      if (sms.getId() == null) {
        sms.setId(UUID.randomUUID());
      }

      smsRepository.saveAndFlush(sms);
    } catch (Throwable e) {
      throw new SMSServiceException("Failed to create the SMS", e);
    }
  }

  /**
   * Delete the existing SMS.
   *
   * @param smsId the ID uniquely identifying the SMS
   */
  @Override
  @Transactional
  public void deleteSMS(UUID smsId) throws SMSNotFoundException, SMSServiceException {
    try {
      if (!smsRepository.existsById(smsId)) {
        throw new SMSNotFoundException(smsId);
      }

      smsRepository.deleteById(smsId);
    } catch (SMSNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SMSServiceException("Failed to delete the SMS (" + smsId + ")", e);
    }
  }

  /**
   * Returns the maximum number of send attempts for a SMS.
   *
   * @return the maximum number of send attempts for a SMS
   */
  @Override
  public int getMaximumSendAttempts() {
    return maximumSendAttempts;
  }

  /**
   * Retrieve the next SMS that has been queued for sending.
   *
   * <p>The SMS will be locked to prevent duplicate sending.
   *
   * @return the next SMS that has been queued for sending or <code>null</code> if no SMSs are
   *     currently queued for sending
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public SMS getNextSMSQueuedForSending() throws SMSServiceException {
    try {
      LocalDateTime lastProcessedBefore = LocalDateTime.now();

      lastProcessedBefore = lastProcessedBefore.minus(sendRetryDelay, ChronoUnit.MILLIS);

      PageRequest pageRequest = PageRequest.of(0, 1);

      List<SMS> smss =
          smsRepository.findSMSsScheduledForExecutionForWrite(lastProcessedBefore, pageRequest);

      if (smss.size() > 0) {
        SMS sms = smss.get(0);

        LocalDateTime when = LocalDateTime.now();

        smsRepository.lockSMSForSending(sms.getId(), instanceName, when);

        entityManager.detach(sms);

        sms.setStatus(SMSStatus.SENDING);
        sms.setLockName(instanceName);
        sms.incrementSendAttempts();
        sms.setLastProcessed(when);

        return sms;
      } else {
        return null;
      }

    } catch (Throwable e) {
      throw new SMSServiceException(
          "Failed to retrieve the next SMS that has been queued for sending from the database", e);
    }
  }

  /**
   * Returns the number of SMS credits remaining.
   *
   * @return the number of SMS credits remaining
   */
  @Override
  public int getNumberOfSMSCreditsRemaining() throws SMSServiceException {

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
    //      throw new SMSServiceException("Failed to retrieve the number of SMS credits remaining",
    // e);
    //    }
  }

  /**
   * Retrieve the SMS.
   *
   * @param smsId the ID uniquely identifying the SMS
   * @return the SMS or <code>null</code> if the SMS could not be found
   */
  @Override
  public SMS getSMS(UUID smsId) throws SMSNotFoundException, SMSServiceException {
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
      throw new SMSServiceException(
          "Failed to retrieve the SMS (" + smsId + ") from the database", e);
    }
  }

  /**
   * Reset the SMS locks.
   *
   * @param status the current status of the SMSs that have been locked
   * @param newStatus the new status for the SMSs that have been unlocked
   */
  @Override
  @Transactional
  public void resetSMSLocks(SMSStatus status, SMSStatus newStatus) throws SMSServiceException {
    try {
      smsRepository.resetSMSLocks(status, newStatus, instanceName);
    } catch (Throwable e) {
      throw new SMSServiceException(
          "Failed to reset the locks for the SMSs with the status ("
              + status
              + ") that have been locked using the lock name ("
              + instanceName
              + ")",
          e);
    }
  }

  /**
   * Send the SMS.
   *
   * <p>NOTE: This will queue the SMS for sending. The SMS will actually be sent asynchronously.
   *
   * @param mobileNumber the mobile number
   * @param message the message
   */
  public void sendSMS(String mobileNumber, String message) throws SMSServiceException {
    try {
      SMS sms = new SMS(mobileNumber, message, SMSStatus.QUEUED_FOR_SENDING);

      createSMS(sms);

      applicationContext.getBean(BackgroundSMSSender.class).sendSMSs();
    } catch (Throwable e) {
      throw new SMSServiceException(
          "Failed to queue the SMS for the mobile number (" + mobileNumber + ") for sending", e);
    }
  }

  /**
   * Send the SMS synchronously.
   *
   * <p>NOTE: This will NOT queue the SMS for sending. The SMS will actually be sent synchronously.
   *
   * @param smsId the ID of the SMS
   * @param mobileNumber the mobile number
   * @param message the message
   * @return <code>true</code> if the SMS was sent successfully or <code>false</code> otherwise
   */
  public boolean sendSMSSynchronously(UUID smsId, String mobileNumber, String message)
      throws SMSServiceException {
    try {
      if (StringUtils.isEmpty(message)) {
        logger.info("Failed to send the empty SMS message to (" + mobileNumber + ")");

        return true;
      }

      mobileNumber = formatMobileNumber(mobileNumber);

      if (message.length() > MAXIMUM_SMS_LENGTH) {
        message = message.substring(0, MAXIMUM_SMS_LENGTH);
      }

      if (logger.isDebugEnabled()) {
        logger.debug("Attempting to send a SMS using the mobile number (" + mobileNumber + ")");
      }

      if (Debug.inDebugMode()) {
        logger.info(
            "Skipping sending of SMS ("
                + message
                + ") to mobile number ("
                + mobileNumber
                + ") in DEBUG mode");

        return true;
      }

      String sendXML = buildSendDataXml(smsId, mobileNumber, message);

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
      throw new SMSServiceException(
          "Failed to send the SMS to the mobile number (" + mobileNumber + ")", e);
    }
  }

  /**
   * Set the status for the SMS.
   *
   * @param smsId the ID uniquely identifying the SMS
   * @param status the new status for the SMS
   */
  @Override
  @Transactional
  public void setSMSStatus(UUID smsId, SMSStatus status)
      throws SMSNotFoundException, SMSServiceException {
    try {
      if (!smsRepository.existsById(smsId)) {
        throw new SMSNotFoundException(smsId);
      }

      smsRepository.setSMSStatus(smsId, status);
    } catch (SMSNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SMSServiceException(
          "Failed to set the status for the SMS ("
              + smsId
              + ") to ("
              + status
              + ") in the database",
          e);
    }
  }

  /**
   * Unlock the SMS.
   *
   * @param smsId the ID uniquely identifying the SMS
   * @param status the new status for the unlocked SMS
   */
  @Override
  @Transactional
  public void unlockSMS(UUID smsId, SMSStatus status)
      throws SMSNotFoundException, SMSServiceException {
    try {
      if (!smsRepository.existsById(smsId)) {
        throw new SMSNotFoundException(smsId);
      }

      smsRepository.unlockSMS(smsId, status);
    } catch (SMSNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new SMSServiceException(
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
    if (StringUtils.isEmpty(mobileNumber)) {
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

        throw new RuntimeException(
            "The MyMobileAPI service returned an error: "
                + (StringUtils.isEmpty(error) ? "UNKNOWN" : error));
      }

      return apiResultElement;
    } catch (Throwable e) {
      throw new RuntimeException("Failed to parse the API result XML", e);
    }
  }
}
