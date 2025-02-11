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

package digital.inception.core.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import digital.inception.core.util.MimeData;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * The <b>MimeDataTests</b> class.
 *
 * @author Marcus Portmann
 */
public class MimeDataTests {

  /** Test the MimeData class. */
  @Test
  public void mimeDataTest() {
    MimeData mimeData =
        new MimeData("text/plain", "This is a simple text string".getBytes(StandardCharsets.UTF_8));

    assertTrue(mimeData.isMimeType("text/*"));
    assertTrue(mimeData.isMimeType("text/plain"));
    assertTrue(mimeData.isMimeType("Text/Plain"));
    assertFalse(mimeData.isMimeType("text/html"));

    String hash = mimeData.getHash();

    assertEquals("tQ7lUmHxUH1UunnEQoiv4TkjEdCRooh8s+Y6ILqwD+8=", hash);
  }
}
