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

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>ConfigurationSummaryRepository</code> interface declares the repository for the
 * <code>ConfigurationSummary</code> domain type.
 *
 * @author Marcus Portmann
 */
public interface ConfigurationSummaryRepository extends JpaRepository<ConfigurationSummary, String>
{
  List<ConfigurationSummary> findAllByOrderByKeyDesc();

  @Query("select c from Configuration c where upper(c.key) like :filter")
  List<ConfigurationSummary> findFiltered(String filter);
}
