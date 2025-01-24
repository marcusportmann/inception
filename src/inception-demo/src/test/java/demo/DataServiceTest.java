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

package digital.inception.demo.test;

import digital.inception.demo.DemoConfiguration;
import digital.inception.demo.service.DataService;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * The <b>DataServiceTest</b> class.
 *
 * @author Marcus Portmann
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(InceptionExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class, DemoConfiguration.class})
public class DataServiceTest {

  /** The Data Service. */
  @Autowired private DataService dataService;

  /** Test the reactive data functionality. */
  @Test
  public void reactiveDataTest() throws Exception {
    dataService.getAllReactiveData().toStream().forEach(System.out::println);
  }
}
