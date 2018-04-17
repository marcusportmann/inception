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

package digital.inception.sms;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.test.TestClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * The <code>SMSServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>SMSService</code> class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@RunWith(TestClassRunner.class)
@ContextConfiguration(classes = { SMSTestConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
@BootstrapWith(SpringBootTestContextBootstrapper.class)
public class SMSServiceTest
{
  /* SMS Service */
  @Autowired
  private ISMSService smsService;

  /**
   * Test.
   */
  @Test
  public void test()
    throws Exception
  {
    //smsService.sendSMSSynchronously(1, "0832763107", "Testing 3.. 2.. 1..");
  }
}
