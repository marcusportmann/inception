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

package digital.inception.config.persistence.jpa;

import digital.inception.config.model.ConfigSummary;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The {@code ConfigSummaryRepository} interface declares the persistence for the {@code
 * ConfigSummary} domain type.
 *
 * @author Marcus Portmann
 */
public interface ConfigSummaryRepository extends JpaRepository<ConfigSummary, String> {

  /**
   * Find all the config summaries ordered by ID ascending.
   *
   * @return all the config summaries ordered by ID ascending
   */
  List<ConfigSummary> findAllByOrderByIdAsc();

  /**
   * Find the filtered config summaries.
   *
   * @param filter the filter to apply to the IDs for the config summaries
   * @return the filtered config summaries
   */
  List<ConfigSummary> findByIdIgnoreCaseContaining(String filter);
}
