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

package demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import demo.model.Car;
import demo.model.Cars;
import demo.model.Vehicle;
import demo.model.VehicleType;
import demo.model.Vehicles;
import demo.service.IVehicleService;
import digital.inception.core.sorting.SortDirection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
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
@SpringBootTest(classes = DemoApplication.class, webEnvironment = WebEnvironment.NONE)
@ExtendWith(SpringExtension.class)
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
