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



import digital.inception.core.service.ServiceException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



/**
 * A <b>DuplicateGroupException</b> is thrown to indicate that a security operation failed as
 * a result of a duplicate group.
 *
 * @author Marcus Portmann
 */
@ResponseStatus(
    value = HttpStatus.CONFLICT,
    reason = "A group with the specified name already exists")
@WebFault(
    name = "DuplicateGroupException",
    targetNamespace = "http://security.inception.digital",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
@SuppressWarnings({"unused", "WeakerAccess"})
public class DuplicateGroupException extends ServiceException {

  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <b>DuplicateGroupException</b>.
   *
   * @param groupName the name of the group
   */
  public DuplicateGroupException(String groupName) {
    super("DuplicateGroupError", "A group with the name (" + groupName + ") already exists");
  }
}
