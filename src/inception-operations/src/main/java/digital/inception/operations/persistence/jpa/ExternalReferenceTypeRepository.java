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

import digital.inception.operations.model.ExternalReferenceType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The {@code ExternalReferenceTypeRepository} interface declares the persistence for the {@code
 * ExternalReferenceType} domain type.
 *
 * @author Marcus Portmann
 */
@Repository("operationsExternalReferenceTypeRepository")
public interface ExternalReferenceTypeRepository
    extends JpaRepository<ExternalReferenceType, String> {

  /**
   * Find all the external reference types that are
   *
   * <ul>
   *   <li>specific to the given object type ({@code objectType} matches), or global (the external
   *       reference type’s {@code objectType} is {@code null}).
   *   <li>specific to the given tenant ({@code tenantId} matches), or global (the document
   *       attribute definition’s {@code tenantId} is {@code null}).
   * </ul>
   *
   * @param objectType the type of object the external reference type is specific to
   * @param tenantId the ID for the tenant
   * @return the matching external reference types, ordered by code
   */
  @Query(
      """
         select ert
           from ExternalReferenceType ert
          where (ert.objectType = :objectType or ert.objectType is null)
             and (ert.tenantId = :tenantId or ert.tenantId is null)
          order by ert.code
         """)
  List<ExternalReferenceType> findForObjectTypeAndTenantOrGlobal(
      @Param("objectType") String objectType, @Param("tenantId") UUID tenantId);

  /**
   * Find all the external reference types that are specific to the given tenant ({@code tenantId}
   * matches), or global (the external reference type’s {@code tenantId} is {@code null}).
   *
   * @param tenantId the ID for the tenant
   * @return the matching external reference types, ordered by code
   */
  @Query(
      """
         select ert
           from ExternalReferenceType ert
          where (ert.tenantId = :tenantId or ert.tenantId is null)
          order by ert.code
         """)
  List<ExternalReferenceType> findForTenantOrGlobal(@Param("tenantId") UUID tenantId);
}
