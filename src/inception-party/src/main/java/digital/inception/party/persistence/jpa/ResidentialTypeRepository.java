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

package digital.inception.party.persistence.jpa;

import digital.inception.party.model.ResidentialType;
import digital.inception.party.model.ResidentialTypeId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The {@code ResidentialTypeRepository} interface declares the persistence for the {@code
 * ResidentialType} domain type.
 *
 * @author Marcus Portmann
 */
public interface ResidentialTypeRepository
    extends JpaRepository<ResidentialType, ResidentialTypeId> {

  /**
   * Find all the residential types sorted by locale ID, sort order, and name.
   *
   * @return all the residential types sorted by locale ID, sort order, and name.
   */
  @Query("select rt from ResidentialType rt order by rt.localeId, rt.sortOrder DESC, rt.name")
  List<ResidentialType> findAllResidentialTypes();
}
