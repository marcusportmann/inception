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

package demo;

import demo.service.IDataService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * The <b>DataServiceTest</b> class.
 *
 * @author Marcus Portmann
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {TestConfiguration.class, DemoConfiguration.class})
@Disabled
public class DataServiceTest {

  /** The Data Service. */
  @Autowired private IDataService dataService;

  /** Test the reactive data functionality. */
  @Test
  public void reactiveDataTest() throws Exception {
    dataService.getAllReactiveData().toStream().forEach(System.out::println);
  }
}
