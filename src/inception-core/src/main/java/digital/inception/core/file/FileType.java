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

package digital.inception.core.file;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;
import org.springframework.util.StringUtils;

/**
 * The <b>FileType</b> enumeration defines the possible file types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The file type")
@XmlEnum
@XmlType(name = "FileType", namespace = "http://inception.digital/core")
public enum FileType {

  /** Audio 3GPP. */
  @XmlEnumValue("Audio3GPP")
  AUDIO_3GPP("audio_3gpp", new String[] {".3gp"}, "audio/3gpp", "Audio 3GPP"),

  /** Audio 3GPP2. */
  @XmlEnumValue("Audio3GPP2")
  AUDIO_3GPP2("audio_3gpp2", new String[] {".3g2"}, "audio/3gpp2", "Audio 3GPP2"),

  /** Audio AAC. */
  @XmlEnumValue("AudioAAC")
  AUDIO_AAC("audio_aac", new String[] {".aac"}, "audio/aac", "Audio AAC"),

  /** Audio MP3. */
  @XmlEnumValue("AudioMP3")
  AUDIO_MP3("audio_mp3", new String[] {".mp3"}, "audio/mpeg", "Audio MP3"),

  /** Audio OGG. */
  @XmlEnumValue("AudioOGG")
  AUDIO_OGG("audio_ogg", new String[] {".oga"}, "audio/ogg", "Audio OGG"),

  /** Audio OPUS. */
  @XmlEnumValue("AudioOPUS")
  AUDIO_OPUS("audio_opus", new String[] {".opus"}, "audio/opus", "Audio OPUS"),

  /** Audio WAV. */
  @XmlEnumValue("AudioWAV")
  AUDIO_WAV("audio_wav", new String[] {".wav"}, "audio/wav", "Audio WAV"),

  /** Audio WEBM. */
  @XmlEnumValue("AudioWEBM")
  AUDIO_WEBM("audio_webm", new String[] {".weba"}, "audio/webm", "Audio WEBM"),

  /** Binary. */
  @XmlEnumValue("Binary")
  BINARY("binary", new String[] {".bin"}, "application/octet-stream", "Binary"),

  /** CSV. */
  @XmlEnumValue("CSV")
  CSV("csv", new String[] {".csv"}, "text/csv", "CSV"),

  /** HTML. */
  @XmlEnumValue("HTML")
  HTML("html", new String[] {".htm", ".html"}, "text/html", "HTML"),

  /** Image BPM. */
  @XmlEnumValue("ImageBMP")
  IMAGE_BMP("image_bmp", new String[] {".bmp"}, "image/bmp", "Image BMP"),

  /** Image GIF. */
  @XmlEnumValue("ImageGIF")
  IMAGE_GIF("image_gif", new String[] {".gif"}, "image/gif", "Image GIF"),

  /** Image JPEG. */
  @XmlEnumValue("ImageJPEG")
  IMAGE_JPEG("image_jpeg", new String[] {".jpg", ".jpeg"}, "image/jpeg", "Image JPEG"),

  /** Image PNG. */
  @XmlEnumValue("ImagePNG")
  IMAGE_PNG("image_png", new String[] {".png"}, "image/png", "Image PNG"),

  /** Image SVG. */
  @XmlEnumValue("ImageSVG")
  IMAGE_SVG("image_svg", new String[] {".svg"}, "image/svg+xml", "Image SVG"),

  /** Image TIFF. */
  @XmlEnumValue("ImageTIFF")
  IMAGE_TIFF("image_tiff", new String[] {".tiff"}, "image/tiff", "Image TIFF"),

  /** Image WEBP. */
  @XmlEnumValue("ImageWEBP")
  IMAGE_WEBP("image_webp", new String[] {".webp"}, "image/webp", "Image WEBP"),

  /** JSON. */
  @XmlEnumValue("JSON")
  JSON("json", new String[] {".json"}, "application/json", "JSON"),

  /** Microsoft Excel. */
  @XmlEnumValue("MicrosoftExcel")
  MICROSOFT_EXCEL(
      "microsoft_excel", new String[] {".xls"}, "application/vnd.ms-excel", "Microsoft Excel"),

  /** Microsoft PowerPoint. */
  @XmlEnumValue("MicrosoftPowerPoint")
  MICROSOFT_POWERPOINT(
      "microsoft_powerpoint",
      new String[] {".ppt"},
      "application/vnd.ms-powerpoint",
      "Microsoft PowerPoint"),

  /** Microsoft Visio. */
  @XmlEnumValue("MicrosoftVisio")
  MICROSOFT_VISIO(
      "microsoft_visio", new String[] {".vsd"}, "application/vnd.visio", "Microsoft Visio"),

  /** Microsoft Word. */
  @XmlEnumValue("MicrosoftWord")
  MICROSOFT_WORD("microsoft_word", new String[] {".doc"}, "application/msword", "Microsoft Word"),

  /** Open Document Format Presentation Document. */
  @XmlEnumValue("ODFPresentation")
  ODF_PRESENTATION(
      "odf_presentation",
      new String[] {".odp"},
      "application/vnd.oasis.opendocument.presentation",
      "Open Document Format Presentation Document"),

  /** Open Document Format Spreadsheet Document. */
  @XmlEnumValue("ODFSpreadsheet")
  ODF_SPREADSHEET(
      "odf_spreadsheet",
      new String[] {".ods"},
      "application/vnd.oasis.opendocument.spreadsheet",
      "Open Document Format Spreadsheet Document"),

  /** Open Document Format Text Document. */
  @XmlEnumValue("ODFText")
  ODF_TEXT(
      "odf_text",
      new String[] {".odt"},
      "application/vnd.oasis.opendocument.text",
      "Open Document Format Spreadsheet Document"),

  /** Office Open XML Document. */
  @XmlEnumValue("OOXMLDocument")
  OOXML_DOCUMENT(
      "ooxml_document",
      new String[] {".docx"},
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
      "Office Open XML Document"),

  /** Office Open XML Presentation. */
  @XmlEnumValue("OOXMLPresentation")
  OOXML_PRESENTATION(
      "ooxml_presentation",
      new String[] {".pptx"},
      "application/vnd.openxmlformats-officedocument.presentationml.presentation",
      "Office Open XML Presentation"),

  /** Office Open XML Spreadsheet. */
  @XmlEnumValue("OOXMLSpreadsheet")
  OOXML_SPREADSHEET(
      "ooxml_spreadsheet",
      new String[] {".xlsx"},
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      "Office Open XML Spreadsheet"),

  /** OGG. */
  @XmlEnumValue("OGG")
  OGG("ogg", new String[] {".ogx"}, "application/ogg", "OGG"),

  /** PDF. */
  @XmlEnumValue("PDF")
  PDF("pdf", new String[] {".pdf"}, "application/pdf", "PDF"),

  /** Rich Text Format. */
  @XmlEnumValue("RTF")
  RTF("rtf", new String[] {".rtf"}, "application/rtf", "Rich Text Format"),

  /** Text. */
  @XmlEnumValue("Text")
  TEXT("text", new String[] {".txt"}, "text/plain", "Text"),

  /** Video 3GPP. */
  @XmlEnumValue("Video3GPP")
  VIDEO_3GPP("video_3gpp", new String[] {".3gp"}, "video/3gpp", "Video 3GPP"),

  /** Video 3GPP2. */
  @XmlEnumValue("Video3GPP2")
  VIDEO_3GPP2("video_3gpp2", new String[] {".3g2"}, "video/3gpp2", "Video 3GPP2"),

  /** Video AVI. */
  @XmlEnumValue("VideoAVI")
  VIDEO_AVI("video_avi", new String[] {".avi"}, "video/x-msvideo", "Video AVI"),

  /** Video MP4. */
  @XmlEnumValue("VideoMP4")
  VIDEO_MP4("video_mp4", new String[] {".mp4"}, "video/mp4", "Video MP4"),

  /** Video MPEG. */
  @XmlEnumValue("VideoMPEG")
  VIDEO_MPEG("video_mpeg", new String[] {".mpeg"}, "video/mpeg", "Video MPEG"),

  /** Video MPEG Transport Stream. */
  @XmlEnumValue("VideoMP2T")
  VIDEO_MP2T("video_mp2t", new String[] {".ts"}, "video/mp2t", "Video MPEG Transport Stream"),

  /** Video OGG. */
  @XmlEnumValue("VideoOGG")
  VIDEO_OGG("video_ogg", new String[] {".ogv"}, "video/ogg", "Video OGG"),

  /** Video WEBM. */
  @XmlEnumValue("VideoWEBM")
  VIDEO_WEBM("video_webm", new String[] {".webm"}, "video/webm", "Video WEBM"),

  /** XHTML. */
  @XmlEnumValue("XHTML")
  XHTML("xhtml", new String[] {".xhtml"}, "application/xhtml+xml", "XHTML"),

  /** XML. */
  @XmlEnumValue("XML")
  XML("xml", new String[] {".xml"}, "application/xml", "XML"),

  /** ZIP. */
  @XmlEnumValue("ZIP")
  ZIP("zip", new String[] {".zip"}, "application/zip", "ZIP");

  /** The file types. */
  public static final FileType[] FILE_TYPES =
      new FileType[] {
        AUDIO_3GPP,
        AUDIO_3GPP2,
        AUDIO_AAC,
        AUDIO_MP3,
        AUDIO_OGG,
        AUDIO_OPUS,
        AUDIO_WAV,
        AUDIO_WEBM,
        BINARY,
        CSV,
        HTML,
        IMAGE_BMP,
        IMAGE_GIF,
        IMAGE_JPEG,
        IMAGE_PNG,
        IMAGE_SVG,
        IMAGE_TIFF,
        IMAGE_WEBP,
        JSON,
        MICROSOFT_EXCEL,
        MICROSOFT_POWERPOINT,
        MICROSOFT_VISIO,
        MICROSOFT_WORD,
        ODF_PRESENTATION,
        ODF_SPREADSHEET,
        ODF_TEXT,
        OOXML_DOCUMENT,
        OOXML_PRESENTATION,
        OOXML_SPREADSHEET,
        OGG,
        PDF,
        RTF,
        TEXT,
        VIDEO_3GPP,
        VIDEO_3GPP2,
        VIDEO_AVI,
        VIDEO_MP4,
        VIDEO_MPEG,
        VIDEO_MP2T,
        VIDEO_OGG,
        VIDEO_WEBM,
        XHTML,
        XML,
        ZIP
      };

  private final String code;

  private final String description;

  private final String[] extensions;

  private final String mimeType;

  FileType(String code, String[] extensions, String mimeType, String description) {
    this.code = code;
    this.extensions = extensions;
    this.mimeType = mimeType;
    this.description = description;
  }

  /**
   * Returns the file type given by the specified code value.
   *
   * @param code the code for the file type
   * @return the file type given by the specified code value
   */
  @JsonCreator
  public static FileType fromCode(String code) {
    if (StringUtils.hasText(code)) {
      for (FileType fileType : FILE_TYPES) {
        if (fileType.code.equals(code)) {
          return fileType;
        }
      }
    }

    throw new RuntimeException(
        "Failed to determine the file type with the invalid code (" + code + ")");
  }

  /**
   * Returns the file type for the specified filename.
   *
   * @param fileName the file name
   * @return the file type for the specified filename
   */
  public static FileType fromFileName(String fileName) {
    if (StringUtils.hasText(fileName)) {
      fileName = fileName.toLowerCase();

      for (FileType fileType : FILE_TYPES) {

        for (String extension : fileType.extensions) {
          if (fileName.endsWith(extension)) {
            return fileType;
          }
        }
      }
    }

    throw new RuntimeException(
        "Failed to determine the file type from the file name (" + fileName + ")");
  }

  /**
   * Returns the file type with the specified MIME type.
   *
   * @param mimeType the MIME type for the file type
   * @return the file type with the specified MIME type
   */
  public static FileType fromMimeType(String mimeType) {
    if (StringUtils.hasText(mimeType)) {
      for (FileType fileType : FILE_TYPES) {
        if (fileType.mimeType.equals(mimeType)) {
          return fileType;
        }
      }
    }

    throw new RuntimeException(
        "Failed to determine the file type with the unknown MIME type (" + mimeType + ")");
  }

  /**
   * Returns the code for the file type.
   *
   * @return the code for the file type
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the file type.
   *
   * @return the description for the file type
   */
  public String description() {
    return description;
  }

  /**
   * Returns the extensions for the file type.
   *
   * @return the extensions for the file type
   */
  public String[] extensions() {
    return extensions;
  }

  /**
   * Returns the MIME type for the file type.
   *
   * @return the MIME type for the file type
   */
  @JsonValue
  public String mimeType() {
    return mimeType;
  }
}
