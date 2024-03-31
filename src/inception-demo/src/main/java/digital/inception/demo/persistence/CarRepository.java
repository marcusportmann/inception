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

package digital.inception.demo.persistence;

import digital.inception.demo.model.Car;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The <b>CarRepository</b> interface declares the repository for the <b>Car</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface CarRepository extends JpaRepository<Car, UUID> {

  /**
   * Retrieve the cars ordered by name ascending.
   *
   * @param pageable the pagination information
   * @return the cars ordered by name ascending
   */
  Page<Car> findAllByOrderByNameAsc(Pageable pageable);

  /**
   * Retrieve the filtered cars.
   *
   * @param filter the filter to apply to the cars
   * @param pageable the pagination information
   * @return the filtered cars
   */
  @Query("select c from Car c where (lower(c.name) like lower(:filter)) order by c.name")
  Page<Car> findFiltered(@Param("filter") String filter, Pageable pageable);
}
