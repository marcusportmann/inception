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

import digital.inception.security.model.PasswordReset;
import digital.inception.security.model.PasswordResetStatus;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * The <b>PasswordResetRepository</b> interface declares the persistence for the <b>
 * PasswordReset</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface PasswordResetRepository extends JpaRepository<PasswordReset, UUID> {

  /**
   * Expire the password resets.
   *
   * @param currentTimestamp the current date and time
   * @param requestedBefore the date and time to expire password resets after
   */
  @Transactional
  @Modifying
  @Query(
      "update PasswordReset pr set pr.expired = :currentTimestamp, "
          + "pr.status = digital.inception.security.model.PasswordResetStatus.EXPIRED "
          + "where pr.status = digital.inception.security.model.PasswordResetStatus.REQUESTED "
          + "and pr.requested < :requestedBefore")
  void expirePasswordResets(
      @Param("currentTimestamp") OffsetDateTime currentTimestamp,
      @Param("requestedBefore") OffsetDateTime requestedBefore);

  /**
   * Retrieve the password reset with the specified status for the user.
   *
   * @param username the username for the user
   * @param status the password reset status
   * @return the password resets with the specified status for the user
   */
  List<PasswordReset> findAllByUsernameAndStatus(String username, PasswordResetStatus status);
}
