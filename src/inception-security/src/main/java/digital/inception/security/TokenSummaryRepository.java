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

package digital.inception.security;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The <b>TokenSummaryRepository</b> interface declares the repository for the <b>TokenSummary</b>
 * domain type.
 *
 * @author Marcus Portmann
 */
public interface TokenSummaryRepository extends JpaRepository<TokenSummary, String> {

  /**
   * Retrieve the filtered token summaries.
   *
   * @param filter the filter to apply to the token summaries
   * @param pageable the pagination information
   * @return the filtered token summaries
   */
  @Query("select ts from TokenSummary ts where (lower(ts.name) like lower(:filter))")
  Page<TokenSummary> findFiltered(String filter, Pageable pageable);
}
