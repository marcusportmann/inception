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

package digital.inception.demo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import digital.inception.core.sorting.SortDirection;
import digital.inception.demo.DemoConfiguration;
import digital.inception.demo.model.Car;
import digital.inception.demo.model.Cars;
import digital.inception.demo.model.Vehicle;
import digital.inception.demo.model.VehicleType;
import digital.inception.demo.model.Vehicles;
import digital.inception.demo.service.IVehicleService;
import digital.inception.test.InceptionExtension;
import digital.inception.test.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * The <b>VehicleServiceTest</b> class.
 *
 * @author Marcus Portmann
 */
@ExtendWith(SpringExtension.class)
@ExtendWith(InceptionExtension.class)
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

    assertEquals(1, cars.getCars().size());

    vehicles = vehicleService.getVehicles("", SortDirection.ASCENDING, 0, 100);

    assertEquals(2, vehicles.getVehicles().size());
  }
}
