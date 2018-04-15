/**
 * The Error interface represents information associated with an error returned by a back-end API call.
 */
export interface Error {

  /**
   * The type of error e.g. the fully qualified name of the exception associated with the error.
   */
  type: string;

  /**
   * The URI for the HTTP request that resulted in the error.
   */
  path: string;

  /**
   * The date and time the error occurred.
   */
  timestamp: Date;

  /**
   * The HTTP status for the error.
   */
  status: string;

  /**
   * The message.
   */
  message: string;

  /**
   * The detail.
   */
  detail: string;

  /**
   * The stack trace associated with the error.
   */
  stackTrace?: string;

  /**
   * The name of the entity associated with the error e.g. the name of the argument or parameter.
   */
  name?: string;

  /**
   * The validation errors associated with the error.
   */
  validationErrors?: Object[];
}





