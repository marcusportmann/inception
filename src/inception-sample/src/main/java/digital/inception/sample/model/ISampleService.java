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

package digital.inception.sample.model;

// ~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.validation.ValidationError;
import java.util.List;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ISampleService</code> interface defines the functionality that must be provided by a
 * Sample Service implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface ISampleService {

  /** Add the data. */
  void addData() throws SampleServiceException;

  /**
   * Add the data.
   *
   * @param data the data
   */
  void addData(Data data) throws SampleServiceException;

  /**
   * Returns the data.
   *
   * @return the data
   */
  List<Data> getAllData() throws SampleServiceException;

  /**
   * Returns the data.
   *
   * @param id the ID uniquely identifying the data
   * @return the data
   */
  Data getData(long id) throws SampleServiceException;

  /** The test method. */
  void testMethod();

  /**
   * Validate the data.
   *
   * @return the validation errors
   */
  List<ValidationError> validate(Data data) throws SampleServiceException;
}
