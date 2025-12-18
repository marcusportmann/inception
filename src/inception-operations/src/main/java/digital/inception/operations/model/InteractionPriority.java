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

package digital.inception.operations.model;

import com.fasterxml.jackson.annotation.JsonValue;
import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The {@code InteractionPriority} enumeration defines the possible interaction priorities.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The interaction priority")
@XmlEnum
@XmlType(name = "InteractionPriority", namespace = "https://inception.digital/operations")
public enum InteractionPriority implements CodeEnum {

  /** Critical. */
  @XmlEnumValue("Critical")
  CRITICAL("critical", 1, "Critical"),

  /** High. */
  @XmlEnumValue("High")
  HIGH("high", 5, "High"),

  /** Normal. */
  @XmlEnumValue("Normal")
  NORMAL("normal", 10, "Normal"),

  /** Low. */
  @XmlEnumValue("Low")
  LOW("low", 20, "Low");

  private final String code;

  private final String description;

  private final int priority;

  InteractionPriority(String code, int priority, String description) {
    this.code = code;
    this.priority = priority;
    this.description = description;
  }

  /**
   * Returns the interaction priority given by the specified numerical priority value.
   *
   * @param priority the numerical priority value
   * @return the interaction priority given by the specified numerical priority value
   */
  public static InteractionPriority fromPriority(int priority) {
    return switch (priority) {
      case 20 -> InteractionPriority.LOW;
      case 10 -> InteractionPriority.NORMAL;
      case 5 -> InteractionPriority.HIGH;
      case 1 -> InteractionPriority.CRITICAL;
      default ->
          throw new RuntimeException(
              "Failed to determine the interaction priority with the invalid numerical priority value ("
                  + priority
                  + ")");
    };
  }

  /**
   * Returns the code for the interaction priority.
   *
   * @return the code for the interaction priority
   */
  @JsonValue
  public String code() {
    return code;
  }

  /**
   * Returns the description for the interaction priority.
   *
   * @return the description for the interaction priority
   */
  public String description() {
    return description;
  }

  /**
   * Returns the numerical priority value for the interaction priority.
   *
   * @return the numerical priority value for the interaction priority
   */
  public int priority() {
    return priority;
  }
}
