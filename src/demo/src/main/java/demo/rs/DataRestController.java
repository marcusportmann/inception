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

package demo.rs;

import demo.model.Data;
import demo.model.DataServiceException;
import demo.model.IDataService;
import digital.inception.core.validation.ValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The <b>DataRestController</b> class.
 *
 * @author Marcus Portmann
 */
@Tag(name = "Data API")
@RestController
@RequestMapping(value = "/api/data")
@CrossOrigin
@SuppressWarnings({"unused"})
public class DataRestController {

  private final IDataService dataService;

  /**
   * Constructs a new <b>DataRestController</b>.
   *
   * @param dataService the Data Service
   */
  public DataRestController(IDataService dataService) {
    this.dataService = dataService;
  }

  /**
   * Returns all the data.
   *
   * @return all the data
   */
  @RequestMapping(value = "/all-data", method = RequestMethod.GET, produces = "application/json")
  public List<Data> getAllData() throws DataServiceException {
    return dataService.getAllData();
  }

  /**
   * Retrieve the data.
   *
   * @return the data
   */
  @RequestMapping(value = "/data", method = RequestMethod.GET, produces = "application/json")
  public Data getData() throws DataServiceException {
    long id = System.currentTimeMillis();

    Data data = new Data(id, 777, "Test Value " + id, LocalDate.now(), LocalDateTime.now());

    dataService.createData(data);

    data = dataService.getData(data.getId());

    return data;
  }

  /** Validate the data. */
  @Operation(summary = "Validate the data", description = "Validate the data")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "OK",
            content =
                @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ValidationError.class))))
      })
  @RequestMapping(value = "/validate", method = RequestMethod.POST, produces = "application/json")
  public List<ValidationError> validate(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The data to validate",
              required = true)
          @RequestBody
          Data data)
      throws DataServiceException {
    return dataService.validateData(data);
  }
}
