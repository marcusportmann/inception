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

package digital.inception.audit.service;

import digital.inception.core.service.AbstractServiceBase;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * The {@code AuditServiceImpl} class provides the Audit Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings({"unused", "WeakerAccess"})
public class AuditServiceImpl extends AbstractServiceBase implements AuditService {

  /**
   * Constructs a new {@code AuditServiceImpl}.
   *
   * @param applicationContext the Spring application context
   */
  public AuditServiceImpl(ApplicationContext applicationContext) {
    super(applicationContext);
  }
}
