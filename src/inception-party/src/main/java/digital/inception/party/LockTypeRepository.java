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
 * The <b>LockTypeRepository</b> interface declares the repository for the <b> LockType</b> domain
 * type.
 *
 * @author Marcus Portmann
 */
public interface LockTypeRepository extends JpaRepository<LockType, LockTypeId> {

  /**
   * Retrieve all the lock types sorted by locale ID, sort index, and name.
   *
   * @return all the lock types sorted by locale ID, sort index, and name.
   */
  @Query("select lt from LockType lt order by lt.localeId, -lt.sortIndex DESC, lt.name")
  List<LockType> findAll();

  /**
   * Retrieve the lock types for the specified locale sorted by locale ID, sort index, and name.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the lock types for
   * @return the lock types for the specified locale sorted by locale ID, sort index, and name
   */
  @Query(
      "select lt from LockType lt where upper(lt.localeId) = upper(:localeId) order by lt.localeId, -lt.sortIndex DESC, lt.name")
  List<LockType> findByLocaleIdIgnoreCase(String localeId);
}
