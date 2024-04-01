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

package digital.inception.reference.persistence;

import digital.inception.reference.model.Region;
import digital.inception.reference.model.RegionId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The <b>RegionRepository</b> interface declares the persistence for the <b>Region</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface RegionRepository extends JpaRepository<Region, RegionId> {

  /**
   * Retrieve all the regions sorted by locale ID, sort index, and name.
   *
   * @return all the regions sorted by locale ID, sort index, and name.
   */
  @Query("select r from Region r order by r.localeId, -r.sortIndex DESC, r.name")
  List<Region> getAllRegions();
}
