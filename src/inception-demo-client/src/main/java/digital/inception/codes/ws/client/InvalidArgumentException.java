
package digital.inception.codes.ws.client;

import jakarta.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 4.0.2
 * 2025-02-12T10:46:22.695+02:00
 * Generated source version: 4.0.2
 */

@WebFault(name = "InvalidArgumentException", targetNamespace = "https://inception.digital/core")
public class InvalidArgumentException extends Exception {

    private digital.inception.core.ws.client.InvalidArgumentError faultInfo;

    public InvalidArgumentException() {
        super();
    }

    public InvalidArgumentException(String message) {
        super(message);
    }

    public InvalidArgumentException(String message, java.lang.Throwable cause) {
        super(message, cause);
    }

    public InvalidArgumentException(String message, digital.inception.core.ws.client.InvalidArgumentError invalidArgumentException) {
        super(message);
        this.faultInfo = invalidArgumentException;
    }

    public InvalidArgumentException(String message, digital.inception.core.ws.client.InvalidArgumentError invalidArgumentException, java.lang.Throwable cause) {
        super(message, cause);
        this.faultInfo = invalidArgumentException;
    }

    public digital.inception.core.ws.client.InvalidArgumentError getFaultInfo() {
        return this.faultInfo;
    }
}
