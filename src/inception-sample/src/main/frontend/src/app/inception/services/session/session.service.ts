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
import {Observable, of, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';


import {
  HttpClient,
  HttpErrorResponse,
  HttpParams
} from '@angular/common/http';
import {Session} from "./session";
import {TokenResponse} from "./token-response";
import {SESSION_STORAGE, WebStorageService} from "ngx-webstorage-service";
import {
  LoginError,
  PasswordExpiredError,
  SessionServiceError,
  UserLockedError
} from "./session.service.errors";
import { JwtHelperService } from '@auth0/angular-jwt';



import {CommunicationError} from "../../errors/communication-error";
import {I18n} from "@ngx-translate/i18n-polyfill";
import {SystemUnavailableError} from "../../errors/system-unavailable-error";

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
   * @param {I18n} i18n                        The internationalisation service.
   * @param {WebStorageService} sessionStorage The session storage service.
   */
  constructor(private httpClient: HttpClient, private i18n: I18n, @Inject(SESSION_STORAGE) private sessionStorageService: WebStorageService) {
    console.log('Initializing the Session Service');
  }

  login(username: string, password: string): Observable<Session> {

    this.sessionStorageService.remove("session");

    // TODO: REMOVE HARD CODED SCOPE AND CLIENT ID -- MARCUS

    let body = new HttpParams()
      .set('grant_type', 'password')
      .set('username', username)
      .set('password', password)
      .set('scope', 'inception-sample')
      .set('client_id', 'inception-sample');

    let options = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}};

    return this.httpClient.post<TokenResponse>('http://localhost:20000/oauth/token', body.toString(), options)
      .pipe(
      map((tokenResponse: TokenResponse) => {

        const helper = new JwtHelperService();

        const token: any = helper.decodeToken(tokenResponse.access_token);

        let accessTokenExpiry: string = helper.getTokenExpirationDate(tokenResponse.access_token).getTime().toString();

        accessTokenExpiry = "0";

        let session: Session = new Session(token.user_name, token.scope, token.authorities, token.organizations, tokenResponse.access_token, accessTokenExpiry, tokenResponse.refresh_token);

        this.sessionStorageService.set('session', session);

        return session;

      }), catchError((httpErrorResponse: HttpErrorResponse) => {

        if (httpErrorResponse.status == 400) {

          if (httpErrorResponse.error && (httpErrorResponse.error.error == 'invalid_grant') && httpErrorResponse.error.error_description) {
            if (httpErrorResponse.error.error_description.includes('Bad credentials')) {
              return throwError(new LoginError(this.i18n({id: '@@session_service_incorrect_username_or_password',
                value: 'Incorrect username or password.'}), httpErrorResponse));
            }
            else if (httpErrorResponse.error.error_description.includes('User locked')) {
              return throwError(new UserLockedError(this.i18n({id: '@@session_service_the_user_is_locked',
                value: 'The user is locked.'}), httpErrorResponse));
            }
            else if (httpErrorResponse.error.error_description.includes('Credentials expired')) {
              return throwError(new PasswordExpiredError(this.i18n({id: '@@session_service_the_password_has_expired',
                value: 'The password has expired.'}), httpErrorResponse));
            }
          }

          return throwError(new LoginError(this.i18n({id: '@@session_service_incorrect_username_or_password',
            value: 'Incorrect username or password.'}), httpErrorResponse));
        }
        else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse));
        }
        else {
          return throwError(new SystemUnavailableError(this.i18n({id: '@@system_unavailable_error',
            value: 'An error has occurred and the system is unable to process your request at this time.'}), httpErrorResponse));
        }
      }));
  }

  /**
   * Returns the current active session if one exists.
   *
   * @return {Session} The current active session if one exists or null.
   */
  getSession(): Observable<Session | null> {

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

                  const helper = new JwtHelperService();

                  const token: any = helper.decodeToken(tokenResponse.access_token);

                  const accessTokenExpiry: string = helper.getTokenExpirationDate(tokenResponse.access_token).getTime().toString();

                  let session: Session = new Session(token.user_name, token.scope, token.authorities, token.organizations, tokenResponse.access_token, accessTokenExpiry, tokenResponse.refresh_token);

                  this.sessionStorageService.set('session', session);

                  return session;

                }), catchError((httpErrorResponse: HttpErrorResponse) => {

                  return throwError(new SessionServiceError(this.i18n({id: '@@session_service_failed_to_refresh_the_user_session',
                    value: 'Failed to refresh the user session.'}), httpErrorResponse));

                }));
            }
        }
        else {

          return of(session);
        }
      }
    }

    return of(null);
  }

  /**
   * Logout the current active session if one exists.
   */
  logout() {
    this.sessionStorageService.remove("session");
  }
}
