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
import static org.junit.jupiter.api.Assertions.assertTrue;

import digital.inception.core.util.MobilePhoneNumberUtil;
import org.junit.jupiter.api.Test;

/**
 * The {@code MobilePhoneNumberUtilTests} class.
 *
 * @author Marcus Portmann
 */
public class MobilePhoneNumberUtilTests {

  /** Test the MobilePhoneNumberUtil class. */
  @Test
  public void mobilePhoneNumberUtilTest() {
    assertEquals(
        "27832763107",
        MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("+27 (0) 83 276 3107", "27"));
    assertEquals(
        "27832763107", MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("0832763107", "27"));
    assertEquals(
        "27832763107", MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("083 276 3107", "27"));
    assertEquals(
        "27832763107", MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("083-276-3107", "27"));
    assertEquals(
        "27832763107", MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("+27832763107", "27"));
    assertEquals(
        "27832763107", MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("+27 83 276 3107", "27"));
    assertEquals(
        "27832763107", MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("27832763107", "27"));
    assertEquals(
        "3655002709", MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("+36 55 002 709", "27"));
    assertEquals(
        "353209106402", MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("+353 20 910 6402", "27"));
    assertEquals(
        "12025550197", MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("+1-202-555-0197", "27"));
    assertEquals(
        "12025550197", MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("+1-202-555-0197", "27"));

    assertTrue(
        MobilePhoneNumberUtil.isValidMSISDN(
            MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("+27 (0) 83 276 3107", "27")));
    assertTrue(
        MobilePhoneNumberUtil.isValidMSISDN(
            MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("0832763107", "27")));
    assertTrue(
        MobilePhoneNumberUtil.isValidMSISDN(
            MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("083 276 3107", "27")));
    assertTrue(
        MobilePhoneNumberUtil.isValidMSISDN(
            MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("083-276-3107", "27")));
    assertTrue(
        MobilePhoneNumberUtil.isValidMSISDN(
            MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("+27832763107", "27")));
    assertTrue(
        MobilePhoneNumberUtil.isValidMSISDN(
            MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("+27 83 276 3107", "27")));
    assertTrue(
        MobilePhoneNumberUtil.isValidMSISDN(
            MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("27832763107", "27")));
    assertTrue(
        MobilePhoneNumberUtil.isValidMSISDN(
            MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("+36 55 002 709", "27")));
    assertTrue(
        MobilePhoneNumberUtil.isValidMSISDN(
            MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("+353 20 910 6402", "27")));
    assertTrue(
        MobilePhoneNumberUtil.isValidMSISDN(
            MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("+1-202-555-0197", "27")));
    assertTrue(
        MobilePhoneNumberUtil.isValidMSISDN(
            MobilePhoneNumberUtil.mobilePhoneNumberToMSISDN("+1-202-555-0197", "27")));
  }
}
