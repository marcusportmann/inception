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

package digital.inception.demo.service;

import digital.inception.core.service.AbstractServiceBase;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.demo.model.Car;
import digital.inception.demo.model.Cars;
import digital.inception.demo.model.Vehicle;
import digital.inception.demo.model.Vehicles;
import digital.inception.demo.persistence.jpa.CarRepository;
import digital.inception.demo.persistence.jpa.VehicleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * The <b>VehicleServiceImpl</b> class provides the Vehicle Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings({"unused", "WeakerAccess"})
public class VehicleServiceImpl extends AbstractServiceBase implements VehicleService {

  /** The maximum number of filtered cars. */
  private static final int MAX_FILTERED_CARS = 100;

  /** The maximum number of filtered vehicles. */
  private static final int MAX_FILTERED_VEHICLES = 100;

  /** The Car Repository. */
  private final CarRepository carRepository;

  /** The Vehicle Repository. */
  private final VehicleRepository vehicleRepository;

  /* Entity Manager */
  @PersistenceContext(unitName = "application")
  private EntityManager entityManager;

  /**
   * Constructs a new <b>VehicleServiceImpl</b>.
   *
   * @param applicationContext the Spring application context
   * @param carRepository the Car Repository
   * @param vehicleRepository the Vehicle Repository
   */
  public VehicleServiceImpl(
      ApplicationContext applicationContext,
      CarRepository carRepository,
      VehicleRepository vehicleRepository) {
    super(applicationContext);

    this.carRepository = carRepository;
    this.vehicleRepository = vehicleRepository;
  }

  @Override
  @Transactional
  public void createCar(Car car)
      throws InvalidArgumentException, DuplicateCarException, ServiceUnavailableException {
    validateArgument("car", car);

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

  @Override
  @Transactional
  public void createVehicle(Vehicle vehicle)
      throws InvalidArgumentException, DuplicateVehicleException, ServiceUnavailableException {
    validateArgument("vehicle", vehicle);

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

    if (pageIndex == null) {
      pageIndex = 0;
    }

    if (pageSize == null) {
      pageSize = MAX_FILTERED_CARS;
    }

    try {
      PageRequest pageRequest =
          PageRequest.of(
              pageIndex,
              Math.min(pageSize, MAX_FILTERED_CARS),
              (sortDirection == SortDirection.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC,
              "name");

      Page<Car> carPage;

      if (StringUtils.hasText(filter)) {
        carPage =
            carRepository.findAll(
                (Specification<Car>)
                    (root, query, criteriaBuilder) -> {
                      return criteriaBuilder.like(
                          criteriaBuilder.lower(root.get("name")),
                          "%" + filter.toLowerCase() + "%");
                    },
                pageRequest);
      } else {
        carPage = carRepository.findAll(pageRequest);
      }

      return new Cars(
          carPage.toList(), carPage.getTotalElements(), filter, sortDirection, pageIndex, pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the filtered cars", e);
    }
  }

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

    if (pageIndex == null) {
      pageIndex = 0;
    }

    if (pageSize == null) {
      pageSize = MAX_FILTERED_VEHICLES;
    }

    try {

      PageRequest pageRequest =
          PageRequest.of(
              pageIndex,
              Math.min(pageSize, MAX_FILTERED_VEHICLES),
              (sortDirection == SortDirection.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC,
              "name");

      Page<Vehicle> vehiclePage;

      if (StringUtils.hasText(filter)) {
        vehiclePage =
            vehicleRepository.findAll(
                (Specification<Vehicle>)
                    (root, query, criteriaBuilder) -> {
                      return criteriaBuilder.like(
                          criteriaBuilder.lower(root.get("name")),
                          "%" + filter.toLowerCase() + "%");
                    },
                pageRequest);
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
