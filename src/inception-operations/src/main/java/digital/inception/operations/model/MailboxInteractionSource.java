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

package digital.inception.operations.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * The <b>MailboxInteractionSource</b> class holds the information for a mailbox interaction source.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A mailbox interaction source")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"type"})
@JsonPropertyOrder({
  "id",
  "name",
  "protocol",
  "host",
  "port",
  "authority",
  "principal",
  "credential",
  "emailAddress",
  "archiveMail",
  "deleteMail",
  "debug"
})
@XmlRootElement(
    name = "MailboxInteractionSource",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "MailboxInteractionSource",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "id",
      "name",
      "protocol",
      "host",
      "port",
      "authority",
      "principal",
      "credential",
      "emailAddress",
      "archiveMail",
      "deleteMail",
      "debug"
    })
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
@Table(name = "operations_mailbox_interaction_sources")
public class MailboxInteractionSource extends InteractionSourceBase implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** Should mail retrieved from the mailbox be archived? */
  @Schema(
      description = "Should mail retrieved from the mailbox be archived",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ArchiveMail", required = true)
  @NotNull
  @Column(name = "archive_mail", nullable = false)
  private boolean archiveMail;

  /**
   * The OAuth authority responsible for issuing tokens and managing authentication and
   * authorization requests.
   */
  @Schema(
      description =
          "The OAuth authority responsible for issuing tokens and managing authentication and authorization requests")
  @JsonProperty
  @XmlElement(name = "Authority")
  @Size(max = 1000)
  @Column(name = "authority", length = 1000)
  private String authority;

  /** The credential used to authenticate the entity attempting to access the mailbox. */
  @Schema(
      description =
          "The credential used to authenticate the entity attempting to access the mailbox",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Credential", required = true)
  @NotNull
  @Size(min = 1, max = 1000)
  @Column(name = "credential", length = 1000, nullable = false)
  private String credential;

  /** Is debugging enabled for the mailbox interaction source? */
  @Schema(
      description = "Is debugging enabled for the mailbox interaction source",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Debug", required = true)
  @NotNull
  @Column(name = "debug", nullable = false)
  private boolean debug;

  /** Should mail retrieved from the mailbox be deleted? */
  @Schema(
      description = "Should mail retrieved from the mailbox be deleted",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DeleteMail", required = true)
  @NotNull
  @Column(name = "delete_mail", nullable = false)
  private boolean deleteMail;

  /** The email address for the mailbox. */
  @Schema(
      description = "The email address for the mailbox",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "EmailAddress", required = true)
  @NotNull
  @Size(min = 1, max = 1000)
  @Column(name = "email_address", length = 1000, nullable = false)
  private String emailAddress;

  /** The hostname or IP address for the service hosting the mailbox. */
  @Schema(
      description = "The hostname or IP address for the service hosting the mailbox",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Host", required = true)
  @NotNull
  @Size(min = 1, max = 500)
  @Column(name = "host", length = 500, nullable = false)
  private String host;

  /** The network port for the service hosting the mailbox. */
  @Schema(
      description = "The network port for the service hosting the mailbox",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Port", required = true)
  @NotNull
  @Column(name = "port", nullable = false)
  private Integer port;

  /**
   * The principal identifying the entity that is attempting to authenticate and gain access to the
   * mailbox.
   */
  @Schema(
      description =
          "The principal identifying the entity that is attempting to authenticate and gain access to the mailbox",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Principal", required = true)
  @NotNull
  @Size(min = 1, max = 1000)
  @Column(name = "principal", length = 1000, nullable = false)
  private String principal;

  /** The service provider and email protocol for the service hosting the mailbox. */
  @Schema(
      description = "The service provider and email protocol for the service hosting the mailbox",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Protocol", required = true)
  @NotNull
  @Column(name = "protocol", length = 50, nullable = false)
  private MailboxProtocol protocol;

  /** Constructs a new <b>MailboxInteractionSource</b>. */
  public MailboxInteractionSource() {
    super(InteractionSourceType.MAILBOX);
  }

  /**
   * Constructs a new <b>MailboxInteractionSource</b>.
   *
   * @param id the ID for the mailbox interaction source
   * @param name the name of the mailbox interaction source
   * @param protocol the service provider and email protocol for the service hosting the mailbox
   * @param host the hostname or IP address for the service hosting the mailbox
   * @param port the network port for the service hosting the mailbox
   * @param principal the principal identifying the entity that is attempting to authenticate and
   *     gain access to the mailbox
   * @param credential the credential used to authenticate the entity attempting to access the
   *     mailbox
   * @param emailAddress the email address for the mailbox
   * @param archiveMail should mail retrieved from the mailbox be archived
   * @param deleteMail should mail retrieved from the mailbox be deleted
   * @param debug is debugging enabled for the mailbox interaction source
   */
  public MailboxInteractionSource(
      String id,
      String name,
      MailboxProtocol protocol,
      String host,
      int port,
      String principal,
      String credential,
      String emailAddress,
      boolean archiveMail,
      boolean deleteMail,
      boolean debug) {
    super(id, InteractionSourceType.MAILBOX, name);
    this.protocol = protocol;
    this.host = host;
    this.port = port;
    this.principal = principal;
    this.credential = credential;
    this.emailAddress = emailAddress;
    this.archiveMail = archiveMail;
    this.deleteMail = deleteMail;
    this.debug = debug;
  }

  /**
   * Constructs a new <b>MailboxInteractionSource</b>.
   *
   * @param id the ID for the mailbox interaction source
   * @param name the name of the mailbox interaction source
   * @param protocol the service provider and email protocol for the service hosting the mailbox
   * @param host the hostname or IP address for the service hosting the mailbox
   * @param port the network port for the service hosting the mailbox
   * @param authority the OAuth authority responsible for issuing tokens and managing authentication
   *     and authorization requests
   * @param principal the principal identifying the entity that is attempting to authenticate and
   *     gain access to the mailbox
   * @param credential the credential used to authenticate the entity attempting to access the
   *     mailbox
   * @param emailAddress the email address for the mailbox
   * @param archiveMail should mail retrieved from the mailbox be archived
   * @param deleteMail should mail retrieved from the mailbox be deleted
   * @param debug is debugging enabled for the mailbox interaction source
   */
  public MailboxInteractionSource(
      String id,
      String name,
      MailboxProtocol protocol,
      String host,
      int port,
      String authority,
      String principal,
      String credential,
      String emailAddress,
      boolean archiveMail,
      boolean deleteMail,
      boolean debug) {
    super(id, InteractionSourceType.MAILBOX, name);
    this.protocol = protocol;
    this.host = host;
    this.port = port;
    this.authority = authority;
    this.principal = principal;
    this.credential = credential;
    this.emailAddress = emailAddress;
    this.archiveMail = archiveMail;
    this.deleteMail = deleteMail;
    this.debug = debug;
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

    MailboxInteractionSource other = (MailboxInteractionSource) object;

    return Objects.equals(getId(), other.getId());
  }

  /**
   * Returns whether mail retrieved from the mailbox should be archived.
   *
   * @return <b>true</b> if mail retrieved from the mailbox should be archived or <b>false</b>
   *     otherwise
   */
  @Schema(
      description = "Should mail retrieved from the mailbox be archived",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ArchiveMail", required = true)
  public boolean getArchiveMail() {
    return archiveMail;
  }

  /**
   * Returns the OAuth authority responsible for issuing tokens and managing authentication and
   * authorization requests.
   *
   * @return the OAuth authority responsible for issuing tokens and managing authentication and
   *     authorization requests
   */
  @Schema(
      description =
          "The OAuth authority responsible for issuing tokens and managing authentication and authorization requests")
  @JsonProperty
  @XmlElement(name = "Authority")
  public String getAuthority() {
    return authority;
  }

  /**
   * Returns the credential used to authenticate the entity attempting to access the mailbox.
   *
   * @return the credential used to authenticate the entity attempting to access the mailbox
   */
  @Schema(
      description =
          "The credential used to authenticate the entity attempting to access the mailbox",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Credential", required = true)
  public String getCredential() {
    return credential;
  }

  /**
   * Returns whether debugging is enabled for the mailbox interaction source.
   *
   * @return <b>true</b> if debugging enabled for the mailbox interaction source or <b>false</b>
   *     otherwise
   */
  @Schema(
      description = "Is debugging enabled for the mailbox interaction source",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Debug", required = true)
  public boolean getDebug() {
    return debug;
  }

  /**
   * Returns whether mail retrieved from the mailbox should be deleted.
   *
   * @return <b>true</b> if mail retrieved from the mailbox should be deleted or <b>false</b>
   *     otherwise
   */
  @Schema(
      description = "Should mail retrieved from the mailbox be deleted",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DeleteMail", required = true)
  public boolean getDeleteMail() {
    return deleteMail;
  }

  /**
   * Returns the email address for the mailbox.
   *
   * @return the email address for the mailbox
   */
  @Schema(
      description = "The email address for the mailbox",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "EmailAddress", required = true)
  public String getEmailAddress() {
    return emailAddress;
  }

  /**
   * Returns the hostname or IP address for the service hosting the mailbox.
   *
   * @return the hostname or IP address for the service hosting the mailbox
   */
  @Schema(
      description = "The hostname or IP address for the service hosting the mailbox",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Host", required = true)
  public String getHost() {
    return host;
  }

  /**
   * Returns the ID for the mailbox interaction source.
   *
   * @return the ID for the mailbox interaction source
   */
  @Schema(
      description = "The ID for the mailbox interaction source",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @Override
  public String getId() {
    return super.getId();
  }

  /**
   * Returns the name of the mailbox interaction source.
   *
   * @return the name of the mailbox interaction source
   */
  @Schema(description = "The name of the mailbox interaction source")
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @Override
  public String getName() {
    return super.getName();
  }

  /**
   * Returns the network port for the service hosting the mailbox.
   *
   * @return the network port for the service hosting the mailbox
   */
  @Schema(
      description = "The network port for the service hosting the mailbox",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Port", required = true)
  public Integer getPort() {
    return port;
  }

  /**
   * Returns the principal identifying the entity that is attempting to authenticate and gain access
   * to the mailbox.
   *
   * @return the principal identifying the entity that is attempting to authenticate and gain access
   *     to the mailbox
   */
  @Schema(
      description =
          "The principal identifying the entity that is attempting to authenticate and gain access to the mailbox",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Principal", required = true)
  public String getPrincipal() {
    return principal;
  }

  /**
   * Returns the service provider and email protocol for the service hosting the mailbox.
   *
   * @return the service provider and email protocol for the service hosting the mailbox
   */
  @Schema(
      description = "The service provider and email protocol for the service hosting the mailbox",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Protocol", required = true)
  public MailboxProtocol getProtocol() {
    return protocol;
  }

  /**
   * Returns the interaction source type for the mailbox interaction source.
   *
   * @return the interaction source type for the mailbox interaction source
   */
  @JsonIgnore
  @Override
  public InteractionSourceType getType() {
    return super.getType();
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((getId() == null) ? 0 : getId().hashCode());
  }

  /**
   * Set whether mail retrieved from the mailbox should be archived.
   *
   * @param archiveMail <b>true</b> if mail retrieved from the mailbox should be archived or
   *     <b>false</b> otherwise
   */
  public void setArchiveMail(boolean archiveMail) {
    this.archiveMail = archiveMail;
  }

  /**
   * Set the OAuth authority responsible for issuing tokens and managing authentication and
   * authorization requests.
   *
   * @param authority the OAuth authority responsible for issuing tokens and managing authentication
   *     and authorization requests
   */
  public void setAuthority(String authority) {
    this.authority = authority;
  }

  /**
   * Set the credential used to authenticate the entity attempting to access the mailbox.
   *
   * @param credential the credential used to authenticate the entity attempting to access the
   *     mailbox
   */
  public void setCredential(String credential) {
    this.credential = credential;
  }

  /**
   * Set whether debugging is enabled for the mailbox interaction source.
   *
   * @param debug <b>true</b> if debugging is enabled for the mailbox interaction source or
   *     <b>false</b> otherwise
   */
  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  /**
   * Set whether mail retrieved from the mailbox should be deleted.
   *
   * @param deleteMail <b>true</b> if mail retrieved from the mailbox should be deleted or
   *     <b>false</b> otherwise
   */
  public void setDeleteMail(boolean deleteMail) {
    this.deleteMail = deleteMail;
  }

  /**
   * Set the email address for the mailbox.
   *
   * @param emailAddress the email address for the mailbox
   */
  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  /**
   * Set the hostname or IP address for the service hosting the mailbox.
   *
   * @param host the hostname or IP address for the service hosting the mailbox
   */
  public void setHost(String host) {
    this.host = host;
  }

  /**
   * Set the ID for the mailbox interaction source.
   *
   * @param id the ID for the mailbox interaction source
   */
  @Override
  public void setId(String id) {
    super.setId(id);
  }

  /**
   * Set the name of the mailbox interaction source.
   *
   * @param name the name of the mailbox interaction source
   */
  @Override
  public void setName(String name) {
    super.setName(name);
  }

  /**
   * Set the network port for the service hosting the mailbox.
   *
   * @param port the network port for the service hosting the mailbox
   */
  public void setPort(Integer port) {
    this.port = port;
  }

  /**
   * Set the principal identifying the entity that is attempting to authenticate and gain access to
   * the mailbox.
   *
   * @param principal the principal identifying the entity that is attempting to authenticate and
   *     gain access to the mailbox
   */
  public void setPrincipal(String principal) {
    this.principal = principal;
  }

  /**
   * Set the service provider and email protocol for the service hosting the mailbox.
   *
   * @param protocol the service provider and email protocol for the service hosting the mailbox
   */
  public void setProtocol(MailboxProtocol protocol) {
    this.protocol = protocol;
  }
}
