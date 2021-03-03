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

package demo.test;

import demo.DemoConfiguration;
import demo.model.Car;
import demo.model.Cars;
import demo.model.IVehicleService;
import demo.model.Vehicle;
import demo.model.VehicleType;
import demo.model.Vehicles;
import digital.inception.core.sorting.SortDirection;
import digital.inception.test.TestClassRunner;
import digital.inception.test.TestConfiguration;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Validator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * The <b>VehicleServiceTest</b> class.
 *
 * @author Marcus Portmann
 */
@RunWith(TestClassRunner.class)
@ContextConfiguration(
    classes = {TestConfiguration.class, DemoConfiguration.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestExecutionListeners(
    listeners = {
      DependencyInjectionTestExecutionListener.class,
      DirtiesContextTestExecutionListener.class,
      TransactionalTestExecutionListener.class
    })
public class VehicleServiceTest {

  /* Entity Manager */
  @Autowired
  @PersistenceContext(unitName = "application")
  private EntityManager entityManager;

  /** The JSR-303 validator. */
  @Autowired private Validator validator;

  /** The Vehicle Service. */
  @Autowired private IVehicleService vehicleService;

  /** Test the vehicle functionality. */
  @Test
  public void vehicleTest() throws Exception {
    Vehicle vehicle = new Vehicle(VehicleType.UNKNOWN, "Test Vehicle");

    vehicleService.createVehicle(vehicle);

    Vehicles vehicles = vehicleService.getVehicles("", SortDirection.ASCENDING, 0, 100);

    Car car = new Car("Audi A5", 4);

    vehicleService.createCar(car);

    Cars cars = vehicleService.getCars("", SortDirection.ASCENDING, 0, 100);

    vehicles = vehicleService.getVehicles("", SortDirection.ASCENDING, 0, 100);

    //    TypedQuery<Vehicle> query = entityManager.createQuery("SELECT v FROM Vehicle v",
    // Vehicle.class);
    //
    //    List<Vehicle> vehicleList = query.getResultList();
    //
    //
    //    Query nativeQuery = entityManager.createNativeQuery("SELECT v.created, v.id, v.name,
    // v.type, v.updated FROM demo.vehicles v", Vehicle.class);
    //
    //    vehicleList = nativeQuery.getResultList();

    int xxx = 0;
    xxx++;
  }
}
