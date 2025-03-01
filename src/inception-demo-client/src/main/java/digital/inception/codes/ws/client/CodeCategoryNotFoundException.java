package digital.inception.codes.ws.client;

import jakarta.xml.ws.WebFault;

/**
 * This class was generated by Apache CXF 4.0.2 2025-02-12T10:46:22.702+02:00 Generated source
 * version: 4.0.2
 */
@WebFault(
    name = "CodeCategoryNotFoundException",
    targetNamespace = "https://inception.digital/codes")
public class CodeCategoryNotFoundException extends Exception {

  private digital.inception.core.ws.client.ServiceError faultInfo;

  public CodeCategoryNotFoundException() {
    super();
  }

  public CodeCategoryNotFoundException(String message) {
    super(message);
  }

  public CodeCategoryNotFoundException(String message, java.lang.Throwable cause) {
    super(message, cause);
  }

  public CodeCategoryNotFoundException(
      String message, digital.inception.core.ws.client.ServiceError codeCategoryNotFoundException) {
    super(message);
    this.faultInfo = codeCategoryNotFoundException;
  }

  public CodeCategoryNotFoundException(
      String message,
      digital.inception.core.ws.client.ServiceError codeCategoryNotFoundException,
      java.lang.Throwable cause) {
    super(message, cause);
    this.faultInfo = codeCategoryNotFoundException;
  }

  public digital.inception.core.ws.client.ServiceError getFaultInfo() {
    return this.faultInfo;
  }
}
