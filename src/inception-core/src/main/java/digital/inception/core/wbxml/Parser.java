/*
 * Copyright 2021 Marcus Portmann
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * The <code>Parser</code> class generates a WBXML object hierarchy from the binary data
 * representation of a WBXML document.
 *
 * @author Marcus Portmann
 */
public class Parser {

  private static final String ENCODING_UTF_8 = "UTF-8";

  private ByteArrayInputStream stream = null;

  private byte[] stringTable = null;

  /** Constructs a new <code>Parser</code>. */
  public Parser() {}

  /**
   * Parse the specified binary data representation of the WBXML document.
   *
   * @param data the binary data representation of the WBXML document
   * @return the WBXML object hierarchy
   */
  public Document parse(byte[] data) throws IOException, ParserException {
    // Local variables
    int tmpValue;

    // Initialize the input stream
    stream = new ByteArrayInputStream(data);

    // Read the version number
    int version;
    if ((version = readByte()) != WBXML.WBXML_VERSION) {
      throw new ParserException("Invalid WBXML version: " + version);
    }

    // Read the well known public identifier and check whether it is supported
    int publicId;
    if ((publicId = readByte()) != Document.PUBLIC_ID_UNKNOWN) {
      throw new ParserException("Unknown document public identifier: " + publicId);
    }

    // Read the character set and check whether it is supported
    int charset;
    if ((charset = readMultiByteUINT32()) != WBXML.CHARSET_UTF_8) {
      throw new ParserException("Unsupported character set: " + charset);
    }

    // Read the length of the string table
    int stringTableLength = readMultiByteUINT32();

    // If we have a string table then read the entire thing and store it for later use
    if (stringTableLength > 0) {
      stringTable = new byte[stringTableLength];

      if (stream.read(stringTable, 0, stringTableLength) != stringTableLength) {
        throw new ParserException(
            "Failed to read " + stringTableLength + " bytes for the string table");
      }
    }

    // Create the root element
    Element rootElement = new Element();

    while ((tmpValue = readByte()) != -1) {
      switch (tmpValue) {
        case WBXML.TOKEN_SWITCH_PAGE:
          {
            throw new ParserException("Unsupported token: TOKEN_SWITCH_PAGE");
          }

        case WBXML.TOKEN_PI:
          {
            throw new ParserException("Unsupported token: TOKEN_PI");
          }

        case WBXML.TOKEN_END:
          {
            throw new ParserException("Unexpected token: TOKEN_END");
          }

        default:
          {
            parseElement(tmpValue, rootElement);

            break;
          }
      }
    }

    return new Document(rootElement);
  }

  void parseAttributes(Element element) throws IOException, ParserException {
    int tmpValue;
    int attributeNameOffset;
    String attributeName;
    String attributeValue;

    // Read the first byte detailing the format of the attribute
    tmpValue = readByte();

    while (tmpValue != WBXML.TOKEN_END) {
      // We can only handle literal attributes
      if (tmpValue != WBXML.TOKEN_LITERAL) {
        throw new ParserException("Unsupported attribute tag identity: " + tmpValue);
      }

      // Read the offset into the string table for the name of this attribute
      attributeNameOffset = readMultiByteUINT32();

      // Read the name of the attribute from the string table
      if ((attributeName = readFromStringTable(attributeNameOffset)) == null) {
        throw new ParserException(
            "Invalid string table offset for attribute name: " + attributeNameOffset);
      }

      // Read the byte giving the value type for this attribute
      tmpValue = readByte();

      // We can only handle inline string attribute values
      if (tmpValue != WBXML.TOKEN_STR_I) {
        throw new ParserException("Unsupported attribute value identity: " + tmpValue);
      }

      // Read the inline string
      attributeValue = readString();

      // Set the attribute
      element.setAttribute(attributeName, attributeValue);

      // Read the next byte. This will either be the end of the attributes or
      // alternatively the start of a new attribute
      tmpValue = readByte();
    }
  }

  void parseContent(Element element) throws IOException, ParserException {
    int tmpValue;

    // Read the first byte detailing the format of the content
    while (true) {
      tmpValue = readByte();

      switch (tmpValue) {
        case WBXML.TOKEN_END:
          {
            // End of content under this element
            return;
          }

        case WBXML.TOKEN_ENTITY:
          {
            // NOTE: We do not process the entity token
            int entityLength = readMultiByteUINT32();

            if (stream.skip(entityLength) != entityLength) {
              throw new IOException(
                  "Unexpected EOF while skipping entity with length: " + entityLength);
            }

            break;
          }

        case WBXML.TOKEN_STR_I:
          {
            // Read the inline string
            String str = readString();

            // Add as text content to the element
            element.addContent(str);

            break;
          }

        case WBXML.TOKEN_STR_T:
          {
            // Read the index into the string table
            int stringTableOffset = readMultiByteUINT32();

            // Read the string from the string table and add as text content to the element
            element.addContent(readFromStringTable(stringTableOffset));

            break;
          }

        case WBXML.TOKEN_OPAQUE:
          {
            // Read the length of the opaque data
            int opaqueLength = readMultiByteUINT32();

            // Read the opaque data
            element.addContent(readOpaque(opaqueLength));

            break;
          }

        case WBXML.TOKEN_EXT_I_0:
          throw new ParserException("Unsupported token: TOKEN_EXT_I_0");

        case WBXML.TOKEN_EXT_I_1:
          throw new ParserException("Unsupported token: TOKEN_EXT_I_1");

        case WBXML.TOKEN_EXT_I_2:
          throw new ParserException("Unsupported token: TOKEN_EXT_I_2");

        case WBXML.TOKEN_EXT_0:
          throw new ParserException("Unsupported token: TOKEN_EXT_0");

        case WBXML.TOKEN_EXT_1:
          throw new ParserException("Unsupported token: TOKEN_EXT_1");

        case WBXML.TOKEN_EXT_2:
          throw new ParserException("Unsupported token: TOKEN_EXT_2");

        case WBXML.TOKEN_EXT_T_0:
          throw new ParserException("Unsupported token: TOKEN_EXT_T_0");

        case WBXML.TOKEN_EXT_T_1:
          throw new ParserException("Unsupported token: TOKEN_EXT_T_1");

        case WBXML.TOKEN_EXT_T_2:
          throw new ParserException("Unsupported token: TOKEN_EXT_T_2");

        case WBXML.TOKEN_PI:
          throw new ParserException("Unsupported token: TOKEN_PI");

        default:
          {
            Element childElement = new Element();

            parseElement(tmpValue, childElement);
            element.addContent(childElement);

            break;
          }
      }
    }
  }

  private void parseElement(int token, Element element) throws IOException, ParserException {
    // Local variables
    // int result = 0;
    boolean hasAttributes = (token & 0x80) > 0;
    boolean hasContent = (token & 0x40) > 0;
    int elementIdentity = token & 0x3F;

    // We only support unknown tags with an offset into the string table
    if (elementIdentity != 0x04) {
      throw new ParserException("Unsupported element tag identity: " + elementIdentity);
    }

    // Read the offset into the string table for the element name
    int elementNameOffset = readMultiByteUINT32();

    // Read the element name from the string table
    String elementName;

    if ((elementName = readFromStringTable(elementNameOffset)) == null) {
      throw new ParserException(
          "Invalid string table offset for element name: " + elementNameOffset);
    }

    element.setName(elementName);

    // If we have attributes parse them now
    if (hasAttributes) {
      parseAttributes(element);
    }

    // If we have content parse it now
    if (hasContent) {
      parseContent(element);
    } else {
      if (readByte() != WBXML.TOKEN_END) {
        throw new ParserException("Missing token after element without content: TOKEN_END");
      }
    }
  }

  private int readByte() throws IOException {
    return stream.read();
  }

  private String readFromStringTable(int offset) throws ParserException {
    int index = offset;

    while (index < stringTable.length) {
      if (stringTable[index] == 0) {
        try {
          return new String(stringTable, offset, index - offset, StandardCharsets.UTF_8);
        } catch (Throwable e) {
          throw new ParserException(
              "Failed to retrieve the string at offset " + offset + " from the string table");
        }
      }

      index++;
    }

    throw new ParserException("String exceeds string table at offset: " + offset);
  }

  private int readMultiByteUINT32() throws IOException {
    int result = 0;
    int i;

    do {
      i = readByte();
      result = (result << 7) | (i & 0x7f);
    } while ((i & 0x80) != 0);

    return result;
  }

  private byte[] readOpaque(int length) throws IOException {
    byte[] data = new byte[length];

    if (stream.read(data, 0, length) != length) {
      throw new IOException("EOF while parsing WBXML and reading opaque data");
    }

    return data;
  }

  private String readString() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    while (true) {
      int tmpValue = readByte();

      if (tmpValue == 0) {
        byte[] bytes = baos.toByteArray();

        return new String(bytes, 0, bytes.length, StandardCharsets.UTF_8);
      } else {
        baos.write(tmpValue);
      }
    }
  }
}
