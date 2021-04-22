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
 * The <b>PreferenceTypeCategoryRepository</b> interface declares the repository for the <b>
 * PreferenceTypeCategory</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface PreferenceTypeCategoryRepository
    extends JpaRepository<PreferenceTypeCategory, PreferenceTypeCategoryId> {

  @Query(
      "select ptc from PreferenceTypeCategory ptc order by ptc.localeId, -ptc.sortIndex DESC, ptc.name")
  List<PreferenceTypeCategory> findAll();

  @Query(
      "select ptc from PreferenceTypeCategory ptc where upper(ptc.localeId) = upper(:localeId) order by ptc.localeId, -ptc.sortIndex DESC, ptc.name")
  List<PreferenceTypeCategory> findByLocaleIdIgnoreCase(String localeId);
}
