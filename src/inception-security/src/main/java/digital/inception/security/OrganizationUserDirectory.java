package digital.inception.security;

// ~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>OrganizationUserDirectory</code> class holds the information for an organization user
 * directory.
 *
 * @author Marcus Portmann
 */
@Schema(description = "OrganizationUserDirectory")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"organizationId", "userDirectoryId"})
@XmlRootElement(name = "OrganizationUserDirectory", namespace = "http://security.inception.digital")
@XmlType(
    name = "OrganizationUserDirectory",
    namespace = "http://security.inception.digital",
    propOrder = {"organizationId", "userDirectoryId"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class OrganizationUserDirectory implements Serializable {

  /**
   * The Universally Unique Identifier (UUID) uniquely identifying the organization.
   */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) uniquely identifying the organization",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "OrganizationId", required = true)
  @NotNull
  @Id
  @Column(name = "organizationId", nullable = false)
  private UUID organizationId;

  /**
   * The Universally Unique Identifier (UUID) uniquely identifying the user directory.
   */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) uniquely identifying the user directory",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "UserDirectoryId", required = true)
  @NotNull
  @Id
  @Column(name = "userDirectoryId", nullable = false)
  private UUID userDirectoryId;

  /**
   * Constructs a new <code>OrganizationUserDirectory</code>.
   */
  public OrganizationUserDirectory() {
  }

  /**
   * Constructs a new <code>OrganizationUserDirectory</code>.
   *
   * @param organizationId  the Universally Unique Identifier (UUID) uniquely identifying the
   *                        organization
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *                        directory
   */
  public OrganizationUserDirectory(UUID organizationId, UUID userDirectoryId) {
    this.organizationId = organizationId;
    this.userDirectoryId = userDirectoryId;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the organization.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the organization
   */
  public UUID getOrganizationId() {
    return organizationId;
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the organization.
   *
   * @param organizationId the Universally Unique Identifier (UUID) uniquely identifying the
   *                       organization
   */
  public void setOrganizationId(UUID organizationId) {
    this.organizationId = organizationId;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the user directory.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the user directory
   */
  public UUID getUserDirectoryId() {
    return userDirectoryId;
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the user directory.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *                        directory
   */
  public void setUserDirectoryId(UUID userDirectoryId) {
    this.userDirectoryId = userDirectoryId;
  }
}
