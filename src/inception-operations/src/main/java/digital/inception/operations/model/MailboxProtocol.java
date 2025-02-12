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

package digital.inception.operations.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The <b>MailboxProtocol</b> enumeration defines the possible mailbox protocols.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The mailbox protocol")
@XmlEnum
@XmlType(name = "MailboxProtocol", namespace = "https://inception.digital/operations")
public enum MailboxProtocol {

  /** Microsoft 365 IMAPS. */
  @XmlEnumValue("Microsoft365IMAPS")
  MICROSOFT_365_IMAPS("microsoft_365_imaps", "Microsoft 365 IMAPS"),

  /** Standard IMAP. */
  @XmlEnumValue("StandardIMAP")
  STANDARD_IMAP("standard_imap", "Standard IMAP"),

  /** Standard IMAPS. */
  @XmlEnumValue("StandardIMAPS")
  STANDARD_IMAPS("standard_imaps", "Standard IMAPS");

  private final String code;

  private final String description;

  MailboxProtocol(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the mailbox protocol given by the specified code value.
   *
   * @param code the code for the mailbox protocol
   * @return the mailbox protocol given by the specified code value
   */
  @JsonCreator
  public static MailboxProtocol fromCode(String code) {
    for (MailboxProtocol value : MailboxProtocol.values()) {
      if (value.code.equalsIgnoreCase(code)) {
        return value;
      }
    }
    throw new RuntimeException(
        "Failed to determine the mailbox protocol with the invalid code (" + code + ")");
  }

  /**
   * Returns the code for the mailbox protocol.
   *
   * @return the code for the mailbox protocol
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the mailbox protocol.
   *
   * @return the description for the mailbox protocol
   */
  public String description() {
    return description;
  }
}
