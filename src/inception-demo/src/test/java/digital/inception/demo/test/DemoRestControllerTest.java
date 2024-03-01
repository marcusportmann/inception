/// *
// * Copyright Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
// package digital.inception.demo.test;
//
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
// import digital.inception.demo.rs.DemoRestController;
// import digital.inception.test.TestClassRunner;
// import digital.inception.test.TestConfiguration;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
// import org.springframework.http.MediaType;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
// import org.springframework.test.context.ContextConfiguration;
// import org.springframework.test.context.TestExecutionListeners;
// import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
// import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
// import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
// import org.springframework.test.web.servlet.MockMvc;
//
/// **
// * The <b>DemoRestControllerTest</b> class.
// *
// * @author Marcus Portmann
// */
// @RunWith(TestClassRunner.class)
// @ContextConfiguration(
//    classes = {TestConfiguration.class},
//    initializers = {ConfigDataApplicationContextInitializer.class})
// @TestExecutionListeners(
//    listeners = {
//      DependencyInjectionTestExecutionListener.class,
//      DirtiesContextTestExecutionListener.class,
//      TransactionalTestExecutionListener.class
//    })
// @WebMvcTest(DemoRestController.class)
// @AutoConfigureMockMvc(addFilters = false)
//// @BootstrapWith(SpringBootTestContextBootstrapper.class)
// public class DemoRestControllerTest {
//
//  @Autowired private MockMvc mvc;
//
//  @Test
//  public void allDataTest() throws Exception {
//
//    mvc.perform(get("/api/demo/all-data").accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isOk());
//
//    WebSecurityConfiguration xxx;
//  }
// }
