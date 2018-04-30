import {Inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs/Rx';
import { catchError } from 'rxjs/operators';
import { map } from 'rxjs/operators';


import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
  HttpParams,
  HttpResponse
} from '@angular/common/http';
import { decode } from "jsonwebtoken";
import {Session} from "./session";
import {TokenResponse} from "./token-response";
import {LoginError} from "./security.service.errors";
import {SESSION_STORAGE, WebStorageService} from "angular-webstorage-service";
import {Organization} from "./organization";
import {OrganizationStatus} from "./organization-status";
import {forEach} from "@angular/router/src/utils/collection";


@Injectable()
export class SecurityService {

  constructor(private httpClient: HttpClient, @Inject(SESSION_STORAGE) private sessionStorage: WebStorageService) {

  }

  public login(username: string, password: string): Observable<Session> {

    let body = new HttpParams()
    .set('grant_type', 'password')
    .set('username', 'Administrator')
    .set('password', 'Password1')
    .set('scope', 'inception-sample')
    .set('client_id', 'inception-sample');

    let options = { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } };

    return this.httpClient.post<TokenResponse>('http://localhost:20000/oauth/token', body.toString(), options).pipe(
      map(tokenResponse => {

        let token:any = decode(tokenResponse.access_token);

        let session:Session = new Session(token.user_name, token.scope, token.authorities, token.exp, tokenResponse.access_token, tokenResponse.refresh_token);

        this.sessionStorage.set('session', session);

        return session;
      }), catchError((error: HttpErrorResponse) => {

        console.log('catchError = ', error);

        // TODO: Map different HTTP error codes to specific error types -- MARCUS


        return Observable.throw(new LoginError(error.status));

      }));

  }

  public getSession(): Session {
    let session:Session = this.sessionStorage.get("session");

    // TODO: Check if session has expired and if so remove from session storage -- MARCUS

    return session;
  }

  /**
   * Retrieve the organizations.
   *
   * @returns {Observable<Organization[]>}
   */
  public getOrganizations(): Observable<Organization[]> {

    return this.httpClient.get<Organization[]>('http://localhost:20000/api/organizations').pipe(
      map(organizations => {


        for(var i:number = 0; i < organizations.length; i++) {


          console.log('organizations[' + i+ '] = ', organizations[i]);

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


        return Observable.throw(new LoginError(error.status));

      }));


  }




}
