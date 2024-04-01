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

package digital.inception.error.persistence;

import digital.inception.error.model.ErrorReportSummary;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The <b>ErrorReportSummaryRepository</b> interface declares the persistence for the
 * <b>ErrorReportSummary</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface ErrorReportSummaryRepository extends JpaRepository<ErrorReportSummary, UUID> {

  /**
   * Retrieve the filtered error report summaries.
   *
   * @param filter the filter to apply to the error report summaries
   * @param after the date and time to retrieve the error report summaries after
   * @param before the date and time to retrieve the error report summaries before
   * @param pageable the pagination information
   * @return the filtered error report summaries
   */
  @Query(
      "select ers from ErrorReportSummary ers where (ers.created >= :after) and (ers.created < :before) and (lower(ers.who) like lower(:filter))")
  Page<ErrorReportSummary> findFiltered(
      @Param("filter") String filter,
      @Param("after") OffsetDateTime after,
      @Param("before") OffsetDateTime before,
      Pageable pageable);

  /**
   * Retrieve the error report summaries.
   *
   * @param after the date and time to retrieve the error report summaries after
   * @param before the date and time to retrieve the error report summaries before
   * @param pageable the pagination information
   * @return the error report summaries
   */
  @Query(
      "select ers from ErrorReportSummary ers where (ers.created >= :after) and (ers.created <= :before)")
  Page<ErrorReportSummary> findFiltered(
      @Param("after") OffsetDateTime after,
      @Param("before") OffsetDateTime before,
      Pageable pageable);
}
