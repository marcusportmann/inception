/*
 * Copyright 2021 Marcus Portmann
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



import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



/**
 * The <code>PasswordResetRepository</code> interface declares the repository for the <code>
 * PasswordReset</code> domain type.
 *
 * @author Marcus Portmann
 */
public interface PasswordResetRepository extends JpaRepository<PasswordReset, PasswordResetId> {

  @Modifying
  @Query(
      "update PasswordReset pr set pr.expired = current_timestamp, pr.status = 3 "
          + "where pr.status = 1 and pr.requested < :requestedBefore")
  void expirePasswordResets(@Param("requestedBefore") LocalDateTime requestedBefore);

  List<PasswordReset> findAllByUsernameAndStatus(String username, PasswordResetStatus status);
}
