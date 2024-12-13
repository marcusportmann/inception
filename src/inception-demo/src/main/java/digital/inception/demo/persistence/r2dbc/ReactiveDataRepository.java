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

package digital.inception.demo.persistence.r2dbc;

import digital.inception.demo.model.ReactiveData;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

/**
 * The <b>ReactiveDataRepository</b> interface declares the R2DBC persistence for the
 * <b>ReactiveData</b> domain type.
 *
 * @author Marcus Portmann
 */
public interface ReactiveDataRepository extends R2dbcRepository<ReactiveData, Long> {}
