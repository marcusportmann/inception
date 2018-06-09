/*
 * Copyright 2018 Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {Inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs/Rx';
import {catchError} from 'rxjs/operators';
import {map} from 'rxjs/operators';


import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
  HttpParams,
  HttpResponse
} from '@angular/common/http';
import {decode} from "jsonwebtoken";
import {Session} from "./session";
import {TokenResponse} from "./token-response";
import {SESSION_STORAGE, WebStorageService} from "angular-webstorage-service";
import {LoginError, SessionError} from "./session.service.errors";
import {OAuthError} from "../../errors/oauth-error";

/**
 * The SessionService class provides the Session Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class SessionService {

  /**
   * Constructs a new SessionService.
   *
   * @param {HttpClient} httpClient            The HTTP client.
   * @param {WebStorageService} sessionStorage The session storage service.
   */
  constructor(private httpClient: HttpClient, @Inject(SESSION_STORAGE) private sessionStorageService: WebStorageService) {

  }

  public login(username: string, password: string): Observable<Session> {

    this.sessionStorageService.remove("session");

    let body = new HttpParams()
      .set('grant_type', 'password')
      .set('username', username)
      .set('password', password)
      .set('scope', 'inception-sample')
      .set('client_id', 'inception-sample');

    let options = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}};

    return this.httpClient.post<TokenResponse>('http://localhost:20000/oauth/token', body.toString(), options).pipe(
      map((tokenResponse: TokenResponse) => {

        let token: any = decode(tokenResponse.access_token);

        var accessTokenExpiry: number = Date.now() + (tokenResponse.expires_in * 1000);

        let session: Session = new Session(token.user_name, token.scope, token.authorities, token.organizations, tokenResponse.access_token, accessTokenExpiry.toString(), tokenResponse.refresh_token);

        this.sessionStorageService.set('session', session);

        return session;

      }), catchError((httpErrorResponse: HttpErrorResponse) => {

        if (httpErrorResponse.error && httpErrorResponse.error.error) {
          let oauthError: OAuthError = httpErrorResponse.error;

          return Observable.throw(new LoginError(new Date(), httpErrorResponse.statusText, oauthError.error_description));
        }
        else {
          return Observable.throw(new LoginError(new Date(), httpErrorResponse.statusText, httpErrorResponse.message));
        }
      }));
  }

  /**
   * Returns the current active session if one exists.
   *
   * @return {Session} The current active session if one exists or null.
   */
  public getSession(): Observable<Session | null> {

    let session: Session = this.sessionStorageService.get("session");

    // If the access token has expired then obtain a new one using the refresh token
    if (session) {
      if (session.accessTokenExpiry) {
        if (Date.now() > parseInt(session.accessTokenExpiry)) {

            this.sessionStorageService.remove("session");

            if (session.refreshToken) {

              let body = new HttpParams()
                .set('grant_type', 'refresh_token')
                .set('refresh_token', session.refreshToken)
                .set('scope', 'inception-sample')
                .set('client_id', 'inception-sample');
              ;

              let options = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}};

              return this.httpClient.post<TokenResponse>('http://localhost:20000/oauth/token', body.toString(), options).pipe(
                map((tokenResponse: TokenResponse) => {

                  let token: any = decode(tokenResponse.access_token);

                  var accessTokenExpiry: number = Date.now() + (tokenResponse.expires_in * 1000);

                  let session: Session = new Session(token.user_name, token.scope, token.authorities, token.organizations, tokenResponse.access_token, accessTokenExpiry.toString(), tokenResponse.refresh_token);

                  this.sessionStorageService.set('session', session);

                  return session;

                }), catchError((httpErrorResponse: HttpErrorResponse) => {

                  if (httpErrorResponse.error && httpErrorResponse.error.error) {

                    let oauthError: OAuthError = httpErrorResponse.error;

                    console.error('Failed to refresh the access token: ', oauthError);

                    return Observable.of(null);
                  }
                  else {

                    console.error('Failed to refresh the access token: ', httpErrorResponse.error);

                    return Observable.of(null);
                  }
                }));
            }
        }
        else {

          return Observable.of(session);
        }
      }
    }

    return Observable.of(null);
  }
}
