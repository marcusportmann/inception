import {OrganizationStatus} from "./organization-status";

export class Organization {



  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the organization.
   */
  private _id: string;

  /**
   * The name of the organization.
   */
  private _name: string;

  /**
   * The status for the organization.
   */
  private _status: OrganizationStatus;


  constructor(id:string, name: string, status: OrganizationStatus) {

    console.log('In Organization constructor');

    this._id = id;
    this._name = name;
    this._status = status;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the organization.
   *
   * @returns the Universally Unique Identifier (UUID) used to uniquely identify the organization
   */
  get id(): string {

    console.log('In get Organization::id()');

    return this._id;
  }

  set id(id: string) {

    console.log('In set Organization::id()');

    this._id = id;
  }

  get name(): string {
    return this._name;
  }

  set name(name: string) {
    this._name = name;
  }


  get status(): OrganizationStatus {
    return this._status;
  }

  set status(status: OrganizationStatus) {
    this._status = status;
  }



}
