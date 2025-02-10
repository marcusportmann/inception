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

package digital.inception.demo.client;

import digital.inception.codes.CodeCategory;
import digital.inception.codes.CodesService;
import digital.inception.codes.ICodesService;
import digital.inception.core.util.CryptoUtil;
import digital.inception.demo.api.client.*;
import digital.inception.ws.security.WebServiceClientSecurityHelper;
import jakarta.annotation.PostConstruct;
import java.security.KeyStore;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The <b>SpringBootDemoClient</b> class.
 *
 * @author Marcus Portmann
 */
@SpringBootApplication
public class SpringBootDemoClient {

  /** The Codes Service endpoint. */
  public static final String CODES_SERVICE_ENDPOINT = "http://localhost:8080/service/CodesService";

  /** The path to the classpath resource containing the WSDL for the Codes Service. */
  public static final String CODES_SERVICE_WSDL = "META-INF/wsdl/CodesService.wsdl";

  /** Enable the web services security X509 certificate token profile for the demo client. */
  private static final boolean DEMO_CLIENT_ENABLE_X509_CERTIFICATE_TOKEN_PROFILE = false;

  /** The keystore alias for the demo client. */
  private static final String DEMO_CLIENT_KEYSTORE_ALIAS = "demo-client";

  /** The keystore password for the demo client. */
  private static final String DEMO_CLIENT_KEYSTORE_PASSWORD = "Password1";

  /** The keystore path for the demo client. */
  private static final String DEMO_CLIENT_KEYSTORE_PATH = "META-INF/demo-client.p12";

  /** The keystore type for the demo client. */
  private static final String DEMO_CLIENT_KEYSTORE_TYPE = "pkcs12";

  /** Constructs a new <b>DemoClient</b>. */
  public SpringBootDemoClient() {}

  /**
   * The main method.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(SpringBootDemoClient.class);
    application.setWebApplicationType(WebApplicationType.NONE);
    application.run(args);
  }

  private static void invokeTestApi() {
    try {
      TestApi testApi = new TestApi();

      OffsetDateTime offsetDateTime = testApi.testOffsetDateTime(OffsetDateTime.now()).block();

      System.out.println("Found time = " + offsetDateTime);
    } catch (Throwable e) {
      System.err.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }

  /** Run the tests. */
  @PostConstruct
  protected void runTests() {
    testCodesService();

    invokeTestApi();
  }

  /** Test the Codes Service. */
  protected void testCodesService() {
    try {
      KeyStore keyStore =
          CryptoUtil.loadKeyStore(
              DEMO_CLIENT_KEYSTORE_TYPE,
              DEMO_CLIENT_KEYSTORE_PATH,
              DEMO_CLIENT_KEYSTORE_PASSWORD,
              DEMO_CLIENT_KEYSTORE_ALIAS);

      ICodesService codesService;

      if (DEMO_CLIENT_ENABLE_X509_CERTIFICATE_TOKEN_PROFILE) {
        codesService =
            WebServiceClientSecurityHelper.getWSSecurityX509CertificateServiceProxy(
                CodesService.class,
                ICodesService.class,
                CODES_SERVICE_WSDL,
                CODES_SERVICE_ENDPOINT,
                keyStore,
                DEMO_CLIENT_KEYSTORE_PASSWORD,
                DEMO_CLIENT_KEYSTORE_ALIAS,
                keyStore);
      } else {
        codesService =
            WebServiceClientSecurityHelper.getServiceProxy(
                CodesService.class,
                ICodesService.class,
                CODES_SERVICE_WSDL,
                CODES_SERVICE_ENDPOINT);
      }
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
