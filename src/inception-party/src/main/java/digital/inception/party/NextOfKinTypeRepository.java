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
 * The <b>NextOfKinTypeRepository</b> interface declares the repository for the <b>
 * NextOfKinType</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface NextOfKinTypeRepository extends JpaRepository<NextOfKinType, NextOfKinTypeId> {

  @Query("select nok from NextOfKinType nok order by nok.localeId, -nok.sortIndex DESC, nok.name")
  List<NextOfKinType> findAll();

  @Query(
      "select nok from NextOfKinType nok where upper(nok.localeId) = upper(:localeId) order by nok.localeId, -nok.sortIndex DESC, nok.name")
  List<NextOfKinType> findByLocaleIdIgnoreCase(String localeId);
}
