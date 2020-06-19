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

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;

/**
 * The <code>Opaque</code> class stores the data for an opaque (binary data) content type in a WBXML
 * document.
 * <p/>
 * This content type is used to store binary data.
 *
 * @author Marcus Portmann
 */
public class Opaque
    implements Serializable, Content {

  private static final long serialVersionUID = 1000000;
  private BinaryBuffer buffer = null;

  /**
   * Constructs a new empty <code>Opaque</code>.
   */
  public Opaque() {
    buffer = new BinaryBuffer();
  }

  /**
   * Constructs a new <code>Opaque</code> containing the specified binary data.
   *
   * @param data the binary data to populate the <code>Opaque</code> instance with
   */
  public Opaque(byte[] data) {
    buffer = new BinaryBuffer(data);
  }

  /**
   * Append the specified binary data to the binary data already contained in the
   * <code>Opaque</code> instance.
   *
   * @param data the binary data to add to the opaque instance
   */
  public void append(byte[] data) {
    buffer.append(data);
  }

  /**
   * Append the binary data, in the specified <code>Opaque</code> instance, to the binary data
   * already contained in the <code>Opaque</code> instance.
   *
   * @param opaque the existing <code>Opaque</code> instance containing the binary data to add
   */
  public void append(Opaque opaque) {
    buffer.append(opaque.buffer);
  }

  /**
   * Returns the binary data for the <code>Opaque</code> instance.
   *
   * @return the binary data for the <code>Opaque</code> instance
   */
  public byte[] getData() {
    return buffer.getData();
  }

  /**
   * Returns the length of the binary data for the <code>Opaque</code> instance.
   *
   * @return the length of the binary data for the <code>Opaque</code> instance
   */
  public int getLength() {
    return buffer.getLength();
  }

  /**
   * Print the content using the specified indent level.
   *
   * @param indent the indent level
   */
  public void print(int indent) {
    print(System.out, indent);
  }

  /**
   * Print the content to the specified <code>OutputStream</code> using the specified indent level.
   *
   * @param out    the <code>OuputStream</code> to output the content to
   * @param indent the indent level
   */
  public void print(OutputStream out, int indent) {
    PrintStream pout = new PrintStream(out);

    pout.print("[BINARY: " + buffer.getLength() + " bytes]");

    // don't close pout - it will close out (the underlying outputstream)
    // see API - PrintStream.close().
  }

  /**
   * @return the string representation of the <code>Opaque</code> instance
   *
   * @see Object#toString()
   */
  @Override
  public String toString() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      print(baos, 0);

      String result = new String(baos.toByteArray());

      baos.close();

      return result;
    } catch (Exception e) {
      return super.toString();
    }
  }
}
