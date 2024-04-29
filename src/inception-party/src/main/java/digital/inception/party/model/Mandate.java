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
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.xml.LocalDateAdapter;
import digital.inception.party.constraint.ValidMandate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * The <b>Mandate</b> class holds the information for a mandate, which is an assignment of authority
 * to act on behalf of a party (the mandator), which is given to another party (the mandatary).
 *
 * @author Marcus Portmann
 */
@Schema(description = "A mandate")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "tenantId",
  "type",
  "requiredMandataries",
  "effectiveFrom",
  "effectiveTo",
  "links",
  "mandataries",
  "properties"
})
@XmlRootElement(name = "Mandate", namespace = "https://inception.digital/party")
@XmlType(
    name = "Mandate",
    namespace = "https://inception.digital/party",
    propOrder = {
      "id",
      "tenantId",
      "type",
      "requiredMandataries",
      "effectiveFrom",
      "effectiveTo",
      "links",
      "mandataries",
      "properties"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@ValidMandate
@Entity
@Table(name = "party_mandates")
public class Mandate implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The links for the mandate. */
  @Schema(description = "The links for the mandate")
  @JsonProperty
  @JsonManagedReference
  @XmlElementWrapper(name = "Links")
  @XmlElement(name = "Link")
  @Valid
  @OneToMany(
      mappedBy = "mandate",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<MandateLink> links = new HashSet<>();

  /** The mandataries for the mandate. */
  @Schema(description = "The mandataries for the mandate")
  @JsonProperty
  @JsonManagedReference
  @XmlElementWrapper(name = "Mandataries")
  @XmlElement(name = "Mandatary")
  @Valid
  @OneToMany(
      mappedBy = "mandate",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<Mandatary> mandataries = new HashSet<>();

  /** The properties for the mandate. */
  @Schema(description = "The properties for the mandate")
  @JsonProperty
  @JsonManagedReference
  @XmlElementWrapper(name = "Properties")
  @XmlElement(name = "Property")
  @Valid
  @OneToMany(
      mappedBy = "mandate",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private final Set<MandateProperty> properties = new HashSet<>();

  /** The date the mandate is effective from. */
  @Schema(description = "The ISO 8601 format date the mandate is effective from")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "EffectiveFrom")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_from")
  private LocalDate effectiveFrom;

  /** The date the mandate is effective to. */
  @Schema(description = "The ISO 8601 format date the mandate is effective to")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "EffectiveTo")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "effective_to")
  private LocalDate effectiveTo;

  /** The ID for the mandate. */
  @Schema(description = "The ID for the mandate", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The number of mandataries required to execute the mandate. */
  @Schema(description = "The number of mandataries required to execute the mandate")
  @JsonProperty(required = true)
  @XmlElement(name = "RequiredMandataries", required = true)
  @NotNull
  @Column(name = "required_mandataries", length = 50, nullable = false)
  private RequiredMandataries requiredMandataries;

  /** The ID for the tenant the mandate is associated with. */
  @Schema(
      description = "The ID for the tenant the mandate is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  @Column(name = "tenant_id", nullable = false)
  private UUID tenantId;

  /** The code for the mandate type. */
  @Schema(
      description = "The code for the mandate type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "type", length = 50, nullable = false)
  private String type;

  /** Constructs a new <b>Mandate</b>. */
  public Mandate() {}

  /**
   * Constructs a new <b>Mandate</b>.
   *
   * @param tenantId the ID for the tenant the mandate is associated with
   * @param type the code for the mandate type
   * @param requiredMandataries the number of mandataries required to execute the mandate
   */
  public Mandate(UUID tenantId, String type, RequiredMandataries requiredMandataries) {
    this.id = UuidCreator.getShortPrefixComb();
    this.tenantId = tenantId;
    this.type = type;
    this.requiredMandataries = requiredMandataries;
  }

  /**
   * Constructs a new <b>Mandate</b>.
   *
   * @param tenantId the ID for the tenant the association is associated with
   * @param type the code for the association type
   * @param requiredMandataries the number of mandataries required to execute the mandate
   * @param effectiveFrom the date that the association is effective from
   */
  public Mandate(
      UUID tenantId,
      String type,
      RequiredMandataries requiredMandataries,
      LocalDate effectiveFrom) {
    this.id = UuidCreator.getShortPrefixComb();
    this.tenantId = tenantId;
    this.type = type;
    this.requiredMandataries = requiredMandataries;
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Constructs a new <b>Mandate</b>.
   *
   * @param tenantId the ID for the tenant the association is associated with
   * @param type the code for the association type
   * @param requiredMandataries the number of mandataries required to execute the mandate
   * @param effectiveFrom the date that the association is effective from
   * @param effectiveTo the date that the association is effective to
   */
  public Mandate(
      UUID tenantId,
      String type,
      RequiredMandataries requiredMandataries,
      LocalDate effectiveFrom,
      LocalDate effectiveTo) {
    this.id = UuidCreator.getShortPrefixComb();
    this.tenantId = tenantId;
    this.type = type;
    this.requiredMandataries = requiredMandataries;
    this.effectiveFrom = effectiveFrom;
    this.effectiveTo = effectiveTo;
  }

  /**
   * Add the link for the mandate.
   *
   * @param link the link
   */
  public void addLink(MandateLink link) {
    links.removeIf(
        existingLink ->
            Objects.equals(existingLink.getType(), link.getType())
                && Objects.equals(existingLink.getTarget(), link.getTarget()));

    link.setMandate(this);

    links.add(link);
  }

  /**
   * Add the mandatary for the mandate.
   *
   * @param mandatary the mandatary
   */
  public void addMandatary(Mandatary mandatary) {
    mandataries.removeIf(
        existingMandatary ->
            Objects.equals(existingMandatary.getPartyId(), mandatary.getPartyId()));

    mandatary.setMandate(this);

    mandataries.add(mandatary);
  }

  /**
   * Add the property for the mandate.
   *
   * @param property the property
   */
  public void addProperty(MandateProperty property) {
    properties.removeIf(
        existingProperty -> Objects.equals(existingProperty.getType(), property.getType()));

    property.setMandate(this);

    properties.add(property);
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

    Mandate other = (Mandate) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the date the association is effective from.
   *
   * @return the date the association is effective from
   */
  public LocalDate getEffectiveFrom() {
    return effectiveFrom;
  }

  /**
   * Returns the date the association is effective to.
   *
   * @return the date the association is effective to
   */
  public LocalDate getEffectiveTo() {
    return effectiveTo;
  }

  /**
   * Returns the ID for the association.
   *
   * @return the ID for the association
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the links for the mandate.
   *
   * @return the links for the mandate
   */
  public Set<MandateLink> getLinks() {
    return links;
  }

  /**
   * Retrieve the links with the specified type for the mandate.
   *
   * @param type the code for the link type
   * @return the links with the specified type for the mandate
   */
  public List<MandateLink> getLinksWithType(String type) {
    return links.stream().filter(link -> Objects.equals(link.getType(), type)).toList();
  }

  /**
   * Returns the mandataries for the mandate.
   *
   * @return the mandataries for the mandate
   */
  public Set<Mandatary> getMandataries() {
    return mandataries;
  }

  /**
   * Returns the properties for the mandate.
   *
   * @return the properties for the mandate
   */
  public Set<MandateProperty> getProperties() {
    return properties;
  }

  /**
   * Retrieve the property with the specified type for the mandate.
   *
   * @param type the code for the mandate property type
   * @return an Optional containing the property with the specified type for the mandate or an empty
   *     Optional if the property could not be found
   */
  public Optional<MandateProperty> getPropertyWithType(String type) {
    return properties.stream()
        .filter(attribute -> Objects.equals(attribute.getType(), type))
        .findFirst();
  }

  /**
   * Returns the number of mandataries required to execute the mandate.
   *
   * @return the number of mandataries required to execute the mandate
   */
  public RequiredMandataries getRequiredMandataries() {
    return requiredMandataries;
  }

  /**
   * Returns the ID for the tenant the association is associated with.
   *
   * @return the ID for the tenant the association is associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the code for the association type.
   *
   * @return the code for the association type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns whether the mandate has a property with the specified type.
   *
   * @param type the code for the mandate property type
   * @return <b>true</b> if the mandate has a property with the specified type or <b>false</b>
   *     otherwise
   */
  public boolean hasPropertyWithType(String type) {
    return properties.stream().anyMatch(property -> Objects.equals(property.getType(), type));
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
  }

  /**
   * Remove the links with the specified type for the mandate.
   *
   * @param type the code for the link type
   */
  public void removeLinksWithType(String type) {
    links.removeIf(existingLink -> Objects.equals(existingLink.getType(), type));
  }

  /**
   * Remove the mandatary for the party with the specified ID.
   *
   * @param partyId the ID for the party who is the recipient of the mandate
   */
  public void removeMandataryForParty(UUID partyId) {
    mandataries.removeIf(
        existingMandatary -> Objects.equals(existingMandatary.getPartyId(), partyId));
  }

  /**
   * Remove the property with the specified type for the mandate.
   *
   * @param type the code for the mandate property type
   */
  public void removePropertyWithType(String type) {
    properties.removeIf(existingProperty -> Objects.equals(existingProperty.getType(), type));
  }

  /**
   * Set the date the association is effective from.
   *
   * @param effectiveFrom the date the association is effective from
   */
  public void setEffectiveFrom(LocalDate effectiveFrom) {
    this.effectiveFrom = effectiveFrom;
  }

  /**
   * Set the date the association is effective to.
   *
   * @param effectiveTo the date the association is effective to
   */
  public void setEffectiveTo(LocalDate effectiveTo) {
    this.effectiveTo = effectiveTo;
  }

  /**
   * Set the ID for the association.
   *
   * @param id the ID for the association
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the links for the mandate.
   *
   * @param links the links for the mandate
   */
  public void setLinks(Set<MandateLink> links) {
    links.forEach(link -> link.setMandate(this));
    this.links.clear();
    this.links.addAll(links);
  }

  /**
   * Set the mandataries for the mandate.
   *
   * @param mandataries the mandataries for the mandate
   */
  public void setMandataries(Set<Mandatary> mandataries) {
    mandataries.forEach(mandatary -> mandatary.setMandate(this));
    this.mandataries.clear();
    this.mandataries.addAll(mandataries);
  }

  /**
   * Set the properties for the mandate.
   *
   * @param properties the properties for the mandate
   */
  public void setProperties(Set<MandateProperty> properties) {
    properties.forEach(property -> property.setMandate(this));
    this.properties.clear();
    this.properties.addAll(properties);
  }

  /**
   * Set the number of mandataries required to execute the mandate.
   *
   * @param requiredMandataries the number of mandataries required to execute the mandate
   */
  public void setRequiredMandataries(RequiredMandataries requiredMandataries) {
    this.requiredMandataries = requiredMandataries;
  }

  /**
   * Set the ID for the tenant the association is associated with.
   *
   * @param tenantId the ID for the tenant the association is associated with
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the code for the association type.
   *
   * @param type the code for the association type
   */
  public void setType(String type) {
    this.type = type;
  }
}
