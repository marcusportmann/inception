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

package digital.inception.operations.util;

import digital.inception.operations.model.InteractionSource;
import digital.inception.operations.model.InteractionSourceAttribute;
import digital.inception.operations.model.WorkflowEngine;
import digital.inception.operations.model.WorkflowEngineAttribute;

/**
 * The {@code AttributeUtil} class is a utility class which provides methods for working with
 * attributes for various model classes.
 *
 * @author Marcus Portmann
 */
public final class AttributeUtil {

  /** Private constructor to prevent instantiation. */
  private AttributeUtil() {}

  /**
   * Returns the {@code String} value for the attribute with the specified code for the interaction
   * source.
   *
   * @param interactionSource the interaction source
   * @param code the code for the attribute
   * @return the {@code String} value for the attribute or {@code null} if the attribute could not
   *     be found or has no value
   */
  public static String getAttributeValue(InteractionSource interactionSource, String code) {
    return interactionSource
        .getAttribute(code)
        .map(InteractionSourceAttribute::getValue)
        .orElse(null);
  }

  /**
   * Returns the {@code String} value for the attribute with the specified code for the workflow
   * engine.
   *
   * @param workflowEngine the workflow engine
   * @param code the code for the attribute
   * @return the {@code String} value for the attribute or {@code null} if the attribute could not
   *     be found or has no value
   */
  public static String getAttributeValue(WorkflowEngine workflowEngine, String code) {
    return workflowEngine.getAttribute(code).map(WorkflowEngineAttribute::getValue).orElse(null);
  }

  /**
   * Returns the {@code Boolean} value for the attribute with the specified code for the interaction
   * source.
   *
   * @param interactionSource the interaction source
   * @param code the code for the attribute
   * @return the {@code Boolean} value for the attribute or {@code null} if the attribute could not
   *     be found or has no value
   */
  public static Boolean getAttributeValueAsBoolean(
      InteractionSource interactionSource, String code) {
    return interactionSource
        .getAttribute(code)
        .map(InteractionSourceAttribute::getValue)
        .map(
            value -> {
              try {
                return Boolean.parseBoolean(value);
              } catch (NumberFormatException e) {
                throw new RuntimeException(
                    "Failed to parse the boolean value ("
                        + value
                        + ") for the interaction source attribute ("
                        + code
                        + ")",
                    e);
              }
            })
        .orElse(null);
  }

  /**
   * Returns the {@code Boolean} value for the attribute with the specified code for the workflow
   * engine.
   *
   * @param workflowEngine the workflow engine
   * @param code the code for the attribute
   * @return the {@code Boolean} value for the attribute or {@code null} if the attribute could not
   *     be found or has no value
   */
  public static Boolean getAttributeValueAsBoolean(WorkflowEngine workflowEngine, String code) {
    return workflowEngine
        .getAttribute(code)
        .map(WorkflowEngineAttribute::getValue)
        .map(
            value -> {
              try {
                return Boolean.parseBoolean(value);
              } catch (NumberFormatException e) {
                throw new RuntimeException(
                    "Failed to parse the boolean value ("
                        + value
                        + ") for the workflow engine attribute ("
                        + code
                        + ")",
                    e);
              }
            })
        .orElse(null);
  }

  /**
   * Returns the {@code Double} value for the attribute with the specified code for the interaction
   * source.
   *
   * @param interactionSource the interaction source
   * @param code the code for the attribute
   * @return the {@code Double} value for the attribute or {@code null} if the attribute could not
   *     be found or has no value
   */
  public static Double getAttributeValueAsDouble(InteractionSource interactionSource, String code) {
    return interactionSource
        .getAttribute(code)
        .map(InteractionSourceAttribute::getValue)
        .map(
            value -> {
              try {
                return Double.parseDouble(value);
              } catch (NumberFormatException e) {
                throw new RuntimeException(
                    "Failed to parse the double value ("
                        + value
                        + ") for the interaction source attribute ("
                        + code
                        + ")",
                    e);
              }
            })
        .orElse(null);
  }

  /**
   * Returns the {@code Double} value for the attribute with the specified code for the workflow
   * engine.
   *
   * @param workflowEngine the workflow engine
   * @param code the code for the attribute
   * @return the {@code Double} value for the attribute or {@code null} if the attribute could not
   *     be found or has no value
   */
  public static Double getAttributeValueAsDouble(WorkflowEngine workflowEngine, String code) {
    return workflowEngine
        .getAttribute(code)
        .map(WorkflowEngineAttribute::getValue)
        .map(
            value -> {
              try {
                return Double.parseDouble(value);
              } catch (NumberFormatException e) {
                throw new RuntimeException(
                    "Failed to parse the double value ("
                        + value
                        + ") for the workflow engine attribute ("
                        + code
                        + ")",
                    e);
              }
            })
        .orElse(null);
  }

  /**
   * Returns the {@code Integer} value for the attribute with the specified code for the interaction
   * source.
   *
   * @param interactionSource the interaction source
   * @param code the code for the attribute
   * @return the {@code Integer} value for the attribute or {@code null} if the attribute could not
   *     be found or has no value
   */
  public static Integer getAttributeValueAsInteger(
      InteractionSource interactionSource, String code) {
    return interactionSource
        .getAttribute(code)
        .map(InteractionSourceAttribute::getValue)
        .map(
            value -> {
              try {
                return Integer.parseInt(value);
              } catch (NumberFormatException e) {
                throw new RuntimeException(
                    "Failed to parse the integer value ("
                        + value
                        + ") for the interaction source attribute ("
                        + code
                        + ")",
                    e);
              }
            })
        .orElse(null);
  }

  /**
   * Returns the {@code Integer} value for the attribute with the specified code for the workflow
   * engine.
   *
   * @param workflowEngine the workflow engine
   * @param code the code for the attribute
   * @return the {@code Integer} value for the attribute or {@code null} if the attribute could not
   *     be found or has no value
   */
  public static Integer getAttributeValueAsInteger(WorkflowEngine workflowEngine, String code) {
    return workflowEngine
        .getAttribute(code)
        .map(WorkflowEngineAttribute::getValue)
        .map(
            value -> {
              try {
                return Integer.parseInt(value);
              } catch (NumberFormatException e) {
                throw new RuntimeException(
                    "Failed to parse the integer value ("
                        + value
                        + ") for the workflow engine attribute ("
                        + code
                        + ")",
                    e);
              }
            })
        .orElse(null);
  }

  /**
   * Returns the {@code Long} value for the attribute with the specified code for the interaction
   * source.
   *
   * @param interactionSource the interaction source
   * @param code the code for the attribute
   * @return the {@code Long} value for the attribute or {@code null} if the attribute could not be
   *     found or has no value
   */
  public static Long getAttributeValueAsLong(InteractionSource interactionSource, String code) {
    return interactionSource
        .getAttribute(code)
        .map(InteractionSourceAttribute::getValue)
        .map(
            value -> {
              try {
                return Long.parseLong(value);
              } catch (NumberFormatException e) {
                throw new RuntimeException(
                    "Failed to parse the long value ("
                        + value
                        + ") for the interaction source attribute ("
                        + code
                        + ")",
                    e);
              }
            })
        .orElse(null);
  }

  /**
   * Returns the {@code Long} value for the attribute with the specified code for the workflow
   * engine.
   *
   * @param workflowEngine the workflow engine
   * @param code the code for the attribute
   * @return the {@code Long} value for the attribute or {@code null} if the attribute could not be
   *     found or has no value
   */
  public static Long getAttributeValueAsLong(WorkflowEngine workflowEngine, String code) {
    return workflowEngine
        .getAttribute(code)
        .map(WorkflowEngineAttribute::getValue)
        .map(
            value -> {
              try {
                return Long.parseLong(value);
              } catch (NumberFormatException e) {
                throw new RuntimeException(
                    "Failed to parse the long value ("
                        + value
                        + ") for the workflow engine attribute ("
                        + code
                        + ")",
                    e);
              }
            })
        .orElse(null);
  }
}
