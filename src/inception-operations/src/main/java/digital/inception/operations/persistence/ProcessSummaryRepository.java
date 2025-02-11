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

package digital.inception.operations.persistence;

import digital.inception.operations.model.ProcessSummary;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The <b>ProcessSummaryRepository</b> interface declares the persistence for the <b>ProcessSummary</b>
 * domain type.
 *
 * @author Marcus Portmann
 */
public interface ProcessSummaryRepository
    extends JpaRepository<ProcessSummary, UUID>, JpaSpecificationExecutor<ProcessSummary> {}
