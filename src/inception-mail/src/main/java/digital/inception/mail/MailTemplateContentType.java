/*
 * Copyright 2017 Discovery Bank
 * All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package digital.inception.mail;

// ~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>MailTemplateContentType</code> enumeration defines the possible content types for mail
 * templates.
 *
 * @author Marcus Portmann
 */
@Schema(description = "MailTemplateContentType")
@XmlEnum
@XmlType(name = "MailTemplateContentType", namespace = "http://mail.inception.digital")
@SuppressWarnings("unused")
public enum MailTemplateContentType {
  @XmlEnumValue("Unknown")
  UNKNOWN(0, "Unknown"),
  @XmlEnumValue("Text")
  TEXT(1, "Text"),
  @XmlEnumValue("HTML")
  HTML(2, "HTML");

  private int code;
  private String description;

  MailTemplateContentType(int code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the mail template content type given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the mail template content type
   * @return the mail template content type given by the specified numeric code value
   */
  @JsonCreator
  public static MailTemplateContentType fromCode(int code) {
    switch (code) {
      case 0:
        return MailTemplateContentType.UNKNOWN;

      case 1:
        return MailTemplateContentType.TEXT;

      case 2:
        return MailTemplateContentType.HTML;

      default:
        throw new RuntimeException(
            "Failed to determine the mail template content type with the invalid code ("
                + code
                + ")");
    }
  }

  /**
   * Returns the numeric code for the mail template content type.
   *
   * @return the numeric code for the mail template content type
   */
  @JsonValue
  public int code() {
    return code;
  }

  /**
   * Returns the description for the mail template content type.
   *
   * @return the description for the mail template content type
   */
  public String description() {
    return description;
  }

  /**
   * Returns the <code>String</code> representation of the numeric code for the mail template
   * content type.
   *
   * @return the <code>String</code> representation of the numeric code for the mail template
   *     content type
   */
  public String getCodeAsString() {
    return String.valueOf(code);
  }
}
