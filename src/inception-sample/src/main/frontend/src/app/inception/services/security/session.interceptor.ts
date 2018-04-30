import {Injectable} from "@angular/core";
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {SecurityService} from "./security.service";
import {Session} from "./session";

@Injectable()
export class SessionInterceptor implements HttpInterceptor {

  constructor(private _securityService: SecurityService) {

  }

  intercept(httpRequest: HttpRequest<any>, nextHttpHandler: HttpHandler): Observable<HttpEvent<any>> {

    let session:Session  = this._securityService.getSession();

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
