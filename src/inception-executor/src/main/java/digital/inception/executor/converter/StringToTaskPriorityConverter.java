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

package digital.inception.executor.converter;

import digital.inception.executor.model.TaskPriority;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * The <b>StringToTaskPriorityConverter</b> class implements the Spring converter that converts a
 * <b>String</b> type into a <b>TaskPriority</b> type.
 *
 * @author Marcus Portmann
 */
@Component
public class StringToTaskPriorityConverter implements Converter<String, TaskPriority> {

  /** Constructs a new <b>StringToTaskPriorityConverter</b>. */
  public StringToTaskPriorityConverter() {}

  @Override
  public TaskPriority convert(@NonNull String source) {
    return TaskPriority.fromCode(source);
  }
}
