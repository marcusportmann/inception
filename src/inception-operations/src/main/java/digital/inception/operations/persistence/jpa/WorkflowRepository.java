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

package digital.inception.operations.persistence.jpa;

import digital.inception.operations.model.Workflow;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The {@link WorkflowRepository} interface provides the persistence operations for the {@link
 * Workflow} domain type.
 *
 * <p>This repository extends {@link JpaRepository} to provide standard CRUD (Create, Read, Update,
 * Delete) operations and {@link JpaSpecificationExecutor} to support more complex queries using
 * specifications. It is designed to handle all data access requirements for workflows within the
 * application.
 *
 * @author Marcus Portmann
 */
public interface WorkflowRepository
    extends JpaRepository<Workflow, UUID>, JpaSpecificationExecutor<Workflow> {}
