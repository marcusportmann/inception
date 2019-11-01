/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.reporting;

//~--- non-JDK imports --------------------------------------------------------

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//~--- JDK imports ------------------------------------------------------------

import java.util.UUID;

/**
 * The <code>ReportDefinitionRepository</code> interface declares the repository for the
 * <code>ReportDefinition</code> domain type.
 *
 * @author Marcus Portmann
 */
public interface ReportDefinitionRepository extends JpaRepository<ReportDefinition, UUID>
{
  @Modifying
  @Query("delete from ReportDefinition rd where rd.id = :reportDefinitionId")
  void deleteById(@Param("reportDefinitionId") UUID reportDefinitionId);
}
