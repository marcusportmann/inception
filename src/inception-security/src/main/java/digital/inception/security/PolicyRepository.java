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

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The <b>PolicyRepository</b> interface declares the repository for the <b>Policy</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface PolicyRepository extends JpaRepository<Policy, String> {

  /**
   * Check whether the policy with the specified name exists.
   *
   * @param name the name of the policy
   * @return <b>true</b> if a policy with the specified name exists or <b>false</b> otherwise
   */
  boolean existsByNameIgnoreCase(String name);

  /**
   * Retrieve the policies ordered by name ascending.
   *
   * @return the policies ordered by name ascending
   */
  List<Policy> findAllByOrderByNameAsc();

  /**
   * Retrieve the filtered policies.
   *
   * @param filter the filter to apply to the policies
   * @param pageable the pagination information
   * @return the filtered policies
   */
  @Query("select p from Policy p where (lower(p.name) like lower(:filter))")
  Page<Policy> findFiltered(String filter, Pageable pageable);

  /**
   * Retrieve the name of the policy.
   *
   * @param policyId the ID for the policy
   * @return an Optional containing the name of the policy or an empty Optional if the policy could
   *     not be found
   */
  @Query("select p.name from Policy p where p.id = :policyId")
  Optional<String> getNameById(@Param("policyId") String policyId);
}
