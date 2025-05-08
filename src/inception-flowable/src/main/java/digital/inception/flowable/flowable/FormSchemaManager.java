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

import org.flowable.common.engine.impl.db.SchemaManager;

/**
 * The {@code FormSchemaManager} class.
 *
 * @author Marcus Portmann
 */
public class FormSchemaManager implements SchemaManager {

  /** Creates a new {@code FormSchemaManager} instance. */
  public FormSchemaManager() {}

  @Override
  public String getContext() {
    throw new RuntimeException("FormSchemaManager::getContext() NOT IMPLEMENTED");
  }

  @Override
  public void schemaCheckVersion() {
    throw new RuntimeException("FormSchemaManager::schemaCheckVersion() NOT IMPLEMENTED");
  }

  @Override
  public void schemaCreate() {
    throw new RuntimeException("FormSchemaManager::schemaCreate() NOT IMPLEMENTED");
  }

  @Override
  public void schemaDrop() {
    throw new RuntimeException("FormSchemaManager::schemaDrop() NOT IMPLEMENTED");
  }

  @Override
  public String schemaUpdate() {
    throw new RuntimeException("FormSchemaManager::schemaUpdate() NOT IMPLEMENTED");
  }

  @Override
  public String schemaUpdate(String engineDbVersion) {
    return SchemaManager.super.schemaUpdate(engineDbVersion);
  }
}
