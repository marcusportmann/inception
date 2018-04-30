import {OrganizationStatus} from "./organization-status";

/**
 * The Organization class stores the information for an organization.
 *
 * @author Marcus Portmann
 */
export class Organization {

  /**
   * Constructs a new Organization.
   *
   * @param _id     The Universally Unique Identifier (UUID) used to uniquely identify the
   *                organization.
   * @param _name   The name of the organization.
   * @param _status The status for the organization.
   */
  constructor(private _id: string, private _name: string, private _status: OrganizationStatus) {
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the organization.
   *
   * @return {string}
   */
  get id(): string {

    console.log('In get Organization::id()');

    return this._id;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the organization.
   *
   * @param {string} id The Universally Unique Identifier (UUID) used to uniquely identify the
   *                    organization.
   */
  set id(id: string) {

    console.log('In set Organization::id()');

    this._id = id;
  }

  /**
   * Returns the name of the organization.
   *
   * @returns {string}
   */
  get name(): string {
    return this._name;
  }

  /**
   * Set the name of the organization.
   *
   * @param {string} name The name of the organization.
   */
  set name(name: string) {
    this._name = name;
  }

  /**
   * Returns the status for the organization.
   *
   * @returns {OrganizationStatus}
   */
  get status(): OrganizationStatus {
    return this._status;
  }

  /**
   * Set the status for the organization.
   *
   * @param {OrganizationStatus} status The status for the organization.
   */
  set status(status: OrganizationStatus) {
    this._status = status;
  }

}
