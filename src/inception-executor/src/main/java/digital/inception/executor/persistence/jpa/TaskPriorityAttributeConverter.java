/*
 * Copyright (c) Discovery Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Discovery Ltd
 * ("Confidential Information"). It may not be copied or reproduced in any manner
 * without the express written permission of Discovery Ltd.
 */

package digital.inception.executor.persistence.jpa;

import digital.inception.executor.model.TaskPriority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * The {@code TaskPriorityAttributeConverter} class implements the custom JPA attribute converter
 * for the {@code TaskPriority} enumeration.
 */
@Converter(autoApply = true)
public class TaskPriorityAttributeConverter implements AttributeConverter<TaskPriority, Integer> {

  /** Creates a new {@code TaskPriorityAttributeConverter} instance. */
  public TaskPriorityAttributeConverter() {}

  /**
   * Converts the value stored in the entity attribute into the data representation to be stored in
   * the database.
   *
   * @param attribute the entity attribute value to be converted
   * @return the converted data to be stored in the database column
   */
  @Override
  public Integer convertToDatabaseColumn(TaskPriority attribute) {
    if (attribute == null) {
      return null;
    }
    return attribute.dbCode();
  }

  /**
   * Converts the data stored in the database column into the value to be stored in the entity
   * attribute. Note that it is the responsibility of the converter writer to specify the correct
   * dbData type for the corresponding column for use by the JDBC driver: i.e., persistence
   * providers are not expected to do such type conversion.
   *
   * @param dbData the data from the database column to be converted
   * @return the converted value to be stored in the entity attribute
   */
  @Override
  public TaskPriority convertToEntityAttribute(Integer dbData) {
    if (dbData == null) {
      return null;
    }
    return TaskPriority.fromDbCode(dbData);
  }
}
