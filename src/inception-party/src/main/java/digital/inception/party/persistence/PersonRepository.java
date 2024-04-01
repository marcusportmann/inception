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

package digital.inception.party.persistence;

import digital.inception.party.model.Person;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The <b>PersonRepository</b> interface declares the persistence for the <b>Person</b> domain type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface PersonRepository extends JpaRepository<Person, UUID> {

  /**
   * Delete the person.
   *
   * @param tenantId the ID for the tenant
   * @param id the ID for the person
   */
  void deleteByTenantIdAndId(UUID tenantId, UUID id);

  /**
   * Check whether the person exists.
   *
   * @param tenantId the ID for the tenant
   * @param id the ID for the person
   * @return <b>true</b> if the person exists or <b>false</b> otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, UUID id);

  /**
   * Retrieve the persons for the tenant.
   *
   * @param tenantId the ID for the tenant
   * @param pageable the pagination information
   * @return the persons for the tenant
   */
  Page<Person> findByTenantId(UUID tenantId, Pageable pageable);

  /**
   * Retrieve the person.
   *
   * @param tenantId the ID for the tenant
   * @param id the ID for the person
   * @return an Optional containing the person or an empty Optional if the person could not be found
   */
  Optional<Person> findByTenantIdAndId(UUID tenantId, UUID id);

  /**
   * Retrieve the filtered persons for the tenant.
   *
   * @param tenantId the ID for the tenant
   * @param filter the filter to apply to the persons
   * @param pageable the pagination information
   * @return the filtered persons for the tenant
   */
  @Query(
      "select p from Person p where (p.tenantId = :tenantId) and (lower(p.name) like lower(:filter))")
  Page<Person> findFiltered(
      @Param("tenantId") UUID tenantId, @Param("filter") String filter, Pageable pageable);
}
