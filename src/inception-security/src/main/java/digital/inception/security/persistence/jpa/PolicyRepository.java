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

import digital.inception.security.model.Policy;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The {@code PolicyRepository} interface declares the persistence for the {@code Policy} domain
 * type.
 *
 * @author Marcus Portmann
 */
public interface PolicyRepository extends JpaRepository<Policy, String> {

  /**
   * Check whether the policy with the specified name exists.
   *
   * @param name the name of the policy
   * @return {@code true} if a policy with the specified name exists or {@code false} otherwise
   */
  boolean existsByNameIgnoreCase(String name);

  /**
   * Find the policies ordered by name ascending.
   *
   * @return the policies ordered by name ascending
   */
  List<Policy> findAllByOrderByNameAsc();

  /**
   * Find the name of the policy.
   *
   * @param policyId the ID for the policy
   * @return an Optional containing the name of the policy or an empty Optional if the policy could
   *     not be found
   */
  @Query("select p.name from Policy p where p.id = :policyId")
  Optional<String> getNameById(@Param("policyId") String policyId);
}
