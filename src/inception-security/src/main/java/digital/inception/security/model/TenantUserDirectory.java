package digital.inception.security.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.UUID;

/**
 * The {@code TenantUserDirectory} class holds the information for a tenant user directory.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A tenant user directory association")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"tenantId", "userDirectoryId"})
@XmlRootElement(name = "TenantUserDirectory", namespace = "https://inception.digital/security")
@XmlType(
    name = "TenantUserDirectory",
    namespace = "https://inception.digital/security",
    propOrder = {"tenantId", "userDirectoryId"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class TenantUserDirectory implements Serializable {

  /** The ID for the tenant. */
  @Schema(description = "The ID for the tenant", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  @NotNull
  @Id
  @Column(name = "tenantId", nullable = false)
  private UUID tenantId;

  /** The ID for the user directory. */
  @Schema(
      description = "The ID for the user directory",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "UserDirectoryId", required = true)
  @NotNull
  @Id
  @Column(name = "userDirectoryId", nullable = false)
  private UUID userDirectoryId;

  /** Creates a new {@code TenantUserDirectory} instance. */
  public TenantUserDirectory() {}

  /**
   * Creates a new {@code TenantUserDirectory} instance.
   *
   * @param tenantId the ID for the tenant
   * @param userDirectoryId the ID for the user directory
   */
  public TenantUserDirectory(UUID tenantId, UUID userDirectoryId) {
    this.tenantId = tenantId;
    this.userDirectoryId = userDirectoryId;
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
   * Returns the ID for the user directory.
   *
   * @return the ID for the user directory
   */
  public UUID getUserDirectoryId() {
    return userDirectoryId;
  }

  /**
   * Set the ID for the tenant.
   *
   * @param tenantId the ID for the tenant
   */
  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Set the ID for the user directory.
   *
   * @param userDirectoryId the ID for the user directory
   */
  public void setUserDirectoryId(UUID userDirectoryId) {
    this.userDirectoryId = userDirectoryId;
  }
}
