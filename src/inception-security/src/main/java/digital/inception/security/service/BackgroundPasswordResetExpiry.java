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

package digital.inception.security.service;

import digital.inception.security.persistence.jpa.PasswordResetRepository;
import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * The {@code BackgroundPasswordResetExpiry} class implements the background password reset expiry.
 *
 * @author Marcus Portmann
 */
@Component
public class BackgroundPasswordResetExpiry {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(BackgroundPasswordResetExpiry.class);

  /** The Password Reset Repository. */
  private final PasswordResetRepository passwordResetRepository;

  /** The password reset expiry in seconds */
  @Value("${inception.security.passwordResetExpiry:900}")
  private int passwordResetExpiry;

  /**
   * Constructs a new {@code BackgroundPasswordResetExpiry}.
   *
   * @param passwordResetRepository the Password Reset Repository
   */
  public BackgroundPasswordResetExpiry(PasswordResetRepository passwordResetRepository) {
    this.passwordResetRepository = passwordResetRepository;
  }

  /** Expire the password resets. */
  @Scheduled(fixedDelay = 60, timeUnit = TimeUnit.SECONDS)
  @Transactional
  public void expirePasswordResets() {
    try {
      OffsetDateTime requestedBefore = OffsetDateTime.now();
      requestedBefore = requestedBefore.minusSeconds(passwordResetExpiry);

      passwordResetRepository.expirePasswordResets(OffsetDateTime.now(), requestedBefore);
    } catch (Throwable e) {
      log.error("Failed to expire the password resets", e);
    }
  }
}
