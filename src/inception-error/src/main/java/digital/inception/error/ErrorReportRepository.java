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

package digital.inception.error;

// ~--- non-JDK imports --------------------------------------------------------

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ErrorRepository</code> interface declares the repository for the <code>ErrorReport
 * </code> domain type.
 *
 * @author Marcus Portmann
 */
public interface ErrorReportRepository extends JpaRepository<ErrorReport, UUID> {

  @Modifying
  @Query("delete from ErrorReport er where er.id = :errorReportId")
  void deleteById(@Param("errorReportId") UUID errorReportId);
}
