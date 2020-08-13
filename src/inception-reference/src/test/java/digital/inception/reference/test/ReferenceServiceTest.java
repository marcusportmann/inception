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

package digital.inception.reference.test;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.reference.IReferenceService;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ReferenceServiceTest</code> class contains the implementation of the JUnit tests for
 * the <code>ReferenceService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@TestExecutionListeners(
    listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class
    })
public class ReferenceServiceTest {

  /**
   * The Reference Service.
   */
  @Autowired
  private IReferenceService referenceService;

  /**
   * Test the reference functionality.
   */
  @Test
  public void referenceTest() throws Exception {
  }
}
