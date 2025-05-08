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

package digital.inception.core.xml;

import digital.inception.core.exception.RichRuntimeException;
import java.io.Serial;
import org.xml.sax.SAXParseException;

/**
 * The {@code XmlParserException} exception is thrown to indicate an error condition when parsing an
 * XML file.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class XmlParserException extends RichRuntimeException {

  @Serial private static final long serialVersionUID = 1000000;

  /** Creates a new {@code XmlParserException} instance with {@code null} as its message. */
  public XmlParserException() {
    super();
  }

  /**
   * Creates a new {@code XmlParserException} instance using the information contained in the
   * specified <b>SAXParseException</b>.
   *
   * @param cause the <b>SAXParseException</b> giving the cause of the exception
   */
  public XmlParserException(SAXParseException cause) {
    super(
        cause.getMessage()
            + " at line ("
            + cause.getLineNumber()
            + ") and column ("
            + (cause.getColumnNumber()
                + ") with SystemID ("
                + ((cause.getSystemId() != null) ? cause.getSystemId() : "UNKNOWN")
                + ") and PublicID ("
                + ((cause.getPublicId() != null) ? cause.getPublicId() : "UNKNOWN")
                + ")"));
  }

  /**
   * Creates a new {@code XmlParserException} instance with the specified message.
   *
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   */
  public XmlParserException(String message) {
    super(message);
  }

  /**
   * Creates a new {@code XmlParserException} instance with the specified cause and a message of
   * {@code (cause==null ? null : cause.toString())} (which typically contains the class and message
   * of cause).
   *
   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A {@code
   *     null} value is permitted if the cause is nonexistent or unknown)
   */
  public XmlParserException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new {@code XmlParserException} instance with the specified code and message.
   *
   * @param code the error code
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   */
  public XmlParserException(String code, String message) {
    super(code, message);
  }

  /**
   * Creates a new {@code XmlParserException} instance with the specified message and cause.
   *
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A {@code
   *     null} value is permitted if the cause is nonexistent or unknown)
   */
  public XmlParserException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new {@code XmlParserException} instance with the specified code, message and cause.
   *
   * @param code the error code
   * @param message The message saved for later retrieval by the {@code getMessage()} method.
   * @param cause The cause saved for later retrieval by the {@code getCause()} method. (A {@code
   *     null} value is permitted if the cause is nonexistent or unknown)
   */
  public XmlParserException(String code, String message, Throwable cause) {
    super(code, message, cause);
  }
}
