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

import digital.inception.party.model.Association;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * The <b>AssociationRepository</b> interface declares the persistence for the <b>Association</b>
 * domain type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface AssociationRepository extends JpaRepository<Association, UUID> {

  /**
   * Delete the association.
   *
   * @param tenantId the ID for the tenant
   * @param id the ID for the association
   */
  @Transactional
  @Modifying
  void deleteByTenantIdAndId(UUID tenantId, UUID id);

  /**
   * Check whether the association exists.
   *
   * @param tenantId the ID for the tenant
   * @param id the ID for the association
   * @return <b>true</b> if the association exists or <b>false</b> otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, UUID id);

  /**
   * Retrieve the association.
   *
   * @param tenantId the ID for the tenant
   * @param id the ID for the association
   * @return an Optional containing the association or an empty Optional if the association could
   *     not be found
   */
  Optional<Association> findByTenantIdAndId(UUID tenantId, UUID id);

  /**
   * Retrieve the associations for the party.
   *
   * @param tenantId the ID for the tenant
   * @param partyId the ID for the party
   * @param pageable the pagination information
   * @return the associations for the party
   */
  @Query(
      "select a from Association a where a.tenantId = :tenantId and ((a.firstPartyId = :partyId) or (a.secondPartyId = :partyId))")
  Page<Association> findByTenantIdAndPartyId(
      @Param("tenantId") UUID tenantId, @Param("partyId") UUID partyId, Pageable pageable);
}
