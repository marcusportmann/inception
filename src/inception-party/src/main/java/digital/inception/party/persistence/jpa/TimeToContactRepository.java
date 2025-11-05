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

import digital.inception.party.model.TimeToContact;
import digital.inception.party.model.TimeToContactId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The {@code TimeToContactRepository} interface declares the persistence for the {@code
 * TimeToContact} domain type.
 *
 * @author Marcus Portmann
 */
public interface TimeToContactRepository extends JpaRepository<TimeToContact, TimeToContactId> {

  /**
   * Find all the times to contact sorted by locale ID, sort order, and name.
   *
   * @return all the times to contact sorted by locale ID, sort order, and name.
   */
  @Query("select ttc from TimeToContact ttc order by ttc.localeId, ttc.sortOrder DESC, ttc.name")
  List<TimeToContact> findAllTimesToContact();
}
