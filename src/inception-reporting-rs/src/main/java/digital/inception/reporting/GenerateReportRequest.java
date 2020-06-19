/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.reporting;

// ~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>GenerateReportRequest</code> class holds the information for a request to generate a
 * report.
 *
 * @author Marcus Portmann
 */
@Schema(description = "GenerateReportRequest")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"reportDefinitionId", "reportParameters"})
@SuppressWarnings({"unused", "WeakerAccess"})
public class GenerateReportRequest implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The ID uniquely identifying the report definition. */
  @Schema(description = "The ID uniquely identifying the report definition", required = true)
  @JsonProperty(required = true)
  private String reportDefinitionId;

  /** The report parameters. */
  @Schema(description = "The report parameters", required = true)
  @JsonProperty(required = true)
  private List<ReportParameter> reportParameters;

  /** Constructs a new <code>GenerateReportRequest</code>. */
  public GenerateReportRequest() {}

  /**
   * Constructs a new <code>GenerateReportRequest</code>.
   *
   * @param reportDefinitionId the ID uniquely identifying the report definition
   * @param reportParameters the report parameters
   */
  public GenerateReportRequest(String reportDefinitionId, List<ReportParameter> reportParameters) {
    this.reportDefinitionId = reportDefinitionId;
    this.reportParameters = reportParameters;
  }

  /**
   * Returns the ID uniquely identifying the report definition.
   *
   * @return the ID uniquely identifying the report definition
   */
  public String getReportDefinitionId() {
    return reportDefinitionId;
  }

  /**
   * Set the ID uniquely identifying the report definition.
   *
   * @param reportDefinitionId the ID uniquely identifying the report definition
   */
  public void setReportDefinitionId(String reportDefinitionId) {
    this.reportDefinitionId = reportDefinitionId;
  }

  /**
   * Returns the report parameters.
   *
   * @return the report parameters
   */
  public List<ReportParameter> getReportParameters() {
    return reportParameters;
  }

  /**
   * Set the report parameters.
   *
   * @param reportParameters the report parameters
   */
  public void setReportParameters(List<ReportParameter> reportParameters) {
    this.reportParameters = reportParameters;
  }
}
