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

import digital.inception.kafka.*;
import java.util.Map;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Component;

/**
 * The {@code TestProcessor} class.
 *
 * @author Marcus Portmann
 */
@Component
public class TestProcessor extends Processor<String, String> {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(TestProcessor.class);

  /**
   * Constructs a new <code>TestProcessor</code>.
   *
   * @param kafkaProperties the Spring Kafka properties
   */
  public TestProcessor(KafkaProperties kafkaProperties) {
    super(kafkaProperties, new StringDeserializer(), new StringDeserializer());
  }

  @Override
  protected String getTopic() {
    return "test";
  }

  @Override
  protected void handleInvalidRecordValue(String value) throws Exception {
    log.error("Failed to process the invalid record value: " + value);
  }

  @Override
  protected void handleRecordProcessingFailure(String value) throws Exception {
    log.error("Failed to process the record value: " + value);
  }

  @Override
  protected void processRecord(Map<String, byte[]> headers, String value)
      throws InvalidRecordValueException, TransientErrorException, ProcessingFailedException {
    headers.forEach(
        (headerKey, headerValue) -> {
          log.info("Found the record header: " + headerKey + " = " + new String(headerValue));
        });
    log.info("Processing the record value: " + value);
  }
}
