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

package demo.ws;

import demo.model.Car;
import demo.model.DuplicateCarException;
import demo.model.IVehicleService;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.validation.InvalidArgumentException;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;

/**
 * The <b>VehicleWebService</b> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "VehicleService",
    name = "IVehicleService",
    targetNamespace = "http://demo")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class VehicleWebService {

  private final IVehicleService vehicleService;

  /**
   * Constructs a new <b>VehicleWebService</b>.
   *
   * @param vehicleService the Vehicle Service
   */
  public VehicleWebService(IVehicleService vehicleService) {
    this.vehicleService = vehicleService;
  }

  /**
   * Create the new car.
   *
   * @param car the car
   */
  @WebMethod(operationName = "CreateCar")
  public void createCode(@WebParam(name = "Car") @XmlElement(required = true) Car car)
      throws InvalidArgumentException, DuplicateCarException, ServiceUnavailableException {
    vehicleService.createCar(car);
  }
}
