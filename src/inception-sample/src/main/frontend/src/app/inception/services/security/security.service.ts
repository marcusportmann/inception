import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import { decode } from "jsonwebtoken";

@Injectable()
export class SecurityService {

  constructor(private httpClient: HttpClient) {

  }

  public login(username: string, password: string): Observable<boolean> {

    let body = new HttpParams()
    .set('grant_type', 'password')
    .set('username', 'Administrator')
    .set('password', 'Password1')
    .set('scope', 'inception-sample')
    .set('client_id', 'inception-sample')

    console.log('body = ' + body.toString());
    console.log('body = ' + body.toString());

    // Authorization: Basic VGVzdENsaWVudDo=

    let headers = new HttpHeaders();
    headers = headers.append('Authorization', 'Basic VGVzdENsaWVudDo=');
    headers = headers.append('Content-Type', 'application/x-www-form-urlencoded');

    //let options = {headers: headers, withCredentials: true};
    let options = {};

    return this.httpClient.post<any>('http://localhost:8080/oauth/token', body.toString(), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    })
      .map(data => {

          console.log('data.access_token = ' + data.access_token);

        let token:any = decode(data.access_token);



        console.log('token = ' + token);

        return false;
      },
      err => {

        console.log('err = ' + err);

        return of(false);
    })
  }
}
