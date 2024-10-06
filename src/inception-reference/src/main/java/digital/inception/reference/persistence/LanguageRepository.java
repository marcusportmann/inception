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

import digital.inception.reference.model.Language;
import digital.inception.reference.model.LanguageId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The <b>LanguageRepository</b> interface declares the persistence for the <b>Language</b> domain
 * type.
 *
 * @author Marcus Portmann
 */
public interface LanguageRepository extends JpaRepository<Language, LanguageId> {

  /**
   * Retrieve all the languages sorted by locale ID, sort index, and name.
   *
   * @return all the languages sorted by locale ID, sort index, and name.
   */
  @Query("select l from Language l order by l.localeId, l.sortIndex DESC, l.shortName")
  List<Language> getAllLanguages();
}
