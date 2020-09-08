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
@Schema(description = "The mail template content type")
@XmlEnum
@XmlType(name = "MailTemplateContentType", namespace = "http://mail.inception.digital")
public enum MailTemplateContentType {
  @XmlEnumValue("Text")
  TEXT("text", "Text"),
  @XmlEnumValue("HTML")
  HTML("html", "HTML");

  private final String code;

  private final String description;

  MailTemplateContentType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the mail template content type given by the specified code value.
   *
   * @param code the code value identifying the mail template content type
   * @return the mail template content type given by the specified code value
   */
  @JsonCreator
  public static MailTemplateContentType fromCode(String code) {
    switch (code) {
      case "text":
        return MailTemplateContentType.TEXT;

      case "html":
        return MailTemplateContentType.HTML;

      default:
        throw new RuntimeException(
            "Failed to determine the mail template content type with the invalid code ("
                + code
                + ")");
    }
  }

  /**
   * Returns the mail template content type for the specified numeric code.
   *
   * @param numericCode the numeric code identifying the mail template content type
   * @return the mail template content type given by the specified numeric code value
   */
  public static MailTemplateContentType fromNumericCode(int numericCode) {
    switch (numericCode) {
      case 1:
        return MailTemplateContentType.TEXT;
      case 2:
        return MailTemplateContentType.HTML;
      default:
        throw new RuntimeException(
            "Failed to determine the mail template content type for the numeric code ("
                + numericCode
                + ")");
    }
  }

  /**
   * Returns the numeric code for the mail template content type.
   *
   * @param mailTemplateContentType the mail template content type
   * @return the numeric code for the mail template content type
   */
  public static int toNumericCode(MailTemplateContentType mailTemplateContentType) {
    switch (mailTemplateContentType) {
      case TEXT:
        return 1;
      case HTML:
        return 2;
      default:
        throw new RuntimeException(
            "Failed to determine the numeric code for the mail template content type ("
                + mailTemplateContentType.code()
                + ")");
    }
  }

  /**
   * Returns the code for the mail template content type.
   *
   * @return the code for the mail template content type
   */
  @JsonValue
  public String code() {
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
}
