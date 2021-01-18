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
import digital.inception.party.constraints.ValidCountryCode;
import digital.inception.party.constraints.ValidPhysicalAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The <code>PhysicalAddress</code> class holds the information for a physical address.
 *
 * <p><b>NOTE:</b> The JPA 2.2 spec (10.6) does not support attribute converters for attributes
 * annotated with @Id. If Enum types are used for these attributes then the ordinal value is always
 * used. As a result, the purpose attribute for this class is an Integer and the Getter and Setters
 * (a.k.a. Accessors and Mutators) convert to and from the Enum type. A consequence of this is that
 * the attribute is marked as @JsonIgnore and @XmlTransient and the Getter is annotated
 * with @JsonProperty and @XmlElement.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A physical address associated with a party")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "type",
  "purpose",
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
  "longitude"
})
@XmlRootElement(name = "PhysicalAddress", namespace = "http://party.inception.digital")
@XmlType(
    name = "PhysicalAddress",
    namespace = "http://party.inception.digital",
    propOrder = {
      "type",
      "purpose",
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
      "longitude"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@ValidPhysicalAddress
@Entity
@Table(schema = "party", name = "physical_addresses")
@IdClass(PhysicalAddressId.class)
public class PhysicalAddress implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The building floor for the physical address. */
  @Schema(description = "The building floor for the physical address")
  @JsonProperty
  @XmlElement(name = "BuildingFloor")
  @Size(max = 20)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.BuildingFloor.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "building_floor")
  private String buildingFloor;

  /** The building name for the physical address. */
  @Schema(
      description =
          "The building name for the physical address that is required for a building address",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "BuildingName", required = true)
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.BuildingName.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "building_name")
  private String buildingName;

  /** The building room for the physical address. */
  @Schema(description = "The building room for the physical address")
  @JsonProperty
  @XmlElement(name = "BuildingRoom")
  @Size(max = 20)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.BuildingRoom.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "building_room")
  private String buildingRoom;

  /** The town or town or city for the physical address. */
  @Schema(description = "The town or town or city for the physical address", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "City", required = true)
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.City.Pattern.message}",
      regexp = "^(?!\\s+)[\\pL\\pN-' ]+(?!\\s+)$")
  @Column(name = "city")
  private String city;

  /** The complex name for the physical address. */
  @Schema(
      description =
          "The complex name for the physical address that is required for a complex address",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "ComplexName", required = true)
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.ComplexName.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "complex_name")
  private String complexName;

  /** The complex unit number for the physical address. */
  @Schema(
      description =
          "The complex unit number for the physical address that is required for a complex address",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "ComplexUnitNumber", required = true)
  @Size(max = 20)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.ComplexUnitNumber.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "complex_unit_number")
  private String complexUnitNumber;

  /** The ISO 3166-1 alpha-2 code for the country for the physical address. */
  @Schema(
      description = "The ISO 3166-1 alpha-2 code for the country for the physical address",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Country", required = true)
  @Size(max = 10)
  @ValidCountryCode
  @Column(name = "country")
  private String country;

  /** The date and time the physical address was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The farm description for the physical address. */
  @Schema(description = "The farm description for the physical address")
  @JsonProperty
  @XmlElement(name = "FarmDescription")
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.FarmDescription.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "farm_description")
  private String farmDescription;

  /** The farm name for the physical address. */
  @Schema(description = "The farm name for the physical address")
  @JsonProperty
  @XmlElement(name = "FarmName")
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.FarmName.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "farm_name")
  private String farmName;

  /** The farm number for the physical address. */
  @Schema(
      description = "The farm number for the physical address that is required for a farm address",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "FarmNumber", required = true)
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.FarmNumber.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "farm_number")
  private String farmNumber;

  /** The optional GPS latitude for the physical address. */
  @Schema(description = "The optional GPS latitude for the physical address")
  @JsonProperty
  @XmlElement(name = "Latitude")
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Latitude.Pattern.message}",
      regexp = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?)$")
  @Column(name = "latitude")
  private String latitude;

  /** The address line 1 for the physical address. */
  @Schema(
      description =
          "The address line 1 for the physical address that is required for an international or unstructured address",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Line1", required = true)
  @Size(max = 100)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Line1.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "line1")
  private String line1;

  /** The address line 2 for the physical address. */
  @Schema(description = "The address line 2 for the physical address")
  @JsonProperty
  @XmlElement(name = "Line2")
  @Size(max = 100)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Line2.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "line2")
  private String line2;

  /** The address line 3 for the physical address. */
  @Schema(description = "The address line 3 for the physical address")
  @JsonProperty
  @XmlElement(name = "Line3")
  @Size(max = 100)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Line3.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "line3")
  private String line3;

  /** The optional GPS longitude for the physical address. */
  @Schema(description = "The optional GPS longitude for the physical address")
  @JsonProperty
  @XmlElement(name = "Longitude")
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Longitude.Pattern.message}",
      regexp = "^[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$")
  @Column(name = "longitude")
  private String longitude;

  /** The party the physical address is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("physicalAddressReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private Party party;

  /** The postal code for the physical address. */
  @Schema(description = "The postal code for the physical address", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "PostalCode", required = true)
  @Size(max = 30)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.PostalCode.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "postal_code")
  private String postalCode;

  /** The physical address purpose. */
  @JsonIgnore
  @XmlTransient
  @NotNull
  @Id
  @Column(name = "purpose", nullable = false)
  private Integer purpose;

  /** The optional code for the region for the physical address. */
  @Schema(description = "The optional code for the region for the physical address")
  @JsonProperty
  @XmlElement(name = "Region")
  @Size(min = 1, max = 10)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Region.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "region")
  private String region;

  /** The site block for the physical address. */
  @Schema(
      description = "The site block for the physical address that is required for a site address",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SiteBlock", required = true)
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.SiteBlock.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "site_block")
  private String siteBlock;

  /** The site number for the physical address. */
  @Schema(
      description = "The site number for the physical address that is required for a site address",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "SiteNumber", required = true)
  @Size(max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.SiteNumber.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "site_number")
  private String siteNumber;

  /** The street name for the physical address. */
  @Schema(
      description =
          "The street name for the physical address that is required for a street address")
  @JsonProperty
  @XmlElement(name = "StreetName")
  @Size(max = 100)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.StreetName.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "street_name")
  private String streetName;

  /** The street number for the physical address. */
  @Schema(
      description =
          "The street number for the physical address that is required for a street address")
  @JsonProperty
  @XmlElement(name = "StreetNumber")
  @Size(max = 30)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.StreetNumber.Pattern.message}",
      regexp = "^[\\pL\\pN-' ]*$")
  @Column(name = "street_number")
  private String streetNumber;

  /** The optional suburb for the physical address. */
  @Schema(description = "The optional suburb for the physical address")
  @JsonProperty
  @XmlElement(name = "Suburb")
  @Size(min = 1, max = 50)
  @Pattern(
      message = "{digital.inception.party.PhysicalAddress.Suburb.Pattern.message}",
      regexp = "^(?!\\s+)[\\pL\\pN-' ]*(?!\\s+)$")
  @Column(name = "suburb")
  private String suburb;

  /** The physical address type. */
  @Schema(description = "The physical address type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Column(name = "type", nullable = false)
  private PhysicalAddressType type;

  /** The date and time the physical address was last updated. */
  @JsonIgnore
  @XmlTransient
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** Constructs a new <code>PhysicalAddress</code>. */
  public PhysicalAddress() {}

  /**
   * Constructs a new <code>PhysicalAddress</code>.
   *
   * @param type the physical address type
   * @param purpose the physical address purpose
   */
  public PhysicalAddress(PhysicalAddressType type, PhysicalAddressPurpose purpose) {
    this.type = type;
    this.purpose = PhysicalAddressPurpose.toNumericCode(purpose);
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <code>true</code> if this object is the same as the object argument otherwise <code>
   * false</code>
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

    return Objects.equals(party, other.party)
        && Objects.equals(type, other.type)
        && Objects.equals(purpose, other.purpose);
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
   * @return building room for the physical address
   */
  public String getBuildingRoom() {
    return buildingRoom;
  }

  /**
   * Returns the town or town or city for the physical address.
   *
   * @return the town or town or city for the physical address
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
  public Party getParty() {
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
   * Returns the physical address purpose.
   *
   * @return the physical address purpose
   */
  @Schema(description = "The physical address purpose", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Purpose", required = true)
  public PhysicalAddressPurpose getPurpose() {
    return PhysicalAddressPurpose.fromNumericCode(purpose);
  }

  /**
   * Returns the optional code for the region for the physical address.
   *
   * @return the optional code for the region for the physical address
   */
  public String getRegion() {
    return region;
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
   * Returns the physical address type.
   *
   * @return the physical address type
   */
  public PhysicalAddressType getType() {
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
    return (((party == null) || (party.getId() == null)) ? 0 : party.getId().hashCode())
        + ((type == null) ? 0 : type.hashCode())
        + ((purpose == null) ? 0 : purpose.hashCode());
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
   * Set the town or town or city for the physical address.
   *
   * @param city the town or town or city for the physical address
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
  public void setParty(Party party) {
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
   * Set the physical address purpose.
   *
   * @param purpose the physical address purpose
   */
  public void setPurpose(PhysicalAddressPurpose purpose) {
    this.purpose = PhysicalAddressPurpose.toNumericCode(purpose);
  }

  /**
   * Set the optional code for the region for the physical address.
   *
   * @param region the optional code for the region for the physical address
   */
  public void setRegion(String region) {
    this.region = region;
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
   * Set the physical address type.
   *
   * @param type the physical address type
   */
  public void setType(PhysicalAddressType type) {
    this.type = type;
  }
}
