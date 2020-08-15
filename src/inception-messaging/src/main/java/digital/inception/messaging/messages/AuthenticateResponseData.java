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

package digital.inception.messaging.messages;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.wbxml.Document;
import digital.inception.core.wbxml.Element;
import digital.inception.core.wbxml.Encoder;
import digital.inception.messaging.MessagePriority;
import digital.inception.messaging.MessagingServiceException;
import digital.inception.messaging.WbxmlMessageData;
import digital.inception.security.Organization;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>AuthenticateResponseData</code> class manages the data for a "Authenticate Response"
 * message.
 *
 * <p>This is a synchronous message.
 *
 * @author Marcus Portmann
 */
public class AuthenticateResponseData extends WbxmlMessageData {

  /** The error code returned when an unknown error occurs during authentication. */
  public static final int ERROR_CODE_UNKNOWN_ERROR = -1;

  /** The UUID for the "Authenticate Response" message. */
  public static final UUID MESSAGE_TYPE_ID =
      UUID.fromString("82223035-1726-407f-8703-3977708e792c");

  /** The error code returned when authentication is successful. */
  private static final int ERROR_CODE_SUCCESS = 0;

  /** The message returned when authentication was successful. */
  private static final String ERROR_MESSAGE_SUCCESS = "Success";

  /**
   * The error code indicating the result of processing the authentication where a code of '0'
   * indicates success and a non-zero code indicates an error condition.
   */
  private int errorCode;

  /** The error message describing the result of processing the authentication. */
  private String errorMessage;

  /** The organizations the authenticated user is associated with. */
  private List<OrganizationData> organizations;

  /**
   * The encryption key used to encrypt data on the user's device and any data passed as part of a
   * message.
   */
  private byte[] userEncryptionKey;

  /** The properties returned for the authenticated user. */
  private Map<String, Object> userProperties;

  /** Constructs a new <code>AuthenticateResponseData</code>. */
  public AuthenticateResponseData() {
    super(MESSAGE_TYPE_ID, MessagePriority.HIGH);
  }

  /**
   * Constructs a new <code>AuthenticateResponseData</code>.
   *
   * @param errorCode the error code
   * @param errorMessage the error message
   */
  public AuthenticateResponseData(int errorCode, String errorMessage) {
    super(MESSAGE_TYPE_ID, MessagePriority.HIGH);

    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.organizations = new ArrayList<>();
    this.userEncryptionKey = new byte[0];
    this.userProperties = new HashMap<>();
  }

  /**
   * Constructs a new <code>AuthenticateResponseData</code>.
   *
   * @param organizations the organizations the authenticated user is associated with
   * @param userEncryptionKey the encryption key used to encrypt data on the user's device and any
   *     data passed as part of a message
   * @param userProperties the properties returned for the authenticated user
   */
  public AuthenticateResponseData(
      List<Organization> organizations,
      byte[] userEncryptionKey,
      Map<String, Object> userProperties) {
    super(MESSAGE_TYPE_ID, MessagePriority.HIGH);

    this.errorCode = ERROR_CODE_SUCCESS;
    this.errorMessage = ERROR_MESSAGE_SUCCESS;
    this.userEncryptionKey = userEncryptionKey;
    this.userProperties = userProperties;

    this.organizations = new ArrayList<>();

    this.organizations.addAll(
        organizations.stream().map(OrganizationData::new).collect(Collectors.toList()));
  }

  /**
   * Extract the message data from the WBXML data for a message.
   *
   * @param messageData the WBXML data for the message
   * @return <code>true</code> if the message data was extracted successfully from the WBXML data or
   *     <code>false</code> otherwise
   */
  @Override
  public boolean fromMessageData(byte[] messageData) throws MessagingServiceException {
    Document document = parseWBXML(messageData);

    Element rootElement = document.getRootElement();

    if (!rootElement.getName().equals("AuthenticateResponse")) {
      return false;
    }

    if ((!rootElement.hasChild("UserEncryptionKey"))
        || (!rootElement.hasChild("ErrorCode"))
        || (!rootElement.hasChild("ErrorMessage"))) {
      return false;
    }

    try {
      this.errorCode = Integer.parseInt(rootElement.getChildText("ErrorCode"));
    } catch (Throwable e) {
      return false;
    }

    this.errorMessage = rootElement.getChildText("ErrorMessage");

    this.organizations = new ArrayList<>();

    if (rootElement.hasChild("Organizations")) {
      Element organizationsElement = rootElement.getChild("Organizations");

      List<Element> organizationElements = organizationsElement.getChildren("Organization");

      this.organizations.addAll(
          organizationElements.stream().map(OrganizationData::new).collect(Collectors.toList()));
    }

    this.userEncryptionKey = rootElement.getChildOpaque("UserEncryptionKey");

    this.userProperties = new HashMap<>();

    if (rootElement.hasChild("UserProperties")) {
      Element userPropertiesElement = rootElement.getChild("UserProperties");

      List<Element> userPropertyElements = userPropertiesElement.getChildren("UserProperty");

      for (Element userPropertyElement : userPropertyElements) {
        try {
          String userPropertyName = userPropertyElement.getAttributeValue("name");
          int userPropertyType = Integer.parseInt(userPropertyElement.getAttributeValue("type"));

          if (userPropertyType == 0) {
            this.userProperties.put(userPropertyName, userPropertyElement.getText());
          } else {
            throw new MessagingServiceException(
                "Failed to read the user property ("
                    + userPropertyName
                    + ") with unknown type ("
                    + userPropertyType
                    + ") from the message data");
          }
        } catch (Throwable e) {
          throw new MessagingServiceException(
              "Failed to read the user property from the message data", e);
        }
      }
    }

    return true;
  }

  /**
   * Returns the error code indicating the result of processing the registration where a code of '0'
   * indicates success and a non-zero code indicates an error condition.
   *
   * @return the error code indicating the result of processing the registration where a code of '0'
   *     indicates success and a non-zero code indicates an error condition
   */
  public int getErrorCode() {
    return errorCode;
  }

  /**
   * Returns the error message describing the result of processing the registration.
   *
   * @return the error message describing the result of processing the registration
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * Returns the organizations the authenticated user is associated with.
   *
   * @return the organizations the authenticated user is associated with
   */
  public List<OrganizationData> getOrganizations() {
    return organizations;
  }

  /**
   * Returns the encryption key used to encrypt data on the user's device and any data passed as
   * part of a message.
   *
   * @return the encryption key used to encrypt data on the user's device and data passed as part of
   *     a message
   */
  public byte[] getUserEncryptionKey() {
    return userEncryptionKey;
  }

  /**
   * Returns the properties returned for the authenticated user.
   *
   * @return the properties returned for the authenticated user
   */
  public Map<String, Object> getUserProperties() {
    return userProperties;
  }

  /**
   * Returns the WBXML data representation of the message data that will be sent as part of a
   * message.
   *
   * @return the WBXML data representation of the message data that will be sent as part of a
   *     message
   */
  @Override
  public byte[] toMessageData() {
    Element rootElement = new Element("AuthenticateResponse");

    rootElement.addContent(new Element("ErrorCode", String.valueOf(errorCode)));
    rootElement.addContent(
        new Element("ErrorMessage", StringUtils.isEmpty(errorMessage) ? "" : errorMessage));
    rootElement.addContent(new Element("UserEncryptionKey", userEncryptionKey));

    if ((organizations != null) && (organizations.size() > 0)) {
      Element organizationsElement = new Element("Organizations");

      for (OrganizationData organization : organizations) {
        organizationsElement.addContent(organization.toElement());
      }

      rootElement.addContent(organizationsElement);
    }

    if ((userProperties != null) && (userProperties.size() > 0)) {
      Element userPropertiesElement = new Element("UserProperties");

      for (String userPropertyName : userProperties.keySet()) {
        Object userPropertyValue = userProperties.get(userPropertyName);

        if (userPropertyValue instanceof String) {
          Element userPropertyElement = new Element("UserProperty");

          userPropertyElement.setAttribute("name", userPropertyName);
          userPropertyElement.setAttribute("type", "0");
          userPropertyElement.setText((String) userPropertyValue);
          userPropertiesElement.addContent(userPropertyElement);
        }
      }

      rootElement.addContent(userPropertiesElement);
    }

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }
}
