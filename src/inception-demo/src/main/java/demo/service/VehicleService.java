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

package demo.service;

import demo.model.Car;
import demo.model.Cars;
import demo.model.Vehicle;
import demo.model.Vehicles;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;

/**
 * The {@code VehicleService} interface defines the functionality that must be provided by a Vehicle
 * Service implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface VehicleService {

  /**
   * Create the car.
   *
   * @param car the car
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateCarException if the car already exists
   * @throws ServiceUnavailableException if the car could not be created
   */
  void createCar(Car car)
      throws InvalidArgumentException, DuplicateCarException, ServiceUnavailableException;

  /**
   * Create the vehicle.
   *
   * @param vehicle the vehicle
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateVehicleException if the vehicle already exists
   * @throws ServiceUnavailableException if the vehicle could not be created
   */
  void createVehicle(Vehicle vehicle)
      throws InvalidArgumentException, DuplicateVehicleException, ServiceUnavailableException;

  /**
   * Retrieve the cars.
   *
   * @param filter the filter to apply to the cars
   * @param sortDirection the sort direction to apply to the cars
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the cars
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the cars could not be retrieved
   */
  Cars getCars(String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;

  /**
   * Retrieve the vehicles.
   *
   * @param filter the filter to apply to the vehicles
   * @param sortDirection the sort direction to apply to the vehicles
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the vehicles
   * @throws InvalidArgumentException if an argument is invalid
   * @throws ServiceUnavailableException if the vehicles could not be retrieved
   */
  Vehicles getVehicles(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException;
}
