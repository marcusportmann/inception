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

import digital.inception.config.model.Config;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * The {@code ConfigRepository} interface declares the persistence for the {@code Config} domain
 * type.
 *
 * @author Marcus Portmann
 */
public interface ConfigRepository extends JpaRepository<Config, String> {

  /**
   * Delete the config with the specified ID.
   *
   * @param configId the ID for the config
   */
  @Transactional
  @Modifying
  void deleteByIdIgnoreCase(String configId);

  /**
   * Check whether the config exists.
   *
   * @param configId the ID for the config
   * @return {@code true} if the config exists or {@code false} otherwise
   */
  boolean existsByIdIgnoreCase(String configId);

  /**
   * Retrieve all the configs ordered by ID ascending.
   *
   * @return all the configs ordered by ID ascending
   */
  List<Config> findAllByOrderByIdAsc();

  /**
   * Retrieve the config.
   *
   * @param configId the ID for the config
   * @return an Optional containing the config or an empty Optional if the config could not be found
   */
  Optional<Config> findByIdIgnoreCase(String configId);

  /**
   * Retrieve the filtered configs.
   *
   * @param filter the filter to apply to the IDs for the configs
   * @return the filtered configs
   */
  List<Config> findByIdIgnoreCaseContaining(String filter);

  /**
   * Retrieve the value for the config.
   *
   * @param configId the ID for the config
   * @return an Optional containing the value for the config or an empty Optional if the config
   *     could not be found
   */
  @Query("select c.value from Config c where lower(c.id) = lower(:configId)")
  Optional<String> getValueByIdIgnoreCase(@Param("configId") String configId);
}
