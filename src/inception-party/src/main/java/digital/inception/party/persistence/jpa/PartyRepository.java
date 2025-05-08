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

import digital.inception.party.model.Party;
import digital.inception.party.model.PartyType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * The {@code PartyRepository} interface declares the persistence for the {@code Party} domain type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface PartyRepository
    extends JpaRepository<Party, UUID>, JpaSpecificationExecutor<Party> {

  /**
   * Delete the party.
   *
   * @param tenantId the ID for the tenant
   * @param id the ID for the party
   */
  @Transactional
  @Modifying
  void deleteByTenantIdAndId(UUID tenantId, UUID id);

  /**
   * Check whether the party exists.
   *
   * @param tenantId the ID for the tenant
   * @param id the ID for the party
   * @return {@code true} if the party exists or {@code false} otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, UUID id);

  /**
   * Retrieve the party.
   *
   * @param tenantId the ID for the tenant
   * @param id the ID for the party
   * @return an Optional containing the party or an empty Optional if the party could not be found
   */
  Optional<Party> findByTenantIdAndId(UUID tenantId, UUID id);

  /**
   * Retrieve the ID for the tenant the party is associated with.
   *
   * @param partyId the ID for the party
   * @return an Optional containing the ID for the tenant the party is associated with or an empty
   *     Optional if the party could not be found
   */
  @Query("select p.tenantId from Party p where p.id = :partyId")
  Optional<UUID> getTenantIdByPartyId(@Param("partyId") UUID partyId);

  /**
   * Retrieve the party type for the party.
   *
   * @param tenantId the ID for the tenant
   * @param partyId the ID for the party
   * @return an Optional containing the party type for the party or an empty Optional if the party
   *     could not be found
   */
  @Query("select p.type from Party p where p.tenantId = :tenantId and p.id = :partyId")
  Optional<PartyType> getTypeByTenantIdAndPartyId(
      @Param("tenantId") UUID tenantId, @Param("partyId") UUID partyId);
}
