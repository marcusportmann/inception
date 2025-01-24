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

package digital.inception.config.test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import digital.inception.config.model.Config;
import digital.inception.config.model.ConfigSummary;
import digital.inception.config.service.ConfigService;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * The <b>ConfigServiceTest</b> class contains the JUnit tests for the <b>ConfigService</b> class.
 *
 * @author Marcus Portmann
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(InceptionExtension.class)
@ContextConfiguration(
    classes = {TestConfiguration.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class ConfigServiceTest {

  private static final String TEST_BINARY_ID = "TestBinaryId";

  private static final byte[] TEST_BINARY_UPDATED_VALUE = "TestBinaryUpdatedValue".getBytes();

  private static final byte[] TEST_BINARY_VALUE = "TestBinaryValue".getBytes();

  private static final String TEST_BOOLEAN_ID = "TestBooleanId";

  private static final boolean TEST_BOOLEAN_VALUE = true;

  private static final String TEST_CONFIGURATION_ID = "TestConfigId";

  private static final String TEST_CONFIGURATION_VALUE = "TestConfigValue";

  private static final String TEST_DESCRIPTION = "Test Description";

  private static final String TEST_DOUBLE_ID = "TestDoubleId";

  private static final Double TEST_DOUBLE_VALUE = 1234.4321;

  private static final String TEST_FILTERED_ID = "TestFilteredId";

  private static final String TEST_INTEGER_ID = "TestIntegerId";

  private static final Integer TEST_INTEGER_VALUE = 1234;

  private static final String TEST_LONG_ID = "TestLongId";

  private static final Long TEST_LONG_VALUE = 4321L;

  private static final String TEST_STRING_ID = "TestStringId";

  private static final String TEST_STRING_VALUE = "TestStringValue";

  /** The Config Service. */
  @Autowired private ConfigService configService;

  /** Test the <b>Binary</b> config. */
  @Test
  public void binaryConfigTest() throws Exception {
    if (configService.idExists(TEST_BINARY_ID)) {
      fail("Found the Binary config ID (" + TEST_BINARY_ID + ") that should not exist");
    }

    configService.setConfig(TEST_BINARY_ID, TEST_BINARY_VALUE, TEST_DESCRIPTION);

    if (!configService.idExists(TEST_BINARY_ID)) {
      fail("Failed to confirm that the Binary config ID (" + TEST_BINARY_ID + ") exists");
    }

    byte[] value = configService.getBinary(TEST_BINARY_ID);

    assertArrayEquals(
        TEST_BINARY_VALUE,
        value,
        "The required Binary value was not retrieved for the config ID (" + TEST_BINARY_ID + ")");

    configService.setConfig(
        TEST_BINARY_ID, TEST_BINARY_UPDATED_VALUE, TEST_DESCRIPTION + " Updated");

    if (!configService.idExists(TEST_BINARY_ID)) {
      fail("Failed to confirm that the Binary config ID (" + TEST_BINARY_ID + ") exists");
    }

    value = configService.getBinary(TEST_BINARY_ID);

    assertArrayEquals(
        TEST_BINARY_UPDATED_VALUE,
        value,
        "The required updated Binary value was not retrieved for the config ID ("
            + TEST_BINARY_ID
            + ")");

    value = configService.getBinary(TEST_BINARY_ID, new byte[0]);

    assertArrayEquals(
        TEST_BINARY_UPDATED_VALUE,
        value,
        "The required updated Binary value was not retrieved for the config ID ("
            + TEST_BINARY_ID
            + ")");
  }

  /** Test the <b>Boolean</b> config. */
  @Test
  public void booleanConfigTest() throws Exception {
    if (configService.idExists(TEST_BOOLEAN_ID)) {
      fail("Found the Boolean config ID (" + TEST_BOOLEAN_ID + ") that should not exist");
    }

    configService.setConfig(TEST_BOOLEAN_ID, TEST_BOOLEAN_VALUE, TEST_DESCRIPTION);

    if (!configService.idExists(TEST_BOOLEAN_ID)) {
      fail("Failed to confirm that the Boolean config ID (" + TEST_BOOLEAN_ID + ") exists");
    }

    boolean value = configService.getBoolean(TEST_BOOLEAN_ID);

    assertEquals(
        TEST_BOOLEAN_VALUE,
        value,
        "The required Boolean value was not retrieved for the config ID (" + TEST_BOOLEAN_ID + ")");

    boolean booleanValue = configService.getBoolean(TEST_BOOLEAN_ID, false);

    assertEquals(
        TEST_BOOLEAN_VALUE,
        booleanValue,
        "The required double value was not retrieved for the config ID (" + booleanValue + ")");

    configService.setConfig(TEST_BOOLEAN_ID, false, TEST_DESCRIPTION + " Updated");

    if (!configService.idExists(TEST_BOOLEAN_ID)) {
      fail("Failed to confirm that the Boolean config ID (" + TEST_BOOLEAN_ID + ") exists");
    }

    value = configService.getBoolean(TEST_BOOLEAN_ID);

    assertFalse(
        value,
        "The required updated Boolean value was not retrieved for the config ID ("
            + TEST_BOOLEAN_ID
            + ")");
  }

  /** Test the <b>Config</b> config. */
  @Test
  public void configConfigTest() throws Exception {
    if (configService.idExists(TEST_CONFIGURATION_ID)) {
      fail("Found the config ID (" + TEST_BINARY_ID + ") that should not exist");
    }

    Config config = new Config();

    config.setId(TEST_CONFIGURATION_ID);
    config.setValue(TEST_CONFIGURATION_VALUE);
    config.setDescription(TEST_DESCRIPTION);

    configService.setConfig(config.getId(), config.getValue(), config.getDescription());

    Config retrievedConfig = configService.getConfig(TEST_CONFIGURATION_ID);

    compareConfig(config, retrievedConfig);

    config.setValue(TEST_CONFIGURATION_VALUE + " Updated");
    config.setDescription(TEST_DESCRIPTION + " Updated");

    configService.setConfig(config.getId(), config.getValue(), config.getDescription());

    retrievedConfig = configService.getConfig(TEST_CONFIGURATION_ID);

    compareConfig(config, retrievedConfig);
  }

  /** Test the config summaries. */
  @Test
  public void configSummariesTest() throws Exception {
    configService.setConfig(TEST_FILTERED_ID, TEST_STRING_VALUE, TEST_DESCRIPTION);

    if (!configService.idExists(TEST_FILTERED_ID)) {
      fail("Failed to confirm that the config ID (" + TEST_FILTERED_ID + ") exists");
    }

    List<ConfigSummary> filteredConfigSummaries =
        configService.getFilteredConfigSummaries("testfiltered");

    assertEquals(
        1,
        filteredConfigSummaries.size(),
        "The required number of filtered config summaries was not retrieved");

    assertEquals(
        TEST_DESCRIPTION,
        filteredConfigSummaries.get(0).getDescription(),
        "The required filtered config summary description ("
            + TEST_DESCRIPTION
            + ") was not retrieved");
  }

  /** Test the configs. */
  @Test
  public void configsTest() throws Exception {
    List<Config> configs = configService.getConfigs();
  }

  /** Test the <b>Double</b> config. */
  @Test
  public void doubleConfigTest() throws Exception {
    if (configService.idExists(TEST_DOUBLE_ID)) {
      fail("Found the Double config ID (" + TEST_DOUBLE_ID + ") that should not exist");
    }

    configService.setConfig(TEST_DOUBLE_ID, TEST_DOUBLE_VALUE, TEST_DESCRIPTION);

    if (!configService.idExists(TEST_DOUBLE_ID)) {
      fail("Failed to confirm that the Double config ID (" + TEST_DOUBLE_ID + ") exists");
    }

    Double value = configService.getDouble(TEST_DOUBLE_ID);

    assertEquals(
        TEST_DOUBLE_VALUE,
        value,
        0.0,
        "The required Double value was not retrieved for the config ID (" + TEST_DOUBLE_ID + ")");

    double doubleValue = configService.getDouble(TEST_DOUBLE_ID, 666.666);

    assertEquals(
        TEST_DOUBLE_VALUE,
        doubleValue,
        0.0,
        "The required double value was not retrieved for the config ID (" + doubleValue + ")");

    configService.setConfig(TEST_DOUBLE_ID, TEST_DOUBLE_VALUE + 1.1, TEST_DESCRIPTION + " Updated");

    if (!configService.idExists(TEST_DOUBLE_ID)) {
      fail("Failed to confirm that the Double config ID (" + TEST_DOUBLE_ID + ") exists");
    }

    value = configService.getDouble(TEST_DOUBLE_ID);

    assertEquals(
        TEST_DOUBLE_VALUE + 1.1,
        value,
        0.0,
        "The required updated Double value was not retrieved for the config ID ("
            + TEST_DOUBLE_ID
            + ")");
  }

  /** Test the filtered configs. */
  @Test
  public void filteredConfigsTest() throws Exception {
    configService.setConfig(TEST_FILTERED_ID, TEST_STRING_VALUE, TEST_DESCRIPTION);

    if (!configService.idExists(TEST_FILTERED_ID)) {
      fail("Failed to confirm that the config ID (" + TEST_FILTERED_ID + ") exists");
    }

    List<Config> filteredConfigs = configService.getFilteredConfigs("testfiltered");

    assertEquals(
        1, filteredConfigs.size(), "The required number of filtered configs was not retrieved");

    assertEquals(
        TEST_STRING_VALUE,
        filteredConfigs.get(0).getValue(),
        "The required filtered configs (" + TEST_STRING_VALUE + ") was not retrieved");
  }

  /** Test the <b>Integer</b> config. */
  @Test
  public void integerConfigTest() throws Exception {
    if (configService.idExists(TEST_INTEGER_ID)) {
      fail("Found the Integer config ID (" + TEST_INTEGER_ID + ") that should not exist");
    }

    configService.setConfig(TEST_INTEGER_ID, TEST_INTEGER_VALUE, TEST_DESCRIPTION);

    if (!configService.idExists(TEST_INTEGER_ID)) {
      fail("Failed to confirm that the Integer config ID (" + TEST_INTEGER_ID + ") exists");
    }

    Integer value = configService.getInteger(TEST_INTEGER_ID);

    assertEquals(
        TEST_INTEGER_VALUE,
        value,
        "The required Integer value was not retrieved for the Integer config ID ("
            + TEST_INTEGER_ID
            + ")");

    int integerValue = configService.getInteger(TEST_INTEGER_ID, 666);

    assertEquals(
        (int) TEST_INTEGER_VALUE,
        integerValue,
        "The required integer value was not retrieved for the Integer config ID ("
            + TEST_INTEGER_ID
            + ")");

    configService.setConfig(TEST_INTEGER_ID, TEST_INTEGER_VALUE + 1, TEST_DESCRIPTION + " Updated");

    if (!configService.idExists(TEST_INTEGER_ID)) {
      fail("Failed to confirm that the Integer config ID (" + TEST_INTEGER_ID + ") exists");
    }

    value = configService.getInteger(TEST_INTEGER_ID);

    assertEquals(
        TEST_INTEGER_VALUE + 1,
        value.intValue(),
        "The required updated Integer value was not retrieved for the config ID ("
            + TEST_INTEGER_ID
            + ")");
  }

  /** Test the <b>Long</b> config. */
  @Test
  public void longConfigTest() throws Exception {
    if (configService.idExists(TEST_LONG_ID)) {
      fail("Found the Long config ID (" + TEST_LONG_ID + ") that should not exist");
    }

    configService.setConfig(TEST_LONG_ID, TEST_LONG_VALUE, TEST_DESCRIPTION);

    if (!configService.idExists(TEST_LONG_ID)) {
      fail("Failed to confirm that the Long config ID (" + TEST_LONG_ID + ") exists");
    }

    Long value = configService.getLong(TEST_LONG_ID);

    assertEquals(
        TEST_LONG_VALUE,
        value,
        "The required Long value was not retrieved for the Long config ID (" + TEST_LONG_ID + ")");

    Long longValue = configService.getLong(TEST_LONG_ID, 666);

    assertEquals(
        TEST_LONG_VALUE,
        longValue,
        "The required Long value was not retrieved for the Long config ID (" + TEST_LONG_ID + ")");

    configService.setConfig(TEST_LONG_ID, TEST_LONG_VALUE + 1, TEST_DESCRIPTION + " Updated");

    if (!configService.idExists(TEST_LONG_ID)) {
      fail("Failed to confirm that the Long config ID (" + TEST_LONG_ID + ") exists");
    }

    value = configService.getLong(TEST_LONG_ID);

    assertEquals(
        TEST_LONG_VALUE + 1L,
        value.longValue(),
        "The required updated Long value was not retrieved for the config ID ("
            + TEST_LONG_ID
            + ")");
  }

  /** Test the <b>String</b> config. */
  @Test
  public void stringConfigTest() throws Exception {
    if (configService.idExists(TEST_STRING_ID)) {
      fail("Found the String config ID (" + TEST_STRING_ID + ") that should not exist");
    }

    configService.setConfig(TEST_STRING_ID, TEST_STRING_VALUE, TEST_DESCRIPTION);

    if (!configService.idExists(TEST_STRING_ID)) {
      fail("Failed to confirm that the String config ID (" + TEST_STRING_ID + ") exists");
    }

    String value = configService.getString(TEST_STRING_ID);

    assertEquals(
        TEST_STRING_VALUE,
        value,
        "The required String value was not retrieved for the String config ID ("
            + TEST_STRING_ID
            + ")");

    configService.setConfig(
        TEST_STRING_ID, TEST_STRING_VALUE + "Updated", TEST_DESCRIPTION + " Updated");

    if (!configService.idExists(TEST_STRING_ID)) {
      fail("Failed to confirm that the String config ID (" + TEST_STRING_ID + ") exists");
    }

    value = configService.getString(TEST_STRING_ID);

    assertEquals(
        TEST_STRING_VALUE + "Updated",
        value,
        "The required updated String value was not retrieved for the config ID ("
            + TEST_STRING_ID
            + ")");

    configService.deleteConfig(TEST_STRING_ID);

    if (configService.idExists(TEST_STRING_ID)) {
      fail("Failed to confirm that the String config ID (" + TEST_STRING_ID + ") does not exist");
    }

    value = configService.getString(TEST_STRING_ID, "DEFAULT VALUE");

    if (!value.equals("DEFAULT VALUE")) {
      fail(
          "Failed to retrieve the default value for the String config ID (" + TEST_STRING_ID + ")");
    }
  }

  private void compareConfig(Config config1, Config config2) {
    assertEquals(config1.getId(), config2.getId(), "The ID values for the codes do not match");
    assertEquals(
        config1.getValue(), config2.getValue(), "The value values for the codes do not match");
    assertEquals(
        config1.getDescription(),
        config2.getDescription(),
        "The description values for the codes do not match");
  }
}
