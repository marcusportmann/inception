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

package digital.inception.core.data;

import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;
import org.springframework.util.StringUtils;

/**
 * The {@code DataFormat} enumeration defines the possible data formats.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The data format")
@XmlEnum
@XmlType(name = "DataFormat", namespace = "https://inception.digital/core")
public enum DataFormat implements CodeEnum {

  /** JSON. */
  @XmlEnumValue("JSON")
  JSON("json", new String[] {".json"}, "application/json", "JSON"),

  /** XML. */
  @XmlEnumValue("XML")
  XML("xml", new String[] {".xml"}, "application/xml", "XML");

  private final String code;

  private final String description;

  private final String[] extensions;

  private final String mimeType;

  DataFormat(String code, String[] extensions, String mimeType, String description) {
    this.code = code;
    this.extensions = extensions;
    this.mimeType = mimeType;
    this.description = description;
  }

  /**
   * Returns the data format for the specified filename.
   *
   * @param filename the filename
   * @return the data format for the specified filename
   */
  public static DataFormat fromFileName(String filename) {
    if (StringUtils.hasText(filename)) {
      filename = filename.toLowerCase();

      for (DataFormat fileType : values()) {

        for (String extension : fileType.extensions) {
          if (filename.endsWith(extension)) {
            return fileType;
          }
        }
      }
    }

    throw new RuntimeException(
        "Failed to determine the data format from the file name (" + filename + ")");
  }

  /**
   * Returns the data format with the specified MIME type.
   *
   * @param mimeType the MIME type for the data format
   * @return the data format with the specified MIME type
   */
  public static DataFormat fromMimeType(String mimeType) {
    if (StringUtils.hasText(mimeType)) {
      for (DataFormat fileType : values()) {
        if (fileType.mimeType.equals(mimeType)) {
          return fileType;
        }
      }
    }

    throw new RuntimeException(
        "Failed to determine the data format with the unknown MIME type (" + mimeType + ")");
  }

  /**
   * Returns the code for the data format.
   *
   * @return the code for the data format
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the data format.
   *
   * @return the description for the data format
   */
  public String description() {
    return description;
  }

  /**
   * Returns the extensions for the data format.
   *
   * @return the extensions for the data format
   */
  public String[] extensions() {
    return extensions;
  }

  /**
   * Returns the MIME type for the data format.
   *
   * @return the MIME type for the data format
   */
  public String mimeType() {
    return mimeType;
  }
}
