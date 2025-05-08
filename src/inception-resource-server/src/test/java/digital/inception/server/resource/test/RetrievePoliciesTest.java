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

package digital.inception.server.resource.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.PostConstruct;
import java.net.ConnectException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * The {@code RetrievePoliciesTest} class.
 *
 * @author Marcus Portmann
 */
@SpringBootApplication
public class RetrievePoliciesTest {

  /** The policies API endpoint. */
  public static final String POLICIES_API_ENDPOINT = "http://localhost:8080/api/security/policies";

  /** Creates a new {@code RetrievePoliciesTest} instance. */
  public RetrievePoliciesTest() {}

  /**
   * The main method.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(RetrievePoliciesTest.class);
    application.setWebApplicationType(WebApplicationType.NONE);
    application.run(args);
  }

  /** Run the tests. */
  @PostConstruct
  protected void run() {
    try {
      RestTemplate restTemplate = new RestTemplate();

      ResponseEntity<List<ExternalPolicy>> response =
          restTemplate.exchange(
              POLICIES_API_ENDPOINT, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
      List<ExternalPolicy> externalPolicies = response.getBody();

      Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

    } catch (ResourceAccessException e) {
      if ((e.getCause() != null) && ((e.getCause() instanceof ConnectException))) {
        System.out.println(
            "Failed to connect to the external policies API endpoint: " + POLICIES_API_ENDPOINT);
      }

      throw new RuntimeException("Failed to retrieve the external policies", e);
    } catch (Throwable e) {
      throw new RuntimeException("Failed to retrieve the external policies", e);
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private record ExternalPolicy(String id, String version, String name, String type, String data) {}
}
