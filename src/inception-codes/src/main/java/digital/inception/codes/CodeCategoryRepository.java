/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.codes;

//~--- non-JDK imports --------------------------------------------------------

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

//~--- JDK imports ------------------------------------------------------------

import java.time.LocalDateTime;

import java.util.Optional;

/**
 * The <code>CodeCategoryRepository</code> interface declares the repository for the
 * <code>CodeCategory</code> domain type.
 *
 * @author Marcus Portmann
 */
public interface CodeCategoryRepository extends JpaRepository<CodeCategory, String>
{
  @Query("select c.data from CodeCategory c where c.id = ?1")
  Optional<String> getDataById(String id);

  @Query("select c.updated from CodeCategory c where c.id = ?1")
  Optional<LocalDateTime> getUpdatedById(String id);

  @Modifying
  @Query("update CodeCategory c set c.data = ?2, c.updated =?3 where c.id = ?1")
  int setDataAndUpdatedById(String id, String data, LocalDateTime updated);
}
