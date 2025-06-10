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

package digital.inception.operations.persistence.jpa;

import digital.inception.operations.model.Interaction;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The {@code InteractionRepository} interface declares the persistence for the {@code Interaction}
 * domain type.
 *
 * @author Marcus Portmann
 */
public interface InteractionRepository
    extends JpaRepository<Interaction, UUID>, JpaSpecificationExecutor<Interaction> {

  /**
   * Returns whether an interaction with the specified source reference for the interaction source
   * with the specified source ID exists.
   *
   * @param sourceId the ID for the interaction source the interaction is associated with
   * @param sourceReference the interaction source specific reference for the interaction
   * @return {@code true} if an interaction with the specified source reference for the interaction
   *     source with the specified ID exists or {@code false} otherwise
   */
  boolean existsBySourceIdAndSourceReference(UUID sourceId, String sourceReference);

  /**
   * Returns whether an interaction with the specified tenant ID and ID exists.
   *
   * @param tenantId the ID for the tenant the interaction is associated with
   * @param interactionSourceId the ID for the interaction
   * @return {@code true} if an interaction with the specified tenant ID and ID exists or {@code
   *     false} otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, UUID interactionSourceId);

  /**
   * Retrieve the interaction.
   *
   * @param tenantId the ID for the tenant the interaction is associated with
   * @param id the ID for the interaction
   * @return an Optional containing the interaction or an empty Optional if the interaction could
   *     not be found
   */
  Optional<Interaction> findByTenantIdAndId(UUID tenantId, UUID id);

  /**
   * Retrieve the ID for the interaction with the specified source reference and source ID.
   *
   * @param tenantId the ID for the tenant the interaction is associated with
   * @param sourceId the ID for the interaction source the interaction is associated with
   * @param sourceReference the interaction source specific reference
   * @return an Optional containing the ID for the interaction with the specified source reference
   *     and source ID or an empty optional if the interaction could not be found
   */
  @Query(
      "select i.id from Interaction i where i.tenantId = :tenantId and i.sourceId = :sourceId "
          + "and i.sourceReference = :sourceReference")
  Optional<UUID> getIdByTenantIdAndSourceIdAndSourceReference(
      @Param("tenantId") UUID tenantId,
      @Param("sourceId") UUID sourceId, @Param("sourceReference") String sourceReference);

  /**
   * Retrieve the subject for the interaction.
   *
   * @param interactionId the ID for the interaction
   * @return an Optional containing the subject for the interaction or an empty Optional if the
   *     interaction could not be found
   */
  @Query("select i.subject from Interaction i where i.id = :interactionId")
  Optional<String> getSubjectById(@Param("interactionId") UUID interactionId);
}
