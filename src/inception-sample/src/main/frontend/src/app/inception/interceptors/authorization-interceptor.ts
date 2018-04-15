import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {Session} from "../services/security/session";


@Injectable()
export class AuthorizationInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>,
            next: HttpHandler): Observable<HttpEvent<any>> {

    let session:Session = JSON.parse(sessionStorage.getItem("session"));

    if (session) {

      const cloned = req.clone({
        headers: req.headers.set("Authorization",
          session.accessToken)
      });

      return next.handle(cloned);
    }
    else {
      return next.handle(req);
    }
  }
}
