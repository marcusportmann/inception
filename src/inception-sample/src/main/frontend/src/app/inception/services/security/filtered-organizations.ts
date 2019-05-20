import {Organization} from "./organization";


export class FilteredOrganizations {

  total: number;

  organizations: Organization[];

  constructor(total: number, organizations: Organization[])
  {
    this.total = total;
    this.organizations = organizations;
  }

}
