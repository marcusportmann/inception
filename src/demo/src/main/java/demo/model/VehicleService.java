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

package demo.model;

import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ValidationError;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * The <b>VehicleService</b> class provides the Vehicle Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings({"unused", "WeakerAccess"})
public class VehicleService implements IVehicleService {

  /** The maximum number of filtered cars. */
  private static final int MAX_FILTERED_CARS = 100;

  /** The maximum number of filtered vehicles. */
  private static final int MAX_FILTERED_VEHICLES = 100;

  /** The Car Repository. */
  private final CarRepository carRepository;

  /** The JSR-303 validator. */
  private final Validator validator;

  /** The Vehicle Repository. */
  private final VehicleRepository vehicleRepository;

  /* Entity Manager */
  @PersistenceContext(unitName = "applicationPersistenceUnit")
  private EntityManager entityManager;

  /**
   * Constructs a new <b>VehicleService</b>.
   *
   * @param validator the JSR-303 validator
   * @param carRepository the Car Repository
   * @param vehicleRepository the Vehicle Repository
   */
  public VehicleService(
      Validator validator, CarRepository carRepository, VehicleRepository vehicleRepository) {
    this.validator = validator;
    this.carRepository = carRepository;
    this.vehicleRepository = vehicleRepository;
  }

  /**
   * Create the new car.
   *
   * @param car the car
   */
  @Override
  @Transactional
  public void createCar(Car car)
      throws InvalidArgumentException, DuplicateCarException, ServiceUnavailableException {
    if (car == null) {
      throw new InvalidArgumentException("car");
    }

    Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "car", ValidationError.toValidationErrors(constraintViolations));
    }

    try {
      if (carRepository.existsById(car.getId())) {
        throw new DuplicateCarException(car.getId());
      }

      carRepository.saveAndFlush(car);
    } catch (DuplicateCarException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to create the car (" + car.getId() + ")", e);
    }
  }

  /**
   * Create the new vehicle.
   *
   * @param vehicle the vehicle
   */
  @Override
  @Transactional
  public void createVehicle(Vehicle vehicle)
      throws InvalidArgumentException, DuplicateVehicleException, ServiceUnavailableException {
    if (vehicle == null) {
      throw new InvalidArgumentException("vehicle");
    }

    Set<ConstraintViolation<Vehicle>> constraintViolations = validator.validate(vehicle);

    if (!constraintViolations.isEmpty()) {
      throw new InvalidArgumentException(
          "vehicle", ValidationError.toValidationErrors(constraintViolations));
    }

    try {
      if (vehicleRepository.existsById(vehicle.getId())) {
        throw new DuplicateVehicleException(vehicle.getId());
      }

      vehicleRepository.saveAndFlush(vehicle);
    } catch (DuplicateVehicleException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the vehicle (" + vehicle.getId() + ")", e);
    }
  }

  /**
   * Retrieve the cars.
   *
   * @param filter the optional filter to apply to the cars
   * @param sortDirection the optional sort direction to apply to the cars
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the cars
   */
  @Override
  public Cars getCars(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    PageRequest pageRequest;

    if ((pageIndex != null) && (pageSize != null)) {
      pageRequest =
          PageRequest.of(pageIndex, (pageSize > MAX_FILTERED_CARS) ? MAX_FILTERED_CARS : pageSize);
    } else {
      pageRequest = PageRequest.of(0, MAX_FILTERED_CARS);
    }

    try {

      Page<Car> carPage;
      if (StringUtils.hasText(filter)) {
        carPage = carRepository.findFiltered("%" + filter + "%", pageRequest);
      } else {
        carPage = carRepository.findAll(pageRequest);
      }

      return new Cars(
          carPage.toList(), carPage.getTotalElements(), filter, sortDirection, pageIndex, pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the filtered cars", e);
    }
  }

  /**
   * Retrieve the vehicles.
   *
   * @param filter the optional filter to apply to the vehicles
   * @param sortDirection the optional sort direction to apply to the vehicles
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the vehicles
   */
  @Override
  public Vehicles getVehicles(
      String filter, SortDirection sortDirection, Integer pageIndex, Integer pageSize)
      throws InvalidArgumentException, ServiceUnavailableException {
    if ((pageIndex != null) && (pageIndex < 0)) {
      throw new InvalidArgumentException("pageIndex");
    }

    if ((pageSize != null) && (pageSize <= 0)) {
      throw new InvalidArgumentException("pageSize");
    }

    PageRequest pageRequest;

    if ((pageIndex != null) && (pageSize != null)) {
      pageRequest =
          PageRequest.of(
              pageIndex, (pageSize > MAX_FILTERED_VEHICLES) ? MAX_FILTERED_VEHICLES : pageSize);
    } else {
      pageRequest = PageRequest.of(0, MAX_FILTERED_VEHICLES);
    }

    try {

      Page<Vehicle> vehiclePage;
      if (StringUtils.hasText(filter)) {
        vehiclePage = vehicleRepository.findFiltered("%" + filter + "%", pageRequest);
      } else {
        vehiclePage = vehicleRepository.findAll(pageRequest);
      }

      return new Vehicles(
          vehiclePage.toList(),
          vehiclePage.getTotalElements(),
          filter,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the filtered vehicles", e);
    }
  }
}
