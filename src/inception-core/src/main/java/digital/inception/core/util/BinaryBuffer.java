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

package digital.inception.core.util;

/**
 * The <code>BinaryBuffer</code> class manages binary data. It provides the capabilities similar to
 * the <code>StringBuffer</code> class when working with binary data (bytes).
 *
 * <p>This class is thread-safe.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class BinaryBuffer implements java.io.Serializable {

  private static final long serialVersionUID = 1000000;
  private byte[] data;

  /**
   * Constructs a new <code>BinaryBuffer</code> and initializes it using the specified binary data.
   *
   * @param data <code>BinaryBuffer</code> instance containing the binary data that will be copied
   *     and managed by the <code>BinaryBuffer</code> instance
   */
  public BinaryBuffer(BinaryBuffer data) {
    this(data.getData());
  }

  /**
   * Constructs a new <code>BinaryBuffer</code> and initializes it using the specified binary data.
   *
   * @param data the binary data that will be copied and managed by the <code>BinaryBuffer</code>
   *     instance
   */
  public BinaryBuffer(byte[] data) {
    this.data = new byte[data.length];
    System.arraycopy(data, 0, this.data, 0, data.length);
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
  public synchronized void append(byte[] data) {
    byte[] currentData = this.data;
    byte[] newData = new byte[currentData.length + data.length];

    System.arraycopy(currentData, 0, newData, 0, currentData.length);
    System.arraycopy(data, 0, newData, currentData.length, data.length);
    this.data = newData;
  }

  /** Clears the <code>BinaryBuffer</code>. */
  public void clear() {
    this.data = new byte[0];
  }

  /**
   * Returns the binary data managed by the <code>BinaryBuffer</code>.
   *
   * @return the binary data managed by the <code>BinaryBuffer</code>
   */
  public byte[] getData() {
    return data;
  }

  /**
   * Returns the amount of binary data managed by the <code>BinaryBuffer</code>.
   *
   * @return the amount of binary data managed by the <code>BinaryBuffer</code>
   */
  public int length() {
    return data.length;
  }
}
