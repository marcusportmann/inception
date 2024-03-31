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

package digital.inception.party;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The <b>MandateRepository</b> interface declares the repository for the <b>Mandate</b> domain
 * type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface MandateRepository extends JpaRepository<Mandate, UUID> {

  /**
   * Delete the mandate.
   *
   * @param tenantId the ID for the tenant
   * @param id the ID for the mandate
   */
  void deleteByTenantIdAndId(UUID tenantId, UUID id);

  /**
   * Check whether the mandate exists.
   *
   * @param tenantId the ID for the tenant
   * @param id the ID for the mandate
   * @return <b>true</b> if the mandate exists or <b>false</b> otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, UUID id);

  /**
   * Retrieve the mandate.
   *
   * @param tenantId the ID for the tenant
   * @param id the ID for the mandate
   * @return an Optional containing the mandate or an empty Optional if the mandate could not be
   *     found
   */
  Optional<Mandate> findByTenantIdAndId(UUID tenantId, UUID id);

  /**
   * Retrieve the mandates for the party.
   *
   * @param tenantId the ID for the tenant
   * @param partyId the ID for the party
   * @param pageable the pagination information
   * @return the mandates for the party
   */
  @Query(
      "select m from Mandate m inner join m.mandataries mandatary where m.tenantId = :tenantId and mandatary.partyId = :partyId")
  Page<Mandate> findByTenantIdAndPartyId(UUID tenantId, UUID partyId, Pageable pageable);
}
