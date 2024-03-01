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

import digital.inception.core.service.Problem;
import digital.inception.core.service.ServiceException;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.ws.WebFault;
import java.io.Serial;

/**
 * A <b>DuplicateGroupException</b> is thrown to indicate that a security operation failed as a
 * result of a duplicate group.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/security/duplicate-group",
    title = "A group with the specified name already exists.",
    status = 409)
@WebFault(
    name = "DuplicateGroupException",
    targetNamespace = "http://inception.digital/security",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DuplicateGroupException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <b>DuplicateGroupException</b>.
   *
   * @param groupName the name of the group
   */
  public DuplicateGroupException(String groupName) {
    super("The group (" + groupName + ") already exists");
  }
}
