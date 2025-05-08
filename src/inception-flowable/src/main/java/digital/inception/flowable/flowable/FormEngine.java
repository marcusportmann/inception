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

package digital.inception.flowable.flowable;

import org.flowable.engine.form.StartFormData;
import org.flowable.engine.form.TaskFormData;

/**
 * The {@code FormEngine} class.
 *
 * @author Marcus Portmann
 */
public class FormEngine implements org.flowable.engine.impl.form.FormEngine {

  /** Creates a new {@code FormEngine} instance. */
  public FormEngine() {}

  @Override
  public String getName() {
    return "Inception";
  }

  @Override
  public Object renderStartForm(StartFormData startForm) {
    return null;
  }

  @Override
  public Object renderTaskForm(TaskFormData taskForm) {
    return null;
  }
}
