/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.core.wbxml;

// ~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>Encoder</code> class generates the binary data representation of a WBXML document from
 * a WBXML object hierarchy.
 *
 * @author Marcus Portmann
 */
public class Encoder {

  private static final String ENCODING_UTF_8 = "UTF-8";
  private BinaryBuffer buffer;
  private byte[] data;
  private int length;
  private List<String> stringTable;

  /**
   * Constructs a new <code>Encoder</code> to WBXML encode the specified document.
   *
   * @param document the document to encode
   */
  public Encoder(Document document) {
    // Initialize member variables
    buffer = new BinaryBuffer();
    stringTable = new ArrayList<>();

    // The WBXML version
    appendByte(WBXML.WBXML_VERSION);

    // The public ID of the DTD associated with the specified document
    appendMultiByteUINT32(document.getPublicId());

    // The IANA assigned ID of the charset for the specified document
    appendMultiByteUINT32(WBXML.CHARSET_UTF_8);

    // If we are dealing with a document with an unknown public ID  for its DTD then
    // we must build a string table
    if (document.getPublicId() == Document.PUBLIC_ID_UNKNOWN) {
      buildStringTable(document.getRootElement());
    }

    // Write the length of the string table
    int stringTableLength = getStringTableLength();

    appendMultiByteUINT32(stringTableLength);

    // Write the string table if required
    if (stringTableLength > 0) {
      for (String tmpStr : stringTable) {
        appendString(tmpStr);
      }
    }

    generateWBXML(document.getRootElement());
    length = buffer.getLength();
    data = buffer.getData();
  }

  /**
   * Returns the binary data which represents the WBXML encoded document
   *
   * @return the binary data which represents the WBXML encoded document
   */
  public byte[] getData() {
    return data;
  }

  /**
   * Returns the length of the binary data which represents the WBXML encoded document
   *
   * @return the length of the binary data which represents the WBXML encoded document
   */
  public int getLength() {
    return length;
  }

  private void addToStringTable(String str) {
    for (String tmpStr : stringTable) {
      // If this string already exists in the string table then dont add it again
      if (tmpStr.equals(str)) {
        return;
      }
    }

    // Add the string to the string table
    stringTable.add(str);
  }

  private void appendBinary(byte[] data) {
    buffer.append(data);
  }

  private void appendByte(int b) {
    buffer.append(b);
  }

  private void appendMultiByteUINT32(int value) {
    byte[] buf = new byte[5];
    int idx = 0;

    do {
      buf[idx++] = (byte) (value & 0x7f);
      value = value >> 7;
    } while (value != 0);

    while (idx > 1) {
      appendByte(buf[--idx] | 0x80);
    }

    appendByte(buf[0]);
  }

  private void appendString(String str) {
    try {
      buffer.append(str.getBytes(ENCODING_UTF_8));
    } catch (Throwable e) {
      throw new RuntimeException(
          "Failed to encode the string of length " + str.length() + " for the WBXML document", e);
    }

    buffer.append(0);
  }

  private void buildStringTable(Element element) {
    // Get the name for the element and add it to the string table
    addToStringTable(element.getName());

    // Process the attributes for this element
    if (element.hasAttributes()) {
      List<Attribute> attributes = element.getAttributes();

      for (Attribute attribute : attributes) {
        addToStringTable(attribute.getName());
      }
    }

    // Process any content under this element
    if (element.hasContent()) {
      List<Content> collection = element.getContent();

      for (Content content : collection) {
        // If this is a child element...
        if (content instanceof Element) {
          buildStringTable(((Element) content));
        }
      }
    }
  }

  private void generateWBXML(Element element) {
    // Literal TAG
    byte elementTag = WBXML.TOKEN_LITERAL;

    // Build the tag for this element and write it to the buffer
    // We assume that all tag names will be LITERALS store in the string table
    if (element.hasAttributes()) {
      elementTag += 0x80;
    }

    if (element.hasContent()) {
      elementTag += 0x40;
    }

    appendByte(elementTag);

    // Append the offset into the string table for the tag name
    appendMultiByteUINT32(getStringTableOffset(element.getName()));

    // If we have attributes then write them out
    if (element.hasAttributes()) {
      List<Attribute> collection = element.getAttributes();

      for (Attribute attribute : collection) {
        // Write out that this attribute is a literal
        appendByte(WBXML.TOKEN_LITERAL);

        // Write the string table offset for the name of the attribute
        appendMultiByteUINT32(getStringTableOffset(attribute.getName()));

        // Inline string value
        appendByte(WBXML.TOKEN_STR_I);

        // Append the string
        appendString(attribute.getValue());
      }

      // Append the end of attribute space token
      appendByte(WBXML.TOKEN_END);
    }

    // Process any content under this element
    if (element.hasContent()) {
      List<Content> collection = element.getContent();

      for (Content content : collection) {
        // If this is a child element...
        if (content instanceof Element) {
          generateWBXML(((Element) content));
        }

        // If this is a CDATA element...
        else if (content instanceof CDATA) {
          CDATA cdata = (CDATA) content;

          appendByte(WBXML.TOKEN_STR_I);
          appendString(cdata.getText());
        }

        // If this is an Opaque element...
        else if (content instanceof Opaque) {
          Opaque opaque = (Opaque) content;

          appendByte(WBXML.TOKEN_OPAQUE);
          appendMultiByteUINT32(opaque.getLength());
          appendBinary(opaque.getData());
        }
      }
    }

    // Append the end of element space token
    appendByte(WBXML.TOKEN_END);
  }

  private int getStringTableLength() {
    int stringTablelength = 0;

    for (String tmpStr : stringTable) {
      try {
        byte[] bytes = tmpStr.getBytes(ENCODING_UTF_8);

        stringTablelength += bytes.length;
        stringTablelength += 1;
      } catch (Throwable e) {
        throw new RuntimeException(
            "Failed to calculate the length of the string table for the " + "WBXML document", e);
      }
    }

    return stringTablelength;
  }

  private int getStringTableOffset(String str) {
    int offset = 0;

    for (String tmpStr : stringTable) {
      if (tmpStr.equals(str)) {
        return offset;
      } else {
        try {
          byte[] bytes = tmpStr.getBytes(ENCODING_UTF_8);

          offset += bytes.length;
          offset += 1;
        } catch (Throwable e) {
          throw new RuntimeException(
              "Failed to calculate the offset of the string ("
                  + str
                  + ") in the string table for the WBXML document",
              e);
        }
      }
    }

    // The string could not be found
    return -1;
  }
}
