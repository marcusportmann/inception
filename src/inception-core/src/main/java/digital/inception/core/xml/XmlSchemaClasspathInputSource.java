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

package digital.inception.core.xml;

//~--- non-JDK imports --------------------------------------------------------

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Reader;
import org.w3c.dom.ls.LSInput;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>XmlSchemaClasspathInputSource</code> class provides an implementation of the
 * <code>org.w3c.dom.ls.LSInput</code> interface that allows resources to be retrieved from the
 * classpath.
 *
 * @author Marcus Portmann
 */
public class XmlSchemaClasspathInputSource
    implements LSInput {

  /**
   * The base URI to be used for resolving a relative systemId to an absolute URI.
   */
  private String baseURI;

  /**
   * Is the input source certified?
   */
  private boolean certifiedText;

  /**
   * The data for the input source.
   */
  private byte[] data;

  /**
   * The character encoding for the input source.
   */
  private String encoding;

  /**
   * The namespace for this input source.
   */
  private String namespaceURI;

  /**
   * The public identifier for this input source.
   */
  private String publicId;

  /**
   * The system identifier for this input source.
   */
  private String systemId;

  /**
   * Constructs a new <code>XmlSchemaClasspathInputSource</code>.
   *
   * @param namespaceURI  the namespace for this input source
   * @param publicId      the public identifier for this input source.
   * @param systemId      the system identifier for this input source
   * @param baseURI       the base URI to be used for resolving a relative systemId to an absolute
   *                      URI
   * @param classpathName the name of the resource on the classpath for this input source
   */
  public XmlSchemaClasspathInputSource(
      String namespaceURI, String publicId, String systemId,
      String baseURI, String classpathName) {
    this.namespaceURI = namespaceURI;
    this.publicId = publicId;
    this.systemId = systemId;
    this.baseURI = baseURI;
    this.certifiedText = false;

    try {
      try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(
          classpathName)) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        int numberOfBytesRead;

        while ((numberOfBytesRead = is.read(buffer)) != -1) {
          baos.write(buffer, 0, numberOfBytesRead);
        }

        this.data = baos.toByteArray();
      }
    } catch (Throwable e) {
      throw new XmlSchemaException("Failed to read the classpath resource (" + classpathName
          + ") for the input source", e);
    }
  }

  /**
   * Returns the base URI to be used for resolving a relative systemId to an absolute URI.
   *
   * @return the base URI to be used for resolving a relative systemId to an absolute URI
   */
  @Override
  public String getBaseURI() {
    return baseURI;
  }

  /**
   * Set the base URI to be used for resolving a relative systemId to an absolute URI.
   *
   * @param baseURI the base URI to be used for resolving a relative systemId to an absolute URI
   */
  @Override
  public void setBaseURI(String baseURI) {
    this.baseURI = baseURI;
  }

  /**
   * Returns the <code>InputStream</code> for the input source.
   *
   * @return the <code>InputStream</code> for the input source
   */
  @Override
  public InputStream getByteStream() {
    return new ByteArrayInputStream(data);
  }

  /**
   * Set the byte stream for the input source.
   *
   * @param byteStream the byte stream for the input source
   */
  @Override
  public void setByteStream(InputStream byteStream) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      byte[] buffer = new byte[4096];
      int numberOfBytesRead;

      while ((numberOfBytesRead = byteStream.read(buffer)) != -1) {
        baos.write(buffer, 0, numberOfBytesRead);
      }

      this.data = baos.toByteArray();
    } catch (Throwable e) {
      throw new XmlSchemaException(
          "Failed to read the data from the byte stream for the input source", e);
    }
  }

  /**
   * Returns <code>true</code> if the input source is certified or <code>false</code> otherwise.
   *
   * @return <code>true</code> if the input source is certified or <code>false</code> otherwise
   */
  @Override
  public boolean getCertifiedText() {
    return certifiedText;
  }

  /**
   * Set whether the input source is certified.
   *
   * @param certifiedText <code>true</code> if the input source is certified or <code>false</code>
   *                      otherwise
   */
  @Override
  public void setCertifiedText(boolean certifiedText) {
    this.certifiedText = certifiedText;
  }

  /**
   * Returns the character stream for the input source.
   *
   * @return the character stream for the input source
   */
  @Override
  public Reader getCharacterStream() {
    return null;
  }

  /**
   * Set the character stream for the input source
   *
   * @param characterStream the character stream for the input source
   */
  @Override
  public void setCharacterStream(Reader characterStream) {
    throw new XmlSchemaException(
        "Reading the data for the input source from a character stream is not supported");
  }

  /**
   * Returns the character encoding for the input source.
   *
   * @return the character encoding for the input source
   */
  @Override
  public String getEncoding() {
    return encoding;
  }

  /**
   * Set the character encoding for the input source.
   *
   * @param encoding the character encoding for the input source
   */
  @Override
  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  /**
   * Returns the namespace for this input source.
   *
   * @return the namespace for this input source
   */
  public String getNamespaceURI() {
    return namespaceURI;
  }

  /**
   * Returns the public identifier for this input source.
   *
   * @return the public identifier for this input source
   */
  @Override
  public String getPublicId() {
    return publicId;
  }

  /**
   * Set the public identifier for this input source.
   *
   * @param publicId the public identifier for this input source
   */
  @Override
  public void setPublicId(String publicId) {
    this.publicId = publicId;
  }

  /**
   * Returns the <code>String</code> data for the input source
   *
   * @return the <code>String</code> data for the input source
   */
  @Override
  public String getStringData() {
    return null;
  }

  /**
   * Set the <code>String</code> data for the input source.
   *
   * @param stringData the <code>String</code> data for the input source
   */
  @Override
  public void setStringData(String stringData) {
    throw new XmlSchemaException(
        "Reading the data for the input source from a string is not supported");
  }

  /**
   * Returns the system identifier for this input source.
   *
   * @return the system identifier for this input source
   */
  @Override
  public String getSystemId() {
    return systemId;
  }

  /**
   * Set the system identifier for this input source.
   *
   * @param systemId the system identifier for this input source
   */
  @Override
  public void setSystemId(String systemId) {
    this.systemId = systemId;
  }
}
