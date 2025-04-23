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

import digital.inception.security.model.UserDirectorySummary;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The <b>UserDirectorySummaryRepository</b> interface declares the persistence for the <b>
 * UserDirectorySummary</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface UserDirectorySummaryRepository
    extends JpaRepository<UserDirectorySummary, UUID>,
        JpaSpecificationExecutor<UserDirectorySummary> {

  /**
   * Retrieve the user directory summaries for the tenant.
   *
   * @param tenantId the ID for the tenant
   * @return the user directory summaries for the tenant
   */
  @Query(
      "select uds from UserDirectorySummary uds join uds.tenants as t where t.id = :tenantId "
          + "order by uds.name")
  List<UserDirectorySummary> findAllByTenantId(@Param("tenantId") UUID tenantId);
}
