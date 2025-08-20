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

import digital.inception.operations.model.InteractionSource;
import digital.inception.operations.model.InteractionSourcePermission;
import digital.inception.operations.model.InteractionSourceType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The {@code InteractionSourceRepository} interface declares the persistence for the {@code
 * InteractionSource} domain type.
 *
 * @author Marcus Portmann
 */
public interface InteractionSourceRepository extends JpaRepository<InteractionSource, UUID> {

  /**
   * Returns whether an interaction source with the specified tenant ID and ID exists.
   *
   * @param tenantId the ID for the tenant the interaction source is associated with
   * @param interactionSourceId the ID for the interaction source
   * @return {@code true} if an interaction source with the specified tenant ID and ID exists or
   *     {@code false} otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, UUID interactionSourceId);

  /**
   * Retrieve the interaction source.
   *
   * @param tenantId the ID for the tenant
   * @param interactionSourceId the ID for the interaction source
   * @return an Optional containing the interaction source or an empty Optional if the interaction
   *     source could not be found
   */
  Optional<InteractionSource> findByTenantIdAndId(UUID tenantId, UUID interactionSourceId);

  /**
   * Retrieve the interaction sources with the specified type ordered by name ascending.
   *
   * @param tenantId the ID for the tenant
   * @param interactionSourceType the interaction source type for the interaction sources
   * @return the interaction sources ordered by name ascending
   */
  List<InteractionSource> findByTenantIdAndTypeOrderByNameAsc(
      UUID tenantId, InteractionSourceType interactionSourceType);

  /**
   * Retrieve the interaction sources ordered by name ascending.
   *
   * @param tenantId the ID for the tenant
   * @return the interaction sources ordered by name ascending
   */
  List<InteractionSource> findByTenantIdOrderByNameAsc(UUID tenantId);

  /**
   * Retrieve the interaction source permissions for the interaction source with the specified ID.
   *
   * @param interactionSourceId the ID for the interaction source
   * @return the interaction source permissions for the interaction source with the specified ID
   */
  @Query(
      """
         select p
         from InteractionSource is
           join is.permissions p
         where is.id = :interactionSourceId
         order by p.roleCode
         """)
  List<InteractionSourcePermission> findPermissionsBySourceId(
      @Param("interactionSourceId") UUID interactionSourceId);
}
