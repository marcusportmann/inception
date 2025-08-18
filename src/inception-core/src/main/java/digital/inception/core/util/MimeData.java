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

package digital.inception.core.util;

import jakarta.activation.MimeType;
import java.io.Serial;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Base64;
import org.springframework.util.StringUtils;

/**
 * The {@code MimeData} class holds binary data along with the Multipurpose Internet Mail Extensions
 * (MIME) type for the data.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class MimeData implements java.io.Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The cached base-64 encoded SHA-256 hash of the data. */
  private transient String cachedHash;

  /** The data. */
  private byte[] data;

  /** The MIME type for the data. */
  private MimeType mimeType;

  /** Constructs a new {@code MimeData}. */
  public MimeData() {}

  /**
   * Constructs a new {@code MimeData}.
   *
   * @param mimeType the MIME type for the data
   * @param data the data
   */
  public MimeData(String mimeType, byte[] data) {
    try {
      this.mimeType = new MimeType(mimeType);
    } catch (Throwable e) {
      try {
        this.mimeType = new MimeType("application/octet-stream");
      } catch (Throwable ignored) {
      }
    }

    this.data = data;
  }

  /**
   * Constructs a new {@code MimeData}.
   *
   * @param mimeType the MIME type for the data
   * @param data the data
   */
  public MimeData(MimeType mimeType, byte[] data) {
    this.mimeType = mimeType;
    this.data = data;
  }

  /**
   * Constructs a new {@code MimeData}.
   *
   * @param data the data
   */
  public MimeData(byte[] data) {
    try {
      this.mimeType = new MimeType("application/octet-stream");
    } catch (Throwable ignored) {
    }
    this.data = data;
  }

  /**
   * Returns the base MIME type for the data, i.e., the MIME type without parameters.
   *
   * @return the base MIME type for the data
   */
  public String getBaseMimeType() {
    return mimeType.getBaseType();
  }

  /**
   * Returns the charset for the data, extracted from the MIME type for the data, or the default
   * charset otherwise.
   *
   * @return the charset for the data, extracted from the MIME type for the data, or the default
   *     charset otherwise
   */
  public Charset getCharset() {
    String charsetName = mimeType.getParameter("charset");

    if (StringUtils.hasText(charsetName)) {
      return Charset.forName(charsetName, Charset.defaultCharset());
    } else {
      return Charset.defaultCharset();
    }
  }

  /**
   * Returns the data.
   *
   * @return the data
   */
  public byte[] getData() {
    return data;
  }

  /**
   * Returns the data as a String.
   *
   * @return the data as a String
   */
  public String getDataAsString() {
    return new String(data, getCharset());
  }

  /**
   * Returns the filename for the data, extracted from the filename parameter on the MIME type for
   * the data, if present.
   *
   * @return the filename for the data extracted from the filename parameter on the MIME type for
   *     the data, if present, otherwise {@code null}
   */
  public String getFileName() {
    return mimeType.getParameter("filename");
  }

  /**
   * Retrieve the base-64 encoded SHA-256 hash of the data.
   *
   * @return the base-64 encoded SHA-256 hash of the data
   */
  public synchronized String getHash() {
    if (cachedHash == null) {
      try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        cachedHash = Base64.getEncoder().encodeToString(digest.digest(data));
      } catch (Throwable e) {
        throw new RuntimeException("Failed to calculate the SHA-256 hash of the data", e);
      }
    }

    return cachedHash;
  }

  /**
   * Returns the MIME type for the data.
   *
   * @return the MIME type for the data
   */
  public MimeType getMimeType() {
    return mimeType;
  }

  /**
   * Returns the name for the data, extracted from the name or filename parameter on the MIME type
   * for the data, if present.
   *
   * @return the name for the data extracted from the name or filename parameter on the MIME type
   *     for the data, if present, otherwise {@code null}
   */
  public String getName() {
    String name = mimeType.getParameter("name");

    if (StringUtils.hasText(name)) {
      return name;
    } else {
      return mimeType.getParameter("filename");
    }
  }

  /**
   * Returns the size of the data.
   *
   * @return the size of the data
   */
  public int getSize() {
    return data.length;
  }

  /**
   * Is the data {@code null} or zero bytes.
   *
   * @return {@code true} if the data is {@code null} or zero bytes or {@code false} otherwise
   */
  public boolean isEmpty() {
    return ((data == null) || (data.length == 0));
  }

  /**
   * Check whether the data has the specified MIME type.
   *
   * @param mimeTypeToCheck the MIME type to check
   * @return {@code true} if the data has the specified MIME type or {@code false} otherwise
   */
  public boolean isMimeType(String mimeTypeToCheck) {
    try {
      return mimeType.match(mimeTypeToCheck);
    } catch (Throwable ignored) {
      return false;
    }
  }

  /**
   * Set the data.
   *
   * @param data the data
   */
  public void setData(byte[] data) {
    this.data = data;
    this.cachedHash = null;
  }

  /**
   * Set the filename for the data, which will be stored as the filename parameter on the MIME type
   * for the data.
   *
   * @param filename the filename for the data
   */
  public void setFileName(String filename) {
    mimeType.setParameter("filename", filename);
  }

  /**
   * Set the MIME type for the data.
   *
   * @param mimeType the MIME type for the data
   */
  public void setMimeType(MimeType mimeType) {
    this.mimeType = mimeType;
  }

  /**
   * Set the name for the data, which will be stored as the name parameter on the MIME type for the
   * data.
   *
   * @param name the name for the data
   */
  public void setName(String name) {
    mimeType.setParameter("name", name);
  }
}
