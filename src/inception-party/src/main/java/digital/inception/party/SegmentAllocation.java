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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.LocalDateAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * The <b>SegmentAllocation</b> class holds the information for the allocation of an organization or
 * person to a segment.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An allocation of an organization or person to a segment")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"segment", "effectiveFrom", "effectiveTo"})
@XmlRootElement(name = "SegmentAllocation", namespace = "http://inception.digital/party")
@XmlType(
    name = "SegmentAllocation",
    namespace = "http://inception.digital/party",
    propOrder = {"segment", "effectiveFrom", "effectiveTo"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "segment_allocations")
@IdClass(SegmentAllocationId.class)
public class SegmentAllocation implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The date and time the segment allocation was created. */
  @JsonIgnore
  @XmlTransient
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The date the segment allocation is effective from. */
  @Schema(description = "The date the segment allocation is effective from")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "EffectiveFrom")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_from")
  private LocalDate effectiveFrom;

  /** The date the segment allocation is effective to. */
  @Schema(description = "The date the segment allocation is effective to")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "EffectiveTo")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_to")
  private LocalDate effectiveTo;

  /** The party the segment allocation is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("segmentAllocationReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private PartyBase party;

  /** The code for the segment. */
  @Schema(description = "The code for the segment", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Segment", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "segment", length = 30, nullable = false)
  private String segment;

  /** The date and time the segment allocation was last updated. */
  @JsonIgnore
  @XmlTransient
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** Constructs a new <b>SegmentAllocation</b>. */
  public SegmentAllocation() {}

  /**
   * Constructs a new <b>SegmentAllocation</b>.
   *
   * @param segment the segment
   */
  public SegmentAllocation(String segment) {
    this.segment = segment;
  }

  /**
   * Constructs a new <b>SegmentAllocation</b>.
   *
   * @param segment the segment
   * @param effectiveFrom the date the segment allocation is effective from
   */
  public SegmentAllocation(String segment, LocalDate effectiveFrom) {
    this.segment = segment;
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Constructs a new <b>SegmentAllocation</b>.
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
   * @return <b>true</b> if this object is the same as the object argument otherwise <b>false</b>
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

    return Objects.equals(party, other.party) && Objects.equals(segment, other.segment);
  }

  /**
   * Returns the date and time the segment allocation was created.
   *
   * @return the date and time the segment allocation was created
   */
  public LocalDateTime getCreated() {
    return created;
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
   * Returns the party the segment allocation is associated with.
   *
   * @return the party the segment allocation is associated with
   */
  @Schema(hidden = true)
  public PartyBase getParty() {
    return party;
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
   * Returns the date and time the segment allocation was last updated.
   *
   * @return the date and time the segment allocation was last updated
   */
  public LocalDateTime getUpdated() {
    return updated;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (((party == null) || (party.getId() == null)) ? 0 : party.getId().hashCode())
        + ((segment == null) ? 0 : segment.hashCode());
  }

  /**
   * Set the date the segment allocation is effective from.
   *
   * @param effectiveFrom the date the segment allocation is effective from
   */
  public void setEffectiveFrom(LocalDate effectiveFrom) {
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Set the date the segment allocation is effective to.
   *
   * @param effectiveTo the date the segment allocation is effective to
   */
  public void setEffectiveTo(LocalDate effectiveTo) {
    this.effectiveTo = effectiveTo;
  }

  /**
   * Set the party the segment allocation is associated with.
   *
   * @param party the party the segment allocation is associated with
   */
  @Schema(hidden = true)
  public void setParty(PartyBase party) {
    this.party = party;
  }

  /**
   * Set the code for the segment.
   *
   * @param segment the code for the segment
   */
  public void setSegment(String segment) {
    this.segment = segment;
  }

  /** The Java Persistence callback method invoked before the entity is created in the database. */
  @PrePersist
  protected void onCreate() {
    created = LocalDateTime.now();
  }

  /** The Java Persistence callback method invoked before the entity is updated in the database. */
  @PreUpdate
  protected void onUpdate() {
    updated = LocalDateTime.now();
  }
}
