/*
 * Copyright (c) Discovery Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Discovery Ltd
 * ("Confidential Information"). It may not be copied or reproduced in any manner
 * without the express written permission of Discovery Ltd.
 */

package digital.inception.application.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import digital.inception.core.jdbc.IdGenerator;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
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
 * The {@code IdGeneratorTests} class contains the JUnit tests for the {@code IdGenerator} class.
 *
 * @author Marcus Portmann
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(InceptionExtension.class)
@ContextConfiguration(
    classes = {TestConfiguration.class, ApplicationTestConfiguration.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class IdGeneratorTests {

  @Autowired private IdGenerator idGenerator;

  /** testIdGeneration */
  @Test
  public void testIdGeneration() throws Exception {
    assertEquals(1, idGenerator.nextId("TestName"));
    assertEquals(2, idGenerator.nextId("TestName"));
  }
}
