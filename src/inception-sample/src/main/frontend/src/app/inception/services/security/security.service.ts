import { Injectable } from '@angular/core';
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
import {TestError} from "./security.service.errors";


@Injectable()
export class SecurityService {

  constructor(private httpClient: HttpClient) {

  }

  public login(username: string, password: string): Observable<HttpErrorResponse | Session> {

    let body = new HttpParams()
    .set('grant_type', 'password')
    .set('username', 'Administrator')
    .set('password', 'Password12')
    .set('scope', 'inception-sample')
    .set('client_id', 'inception-sample');

    let options = { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } };

    return this.httpClient.post<TokenResponse>('http://localhost:8080/oauth/token', body.toString(), options).pipe(
      map(tokenResponse => {

        let token:any = decode(tokenResponse.access_token);

        return new Session(token.user_name, token.scope, token.authorities, token.exp, tokenResponse.access_token, tokenResponse.refresh_token);
      }), catchError((error: HttpErrorResponse) => {

        console.log('catchError = ', error);

        //return Observable.of(error);

        return Observable.throw(new TestError(error.status));

      }));

  }
}
