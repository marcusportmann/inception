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

import digital.inception.party.model.ResidencePermitType;
import digital.inception.party.model.ResidencePermitTypeId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The {@code ResidencePermitTypeRepository} interface declares the persistence for the {@code
 * ResidencePermitType} domain type.
 *
 * @author Marcus Portmann
 */
public interface ResidencePermitTypeRepository
    extends JpaRepository<ResidencePermitType, ResidencePermitTypeId> {

  /**
   * Retrieve all the residence permit types sorted by locale ID, sort index, and name.
   *
   * @return all the residence permit types sorted by locale ID, sort index, and name.
   */
  @Query(
      "select rpt from ResidencePermitType rpt order by rpt.localeId, rpt.sortIndex DESC, rpt.name")
  List<ResidencePermitType> getAllResidencePermitTypes();
}
