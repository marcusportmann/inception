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

package digital.inception.kafka;

import io.micrometer.core.instrument.MeterRegistry;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

/**
 * The <code>AvroProcessor</code> class implements the base class for a thread that retrieves and
 * processes Avro records from an Apache Kafka topic.
 *
 * @param <K> the key type
 * @param <V> the value type
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public abstract class AvroProcessor<K, V extends SpecificRecordBase> extends Processor<K, V> {

  /**
   * Constructs a new <code>AvroProcessor</code>.
   *
   * @param kafkaProperties the Spring Kafka properties
   * @param keyDeserializer the Apache Kafka key deserializer
   * @param valueDeserializer the Apache Kafka value deserializer
   */
  public AvroProcessor(
      KafkaProperties kafkaProperties,
      Deserializer<K> keyDeserializer,
      Deserializer<V> valueDeserializer) {
    this(kafkaProperties, keyDeserializer, valueDeserializer, null);
  }

  /**
   * Constructs a new <code>AvroProcessor</code>.
   *
   * @param kafkaProperties the Spring Kafka properties
   * @param keyDeserializer the Apache Kafka key deserializer
   * @param valueDeserializer the Apache Kafka value deserializer
   * @param meterRegistry the meter registry
   */
  public AvroProcessor(
      KafkaProperties kafkaProperties,
      Deserializer<K> keyDeserializer,
      Deserializer<V> valueDeserializer,
      MeterRegistry meterRegistry) {
    super(kafkaProperties, keyDeserializer, valueDeserializer, meterRegistry);
  }
}
