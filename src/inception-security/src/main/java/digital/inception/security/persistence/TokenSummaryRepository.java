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

package digital.inception.security.persistence;

import digital.inception.security.model.TokenSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The <b>TokenSummaryRepository</b> interface declares the persistence for the <b>TokenSummary</b>
 * domain type.
 *
 * @author Marcus Portmann
 */
public interface TokenSummaryRepository extends JpaRepository<TokenSummary, String> {

  /**
   * Retrieve the active token summaries.
   *
   * @param pageable the pagination information
   * @return the active token summaries
   */
  @Query(
      "select ts from TokenSummary ts where (ts.revocationDate is null)"
          + " and ((ts.validFromDate is null) or (ts.validFromDate < now()))"
          + " and ((ts.expiryDate is null) or (ts.expiryDate > now()))")
  Page<TokenSummary> findAllActive(Pageable pageable);

  /**
   * Retrieve the expired token summaries.
   *
   * @param pageable the pagination information
   * @return the expired token summaries
   */
  @Query(
      "select ts from TokenSummary ts where (ts.revocationDate is null)"
          + " and ((ts.expiryDate is not null) and (ts.expiryDate < now()))")
  Page<TokenSummary> findAllExpired(Pageable pageable);

  /**
   * Retrieve the pending token summaries.
   *
   * @param pageable the pagination information
   * @return the pending token summaries
   */
  @Query(
      "select ts from TokenSummary ts where (ts.revocationDate is null)"
          + " and ((ts.validFromDate is not null) and (ts.validFromDate > now()))")
  Page<TokenSummary> findAllPending(Pageable pageable);

  /**
   * Retrieve the revoked token summaries.
   *
   * @param pageable the pagination information
   * @return the revoked token summaries
   */
  @Query("select ts from TokenSummary ts where (ts.revocationDate is not null)")
  Page<TokenSummary> findAllRevoked(Pageable pageable);

  /**
   * Retrieve the filtered token summaries.
   *
   * @param filter the filter to apply to the token summaries
   * @param pageable the pagination information
   * @return the filtered token summaries
   */
  @Query("select ts from TokenSummary ts where (lower(ts.name) like lower(:filter))")
  Page<TokenSummary> findFiltered(String filter, Pageable pageable);

  /**
   * Retrieve the filtered active token summaries.
   *
   * @param filter the filter to apply to the active token summaries
   * @param pageable the pagination information
   * @return the filtered active token summaries
   */
  @Query(
      "select ts from TokenSummary ts where (lower(ts.name) like lower(:filter))"
          + " and (ts.revocationDate is null)"
          + " and ((ts.validFromDate is null) or (ts.validFromDate < now()))"
          + " and ((ts.expiryDate is null) or (ts.expiryDate > now()))")
  Page<TokenSummary> findFilteredActive(String filter, Pageable pageable);

  /**
   * Retrieve the filtered expired token summaries.
   *
   * @param filter the filter to apply to the expired token summaries
   * @param pageable the pagination information
   * @return the filtered expired token summaries
   */
  @Query(
      "select ts from TokenSummary ts where (lower(ts.name) like lower(:filter))"
          + " and (ts.revocationDate is null)"
          + " and ((ts.expiryDate is not null) and (ts.expiryDate < now()))")
  Page<TokenSummary> findFilteredExpired(String filter, Pageable pageable);

  /**
   * Retrieve the filtered pending token summaries.
   *
   * @param filter the filter to apply to the pending token summaries
   * @param pageable the pagination information
   * @return the filtered pending token summaries
   */
  @Query(
      "select ts from TokenSummary ts where (lower(ts.name) like lower(:filter))"
          + " and (ts.revocationDate is null)"
          + " and ((ts.validFromDate is not null) and (ts.validFromDate > now()))")
  Page<TokenSummary> findFilteredPending(String filter, Pageable pageable);

  /**
   * Retrieve the filtered revoked token summaries.
   *
   * @param filter the filter to apply to the revoked token summaries
   * @param pageable the pagination information
   * @return the filtered revoked token summaries
   */
  @Query(
      "select ts from TokenSummary ts where (lower(ts.name) like lower(:filter))"
          + " and (ts.revocationDate is not null)")
  Page<TokenSummary> findFilteredRevoked(String filter, Pageable pageable);
}
