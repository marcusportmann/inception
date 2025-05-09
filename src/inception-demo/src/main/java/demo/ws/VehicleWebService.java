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

package demo.ws;

import demo.model.Car;
import demo.service.DuplicateCarException;
import demo.service.VehicleService;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.ws.AbstractWebServiceBase;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlElement;
import org.springframework.context.ApplicationContext;

/**
 * The {@code VehicleWebService} class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "VehicleService",
    name = "IVehicleService",
    targetNamespace = "https://demo")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class VehicleWebService extends AbstractWebServiceBase {

  private final VehicleService vehicleService;

  /**
   * Constructs a new {@code VehicleWebService}.
   *
   * @param applicationContext the Spring application context
   * @param vehicleService the Vehicle Service
   */
  public VehicleWebService(ApplicationContext applicationContext, VehicleService vehicleService) {
    super(applicationContext);

    this.vehicleService = vehicleService;
  }

  /**
   * Create the new car.
   *
   * @param car the car
   * @throws InvalidArgumentException if an argument is invalid
   * @throws DuplicateCarException if the car already exists
   * @throws ServiceUnavailableException if the car could not be created
   */
  @WebMethod(operationName = "CreateCar")
  public void createCode(@WebParam(name = "Car") @XmlElement(required = true) Car car)
      throws InvalidArgumentException, DuplicateCarException, ServiceUnavailableException {
    vehicleService.createCar(car);
  }
}
