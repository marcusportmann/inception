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

package digital.inception.messaging.test;

// ~--- non-JDK imports --------------------------------------------------------

import static org.junit.Assert.assertArrayEquals;

import digital.inception.core.util.CryptoUtil;
import digital.inception.messaging.MessageTranslator;
import org.junit.Test;

/**
 * The <code>MessagingServiceTest</code> class contains the implementation of the JUnit tests for
 * the <code>MessageTranslator</code> class.
 *
 * @author Marcus Portmann
 */
public class MessageTranslatorTest {

  /**
   * Test the encryption and decryption functionality.
   */
  @Test
  public void encryptionTest() throws Exception {
    byte[] encryptionIV = CryptoUtil.createRandomEncryptionIV(CryptoUtil.AES_BLOCK_SIZE);

    byte[] encryptionKey = CryptoUtil.passwordToAESKey("ThisIsAPassword");

    byte[] data = "Hello World".getBytes();

    byte[] encryptedData = MessageTranslator.encryptMessageData(encryptionKey, encryptionIV, data);

    byte[] decryptedData =
        MessageTranslator.decryptMessageData(encryptionKey, encryptionIV, encryptedData);

    assertArrayEquals(data, decryptedData);
  }
}
