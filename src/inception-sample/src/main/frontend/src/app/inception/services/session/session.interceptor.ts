import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs/Observable";

import {Session} from "./session";
import {SessionService} from "./session.service";

/**
 * The SessionInterceptor class implements an Angular HTTP interceptor, which injects the OAuth2
 * access token associated with the current active session as an Authorization header.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class SessionInterceptor implements HttpInterceptor {

  /**
   * Constructs a new SessionInterceptor.
   *
   * @param {SessionService} _sessionService The Session Service.
   */
  constructor(private _sessionService: SessionService) {
  }

  intercept(httpRequest: HttpRequest<any>, nextHttpHandler: HttpHandler): Observable<HttpEvent<any>> {

    let session: Session = this._sessionService.getSession();

    if ((!httpRequest.url.endsWith('/oauth/token')) && session) {

      httpRequest = httpRequest.clone({
        setHeaders: {
          Authorization: `Bearer ${session.accessToken}`
        }
      });
    }

    return nextHttpHandler.handle(httpRequest);
  }

}
