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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.party.constraints.ValidCountryCode;
import digital.inception.party.constraints.ValidPhysicalAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

/**
 * The <b>PhysicalAddress</b> class holds the information for a physical address.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A physical address associated with a party")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "type",
  "role",
  "buildingName",
  "buildingFloor",
  "buildingRoom",
  "complexName",
  "complexUnitNumber",
  "farmNumber",
  "farmName",
  "farmDescription",
  "siteBlock",
  "siteNumber",
  "streetNumber",
  "streetName",
  "line1",
  "line2",
  "line3",
  "suburb",
  "city",
  "region",
  "country",
  "postalCode",
  "latitude",
  "longitude",
  "purposes"
})
@XmlRootElement(name = "PhysicalAddress", namespace = "http://inception.digital/party")
@XmlType(
    name = "PhysicalAddress",
    namespace = "http://inception.digital/party",
    propOrder = {
      "id",
      "type",
      "role",
      "buildingName",
      "buildingFloor",
      "buildingRoom",
      "complexName",
      "complexUnitNumber",
      "farmNumber",
      "farmName",
      "farmDescription",
      "siteBlock",
      "siteNumber",
      "streetNumber",
      "streetName",
      "line1",
      "line2",
      "line3",
      "suburb",
      "city",
      "region",
      "country",
      "postalCode",
      "latitude",
      "longitude",
      "purposes"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@ValidPhysicalAddress
@Entity
@Table(schema = "party", name = "physical_addresses")
public class PhysicalAddress implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The optional building floor for the physical address. */
  @Schema(description = "The optional building floor for the physical address")
  @JsonProperty
  @XmlElement(name = "BuildingFloor")
  @Size(max = 20)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.BuildingFloor.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "building_floor", length = 20)
  private String buildingFloor;

  /** The building name for the physical address that is required for a building address. */
  @Schema(
      description =
          "The building name for the physical address that is required for a building address")
  @JsonProperty
  @XmlElement(name = "BuildingName")
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.BuildingName.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "building_name", length = 50)
  private String buildingName;

  /** The optional building room for the physical address. */
  @Schema(description = "The optional building room for the physical address")
  @JsonProperty
  @XmlElement(name = "BuildingRoom")
  @Size(max = 30)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.BuildingRoom.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "building_room", length = 30)
  private String buildingRoom;

  /**
   * The town or city for the physical address that is required for a building, complex, postal,
   * site or street address.
   */
  @Schema(
      description =
          "The town or city for the physical address that is required for a building, complex, postal, site or street address")
  @JsonProperty
  @XmlElement(name = "City")
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.City.Pattern.message}",
      regexp = "^(?!\\s+)[\\pL\\pN-' ]+(?!\\s+)$")
  @Column(name = "city", length = 50)
  private String city;

  /** The complex name for the physical address that is required for a complex address. */
  @Schema(
      description =
          "The complex name for the physical address that is required for a complex address")
  @JsonProperty
  @XmlElement(name = "ComplexName")
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.ComplexName.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "complex_name", length = 50)
  private String complexName;

  /** The complex unit number for the physical address that is required for a complex address. */
  @Schema(
      description =
          "The complex unit number for the physical address that is required for a complex address")
  @JsonProperty
  @XmlElement(name = "ComplexUnitNumber")
  @Size(max = 20)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.ComplexUnitNumber.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "complex_unit_number", length = 20)
  private String complexUnitNumber;

  /** The ISO 3166-1 alpha-2 code for the country for the physical address. */
  @Schema(
      description = "The ISO 3166-1 alpha-2 code for the country for the physical address",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Country", required = true)
  @Size(min = 2, max = 2)
  @ValidCountryCode
  @Column(name = "country", length = 2)
  private String country;

  /** The date and time the physical address was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The optional farm description for the physical address. */
  @Schema(description = "The optional farm description for the physical address")
  @JsonProperty
  @XmlElement(name = "FarmDescription")
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.FarmDescription.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "farm_description", length = 50)
  private String farmDescription;

  /** The optional farm name for the physical address. */
  @Schema(description = "The optional farm name for the physical address")
  @JsonProperty
  @XmlElement(name = "FarmName")
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.FarmName.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "farm_name", length = 50)
  private String farmName;

  /** The farm number for the physical address that is required for a farm address. */
  @Schema(
      description = "The farm number for the physical address that is required for a farm address")
  @JsonProperty
  @XmlElement(name = "FarmNumber")
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.FarmNumber.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "farm_number", length = 50)
  private String farmNumber;

  /** The Universally Unique Identifier (UUID) for the physical address. */
  @Schema(
      description = "The Universally Unique Identifier (UUID) for the physical address",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The optional GPS latitude for the physical address. */
  @Schema(description = "The optional GPS latitude for the physical address")
  @JsonProperty
  @XmlElement(name = "Latitude")
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Latitude.Pattern.message}",
      regexp = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?)$")
  @Column(name = "latitude", length = 50)
  private String latitude;

  /**
   * The address line 1 for the physical address that is required for an international, postal or
   * unstructured address.
   */
  @Schema(
      description =
          "The address line 1 for the physical address that is required for an international, postal or unstructured address")
  @JsonProperty
  @XmlElement(name = "Line1")
  @Size(max = 100)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Line1.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "line1", length = 100)
  private String line1;

  /** The optional address line 2 for the physical address. */
  @Schema(description = "The optional address line 2 for the physical address")
  @JsonProperty
  @XmlElement(name = "Line2")
  @Size(max = 100)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Line2.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "line2", length = 100)
  private String line2;

  /** The optional address line 3 for the physical address. */
  @Schema(description = "The optional address line 3 for the physical address")
  @JsonProperty
  @XmlElement(name = "Line3")
  @Size(max = 100)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Line3.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "line3", length = 100)
  private String line3;

  /** The optional GPS longitude for the physical address. */
  @Schema(description = "The optional GPS longitude for the physical address")
  @JsonProperty
  @XmlElement(name = "Longitude")
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Longitude.Pattern.message}",
      regexp = "^[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$")
  @Column(name = "longitude", length = 50)
  private String longitude;

  /** The party the physical address is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("physicalAddressReference")
  @XmlTransient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private PartyBase party;

  /** The postal code for the physical address. */
  @Schema(description = "The postal code for the physical address", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "PostalCode", required = true)
  @Size(max = 30)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.PostalCode.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "postal_code", length = 30)
  private String postalCode;

  /** The optional comma-delimited codes for the physical address purposes. */
  @JsonIgnore
  @XmlTransient
  @Column(name = "purposes", length = 310)
  private String purposes;

  /** The optional ISO 3166-2 subdivision code for the region for the physical address. */
  @Schema(
      description =
          "The optional ISO 3166-2 subdivision code for the region for the physical address")
  @JsonProperty
  @XmlElement(name = "Region")
  @Size(min = 2, max = 3)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Region.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "region", length = 3)
  private String region;

  /** The code for the physical address role. */
  @Schema(
      description = "The code for the physical address role",
      required = true,
      allowableValues = {
        "business",
        "home",
        "main",
        "permanent",
        "registered_office",
        "residential",
        "service",
        "sole_trader",
        "temporary",
        "work"
      })
  @JsonProperty(required = true)
  @XmlElement(name = "Role", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Column(name = "role", length = 30, nullable = false)
  private String role;

  /** The site block for the physical address that is required for a site address. */
  @Schema(
      description = "The site block for the physical address that is required for a site address")
  @JsonProperty
  @XmlElement(name = "SiteBlock")
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.SiteBlock.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "site_block", length = 50)
  private String siteBlock;

  /** The site number for the physical address that is required for a site address. */
  @Schema(
      description = "The site number for the physical address that is required for a site address")
  @JsonProperty
  @XmlElement(name = "SiteNumber")
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.SiteNumber.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "site_number", length = 50)
  private String siteNumber;

  /** The street name for the physical address that is required for a street address. */
  @Schema(
      description =
          "The street name for the physical address that is required for a street address")
  @JsonProperty
  @XmlElement(name = "StreetName")
  @Size(max = 100)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.StreetName.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "street_name", length = 100)
  private String streetName;

  /** The street number for the physical address that is required for a street address. */
  @Schema(
      description =
          "The street number for the physical address that is required for a street address")
  @JsonProperty
  @XmlElement(name = "StreetNumber")
  @Size(max = 30)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.StreetNumber.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "street_number", length = 30)
  private String streetNumber;

  /** The optional suburb for the physical address. */
  @Schema(description = "The optional suburb for the physical address")
  @JsonProperty
  @XmlElement(name = "Suburb")
  @Size(min = 1, max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Suburb.Pattern.message}",
      regexp = "^(?!\\s+)[\\pL\\pN-' ]*(?!\\s+)$")
  @Column(name = "suburb", length = 50)
  private String suburb;

  /** The code for the physical address type. */
  @Schema(
      description = "The code for the physical address type",
      required = true,
      allowableValues = {
        "building",
        "complex",
        "farm",
        "international",
        "postal",
        "site",
        "street",
        "unstructured"
      })
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Column(name = "type", length = 30, nullable = false)
  private String type;

  /** The date and time the physical address was last updated. */
  @JsonIgnore
  @XmlTransient
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** Constructs a new <b>PhysicalAddress</b>. */
  public PhysicalAddress() {}

  /**
   * Constructs a new <b>PhysicalAddress</b>.
   *
   * @param type the code for the physical address type
   * @param role the code for the physical address role
   */
  public PhysicalAddress(String type, String role) {
    this.id = UuidCreator.getShortPrefixComb();
    this.type = type;
    this.role = role;
  }

  /**
   * Constructs a new <b>PhysicalAddress</b>.
   *
   * @param type the code for the physical address type
   * @param role the code for the physical address role
   * @param purpose the code for the physical address purpose
   */
  public PhysicalAddress(String type, String role, String purpose) {
    this.id = UuidCreator.getShortPrefixComb();
    this.type = type;
    this.role = role;
    this.purposes = purpose;
  }

  /**
   * Constructs a new <b>PhysicalAddress</b>.
   *
   * @param type the code for the physical address type
   * @param role the code for the physical address role
   * @param purposes the codes for the physical address purposes
   */
  public PhysicalAddress(String type, String role, Set<String> purposes) {
    this.id = UuidCreator.getShortPrefixComb();
    this.type = type;
    this.role = role;

    if ((purposes != null) && (purposes.size() > 0)) {
      this.purposes = StringUtils.collectionToCommaDelimitedString(purposes);
    }
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

    PhysicalAddress other = (PhysicalAddress) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the optional building floor for the physical address.
   *
   * @return the optional building floor for the physical address
   */
  public String getBuildingFloor() {
    return buildingFloor;
  }

  /**
   * Returns the building name for the physical address.
   *
   * @return the building name for the physical address
   */
  public String getBuildingName() {
    return buildingName;
  }

  /**
   * Returns the optional building room for the physical address.
   *
   * @return the optional building room for the physical address
   */
  public String getBuildingRoom() {
    return buildingRoom;
  }

  /**
   * Returns the town or city for the physical address.
   *
   * @return the town or city for the physical address
   */
  public String getCity() {
    return city;
  }

  /**
   * Returns the complex name for the physical address.
   *
   * @return the complex name for the physical address
   */
  public String getComplexName() {
    return complexName;
  }

  /**
   * Returns the complex unit number for the physical address.
   *
   * @return the complex unit number for the physical address
   */
  public String getComplexUnitNumber() {
    return complexUnitNumber;
  }

  /**
   * Returns the ISO 3166-1 alpha-2 code for the country for the physical address.
   *
   * @return the ISO 3166-1 alpha-2 code for the country for the physical address
   */
  public String getCountry() {
    return country;
  }

  /**
   * Returns the date and time the physical address was created.
   *
   * @return the date and time the physical address was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the optional farm description for the physical address.
   *
   * @return the optional farm description for the physical address
   */
  public String getFarmDescription() {
    return farmDescription;
  }

  /**
   * Returns the optional farm name for the physical address.
   *
   * @return the optional farm name for the physical address
   */
  public String getFarmName() {
    return farmName;
  }

  /**
   * Returns the farm number for the physical address.
   *
   * @return the farm number for the physical address
   */
  public String getFarmNumber() {
    return farmNumber;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the physical address.
   *
   * @return the Universally Unique Identifier (UUID) for the physical address
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the optional GPS latitude for the physical address.
   *
   * @return the optional GPS latitude for the physical address
   */
  public String getLatitude() {
    return latitude;
  }

  /**
   * Returns the address line 1 for the physical address.
   *
   * @return the address line 1 for the physical address
   */
  public String getLine1() {
    return line1;
  }

  /**
   * Returns the optional address line 2 for the physical address.
   *
   * @return the optional address line 2 for the physical address
   */
  public String getLine2() {
    return line2;
  }

  /**
   * Returns the optional address line 3 for the physical address.
   *
   * @return the optional address line 3 for the physical address
   */
  public String getLine3() {
    return line3;
  }

  /**
   * Returns the optional GPS longitude for the physical address.
   *
   * @return the optional GPS longitude for the physical address
   */
  public String getLongitude() {
    return longitude;
  }

  /**
   * Returns the party the physical address is associated with.
   *
   * @return the party the physical address is associated with
   */
  @Schema(hidden = true)
  public PartyBase getParty() {
    return party;
  }

  /**
   * Returns the postal code for the physical address.
   *
   * @return the postal code for the physical address
   */
  public String getPostalCode() {
    return postalCode;
  }

  /**
   * Returns the optional codes for the physical address purposes.
   *
   * @return the optional codes for the physical address purposes
   */
  @Schema(description = "The codes for the physical address purposes", required = true)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Purposes", required = true)
  @XmlElement(name = "Purpose", required = true)
  public Set<String> getPurposes() {
    if (this.purposes != null) {
      return StringUtils.commaDelimitedListToSet(this.purposes);
    } else {
      return new LinkedHashSet<>();
    }
  }

  /**
   * Returns the optional ISO 3166-2 subdivision code for the region for the physical address.
   *
   * @return the optional ISO 3166-2 subdivision code for the region for the physical address
   */
  public String getRegion() {
    return region;
  }

  /**
   * Returns the code for the physical address role.
   *
   * @return the code for the physical address role
   */
  public String getRole() {
    return role;
  }

  /**
   * Returns the site block for the physical address.
   *
   * @return the site block for the physical address
   */
  public String getSiteBlock() {
    return siteBlock;
  }

  /**
   * Returns the site number for the physical address.
   *
   * @return the site number for the physical address
   */
  public String getSiteNumber() {
    return siteNumber;
  }

  /**
   * Returns the street name for the physical address.
   *
   * @return the street name for the physical address
   */
  public String getStreetName() {
    return streetName;
  }

  /**
   * Returns the street number for the physical address.
   *
   * @return the street number for the physical address
   */
  public String getStreetNumber() {
    return streetNumber;
  }

  /**
   * Returns the optional suburb for the physical address.
   *
   * @return the optional suburb for the physical address
   */
  public String getSuburb() {
    return suburb;
  }

  /**
   * Returns the code for the physical address type.
   *
   * @return the code for the physical address type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the date and time the physical address was last updated.
   *
   * @return the date and time the physical address was last updated
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
    return (id == null) ? 0 : id.hashCode();
  }

  /**
   * Set the optional building floor for the physical address.
   *
   * @param buildingFloor the optional building floor for the physical address
   */
  public void setBuildingFloor(String buildingFloor) {
    this.buildingFloor = buildingFloor;
  }

  /**
   * Set the building name for the physical address.
   *
   * @param buildingName the building name for the physical address
   */
  public void setBuildingName(String buildingName) {
    this.buildingName = buildingName;
  }

  /**
   * Set the optional building room for the physical address.
   *
   * @param buildingRoom the optional building room for the physical address
   */
  public void setBuildingRoom(String buildingRoom) {
    this.buildingRoom = buildingRoom;
  }

  /**
   * Set the town or city for the physical address.
   *
   * @param city the town or city for the physical address
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Set the complex name for the physical address.
   *
   * @param complexName the complex name for the physical address
   */
  public void setComplexName(String complexName) {
    this.complexName = complexName;
  }

  /**
   * Set the complex unit number for the physical address.
   *
   * @param complexUnitNumber the complex unit number for the physical address
   */
  public void setComplexUnitNumber(String complexUnitNumber) {
    this.complexUnitNumber = complexUnitNumber;
  }

  /**
   * Set the ISO 3166-1 alpha-2 code for the country for the physical address.
   *
   * @param country the ISO 3166-1 alpha-2 code for the country for the physical address
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Set the optional farm description for the physical address.
   *
   * @param farmDescription the optional farm description for the physical address
   */
  public void setFarmDescription(String farmDescription) {
    this.farmDescription = farmDescription;
  }

  /**
   * Set the optional farm name for the physical address.
   *
   * @param farmName the optional farm name for the physical address
   */
  public void setFarmName(String farmName) {
    this.farmName = farmName;
  }

  /**
   * Set the farm number for the physical address.
   *
   * @param farmNumber farm number for the physical address
   */
  public void setFarmNumber(String farmNumber) {
    this.farmNumber = farmNumber;
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the physical address.
   *
   * @param id the Universally Unique Identifier (UUID) for the physical address
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the optional GPS latitude for the physical address.
   *
   * @param latitude the optional GPS latitude for the physical address
   */
  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  /**
   * Set the address line 1 for the physical address.
   *
   * @param line1 the address line 1 for the physical address
   */
  public void setLine1(String line1) {
    this.line1 = line1;
  }

  /**
   * Set the optional address line 2 for the physical address.
   *
   * @param line2 the optional address line 2 for the physical address
   */
  public void setLine2(String line2) {
    this.line2 = line2;
  }

  /**
   * Set the optional address line 3 for the physical address.
   *
   * @param line3 the optional address line 3 for the physical address
   */
  public void setLine3(String line3) {
    this.line3 = line3;
  }

  /**
   * Set the optional GPS longitude for the physical address.
   *
   * @param longitude the optional GPS longitude for the physical address
   */
  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  /**
   * Set the party the physical address is associated with.
   *
   * @param party the party the physical address is associated with
   */
  @Schema(hidden = true)
  public void setParty(PartyBase party) {
    this.party = party;
  }

  /**
   * Set the postal code for the physical address.
   *
   * @param postalCode the postal code for the physical address
   */
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  /**
   * Set the optional codes for the physical address purposes.
   *
   * @param purposes the optional codes for the physical address purposes
   */
  public void setPurposes(Set<String> purposes) {
    if ((purposes != null) && (purposes.size() > 0)) {
      this.purposes = StringUtils.collectionToCommaDelimitedString(purposes);
    } else {
      this.purposes = null;
    }
  }

  /**
   * Set the optional ISO 3166-2 subdivision code for the region for the physical address.
   *
   * @param region the optional ISO 3166-2 subdivision code for the region for the physical address
   */
  public void setRegion(String region) {
    this.region = region;
  }

  /**
   * Set the code for the physical address role.
   *
   * @param role the code for the physical address role
   */
  public void setRole(String role) {
    this.role = role;
  }

  /**
   * Set the site block for the physical address.
   *
   * @param siteBlock the site block for the physical address
   */
  public void setSiteBlock(String siteBlock) {
    this.siteBlock = siteBlock;
  }

  /**
   * Set the site number for the physical address.
   *
   * @param siteNumber the site number for the physical address
   */
  public void setSiteNumber(String siteNumber) {
    this.siteNumber = siteNumber;
  }

  /**
   * Set the street name for the physical address.
   *
   * @param streetName the street name for the physical address
   */
  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }

  /**
   * Set the street number for the physical address.
   *
   * @param streetNumber the street number for the physical address
   */
  public void setStreetNumber(String streetNumber) {
    this.streetNumber = streetNumber;
  }

  /**
   * Set the optional suburb for the physical address.
   *
   * @param suburb optional suburb for the physical address
   */
  public void setSuburb(String suburb) {
    this.suburb = suburb;
  }

  /**
   * Set the code for the physical address type.
   *
   * @param type the code for the physical address type
   */
  public void setType(String type) {
    this.type = type;
  }
}
