/*
 * Copyright 2020 Marcus Portmann
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

package digital.inception.security;

// ~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.LocalDateTimeAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>User</code> class holds the information for a user.
 *
 * @author Marcus Portmann
 */
@Schema(description = "User")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "userDirectoryId",
  "username",
  "firstName",
  "lastName",
  "mobileNumber",
  "phoneNumber",
  "email",
  "status",
  "password",
  "passwordAttempts",
  "passwordExpiry"
})
@XmlRootElement(name = "User", namespace = "http://security.inception.digital")
@XmlType(
    name = "User",
    namespace = "http://security.inception.digital",
    propOrder = {
      "userDirectoryId",
      "username",
      "firstName",
      "lastName",
      "mobileNumber",
      "phoneNumber",
      "email",
      "status",
      "password",
      "passwordAttempts",
      "passwordExpiry"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "security", name = "users")
@SuppressWarnings({"unused", "WeakerAccess"})
public class User implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The e-mail address for the user. */
  @Schema(description = "The e-mail address for the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Email", required = true)
  @NotNull
  @Size(max = 100)
  @Pattern(
      message = "invalid e-mail address",
      regexp =
          "^$|(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\"
              + ".[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d"
              + "-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\"
              + ".)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}"
              + "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:"
              + "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e"
              + "-\\x7f])+)\\])")
  @Column(name = "email", nullable = false, length = 100)
  private String email;

  /** The first name for the user. */
  @Schema(description = "The first name for the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "FirstName", required = true)
  @NotNull
  @Size(max = 100)
  @Column(name = "first_name", nullable = false, length = 100)
  private String firstName;

  /** The groups the user is associated with. */
  @JsonIgnore
  @XmlTransient
  @ManyToMany(mappedBy = "users")
  private Set<Group> groups = new HashSet<>();

  /** The Universally Unique Identifier (UUID) uniquely identifying the user. */
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The last name for the user. */
  @Schema(description = "The last name for the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "LastName", required = true)
  @NotNull
  @Size(max = 100)
  @Column(name = "last_name", nullable = false, length = 100)
  private String lastName;

  /** The mobile number for the user. */
  @Schema(description = "The mobile number for the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "MobileNumber", required = true)
  @NotNull
  @Size(max = 100)
  @Pattern(
      message = "invalid mobile number",
      regexp =
          "^$|(\\+|00)(297|93|244|1264|358|355|376|971|54|374|1684|1268|61"
              + "|43|994|257|32|229|226|880|359|973|1242|387|590|375|501|1441|591|55|1246|673|975|267|236|1"
              + "|61|41|56|86|225|237|243|242|682|57|269|238|506|53|5999|61|1345|357|420|49|253|1767|45|1809"
              + "|1829|1849|213|593|20|291|212|34|372|251|358|679|500|33|298|691|241|44|995|44|233|350|224"
              + "|590|220|245|240|30|1473|299|502|594|1671|592|852|504|385|509|36|62|44|91|246|353|98|964|354"
              + "|972|39|1876|44|962|81|76|77|254|996|855|686|1869|82|383|965|856|961|231|218|1758|423|94|266"
              + "|370|352|371|853|590|212|377|373|261|960|52|692|389|223|356|95|382|976|1670|258|222|1664|596"
              + "|230|265|60|262|264|687|227|672|234|505|683|31|47|977|674|64|968|92|507|64|51|63|680|675|48"
              + "|1787|1939|850|351|595|970|689|974|262|40|7|250|966|249|221|65|500|4779|677|232|503|378|252"
              + "|508|381|211|239|597|421|386|46|268|1721|248|963|1649|235|228|66|992|690|993|670|676|1868"
              + "|216|90|688|886|255|256|380|598|1|998|3906698|379|1784|58|1284|1340|84|678|681|685|967|27"
              + "|260|263)(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210"
              + "]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{4,20}$")
  @Column(name = "mobile_number", nullable = false, length = 100)
  private String mobileNumber;

  /**
   * The password or password hash for the user.
   *
   * <p>NOTE: The password is not required as part of the JSON or XML representation of the user,
   * other than when creating the user, so the field is nullable but the database column is not.
   */
  @Schema(description = "The password or password hash for the user")
  @JsonProperty
  @XmlElement(name = "Password")
  @Size(max = 100)
  @Column(name = "password", nullable = false, length = 100)
  private String password;

  /**
   * The number of failed authentication attempts as a result of an incorrect password for the user.
   */
  @Schema(
      description =
          "The number of failed authentication attempts as a result of an incorrect password for "
              + "the user",
      example = "0")
  @JsonProperty
  @XmlElement(name = "PasswordAttempts")
  @Column(name = "password_attempts", nullable = false)
  private Integer passwordAttempts;

  /** The date and time the password for the user expires. */
  @Schema(description = "The date and time the password for the user expires")
  @JsonProperty
  @XmlElement(name = "PasswordExpiry")
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "password_expiry", nullable = false)
  private LocalDateTime passwordExpiry;

  /** The phone number for the user. */
  @Schema(description = "The phone number for the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "PhoneNumber", required = true)
  @NotNull
  @Size(max = 100)
  @Column(name = "phone_number", nullable = false, length = 100)
  private String phoneNumber;

  /** The status for the user. */
  @Schema(
      description = "The status for the user",
      allowableValues = "0 = Inactive, 1 = Active, 2 = Locked, 3 = Expired",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  @Column(name = "status", nullable = false)
  private UserStatus status;

  /**
   * The Universally Unique Identifier (UUID) uniquely identifying the user directory the user is
   * associated with.
   */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) uniquely identifying the user directory the "
              + "user is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "UserDirectoryId", required = true)
  @NotNull
  @Column(name = "user_directory_id", nullable = false)
  private UUID userDirectoryId;

  /** The username for the user. */
  @Schema(description = "The username for the user", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Username", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "username", nullable = false, length = 100)
  private String username;

  /** Constructs a new <code>User</code>. */
  public User() {}

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <code>true</code> if this object is the same as the object argument otherwise <code>
   *     false</code>
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

    User other = (User) object;

    return (id != null) && id.equals(other.id);
  }

  /**
   * Returns the e-mail address for the user
   *
   * @return the e-mail address for the user
   */
  public String getEmail() {
    return email;
  }

  /**
   * Set the e-mail address for the user.
   *
   * @param email the e-mail address for the user
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Returns the first name for the user
   *
   * @return the first name for the user
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Set the first name for the user.
   *
   * @param firstName the first name for the user
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Returns the groups the user is associated with.
   *
   * @return the groups the user is associated with
   */
  public Set<Group> getGroups() {
    return groups;
  }

  /**
   * Set the groups the user is associated with.
   *
   * @param groups the groups the user is associated with
   */
  public void setGroups(Set<Group> groups) {
    this.groups = groups;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the user.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the user
   */
  public UUID getId() {
    return id;
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the user.
   *
   * @param id the Universally Unique Identifier (UUID) uniquely identifying the user
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Returns the last name for the user
   *
   * @return the last name for the user
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Set the last name for the user.
   *
   * @param lastName the last name for the user
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Returns the mobile number for the user
   *
   * @return the mobile number for the user
   */
  public String getMobileNumber() {
    return mobileNumber;
  }

  /**
   * Set the mobile number for the user.
   *
   * @param mobileNumber the mobile number for the user
   */
  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  /**
   * Returns the password or password hash for the user
   *
   * @return the password or password hash for the user
   */
  public String getPassword() {
    return password;
  }

  /**
   * Set the password or password hash for the user.
   *
   * @param password the password or password hash for the user
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Returns the number of failed authentication attempts as a result of an incorrect password for
   * the user
   *
   * @return the number of failed authentication attempts as a result of an incorrect password for
   *     the user
   */
  public Integer getPasswordAttempts() {
    return passwordAttempts;
  }

  /**
   * Set the password attempts for the user.
   *
   * @param passwordAttempts the password attempts for the user
   */
  public void setPasswordAttempts(Integer passwordAttempts) {
    this.passwordAttempts = passwordAttempts;
  }

  /**
   * Returns the date and time the password for the user expires
   *
   * @return the date and time the password for the user expires
   */
  public LocalDateTime getPasswordExpiry() {
    return passwordExpiry;
  }

  /**
   * Set the date and time the password for the user expires.
   *
   * @param passwordExpiry the date and time the password for the user expires
   */
  public void setPasswordExpiry(LocalDateTime passwordExpiry) {
    this.passwordExpiry = passwordExpiry;
  }

  /**
   * Returns the phone number for the user.
   *
   * @return the phone number for the user
   */
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Set the phone number for the user.
   *
   * @param phoneNumber the phone number for the user
   */
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * Returns the status for the user.
   *
   * @return the status for the user
   */
  public UserStatus getStatus() {
    return status;
  }

  /**
   * Set the status for the user.
   *
   * @param status the status for the user
   */
  public void setStatus(UserStatus status) {
    this.status = status;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the user directory the
   * user is associated with.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the user directory the
   *     user is associated with
   */
  public UUID getUserDirectoryId() {
    return userDirectoryId;
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the user directory the user
   * is associated with.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory the user is associated with
   */
  public void setUserDirectoryId(UUID userDirectoryId) {
    this.userDirectoryId = userDirectoryId;
  }

  /**
   * Returns the username for the user.
   *
   * @return the username for the user
   */
  public String getUsername() {
    if (username != null) {
      username = username.toLowerCase();
    }

    return username;
  }

  /**
   * Set the username for the user.
   *
   * @param username the username for the user
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Has the password for the user expired?
   *
   * @return <code>true</code> if the password for the user has expired or <code>false</code>
   *     otherwise
   */
  public boolean hasPasswordExpired() {
    if (passwordExpiry != null) {
      return LocalDateTime.now().isAfter(passwordExpiry);
    }

    return false;
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
   * Is the user active?
   *
   * @return <code>true</code> if the user is active or <code>false</code> otherwise
   */
  @JsonIgnore
  @XmlTransient
  public boolean isActive() {
    return (status == UserStatus.ACTIVE);
  }

  /**
   * Is the user expired?
   *
   * @return <code>true</code> if the user is expired or <code>false</code> otherwise
   */
  @JsonIgnore
  @XmlTransient
  public boolean isExpired() {
    return (status == UserStatus.EXPIRED);
  }

  /**
   * Is the user locked?
   *
   * @return <code>true</code> if the user is locked or <code>false</code> otherwise
   */
  @JsonIgnore
  @XmlTransient
  public boolean isLocked() {
    return (status == UserStatus.LOCKED);
  }
}
