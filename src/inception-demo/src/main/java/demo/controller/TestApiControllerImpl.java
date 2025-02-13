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

package demo.controller;

import demo.model.CarType;
import demo.task.DemoTaskData;
import digital.inception.api.SecureApiController;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.executor.service.ExecutorService;
import java.time.OffsetDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * The <b>TestApiControllerImpl</b> class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused"})
public class TestApiControllerImpl extends SecureApiController implements TestApiController {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(TestApiControllerImpl.class);

  private final ExecutorService executorService;

  private final WebClient.Builder webClientBuilder;

  /**
   * Constructs a new <b>TestApiControllerImpl</b>.
   *
   * @param applicationContext the Spring application context
   * @param webClientBuilder the web client builder
   * @param executorService the Executor Service
   */
  public TestApiControllerImpl(
      ApplicationContext applicationContext,
      WebClient.Builder webClientBuilder,
      ExecutorService executorService) {
    super(applicationContext);
    this.webClientBuilder = webClientBuilder;
    this.executorService = executorService;
  }

  @Override
  public void testApiCall() throws ServiceUnavailableException {
    try {
      WebClient webClient = webClientBuilder.build();

      String enumValue =
          webClient
              .get()
              .uri("http://localhost:8080/api/test/test-returning-enum")
              .retrieve()
              .bodyToMono(String.class)
              .block();

      log.info("Retrieved the enum value: " + enumValue);

    } catch (Throwable e) {
      log.error("Failed to invoke the API", e);
    }
  }

  @Override
  public void testExceptionHandling() throws ServiceUnavailableException {
    throw new ServiceUnavailableException(
        "This is a longer description for the test exception number " + System.currentTimeMillis());
  }

  @Override
  public OffsetDateTime testOffsetDateTime(OffsetDateTime offsetDateTime) {
    System.out.println("offsetDateTime = " + offsetDateTime);

    return offsetDateTime;
  }

  @Override
  public void testPolicyDecisionPointAuthorization(
      String pathVariable, String anotherPathVariable, String requestParameter)
      throws InvalidArgumentException, ServiceUnavailableException {
    if (!StringUtils.hasText(pathVariable)) {
      throw new InvalidArgumentException("pathVariable");
    }

    if (!StringUtils.hasText(anotherPathVariable)) {
      throw new InvalidArgumentException("anotherPathVariable");
    }

    log.info("pathVariable = " + pathVariable);
    log.info("anotherPathVariable = " + anotherPathVariable);
    log.info("requestParameter = " + requestParameter);

    try {
      StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

      if (stackTraceElements.length > 2) {}

    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the API and request information using reflection", e);
    }
  }

  @Override
  public CarType testReturningEnum() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
      log.info("Processing secure API call for authenticated user: " + authentication.getName());
    }

    return CarType.PETROL;
  }

  @Override
  public String testReturningString(Boolean throwException) throws ServiceUnavailableException {
    if ((throwException != null) && (throwException)) {
      throw new ServiceUnavailableException("This is a test exception");
    } else {
      return "This is a test string";
    }
  }

  @Override
  public void testTaskExecution(Boolean slowTask) throws ServiceUnavailableException {
    try {
      DemoTaskData demoTaskData = new DemoTaskData("This is a test message.");

      if ((slowTask != null) && (slowTask)) {
        demoTaskData.setSlowTask(true);
      }

      executorService.queueTask("demo_task", demoTaskData);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to execute the Demo Task");
    }
  }
}
