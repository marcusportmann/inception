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

package digital.inception.party;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The <b>MandataryRoleRepository</b> interface declares the repository for the <b>MandataryRole</b>
 * domain type.
 *
 * @author Marcus Portmann
 */
public interface MandataryRoleRepository extends JpaRepository<MandataryRole, MandataryRoleId> {

  /**
   * Retrieve all the mandatary roles sorted by locale ID, sort index, and name.
   *
   * @return all the mandatary roles sorted by locale ID, sort index, and name.
   */
  @Query("select mr from MandataryRole mr order by mr.localeId, -mr.sortIndex DESC, mr.name")
  List<MandataryRole> findAll();

  /**
   * Retrieve the mandatary roles for the specified locale sorted by locale ID, sort index, and
   * name.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the mandatary roles
   *     for
   * @return the mandatary roles for the specified locale sorted by locale ID, sort index, and name
   */
  @Query(
      "select mr from MandataryRole mr where upper(mr.localeId) = upper(:localeId) order by mr.localeId, -mr.sortIndex DESC, mr.name")
  List<MandataryRole> findByLocaleIdIgnoreCase(String localeId);
}