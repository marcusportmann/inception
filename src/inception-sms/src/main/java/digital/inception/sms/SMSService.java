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

//~--- non-JDK imports --------------------------------------------------------

import com.mymobileapi.api5.API;
import com.mymobileapi.api5.APISoap;
import digital.inception.Debug;
import digital.inception.core.persistence.IDGenerator;
import digital.inception.core.util.ServiceUtil;
import digital.inception.core.util.StringUtil;
import digital.inception.core.xml.XmlParserErrorHandler;
import digital.inception.core.xml.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.sql.DataSource;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.BindingProvider;
import java.io.StringReader;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SMSService</code> class provides the SMS Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
public class SMSService
  implements ISMSService
{
  /**
   * The maximum SMS length.
   */
  private static final int MAXIMUM_SMS_LENGTH = 160;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SMSService.class);

  /* The name of the SMS Service instance. */
  private String instanceName = ServiceUtil.getServiceInstanceName("SMSService");

  /**
   * The Spring application context.
   */
  @Autowired
  private ApplicationContext applicationContext;

  /**
   * The data source used to provide connections to the application database.
   */
  @Autowired
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

  /**
   * The ID Generator.
   */
  @Autowired
  private IDGenerator idGenerator;

  /**
   * The MyMobileAPI endpoint.
   */
  @Value("${application.sms.myMobileAPIEndPoint:#{null}}")
  private String myMobileAPIEndPoint;

  /**
   * The MyMobileAPI username.
   */
  @Value("${application.sms.myMobileAPIUsername:#{null}}")
  private String myMobileAPIUsername;

  /**
   * The MyMobileAPI password.
   */
  @Value("${application.sms.myMobileAPIPassword:#{null}}")
  private String myMobileAPIPassword;

  /**
   * The maximum number of times sending will be attempted for a SMS.
   */
  @Value("${application.sms.maximumSendAttempts:#{100}}")
  private int maximumSendAttempts;

  /**
   *  The delay in milliseconds to wait before re-attempting to send a SMS.
   */
  @Value("${application.sms.sendRetryDelay:#{600000}}")
  private int sendRetryDelay;

  /**
   * Create the SMS.
   *
   * @param sms the <code>SMS</code> instance containing the information for the SMS
   */
  @Override
  public void createSMS(SMS sms)
    throws SMSServiceException
  {
    String createSMSSQL =
        "INSERT INTO sms.sms (id, mobile_number, message, status, send_attempts) "
        + "VALUES (?, ?, ?, ?, 0)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createSMSSQL))
    {
      long id = idGenerator.next("Application.SMSId");

      statement.setLong(1, id);
      statement.setString(2, sms.getMobileNumber());
      statement.setString(3, sms.getMessage());
      statement.setInt(4, sms.getStatus().getCode());

      if (statement.executeUpdate() != 1)
      {
        throw new SMSServiceException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)", createSMSSQL));
      }

      sms.setId(id);
    }
    catch (Throwable e)
    {
      throw new SMSServiceException("Failed to create the SMS", e);
    }
  }

  /**
   * Delete the existing SMS.
   *
   * @param smsId the ID uniquely identifying the SMS
   */
  @Override
  public void deleteSMS(long smsId)
    throws SMSNotFoundException, SMSServiceException
  {
    String deleteSMSSQL = "DELETE FROM sms.sms WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteSMSSQL))
    {
      statement.setLong(1, smsId);

      if (statement.executeUpdate() <= 0)
      {
        throw new SMSNotFoundException(smsId);
      }
    }
    catch (SMSNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SMSServiceException(String.format("Failed to delete the SMS (%d)", smsId), e);
    }
  }

  /**
   * Returns the maximum number of send attempts for a SMS.
   *
   * @return the maximum number of send attempts for a SMS
   */
  @Override
  public int getMaximumSendAttempts()
  {
    return maximumSendAttempts;
  }

  /**
   * Retrieve the next SMS that has been queued for sending.
   * <p/>
   * The SMS will be locked to prevent duplicate sending.
   *
   * @return the next SMS that has been queued for sending or <code>null</code> if no SMSs are
   *         currently queued for sending
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public SMS getNextSMSQueuedForSending()
    throws SMSServiceException
  {
    String getNextSMSQueuedForSendingSQL =
        "SELECT id, mobile_number, message, status, send_attempts, lock_name, last_processed FROM "
        + "sms.sms WHERE status=? AND (last_processed<? OR last_processed IS NULL) "
        + "FETCH FIRST 1 ROWS ONLY FOR UPDATE";

    String lockSMSSQL = "UPDATE sms.sms SET status=?, lock_name=? WHERE id=?";

    try
    {
      SMS sms = null;

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(getNextSMSQueuedForSendingSQL))
      {
        Timestamp processedBefore = new Timestamp(System.currentTimeMillis() - sendRetryDelay);

        statement.setInt(1, SMS.Status.QUEUED_FOR_SENDING.getCode());
        statement.setTimestamp(2, processedBefore);

        try (ResultSet rs = statement.executeQuery())
        {
          if (rs.next())
          {
            sms = getSMS(rs);

            sms.setStatus(SMS.Status.SENDING);
            sms.setLockName(instanceName);

            try (PreparedStatement updateStatement = connection.prepareStatement(lockSMSSQL))
            {
              updateStatement.setInt(1, SMS.Status.SENDING.getCode());
              updateStatement.setString(2, instanceName);
              updateStatement.setLong(3, sms.getId());

              if (updateStatement.executeUpdate() != 1)
              {
                throw new SMSServiceException(String.format(
                    "No rows were affected as a result of executing the SQL statement (%s)",
                    lockSMSSQL));
              }
            }
          }
        }
      }

      return sms;
    }
    catch (Throwable e)
    {
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
  public int getNumberOfSMSCreditsRemaining()
    throws SMSServiceException
  {
    try
    {
      APISoap myMobileAPIService = getMyMobileAPIService();

      String apiResultXml = myMobileAPIService.creditsSTR(myMobileAPIUsername, myMobileAPIPassword);

      Element apiResultElement = parseAPIResultXML(apiResultXml);

      Element dataElement = XmlUtil.getChildElement(apiResultElement, "data");

      if (dataElement == null)
      {
        throw new RuntimeException("Invalid API result XML: data element not found");
      }

      String credits = XmlUtil.getChildElementText(dataElement, "credits");

      return StringUtil.isNullOrEmpty(credits)
          ? 0
          : Integer.parseInt(credits);
    }
    catch (Throwable e)
    {
      throw new SMSServiceException("Failed to retrieve the number of SMS credits remaining", e);
    }
  }

  /**
   * Retrieve the SMS.
   *
   * @param smsId the ID uniquely identifying the SMS
   *
   * @return the SMS or <code>null</code> if the SMS could not be found
   */
  @Override
  public SMS getSMS(long smsId)
    throws SMSNotFoundException, SMSServiceException
  {
    String getSMSByIdSQL =
        "SELECT id, mobile_number, message, status, send_attempts, lock_name, last_processed "
        + "FROM sms.sms WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getSMSByIdSQL))
    {
      statement.setLong(1, smsId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return getSMS(rs);
        }
        else
        {
          throw new SMSNotFoundException(smsId);
        }
      }
    }
    catch (SMSNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SMSServiceException(String.format(
          "Failed to retrieve the SMS (%d) from the database", smsId), e);
    }
  }

  /**
   * Increment the send attempts for the SMS.
   *
   * @param sms the SMS whose send attempts should be incremented
   */
  @Override
  public void incrementSMSSendAttempts(SMS sms)
    throws SMSNotFoundException, SMSServiceException
  {
    String incrementSMSSendAttemptsSQL =
        "UPDATE sms.sms SET send_attempts=send_attempts + 1, last_processed=? WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(incrementSMSSendAttemptsSQL))
    {
      Timestamp currentTime = new Timestamp(System.currentTimeMillis());

      statement.setTimestamp(1, currentTime);
      statement.setLong(2, sms.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new SMSNotFoundException(sms.getId());
      }

      sms.setSendAttempts(sms.getSendAttempts() + 1);
      sms.setLastProcessed(currentTime);
    }
    catch (SMSNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SMSServiceException(String.format(
          "Failed to increment the send attempts for the SMS (%d) in the database", sms.getId()),
          e);
    }
  }

  /**
   * Reset the SMS locks.
   *
   * @param status    the current status of the SMSs that have been locked
   * @param newStatus the new status for the SMSs that have been unlocked
   */
  @Override
  public void resetSMSLocks(SMS.Status status, SMS.Status newStatus)
    throws SMSServiceException
  {
    String resetSMSLocksSQL =
        "UPDATE sms.sms SET status=?, lock_name=NULL WHERE lock_name=? AND status=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(resetSMSLocksSQL))
    {
      statement.setInt(1, newStatus.getCode());
      statement.setString(2, instanceName);
      statement.setInt(3, status.getCode());

      statement.executeUpdate();
    }
    catch (Throwable e)
    {
      throw new SMSServiceException(String.format(
          "Failed to reset the locks for the SMSs with the status (%s) that have been locked using "
          + "the lock name (%s)", status, instanceName), e);
    }
  }

  /**
   * Send the SMS.
   * <p/>
   * NOTE: This will queue the SMS for sending. The SMS will actually be sent asynchronously.
   *
   * @param mobileNumber the mobile number
   * @param message      the message
   */
  public void sendSMS(String mobileNumber, String message)
    throws SMSServiceException
  {
    try
    {
      SMS sms = new SMS(mobileNumber, message, SMS.Status.QUEUED_FOR_SENDING);

      createSMS(sms);

      applicationContext.getBean(BackgroundSMSSender.class).sendSMSs();
    }
    catch (Throwable e)
    {
      throw new SMSServiceException(String.format(
          "Failed to queue the SMS for the mobile number (%s) for sending", mobileNumber), e);
    }
  }

  /**
   * Send the SMS synchronously.
   * <p/>
   * NOTE: This will NOT queue the SMS for sending. The SMS will actually be sent synchronously.
   *
   * @param smsId        the ID of the SMS
   * @param mobileNumber the mobile number
   * @param message      the message
   *
   * @return <code>true</code> if the SMS was sent successfully or <code>false</code> otherwise
   */
  public boolean sendSMSSynchronously(long smsId, String mobileNumber, String message)
    throws SMSServiceException
  {
    try
    {
      if (StringUtil.isNullOrEmpty(message))
      {
        logger.info(String.format("Failed to send the empty SMS message to (%s)", mobileNumber));

        return true;
      }

      mobileNumber = formatMobileNumber(mobileNumber);

      if (message.length() > MAXIMUM_SMS_LENGTH)
      {
        message = message.substring(0, MAXIMUM_SMS_LENGTH);
      }

      if (logger.isDebugEnabled())
      {
        logger.debug(String.format("Attempting to send a SMS using the mobile number (%s)",
            mobileNumber));
      }

      if (Debug.inDebugMode())
      {
        logger.info(String.format(
            "Skipping sending of SMS (%s) to mobile number (%s) in DEBUG mode", message,
            mobileNumber));

        return true;
      }

      String sendXML = buildSendDataXml(smsId, mobileNumber, message);

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

      if (!apiResultElement.getNodeName().equals("api_result"))
      {
        throw new RuntimeException("Invalid API result XML: api_result element not found");
      }

      Element callResultElement = XmlUtil.getChildElement(apiResultElement, "call_result");

      if (callResultElement == null)
      {
        throw new RuntimeException("Invalid API result XML: call_result element not found");
      }

      Boolean result = XmlUtil.getChildElementBoolean(callResultElement, "result");

      if (result == null)
      {
        throw new RuntimeException("Invalid API result XML: result element not found");
      }

      if (!result)
      {
        String error = StringUtil.notNull(XmlUtil.getChildElementText(callResultElement, "error"));

        // If the SMS cannot be sent...
        if (error.equalsIgnoreCase("No data to send"))
        {
          return false;
        }

        throw new RuntimeException("The MyMobileAPI service returned an error: "
            + (StringUtil.isNullOrEmpty(error)
            ? "UNKNOWN"
            : error));
      }

      Element sendInfoElement = XmlUtil.getChildElement(apiResultElement, "send_info");

      if (sendInfoElement == null)
      {
        throw new RuntimeException("Invalid API result XML: send_info element not found");
      }

      String credits = XmlUtil.getChildElementText(sendInfoElement, "credits");

      int remainingCredits = StringUtil.isNullOrEmpty(credits)
          ? 0
          : Integer.parseInt(credits);

      if (remainingCredits < 100)
      {
        logger.warn(String.format("There are %d SMS credits remaining", remainingCredits));
      }

      if (logger.isDebugEnabled())
      {
        logger.debug(String.format("Successfully sent a SMS using the mobile number (%s)",
            mobileNumber));
      }

      return true;
    }
    catch (Throwable e)
    {
      throw new SMSServiceException(String.format(
          "Failed to send the SMS to the mobile number (%s)", mobileNumber), e);
    }
  }

  /**
   * Set the status for the SMS.
   *
   * @param smsId     the ID uniquely identifying the SMS
   * @param status the new status for the SMS
   */
  public void setSMSStatus(long smsId, SMS.Status status)
    throws SMSNotFoundException, SMSServiceException
  {
    String setSMSStatusSQL = "UPDATE sms.sms SET status=? WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(setSMSStatusSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setLong(2, smsId);

      if (statement.executeUpdate() != 1)
      {
        throw new SMSNotFoundException(smsId);
      }
    }
    catch (SMSNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SMSServiceException(String.format(
          "Failed to set the status for the SMS (%d) to (%s) in the database", smsId,
          status.toString()), e);
    }
  }

  /**
   * Unlock the SMS.
   *
   * @param smsId     the ID uniquely identifying the SMS
   * @param status the new status for the unlocked SMS
   */
  public void unlockSMS(long smsId, SMS.Status status)
    throws SMSNotFoundException, SMSServiceException
  {
    String unlockSMSSQL = "UPDATE sms.sms SET status=?, lock_name=NULL WHERE id=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(unlockSMSSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setLong(2, smsId);

      if (statement.executeUpdate() != 1)
      {
        throw new SMSNotFoundException(smsId);
      }
    }
    catch (SMSNotFoundException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new SMSServiceException(String.format(
          "Failed to unlock and set the status for the SMS (%d) to (%s) in the database", smsId,
          status.toString()), e);
    }
  }

  private String buildSendDataXml(long smsId, String mobileNumber, String message)
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    Date now = new Date();

    // buffer.append("<validityperiod>").append("48").append("</validityperiod>");

    return "<senddata>" + "<settings>" + "<live>True</live>"
        + "<return_credits>True</return_credits>" + "<default_date>" + dateFormat.format(now)
        + "</default_date>" + "<default_time>" + timeFormat.format(now) + "</default_time>"
        + "<default_curdate>" + dateFormat.format(now) + "</default_curdate>" + "<default_curtime>"
        + timeFormat.format(now) + "</default_curtime>" + "<mo_forwardemail>"
        + "sms-reply@mmp.guru" + "</mo_forwardemail>" + "</settings>" + "<entries>" + "<numto>"
        + mobileNumber + "</numto>" + "<customerid>" + smsId + "</customerid>" + "<data1>"
        + message + "</data1>" + "<type>" + "SMS" + "</type>" + "</entries>" + "</senddata>";
  }

  private String formatMobileNumber(String mobileNumber)
  {
    mobileNumber = StringUtil.notNull(mobileNumber);

    // Remove whitespace
    mobileNumber = mobileNumber.trim();
    mobileNumber = StringUtil.replace(mobileNumber, " ", "");
    mobileNumber = StringUtil.replace(mobileNumber, "\t", "");

    if (mobileNumber.length() > 30)
    {
      mobileNumber = mobileNumber.substring(0, 30);
    }

    if (mobileNumber.startsWith("0") && (mobileNumber.length() > 1))
    {
      mobileNumber = "27" + mobileNumber.substring(1);
    }

    if (!mobileNumber.startsWith("+"))
    {
      mobileNumber = "+" + mobileNumber;
    }

    return mobileNumber;
  }

  private APISoap getMyMobileAPIService()
  {
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

  private SMS getSMS(ResultSet rs)
    throws SQLException
  {
    return new SMS(rs.getLong(1), rs.getString(2), rs.getString(3), SMS.Status.fromCode(rs.getInt(
        4)), rs.getInt(5), rs.getString(6), rs.getTimestamp(7));
  }

  private Element parseAPIResultXML(String xml)
  {
    try
    {
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

      if (!apiResultElement.getNodeName().equals("api_result"))
      {
        throw new RuntimeException("Invalid API result XML: api_result element not found");
      }

      Element callResultElement = XmlUtil.getChildElement(apiResultElement, "call_result");

      if (callResultElement == null)
      {
        throw new RuntimeException("Invalid API result XML: call_result element not found");
      }

      Boolean result = XmlUtil.getChildElementBoolean(callResultElement, "result");

      if (result == null)
      {
        throw new RuntimeException("Invalid API result XML: result element not found");
      }

      if (!result)
      {
        String error = XmlUtil.getChildElementText(callResultElement, "error");

        throw new RuntimeException("The MyMobileAPI service returned an error: "
            + (StringUtil.isNullOrEmpty(error)
            ? "UNKNOWN"
            : error));
      }

      return apiResultElement;
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to parse the API result XML", e);
    }
  }
}
