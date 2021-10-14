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
 * The <b>TimeToContactRepository</b> interface declares the repository for the <b>
 * TimeToContact</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface TimeToContactRepository extends JpaRepository<TimeToContact, TimeToContactId> {

  /**
   * Retrieve all the times to contact sorted by locale ID, sort index, and name.
   *
   * @return all the times to contact sorted by locale ID, sort index, and name.
   */
  @Query("select ttc from TimeToContact ttc order by ttc.localeId, -ttc.sortIndex DESC, ttc.name")
  List<TimeToContact> findAll();

  /**
   * Retrieve the times to contact for the specified locale sorted by locale ID, sort index, and
   * name.
   *
   * @param localeId the Unicode locale identifier for the locale to retrieve the times to contact
   *     for
   * @return the times to contact for the specified locale sorted by locale ID, sort index, and name
   */
  @Query(
      "select ttc from TimeToContact ttc where upper(ttc.localeId) = upper(:localeId) order by ttc.localeId, -ttc.sortIndex DESC, ttc.name")
  List<TimeToContact> findByLocaleIdIgnoreCase(String localeId);
}
