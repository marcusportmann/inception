/*
 * Copyright 2020 Marcus Portmann
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

package digital.inception.reference;

// ~--- non-JDK imports --------------------------------------------------------

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>EmploymentTypeRepository</code> interface declares the repository for the <code>
 * EmploymentType</code> domain type.
 *
 * @author Marcus Portmann
 */
public interface EmploymentTypeRepository extends JpaRepository<EmploymentType, EmploymentTypeId> {

  List<EmploymentType> findByLocaleIgnoreCase(String locale, Sort sort);
}
