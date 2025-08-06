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

package digital.inception.party.persistence.jpa;

import digital.inception.party.model.Person;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 * The {@code PersonRepository} interface declares the persistence for the {@code Person} domain
 * type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface PersonRepository
    extends JpaRepository<Person, UUID>, JpaSpecificationExecutor<Person> {

  /**
   * Delete the person.
   *
   * @param tenantId the ID for the tenant
   * @param id the ID for the person
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  void deleteByTenantIdAndId(UUID tenantId, UUID id);

  /**
   * Check whether the person exists.
   *
   * @param tenantId the ID for the tenant
   * @param id the ID for the person
   * @return {@code true} if the person exists or {@code false} otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, UUID id);

  /**
   * Retrieve the person.
   *
   * @param tenantId the ID for the tenant
   * @param id the ID for the person
   * @return an Optional containing the person or an empty Optional if the person could not be found
   */
  Optional<Person> findByTenantIdAndId(UUID tenantId, UUID id);
}
