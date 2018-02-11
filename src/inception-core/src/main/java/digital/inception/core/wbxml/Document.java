/*
 * Copyright 2018 Marcus Portmann
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

//~--- JDK imports ------------------------------------------------------------

import java.io.OutputStream;
import java.io.Serializable;

/**
 * The <code>Document</code> class represents a WBXML document.
 *
 * @author Marcus Portmann
 */
public class Document
  implements Serializable
{
  /**
   * The Public ID for an Unknown WBXML document type.
   */
  public static final int PUBLIC_ID_UNKNOWN = 0x01;
  private static final long serialVersionUID = 1000000;
  private int publicId = PUBLIC_ID_UNKNOWN;
  private Element rootElement = null;

  /**
   * Constructs a new <code>Document</code> with the specified root element.
   *
   * @param element the root element for the document
   */
  public Document(Element element)
  {
    rootElement = element;
  }

  /**
   * Constructs a new <code>Document</code> with the specified root element and public ID.
   *
   * @param element  the root element for the document
   * @param publicId the public ID for the document
   */
  public Document(Element element, int publicId)
  {
    rootElement = element;
    this.publicId = publicId;
  }

  /**
   * Get the public ID for the document.
   *
   * @return the public ID for the document
   */
  public int getPublicId()
  {
    return publicId;
  }

  /**
   * Get the root element for the document.
   *
   * @return the root element for the document
   */
  public Element getRootElement()
  {
    return rootElement;
  }

  /**
   * Print the document.
   */
  public void print()
  {
    rootElement.print(System.out, 0);
  }

  /**
   * Print the document to the specified <code>OutputStream</code>.
   *
   * @param out the <code>OuputStream</code> to output the document to
   */
  public void print(OutputStream out)
  {
    rootElement.print(out, 0);
  }

  /**
   * Returns the string representation of the document.
   *
   * @return the string representation of the document
   */
  @Override
  public String toString()
  {
    return rootElement.toString();
  }
}
