import {enableProdMode} from '@angular/core';
import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';

import {AppModule} from './app/app.module';
import {environment} from './environments/environment';

import {TRANSLATIONS, TRANSLATIONS_FORMAT} from '@angular/core';



if (environment.production) {
  enableProdMode();
}

// Use the require method provided by webpack
declare const require;

// We use the webpack raw-loader to return the content as a string
const translations = require('raw-loader!./locale/messages.en.xlf');


platformBrowserDynamic().bootstrapModule(AppModule, {
  providers: [
    {provide: TRANSLATIONS, useValue: translations},
    {provide: TRANSLATIONS_FORMAT, useValue: 'xlf2'}
    ]
}).catch(err => console.log(err));






// platformBrowserDynamic().bootstrapModule(AppModule, {
//   providers: []
// }).catch(err => console.log(err));
