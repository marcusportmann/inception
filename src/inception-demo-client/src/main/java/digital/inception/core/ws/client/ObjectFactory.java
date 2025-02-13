package digital.inception.core.ws.client;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the digital.inception.core.ws.client package.
 *
 * <p>An ObjectFactory allows you to programatically construct new instances of the Java
 * representation for XML content. The Java representation of XML content can consist of schema
 * derived interfaces and classes representing the binding of schema type definitions, element
 * declarations and model groups. Factory methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

  private static final QName _InvalidArgumentError_QNAME =
      new QName("https://inception.digital/core", "InvalidArgumentError");

  private static final QName _InvalidArgumentException_QNAME =
      new QName("https://inception.digital/core", "InvalidArgumentException");

  private static final QName _ServiceError_QNAME =
      new QName("https://inception.digital/core", "ServiceError");

  private static final QName _ServiceUnavailableException_QNAME =
      new QName("https://inception.digital/core", "ServiceUnavailableException");

  private static final QName _ValidationError_QNAME =
      new QName("https://inception.digital/core", "ValidationError");

  /**
   * Create a new ObjectFactory that can be used to create new instances of schema derived classes
   * for package: digital.inception.core.ws.client
   */
  public ObjectFactory() {}

  /** Create an instance of {@link InvalidArgumentError } */
  public InvalidArgumentError createInvalidArgumentError() {
    return new InvalidArgumentError();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link InvalidArgumentError }{@code >}
   *
   * @param value Java instance representing xml element's value.
   * @return the new instance of {@link JAXBElement }{@code <}{@link InvalidArgumentError }{@code >}
   */
  @XmlElementDecl(namespace = "https://inception.digital/core", name = "InvalidArgumentError")
  public JAXBElement<InvalidArgumentError> createInvalidArgumentError(InvalidArgumentError value) {
    return new JAXBElement<InvalidArgumentError>(
        _InvalidArgumentError_QNAME, InvalidArgumentError.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link InvalidArgumentError }{@code >}
   *
   * @param value Java instance representing xml element's value.
   * @return the new instance of {@link JAXBElement }{@code <}{@link InvalidArgumentError }{@code >}
   */
  @XmlElementDecl(namespace = "https://inception.digital/core", name = "InvalidArgumentException")
  public JAXBElement<InvalidArgumentError> createInvalidArgumentException(
      InvalidArgumentError value) {
    return new JAXBElement<InvalidArgumentError>(
        _InvalidArgumentException_QNAME, InvalidArgumentError.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link ServiceError }{@code >}
   *
   * @param value Java instance representing xml element's value.
   * @return the new instance of {@link JAXBElement }{@code <}{@link ServiceError }{@code >}
   */
  @XmlElementDecl(namespace = "https://inception.digital/core", name = "ServiceError")
  public JAXBElement<ServiceError> createServiceError(ServiceError value) {
    return new JAXBElement<ServiceError>(_ServiceError_QNAME, ServiceError.class, null, value);
  }

  /** Create an instance of {@link ServiceError } */
  public ServiceError createServiceError() {
    return new ServiceError();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link ServiceError }{@code >}
   *
   * @param value Java instance representing xml element's value.
   * @return the new instance of {@link JAXBElement }{@code <}{@link ServiceError }{@code >}
   */
  @XmlElementDecl(
      namespace = "https://inception.digital/core",
      name = "ServiceUnavailableException")
  public JAXBElement<ServiceError> createServiceUnavailableException(ServiceError value) {
    return new JAXBElement<ServiceError>(
        _ServiceUnavailableException_QNAME, ServiceError.class, null, value);
  }

  /** Create an instance of {@link ValidationError } */
  public ValidationError createValidationError() {
    return new ValidationError();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link ValidationError }{@code >}
   *
   * @param value Java instance representing xml element's value.
   * @return the new instance of {@link JAXBElement }{@code <}{@link ValidationError }{@code >}
   */
  @XmlElementDecl(namespace = "https://inception.digital/core", name = "ValidationError")
  public JAXBElement<ValidationError> createValidationError(ValidationError value) {
    return new JAXBElement<ValidationError>(
        _ValidationError_QNAME, ValidationError.class, null, value);
  }
}
