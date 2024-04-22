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

package digital.inception.security.persistence;

import digital.inception.security.model.PolicySummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The <b>PolicySummaryRepository</b> interface declares the persistence for the
 * <b>PolicySummary</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface PolicySummaryRepository extends JpaRepository<PolicySummary, String> {

  /**
   * Retrieve the filtered policy summaries.
   *
   * @param filter the filter to apply to the policy summaries
   * @param pageable the pagination information
   * @return the filtered policy summaries
   */
  @Query("select ps from PolicySummary ps where (lower(ps.name) like lower(:filter))")
  Page<PolicySummary> findFiltered(@Param("filter") String filter, Pageable pageable);
}
