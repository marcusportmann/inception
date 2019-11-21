/*
 * Copyright 2019 Marcus Portmann
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

import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, of, Subject, throwError, timer} from 'rxjs';
import {catchError, flatMap, map, mergeMap, switchMap} from 'rxjs/operators';
import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {Session} from './session';
import {TokenResponse} from './token-response';
import {LoginError, PasswordExpiredError, UserLockedError} from './session.service.errors';
import {JwtHelperService} from '@auth0/angular-jwt';
import {CommunicationError} from '../../errors/communication-error';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {environment} from '../../../../environments/environment';

/**
 * The Session Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class SessionService {

  /**
   * The current active session.
   */
  session$: Subject<Session | null> = new BehaviorSubject<Session | null>(null);

  /**
   * Constructs a new SessionService.
   *
   * @param httpClient     The HTTP client.
   * @param i18n           The internationalisation service.
   */
  constructor(private httpClient: HttpClient, private i18n: I18n) {
    console.log('Initializing the Session Service');

    // Start the session refresher
    timer(0, 10000).pipe(switchMap(() => this.refreshSession()))
      .subscribe((refreshedSession: Session | null) => {
        if (refreshedSession) {
          console.log('Successfully refreshed session: ', refreshedSession);
        }
      });
  }

  /**
   * Logon.
   *
   * @param username The username.
   * @param password The password.
   *
   * @return The current active session.
   */
  login(username: string, password: string): Observable<Session | null> {

    // TODO: REMOVE HARD CODED SCOPE AND CLIENT ID -- MARCUS

    const body = new HttpParams()
      .set('grant_type', 'password')
      .set('username', username)
      .set('password', password)
      .set('scope', 'inception-sample')
      .set('client_id', 'inception-sample');

    const options = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}};

    return this.httpClient.post<TokenResponse>(environment.oauthTokenUrl, body.toString(), options)
      .pipe(flatMap((tokenResponse: TokenResponse) => {
        this.session$.next(this.createSessionFromAccessToken(tokenResponse.access_token,
          tokenResponse.refresh_token));

        return this.session$;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (httpErrorResponse.status === 400) {
          if (httpErrorResponse.error && (httpErrorResponse.error.error === 'invalid_grant') &&
            httpErrorResponse.error.error_description) {
            if (httpErrorResponse.error.error_description.includes('Bad credentials')) {
              return throwError(new LoginError(this.i18n({
                id: '@@session_service_incorrect_username_or_password',
                value: 'Incorrect username or password.'
              }), httpErrorResponse));
            } else if (httpErrorResponse.error.error_description.includes('User locked')) {
              return throwError(new UserLockedError(this.i18n({
                id: '@@session_service_the_user_is_locked',
                value: 'The user is locked.'
              }), httpErrorResponse));
            } else if (httpErrorResponse.error.error_description.includes('Credentials expired')) {
              return throwError(new PasswordExpiredError(this.i18n({
                id: '@@session_service_the_password_has_expired',
                value: 'The password has expired.'
              }), httpErrorResponse));
            }
          }

          return throwError(new LoginError(this.i18n({
            id: '@@session_service_incorrect_username_or_password',
            value: 'Incorrect username or password.'
          }), httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(new CommunicationError(httpErrorResponse, this.i18n));
        } else {
          return throwError(new SystemUnavailableError(httpErrorResponse, this.i18n));
        }
      }));
  }

  /**
   * Logout the current active session if one exists.
   */
  logout(): void {
    this.session$.next(null);
  }

  private createSessionFromAccessToken(accessToken: string,
                                       refreshToken: string | undefined): Session {
    const helper = new JwtHelperService();

    // tslint:disable-next-line
    const token: any = helper.decodeToken(accessToken);

    const accessTokenExpiry: Date | null = helper.getTokenExpirationDate(
      accessToken);

    return new Session(
      (!!token.user_name) ? token.user_name : '',
      (!!token.user_directory_id) ? token.user_directory_id : '',
      (!!token.user_full_name) ? token.user_full_name : '',
      (!!token.scope) ? token.scope : [],
      (!!token.authorities) ? token.authorities : [],
      accessToken,
      (!!accessTokenExpiry) ? accessTokenExpiry : undefined,
      refreshToken);
  }

  private refreshSession(): Observable<Session | null> {
    return this.session$.pipe(mergeMap((currentSession: Session | null) => {
      if (currentSession) {
        const selectedOrganization = currentSession.organization;

        /*
         * If the access token will expire with 60 seconds then obtain a new one using the refresh
         * token if it exists.
         */
        if (currentSession.accessTokenExpiry && currentSession.refreshToken) {
          if (Date.now() > (currentSession.accessTokenExpiry.getTime() - 60000)) {
            const body = new HttpParams()
              .set('grant_type', 'refresh_token')
              .set('refresh_token', currentSession.refreshToken)
              .set('scope', 'inception-sample')
              .set('client_id', 'inception-sample');

            const options = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}};

            return this.httpClient.post<TokenResponse>(environment.oauthTokenUrl, body.toString(),
              options).pipe(map((tokenResponse: TokenResponse) => {
              const refreshedSession: Session = this.createSessionFromAccessToken(
                tokenResponse.access_token, currentSession.refreshToken);

              refreshedSession.organization = selectedOrganization;

              this.session$.next(refreshedSession);

              return refreshedSession;
            }), catchError((httpErrorResponse: HttpErrorResponse) => {
              console.log(this.i18n({
                id: '@@session_service_failed_to_refresh_the_user_session',
                value: 'Failed to refresh the user session.'
              }), httpErrorResponse);

              if (httpErrorResponse.status === 401) {
                this.session$.next(null);
              }

              return of(null);
            }));
          }
        }
      }

      return of(null);
    }));
  }
}
