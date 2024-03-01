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

package digital.inception.kafka.test;

import digital.inception.kafka.Processor;
import digital.inception.kafka.ProcessorApplication;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * The <b>KafkaConsumerTest</b> class.
 *
 * @author Marcus Portmann
 */
@SpringBootApplication
public class KafkaConsumerTest extends ProcessorApplication {

  /**
   * Constructs a new <b>KafkaConsumerTest</b>.
   *
   * @param applicationContext the Spring application context
   */
  public KafkaConsumerTest(ApplicationContext applicationContext) {
    super(applicationContext);
  }

  /**
   * The main method.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    try {
      SpringApplication application = new SpringApplication(KafkaConsumerTest.class);
      application.setWebApplicationType(WebApplicationType.NONE);
      application.run(args);
    } catch (Throwable e) {
      System.err.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }

  @Override
  protected List<Class<? extends Processor<?, ?>>> getProcessorClasses() {
    List<Class<? extends Processor<?, ?>>> processors = new ArrayList<>();

    processors.add(TestProcessor.class);

    return processors;
  }
}
