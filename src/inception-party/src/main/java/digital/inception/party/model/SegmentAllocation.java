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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.LocalDateAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code SegmentAllocation} class holds the information for the allocation of an organization
 * or person to a segment.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An allocation of an organization or person to a segment")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"segment", "effectiveFrom", "effectiveTo"})
@XmlRootElement(name = "SegmentAllocation", namespace = "https://inception.digital/party")
@XmlType(
    name = "SegmentAllocation",
    namespace = "https://inception.digital/party",
    propOrder = {"segment", "effectiveFrom", "effectiveTo"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_segment_allocations")
@IdClass(SegmentAllocationId.class)
public class SegmentAllocation implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The date the segment allocation is effective from. */
  @Schema(description = "The ISO 8601 format date the segment allocation is effective from")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "EffectiveFrom")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_from")
  private LocalDate effectiveFrom;

  /** The date the segment allocation is effective to. */
  @Schema(description = "The ISO 8601 format date the segment allocation is effective to")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "EffectiveTo")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_to")
  private LocalDate effectiveTo;

  /** The ID for the party the segment allocation is associated with. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "party_id", nullable = false)
  private UUID partyId;

  /** The code for the segment. */
  @Schema(description = "The code for the segment", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Segment", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "segment", length = 50, nullable = false)
  private String segment;

  /** Constructs a new {@code SegmentAllocation}. */
  public SegmentAllocation() {}

  /**
   * Constructs a new {@code SegmentAllocation}.
   *
   * @param segment the segment
   */
  public SegmentAllocation(String segment) {
    this.segment = segment;
  }

  /**
   * Constructs a new {@code SegmentAllocation}.
   *
   * @param segment the segment
   * @param effectiveFrom the date the segment allocation is effective from
   */
  public SegmentAllocation(String segment, LocalDate effectiveFrom) {
    this.segment = segment;
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Constructs a new {@code SegmentAllocation}.
   *
   * @param segment the segment
   * @param effectiveFrom the date the segment allocation is effective from
   * @param effectiveTo the date the segment allocation is effective to
   */
  public SegmentAllocation(String segment, LocalDate effectiveFrom, LocalDate effectiveTo) {
    this.segment = segment;
    this.effectiveFrom = effectiveFrom;
    this.effectiveTo = effectiveTo;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument, otherwise {@code false}
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (getClass() != object.getClass()) {
      return false;
    }

    SegmentAllocation other = (SegmentAllocation) object;

    return Objects.equals(partyId, other.partyId) && Objects.equals(segment, other.segment);
  }

  /**
   * Returns the date the segment allocation is effective from.
   *
   * @return the date the segment allocation is effective from
   */
  public LocalDate getEffectiveFrom() {
    return effectiveFrom;
  }

  /**
   * Returns the date the segment allocation is effective to.
   *
   * @return the date the segment allocation is effective to
   */
  public LocalDate getEffectiveTo() {
    return effectiveTo;
  }

  /**
   * Returns the code for the segment.
   *
   * @return the code for the segment
   */
  public String getSegment() {
    return segment;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((partyId == null) ? 0 : partyId.hashCode())
        + ((segment == null) ? 0 : segment.hashCode());
  }

  /**
   * Sets the date the segment allocation is effective from.
   *
   * @param effectiveFrom the date the segment allocation is effective from
   */
  public void setEffectiveFrom(LocalDate effectiveFrom) {
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Sets the date the segment allocation is effective to.
   *
   * @param effectiveTo the date the segment allocation is effective to
   */
  public void setEffectiveTo(LocalDate effectiveTo) {
    this.effectiveTo = effectiveTo;
  }

  /**
   * Sets the party the segment allocation is associated with.
   *
   * @param party the party the segment allocation is associated with
   */
  @JsonBackReference("segmentAllocationReference")
  @Schema(hidden = true)
  public void setParty(PartyBase party) {
    if (party != null) {
      partyId = party.getId();
    } else {
      partyId = null;
    }
  }

  /**
   * Sets the code for the segment.
   *
   * @param segment the code for the segment
   */
  public void setSegment(String segment) {
    this.segment = segment;
  }

  /**
   * Called by the JAXB runtime when an instance of this class has been completely unmarshalled, but
   * before it is added to its parent.
   *
   * @param unmarshaller the JAXB unmarshaller
   * @param parentObject the parent object
   */
  @SuppressWarnings("unused")
  private void afterUnmarshal(Unmarshaller unmarshaller, Object parentObject) {
    if (parentObject instanceof PartyBase parent) {
      setParty(parent);
    }
  }
}
