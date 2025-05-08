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

package digital.inception.party.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.sorting.SortDirection;
import digital.inception.core.xml.LocalDateAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * The {@code Snapshots} class holds the results of a request to retrieve a list of snapshots for a
 * particular entity.
 *
 * @author Marcus Portmann
 */
@Schema(
    description =
        "The results of a request to retrieve a list of snapshots for a particular entity")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "tenantId",
  "snapshots",
  "total",
  "entityType",
  "entityId",
  "from",
  "to",
  "sortDirection",
  "pageIndex",
  "pageSize"
})
@XmlRootElement(name = "Snapshots", namespace = "https://inception.digital/party")
@XmlType(
    name = "Snapshots",
    namespace = "https://inception.digital/party",
    propOrder = {
      "tenantId",
      "snapshots",
      "total",
      "entityType",
      "entityId",
      "from",
      "to",
      "sortDirection",
      "pageIndex",
      "pageSize"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class Snapshots implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the entity. */
  @Schema(description = "The ID for the entity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "EntityId", required = true)
  private UUID entityId;

  /** The type of entity. */
  @Schema(description = "The type of entity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "EntityType", required = true)
  private EntityType entityType;

  /** The date to retrieve the snapshots from. */
  @Schema(description = "The ISO 8601 format date to retrieve the snapshots from")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "From")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  private LocalDate from;

  /** The page index. */
  @Schema(description = "The page index")
  @JsonProperty
  @XmlElement(name = "PageIndex")
  private Integer pageIndex;

  /** The page size. */
  @Schema(description = "The page size")
  @JsonProperty
  @XmlElement(name = "PageSize")
  private Integer pageSize;

  /** The snapshots. */
  @Schema(description = "The snapshots", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Snapshots", required = true)
  @XmlElement(name = "Snapshot", required = true)
  private List<Snapshot> snapshots;

  /** The sort direction that was applied to the snapshots. */
  @Schema(description = "The sort direction that was applied to the snapshots")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The ID for the tenant the snapshots are associated with. */
  @Schema(
      description = "The ID for the tenant the snapshots are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  private UUID tenantId;

  /** The date to retrieve the snapshots to. */
  @Schema(description = "The ISO 8601 format date to retrieve the snapshots to")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "To")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  private LocalDate to;

  /** The total number of snapshots. */
  @Schema(
      description = "The total number of snapshots",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Creates a new {@code Snapshots} instance. */
  public Snapshots() {}

  /**
   * Creates a new {@code Snapshots} instance.
   *
   * @param tenantId the ID for the tenant
   * @param snapshots the snapshots
   * @param total the total number of snapshots
   * @param entityType the type of entity
   * @param entityId the ID for the entity
   * @param from the date to retrieve the snapshots from
   * @param to the date to retrieve the snapshots to
   * @param sortDirection the sort direction that was applied to the snapshots
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public Snapshots(
      UUID tenantId,
      List<Snapshot> snapshots,
      long total,
      EntityType entityType,
      UUID entityId,
      LocalDate from,
      LocalDate to,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize) {
    this.tenantId = tenantId;
    this.snapshots = snapshots;
    this.total = total;
    this.entityType = entityType;
    this.entityId = entityId;
    this.from = from;
    this.to = to;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the ID for the entity.
   *
   * @return the ID for the entity
   */
  public UUID getEntityId() {
    return entityId;
  }

  /**
   * Returns the type of entity.
   *
   * @return the type of entity
   */
  public EntityType getEntityType() {
    return entityType;
  }

  /**
   * Returns the date to retrieve the snapshots from.
   *
   * @return the date to retrieve the snapshots from
   */
  public LocalDate getFrom() {
    return from;
  }

  /**
   * Returns the page index.
   *
   * @return the page index
   */
  public Integer getPageIndex() {
    return pageIndex;
  }

  /**
   * Returns the page size.
   *
   * @return the page size
   */
  public Integer getPageSize() {
    return pageSize;
  }

  /**
   * Returns the snapshots.
   *
   * @return the snapshots
   */
  public List<Snapshot> getSnapshots() {
    return snapshots;
  }

  /**
   * Returns the sort direction that was applied to the snapshots.
   *
   * @return the sort direction that was applied to the snapshots
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the ID for the tenant.
   *
   * @return the ID for the tenant
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the date to retrieve the snapshots to.
   *
   * @return the date to retrieve the snapshots to
   */
  public LocalDate getTo() {
    return to;
  }

  /**
   * Returns the total number of snapshots.
   *
   * @return the total number of snapshots
   */
  public Long getTotal() {
    return total;
  }
}
