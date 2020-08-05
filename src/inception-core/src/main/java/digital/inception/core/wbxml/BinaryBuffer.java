/*
 * Copyright 2020 Marcus Portmann
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * The <code>BinaryBuffer</code> class provides the capabilities similar to the <code>StringBuffer
 * </code> class when working with binary data (bytes).
 *
 * @author Marcus Portmann
 */
public class BinaryBuffer implements Serializable {

  private static final long serialVersionUID = 1000000;

  private ByteArrayOutputStream stream = null;

  /**
   * Constructs a new empty <code>BinaryBuffer</code>.
   */
  public BinaryBuffer() {
    stream = new ByteArrayOutputStream();
  }

  /**
   * Constructs a new <code>BinaryBuffer</code> containing the specified binary data.
   *
   * @param data the binary data to initialize the <code>BinaryBuffer</code> with
   */
  public BinaryBuffer(byte[] data) {
    stream = new ByteArrayOutputStream(data.length);

    try {
      stream.write(data);
    } catch (IOException ignored) {
    }
  }

  /**
   * Append the binary data stored in the specified <code>BinaryBuffer</code> instance to this
   * <code>BinaryBuffer</code>.
   *
   * @param buffer the existing <code>BinaryBuffer</code> containing the binary data to append
   */
  public void append(BinaryBuffer buffer) {
    append(buffer.getData());
  }

  /**
   * Append the specified binary data to the binary data already stored in the <code>BinaryBuffer
   * </code>.
   *
   * @param data the binary data to append
   */
  public void append(byte[] data) {
    try {
      stream.write(data);
    } catch (IOException ignored) {
    }
  }

  /**
   * Empty the <code>BinaryBuffer</code> removing any binary data stored by the buffer.
   */
  public void empty() {
    stream.reset();
  }

  /**
   * Returns the binary data stored by the <code>BinaryBuffer</code>.
   *
   * @return the binary data stored by the <code>BinaryBuffer</code>
   */
  public byte[] getData() {
    return stream.toByteArray();
  }

  /**
   * Returns the length of the binary data stored by the <code>BinaryBuffer</code>.
   *
   * @return the length of the binary data stored by the <code>BinaryBuffer</code>
   */
  public int getLength() {
    return stream.size();
  }

  /**
   * Append the specified byte to the binary data already stored in the <code>BinaryBuffer</code>.
   *
   * @param b the byte to append
   */
  void append(int b) {
    stream.write(b);
  }
}
