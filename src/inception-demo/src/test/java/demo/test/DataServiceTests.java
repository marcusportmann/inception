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

package demo.test;

import static org.junit.jupiter.api.Assertions.assertFalse;

import demo.model.Data;
import demo.service.DataService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * The {@code DataServiceTests} class.
 *
 * @author Marcus Portmann
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class DataServiceTests {

  /** The Data Service. */
  @Autowired private DataService dataService;

  /** Test the data functionality. */
  @Test
  public void dataTest() throws Exception {
    List<Data> allData = dataService.getAllData();

    assertFalse(allData.isEmpty());
  }

  /** Test the reactive data functionality. */
  @Test
  public void reactiveDataTest() throws Exception {
    dataService.getAllReactiveData().toStream().forEach(System.out::println);
  }
}
