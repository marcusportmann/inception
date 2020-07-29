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

package digital.inception.sample;

import digital.inception.codes.CodeCategory;
import digital.inception.codes.CodesService;
import digital.inception.codes.ICodesService;
import digital.inception.ws.security.WebServiceClientSecurityHelper;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The <code>SampleClient</code> class.
 *
 * @author Marcus Portmann
 */
@SpringBootApplication
public class SpringBootSampleClient {

  /** The Codes Service endpoint. */
  public static final String CODES_SERVICE_ENDPOINT = "http://localhost:8080/service/CodesService";

  /** The path to the classpath resource containing the WSDL for the Codes Service. */
  public static final String CODES_SERVICE_WSDL = "META-INF/wsdl/CodesService.wsdl";

  /** Constructs a new <code>SampleClient</code>. */
  public SpringBootSampleClient() {}

  /**
   * The main method.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(SpringBootSampleClient.class, args);
  }

  /** Test the Codes Service. */
  @PostConstruct
  protected void testCodesService() {
    try {
      ICodesService codesService =
          WebServiceClientSecurityHelper.getServiceProxy(
              CodesService.class, ICodesService.class, CODES_SERVICE_WSDL, CODES_SERVICE_ENDPOINT);

      List<CodeCategory> codeCategories = codesService.getCodeCategories();

      for (CodeCategory codeCategory : codeCategories) {
        System.out.println(codeCategory.getName());
      }
    } catch (Throwable e) {
      System.err.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }
}
