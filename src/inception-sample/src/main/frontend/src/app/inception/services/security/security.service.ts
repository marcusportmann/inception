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

    return this.httpClient.post<TokenResponse>('http://localhost:8080/oauth/token', body.toString(), options).pipe(
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

    return session;
  }

  public getOrganizations(): Observable<Organization[]> {

    let session:Session  = this.getSession();

    let options = { headers: { 'Authorization': 'Bearer ' + session.accessToken } };


    return this.httpClient.get<Organization[]>('http://localhost:20000/api/organizations', options).pipe(
      map(organizations => {



        return organizations;

      }), catchError((error: HttpErrorResponse) => {

        console.log('catchError = ', error);

        // TODO: Map different HTTP error codes to specific error types -- MARCUS


        return Observable.throw(new LoginError(error.status));

      }));


  }




}
