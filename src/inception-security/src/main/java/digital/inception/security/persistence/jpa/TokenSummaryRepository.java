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

package digital.inception.security.persistence.jpa;

import digital.inception.security.model.TokenSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The {@code TokenSummaryRepository} interface declares the persistence for the {@code
 * TokenSummary} domain type.
 *
 * @author Marcus Portmann
 */
public interface TokenSummaryRepository
    extends JpaRepository<TokenSummary, String>, JpaSpecificationExecutor<TokenSummary> {}
