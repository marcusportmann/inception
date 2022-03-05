/*
 * Copyright 2022 Marcus Portmann
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
 * The <b>GenderRepository</b> interface declares the repository for the <b>Gender</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface GenderRepository extends JpaRepository<Gender, GenderId> {

  /**
   * Retrieve all the genders sorted by locale ID, sort index, and name.
   *
   * @return all the genders sorted by locale ID, sort index, and name.
   */
  @Query("select g from Gender g order by g.localeId, -g.sortIndex DESC, g.name")
  List<Gender> findAll();
}
