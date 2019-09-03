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

package digital.inception.configuration;

//~--- non-JDK imports --------------------------------------------------------

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Optional;

/**
 * The <code>ConfigurationRepository</code> interface declares the repository for the
 * <code>Configuration</code> domain type.
 *
 * @author Marcus Portmann
 */
public interface ConfigurationRepository extends JpaRepository<Configuration, String>
{
  void deleteByKeyIgnoreCase(String key);

  boolean existsByKeyIgnoreCase(String key);

  List<Configuration> findAllByOrderByKeyDesc();

  Optional<Configuration> findByKeyIgnoreCase(String key);

  @Query("select c from Configuration c where upper(c.key) like upper(:filter) order by c.key")
  List<Configuration> findFiltered(@Param("filter") String filter);

  @Query("select value from Configuration c where upper(c.key) = upper(:key)")
  Optional<String> getValueByKeyIgnoreCase(@Param("key") String key);
}
