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

package digital.inception.party;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.sorting.SortDirection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The <b>PartySnapshots</b> class holds the results of a request to retrieve a list of party
 * snapshots.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of party snapshots")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "partySnapshots",
  "total",
  "partyId",
  "from",
  "to",
  "sortDirection",
  "pageIndex",
  "pageSize"
})
@XmlRootElement(name = "PartySnapshots", namespace = "http://inception.digital/party")
@XmlType(
    name = "PartySnapshots",
    namespace = "http://inception.digital/party",
    propOrder = {
      "partySnapshots",
      "total",
      "partyId",
      "from",
      "to",
      "sortDirection",
      "pageIndex",
      "pageSize"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class PartySnapshots implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The optional date to retrieve the party snapshots from. */
  @Schema(description = "The optional date to retrieve the party snapshots from")
  @JsonProperty
  @XmlElement(name = "From")
  private LocalDate from;

  /** The optional page index. */
  @Schema(description = "The optional page index")
  @JsonProperty
  @XmlElement(name = "PageIndex")
  private Integer pageIndex;

  /** The optional page size. */
  @Schema(description = "The optional page size")
  @JsonProperty
  @XmlElement(name = "PageSize")
  private Integer pageSize;

  /** The Universally Unique Identifier (UUID) for the party. */
  @Schema(description = "The Universally Unique Identifier (UUID) for the party", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "PartyId", required = true)
  private UUID partyId;

  /** The party snapshots. */
  @Schema(description = "The party snapshots", required = true)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "PartySnapshots", required = true)
  @XmlElement(name = "PartySnapshot", required = true)
  private List<PartySnapshot> partySnapshots;

  /** The optional sort direction that was applied to the party snapshots. */
  @Schema(description = "The optional sort direction that was applied to the party snapshots")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The optional date to retrieve the party snapshots to. */
  @Schema(description = "The optional date to retrieve the party snapshots to")
  @JsonProperty
  @XmlElement(name = "To")
  private LocalDate to;

  /** The total number of party snapshots. */
  @Schema(description = "The total number of party snapshots", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new <b>PartySnapshots</b>. */
  public PartySnapshots() {}

  /**
   * Constructs a new <b>PartySnapshots</b>.
   *
   * @param partySnapshots the party snapshots
   * @param total the total number of party snapshots
   * @param partyId the Universally Unique Identifier (UUID) for the party
   * @param from the optional date to retrieve the party snapshots from
   * @param to the optional date to retrieve the party snapshots to
   * @param sortDirection the optional sort direction that was applied to the party snapshots
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   */
  public PartySnapshots(
      List<PartySnapshot> partySnapshots,
      long total,
      UUID partyId,
      LocalDate from,
      LocalDate to,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize) {
    this.partySnapshots = partySnapshots;
    this.total = total;
    this.partyId = partyId;
    this.from = from;
    this.to = to;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the optional date to retrieve the party snapshots from.
   *
   * @return the optional date to retrieve the party snapshots from
   */
  public LocalDate getFrom() {
    return from;
  }

  /**
   * Returns the optional page index.
   *
   * @return the optional page index
   */
  public Integer getPageIndex() {
    return pageIndex;
  }

  /**
   * Returns the optional page size.
   *
   * @return the optional page size
   */
  public Integer getPageSize() {
    return pageSize;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the party.
   *
   * @return the Universally Unique Identifier (UUID) for the party
   */
  public UUID getPartyId() {
    return partyId;
  }

  /**
   * Returns the party snapshots.
   *
   * @return the party snapshots
   */
  public List<PartySnapshot> getPartySnapshots() {
    return partySnapshots;
  }

  /**
   * Returns the optional sort direction that was applied to the party snapshots.
   *
   * @return the optional sort direction that was applied to the party snapshots
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the optional date to retrieve the party snapshots to.
   *
   * @return the optional date to retrieve the party snapshots to
   */
  public LocalDate getTo() {
    return to;
  }

  /**
   * Returns the total number of party snapshots.
   *
   * @return the total number of party snapshots
   */
  public Long getTotal() {
    return total;
  }
}
