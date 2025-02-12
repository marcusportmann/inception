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

package digital.inception.operations.converter;

import digital.inception.operations.model.WorkflowDocumentStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

/**
 * The <b>StringToWorkflowDocumentStatusConverter</b> class implements the Spring converter that
 * converts a <b>String</b> type into a <b>WorkflowDocumentStatus</b> type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@Component
@ReadingConverter
public class StringToWorkflowDocumentStatusConverter
    implements Converter<String, WorkflowDocumentStatus> {

  /** Constructs a new <b>StringToWorkflowDocumentStatusConverter</b>. */
  public StringToWorkflowDocumentStatusConverter() {}

  @Override
  public WorkflowDocumentStatus convert(String source) {
    return WorkflowDocumentStatus.fromCode(source);
  }
}
