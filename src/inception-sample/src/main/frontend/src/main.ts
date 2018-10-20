import {enableProdMode} from '@angular/core';
import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';

import {AppModule} from './app/app.module';
import {environment} from './environments/environment';



import {TRANSLATIONS, TRANSLATIONS_FORMAT} from '@angular/core';

// Use the require method provided by webpack
declare const require;

// We use the webpack raw-loader to return the content as a string
const translations = require('raw-loader!./locale/messages.en.xlf');






if (environment.production) {
  enableProdMode();
}



platformBrowserDynamic().bootstrapModule(AppModule, {
  providers: [
    {provide: TRANSLATIONS_FORMAT, useValue: 'xlf2'},
    {provide: TRANSLATIONS, useValue: translations}
    ]
}).catch(err => console.log(err));
