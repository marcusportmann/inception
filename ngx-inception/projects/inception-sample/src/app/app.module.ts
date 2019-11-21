import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {InceptionConfig} from '../../../ngx-inception/src/lib/inception-config';


const ngxInceptionConfiguration: InceptionConfig = {
  // Application Information
  applicationId: 'digital.inception.sample.angular',
  applicationVersion: '1.0.0',

  // OAuth Token URL
  oauthTokenUrl: 'http://localhost:8080/oauth/token',

  // Inception API URLs
  codesApiUrlPrefix: 'http://localhost:8080/api/codes',
  configurationApiUrlPrefix: 'http://localhost:8080/api/configuration',
  errorApiUrlPrefix: 'http://localhost:8080/api/error',
  mailApiUrlPrefix: 'http://localhost:8080/api/mail',
  reportingApiUrlPrefix: 'http://localhost:8080/api/reporting',
  schedulerApiUrlPrefix: 'http://localhost:8080/api/scheduler',
  securityApiUrlPrefix: 'http://localhost:8080/api/security'
}




@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
