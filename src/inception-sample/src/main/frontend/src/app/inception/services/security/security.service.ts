import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable()
export class SecurityService {

  constructor(private httpClient: HttpClient) {

  }

  public login(username: string, password: string): Observable<boolean> {

    let body = new URLSearchParams();
    body.set('grant_type', 'password');
    body.set('username', 'Administrator');
    body.set('password', 'Password1');
    body.set('scope', 'inception-sample');
    body.set('client_id', 'inception-sample');

    console.log('body = ' + body);

    // Authorization: Basic VGVzdENsaWVudDo=

    let headers = new HttpHeaders();
    headers = headers.append('Authorization', 'Basic VGVzdENsaWVudDo=');
    headers = headers.append('Content-Type', 'application/x-www-form-urlencoded');

    return this.httpClient.post('http://localhost:20000/oauth/token', body.toString(), {headers: headers})
      .map(data => {

        console.log('data = ' + data);

        return false;
      },
      err => {

        console.log('err = ' + err);

        return of(false);
    })
  }
}
