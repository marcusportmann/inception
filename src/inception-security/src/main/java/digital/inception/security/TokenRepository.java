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

package digital.inception.security;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The <b>TokenRepository</b> interface declares the repository for the <b>Token</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface TokenRepository extends JpaRepository<Token, String> {

  /**
   * Delete the token.
   *
   * @param tokenId the ID for the token
   */
  @Modifying
  @Query("delete from Token t where t.id = :tokenId")
  void deleteById(@Param("tokenId") String tokenId);

  /**
   * Check whether the token with the specified name exists.
   *
   * @param name the name of the token
   * @return <b>true</b> if a token with the specified name exists or <b>false</b> otherwise
   */
  boolean existsByNameIgnoreCase(String name);

  /**
   * Retrieve the tokens.
   *
   * @param pageable the pagination information
   * @return the tokens
   */
  Page<Token> findAll(Pageable pageable);

  /**
   * Retrieve the filtered tokens.
   *
   * @param filter the filter to apply to the tokens
   * @param pageable the pagination information
   * @return the filtered tokens
   */
  @Query("select t from Token t where (lower(t.name) like lower(:filter))")
  Page<Token> findFiltered(String filter, Pageable pageable);

  /**
   * Retrieve the name of the token.
   *
   * @param tokenId the ID for the token
   * @return an Optional containing the name of the token or an empty Optional if the token could
   *     not be found
   */
  @Query("select t.name from Token t where t.id = :tokenId")
  Optional<String> getNameById(@Param("tokenId") String tokenId);

  /**
   * Retrieve the revoked tokens.
   *
   * @return the revoked tokens
   */
  @Query(
      "select new digital.inception.security.RevokedToken(t.id, t.type, t.name, t.issued, "
          + "t.validFromDate, t.expiryDate, t.revocationDate) from Token t "
          + "where t.revocationDate is not null order by t.revocationDate desc")
  List<RevokedToken> getRevokedTokens();

  /**
   * Reinstate the token.
   *
   * @param tokenId the ID for the token
   * @return the number of tokens reinstated
   */
  @Modifying
  @Query("update Token t set t.revocationDate = null where t.id = :tokenId")
  int reinstateToken(@Param("tokenId") String tokenId);

  /**
   * Revoke the token.
   *
   * @param tokenId the ID for the token
   * @param revoked the revocation date
   * @return the number of tokens revoked
   */
  @Modifying
  @Query("update Token t set t.revocationDate = :revoked where t.id = :tokenId")
  int revokeToken(@Param("tokenId") String tokenId, @Param("revoked") LocalDate revoked);
}
