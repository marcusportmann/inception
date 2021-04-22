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
 * The <b>AttributeTypeRepository</b> interface declares the repository for the <b>
 * AttributeType</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface AttributeTypeRepository extends JpaRepository<AttributeType, AttributeTypeId> {

  @Query("select at from AttributeType at order by at.localeId, -at.sortIndex DESC, at.name")
  List<AttributeType> findAll();

  @Query(
      "select at from AttributeType at where upper(at.localeId) = upper(:localeId) order by at.localeId, -at.sortIndex DESC, at.name")
  List<AttributeType> findByLocaleIdIgnoreCase(String localeId);
}
