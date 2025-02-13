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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.jpa.StringListAttributeConverter;
import digital.inception.party.constraint.ValidCountryCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * The <b>PhysicalAddress</b> class holds the information for a physical address for an organization
 * or person.
 *
 * <p>The primary key for the physical address (ID) is a surrogate key to enable multiple physical
 * addresses with the same type and role for a single organization or person. The surrogate key also
 * supports the management of related data in one or more external stores, e.g. an image of the
 * proof of residential address in an enterprise content management persistence. This approach
 * allows an entity to be modified without impacting the related data's referential integrity, for
 * example, when correcting an error that occurred during the initial capture of the information for
 * a physical address.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A physical address for an organization or person")
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
  "line4",
  "suburb",
  "city",
  "region",
  "country",
  "postalCode",
  "latitude",
  "longitude",
  "purposes"
})
@XmlRootElement(name = "PhysicalAddress", namespace = "https://inception.digital/party")
@XmlType(
    name = "PhysicalAddress",
    namespace = "https://inception.digital/party",
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
      "line4",
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
@Entity
@Table(name = "party_physical_addresses")
public class PhysicalAddress implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The building floor for the physical address. */
  @Schema(description = "The building floor for the physical address")
  @JsonProperty
  @XmlElement(name = "BuildingFloor")
  @Size(max = 20)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.BuildingFloor.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}]*(?!\\s+)$")
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
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,' ]*(?!\\s+)$")
  @Column(name = "building_name", length = 50)
  private String buildingName;

  /** The building room for the physical address. */
  @Schema(description = "The building room for the physical address")
  @JsonProperty
  @XmlElement(name = "BuildingRoom")
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.BuildingRoom.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "building_room", length = 50)
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
      regexp = "^(?!\\s+)[(?U)\\p{L}-., ]*(?!\\s+)$")
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
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,' ]*(?!\\s+)$")
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
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "complex_unit_number", length = 20)
  private String complexUnitNumber;

  /** The ISO 3166-1 alpha-2 code for the country for the physical address. */
  @Schema(
      description = "The ISO 3166-1 alpha-2 code for the country for the physical address",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Country", required = true)
  @Size(min = 2, max = 2)
  @ValidCountryCode
  @Column(name = "country", length = 2)
  private String country;

  /** The farm description for the physical address. */
  @Schema(description = "The farm description for the physical address")
  @JsonProperty
  @XmlElement(name = "FarmDescription")
  @Size(max = 100)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.FarmDescription.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "farm_description", length = 100)
  private String farmDescription;

  /** The farm name for the physical address. */
  @Schema(description = "The farm name for the physical address")
  @JsonProperty
  @XmlElement(name = "FarmName")
  @Size(max = 100)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.FarmName.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "farm_name", length = 100)
  private String farmName;

  /** The farm number for the physical address that is required for a farm address. */
  @Schema(
      description = "The farm number for the physical address that is required for a farm address")
  @JsonProperty
  @XmlElement(name = "FarmNumber")
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.FarmNumber.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "farm_number", length = 50)
  private String farmNumber;

  /** The ID for the physical address. */
  @Schema(
      description = "The ID for the physical address",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The GPS latitude for the physical address. */
  @Schema(description = "The GPS latitude for the physical address")
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
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "line1", length = 100)
  private String line1;

  /** The address line 2 for the physical address. */
  @Schema(description = "The address line 2 for the physical address")
  @JsonProperty
  @XmlElement(name = "Line2")
  @Size(max = 100)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Line2.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "line2", length = 100)
  private String line2;

  /** The address line 3 for the physical address. */
  @Schema(description = "The address line 3 for the physical address")
  @JsonProperty
  @XmlElement(name = "Line3")
  @Size(max = 100)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Line3.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "line3", length = 100)
  private String line3;

  /** The address line 4 for the physical address. */
  @Schema(description = "The address line 4 for the physical address")
  @JsonProperty
  @XmlElement(name = "Line4")
  @Size(max = 100)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Line4.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "line4", length = 100)
  private String line4;

  /** The GPS longitude for the physical address. */
  @Schema(description = "The GPS longitude for the physical address")
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
  @Schema(
      description = "The postal code for the physical address",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "PostalCode", required = true)
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.PostalCode.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,# ]*(?!\\s+)$")
  @Column(name = "postal_code", length = 50)
  private String postalCode;

  /** The codes for the physical address purposes. */
  @Schema(description = "The codes for the physical address purposes")
  @JsonProperty
  @XmlElementWrapper(name = "Purposes")
  @XmlElement(name = "Purpose")
  @Size(min = 1, max = 10)
  @Convert(converter = StringListAttributeConverter.class)
  @Column(name = "purposes", length = 510)
  private List<String> purposes;

  /** The ISO 3166-2 subdivision code for the region for the physical address. */
  @Schema(description = "The ISO 3166-2 subdivision code for the region for the physical address")
  @JsonProperty
  @XmlElement(name = "Region")
  @Size(min = 4, max = 6)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Region.Pattern.message}",
      regexp = "[A-Z]{2}-[A-Z0-9]{1,3}")
  @Column(name = "region", length = 6)
  private String region;

  /** The code for the physical address role. */
  @Schema(
      description = "The code for the physical address role",
      requiredMode = Schema.RequiredMode.REQUIRED,
      allowableValues = {
        "business",
        "home",
        "main",
        "permanent",
        "postal",
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
  @Size(min = 1, max = 50)
  @Column(name = "role", length = 50, nullable = false)
  private String role;

  /** The site block for the physical address that is required for a site address. */
  @Schema(
      description = "The site block for the physical address that is required for a site address")
  @JsonProperty
  @XmlElement(name = "SiteBlock")
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.SiteBlock.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,# ]*(?!\\s+)$")
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
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,# ]*(?!\\s+)$")
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
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "street_name", length = 100)
  private String streetName;

  /** The street number for the physical address that is required for a street address. */
  @Schema(
      description =
          "The street number for the physical address that is required for a street address")
  @JsonProperty
  @XmlElement(name = "StreetNumber")
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.StreetNumber.Pattern.message}",
      regexp = "^(?!\\s+)[0-9(?U)\\p{L}-.,#' ]*(?!\\s+)$")
  @Column(name = "street_number", length = 50)
  private String streetNumber;

  /** The suburb for the physical address. */
  @Schema(description = "The suburb for the physical address")
  @JsonProperty
  @XmlElement(name = "Suburb")
  @Size(min = 1, max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Suburb.Pattern.message}",
      regexp = "^(?!\\s+)[(?U)\\p{L}-., ]*(?!\\s+)$")
  @Column(name = "suburb", length = 50)
  private String suburb;

  /** The code for the physical address type. */
  @Schema(
      description = "The code for the physical address type",
      requiredMode = Schema.RequiredMode.REQUIRED,
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
  @Size(min = 1, max = 50)
  @Column(name = "type", length = 50, nullable = false)
  private String type;

  /** Constructs a new <b>PhysicalAddress</b>. */
  public PhysicalAddress() {}

  /**
   * Constructs a new <b>PhysicalAddress</b>.
   *
   * @param type the code for the physical address type
   * @param role the code for the physical address role
   */
  public PhysicalAddress(String type, String role) {
    this.id = UuidCreator.getTimeOrderedEpoch();
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
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.type = type;
    this.role = role;
    this.purposes = new ArrayList<>(List.of(purpose));
  }

  /**
   * Constructs a new <b>PhysicalAddress</b>.
   *
   * @param type the code for the physical address type
   * @param role the code for the physical address role
   * @param purposes the codes for the physical address purposes
   */
  public PhysicalAddress(String type, String role, List<String> purposes) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.type = type;
    this.role = role;
    this.purposes = purposes;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument, otherwise <b>false</b>
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
   * Returns the building floor for the physical address.
   *
   * @return the building floor for the physical address
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
   * Returns the building room for the physical address.
   *
   * @return the building room for the physical address
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
   * Returns the farm description for the physical address.
   *
   * @return the farm description for the physical address
   */
  public String getFarmDescription() {
    return farmDescription;
  }

  /**
   * Returns the farm name for the physical address.
   *
   * @return the farm name for the physical address
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
   * Returns the ID for the physical address.
   *
   * @return the ID for the physical address
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the GPS latitude for the physical address.
   *
   * @return the GPS latitude for the physical address
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
   * Returns the address line 2 for the physical address.
   *
   * @return the address line 2 for the physical address
   */
  public String getLine2() {
    return line2;
  }

  /**
   * Returns the address line 3 for the physical address.
   *
   * @return the address line 3 for the physical address
   */
  public String getLine3() {
    return line3;
  }

  /**
   * Returns the address line 4 for the physical address.
   *
   * @return the address line 4 for the physical address
   */
  public String getLine4() {
    return line4;
  }

  /**
   * Returns the GPS longitude for the physical address.
   *
   * @return the GPS longitude for the physical address
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
   * Returns the codes for the physical address purposes.
   *
   * @return the codes for the physical address purposes
   */
  public List<String> getPurposes() {
    return purposes;
  }

  /**
   * Returns the ISO 3166-2 subdivision code for the region for the physical address.
   *
   * @return the ISO 3166-2 subdivision code for the region for the physical address
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
   * Returns the suburb for the physical address.
   *
   * @return the suburb for the physical address
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
   * Returns whether the physical address has the specified purpose.
   *
   * @param purpose the code for the physical address purpose
   * @return <b>true</b> if the physical address has the specified physical address purpose or
   *     <b>false</b> otherwise
   */
  @JsonIgnore
  public boolean hasPurpose(String purpose) {
    if (purposes != null) {
      return purposes.contains(purpose);
    } else {
      return false;
    }
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
   * Set the building floor for the physical address.
   *
   * @param buildingFloor the building floor for the physical address
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
   * Set the building room for the physical address.
   *
   * @param buildingRoom the building room for the physical address
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
   * Set the farm description for the physical address.
   *
   * @param farmDescription the farm description for the physical address
   */
  public void setFarmDescription(String farmDescription) {
    this.farmDescription = farmDescription;
  }

  /**
   * Set the farm name for the physical address.
   *
   * @param farmName the farm name for the physical address
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
   * Set the ID for the physical address.
   *
   * @param id the ID for the physical address
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the GPS latitude for the physical address.
   *
   * @param latitude the GPS latitude for the physical address
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
   * Set the address line 2 for the physical address.
   *
   * @param line2 the address line 2 for the physical address
   */
  public void setLine2(String line2) {
    this.line2 = line2;
  }

  /**
   * Set the address line 3 for the physical address.
   *
   * @param line3 the address line 3 for the physical address
   */
  public void setLine3(String line3) {
    this.line3 = line3;
  }

  /**
   * Set the address line 4 for the physical address.
   *
   * @param line4 the address line 4 for the physical address
   */
  public void setLine4(String line4) {
    this.line4 = line4;
  }

  /**
   * Set the GPS longitude for the physical address.
   *
   * @param longitude the GPS longitude for the physical address
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
   * Set the codes for the physical address purposes.
   *
   * @param purposes the codes for the physical address purposes
   */
  public void setPurposes(List<String> purposes) {
    this.purposes = purposes;
  }

  /**
   * Set the ISO 3166-2 subdivision code for the region for the physical address.
   *
   * @param region the ISO 3166-2 subdivision code for the region for the physical address
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
   * Set the suburb for the physical address.
   *
   * @param suburb suburb for the physical address
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
