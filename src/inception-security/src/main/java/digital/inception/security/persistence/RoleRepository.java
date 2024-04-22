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

import digital.inception.security.model.Role;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The <b>RoleRepository</b> interface declares the persistence for the <b>Role</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface RoleRepository extends JpaRepository<Role, String> {

  /**
   * Retrieve the roles ordered by name ascending.
   *
   * @return the roles ordered by name ascending
   */
  List<Role> findAllByOrderByNameAsc();
}
