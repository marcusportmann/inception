import {Inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs/Rx';
import {catchError} from 'rxjs/operators';
import {map} from 'rxjs/operators';


import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Organization} from "./organization";
import {OrganizationStatus} from "./organization-status";


@Injectable()
export class SecurityService {

  constructor(private httpClient: HttpClient) {

  }


  /**
   * Retrieve the organizations.
   *
   * @returns {Observable<Organization[]>}
   */
  public getOrganizations(): Observable<Organization[]> {

    return this.httpClient.get<Organization[]>('http://localhost:20000/api/organizations').pipe(
      map(organizations => {


        for (var i: number = 0; i < organizations.length; i++) {


          console.log('organizations[' + i + '] = ', organizations[i]);

          if (organizations[i].status == OrganizationStatus.Active) {
            console.log('Found active organization ', organizations[i].name);
          }
          else if (organizations[i].status == OrganizationStatus.Inactive) {
            console.log('Found inactive organization ', organizations[i].name);
          }
        }


        return organizations;

      }), catchError((error: HttpErrorResponse) => {

        console.log('catchError = ', error);

        // TODO: Map different HTTP error codes to specific error types -- MARCUS


        return Observable.throw(error);

        //return Observable.throw(new LoginError(error.status));

      }));


  }


}
