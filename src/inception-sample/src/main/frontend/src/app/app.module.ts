import { BrowserModule } from '@angular/platform-browser';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import 'hammerjs';

import {InceptionModule} from './inception/inception.module';
import {InceptionAppModule} from "./inception/inception-app.module";
import {NavigationItem} from "./inception/services/navigation/navigation-item";
import {NavigationBadge} from "./inception/services/navigation/navigation-badge";
import {NavigationTitle} from "./inception/services/navigation/navigation-title";


import {LOCALE_ID} from '@angular/core';


// TODO: Remove the lines below when Angular provides support for I18N in source natively -- MARCUS
//import {I18n} from '@ngx-translate/i18n-polyfill';
//import {TRANSLATIONS, TRANSLATIONS_FORMAT} from '@angular/core';

// Use the require method provided by webpack
//declare const require;

// We use the webpack raw-loader to return the content as a string
//const translations = require('raw-loader!../locale/messages.en.xlf');
//// TODO: Remove the lines above when Angular provides support for I18N in source natively -- MARCUS



@NgModule({
  declarations: [
    AppComponent
  ],
  exports: [ 
    InceptionModule 
  ],
  imports: [
    AppRoutingModule,

    BrowserModule,
    BrowserAnimationsModule,

    InceptionModule.forRoot()
  ],
  providers: [
//    { provide: LOCALE_ID, useValue: 'en' },
//    { provide: TRANSLATIONS, useValue: translations },
//    { provide: TRANSLATIONS_FORMAT, useValue: 'xlf2' },
//    I18n
  ],
  bootstrap: [AppComponent]
})
export class AppModule extends InceptionAppModule {

  constructor() {
    super();
    console.log('Initialising the Application Module');
  }

  /**
   * Initialise the navigation for the application.
   *
   * @returns {NavigationItem[]}
   */
  protected initNavigation(): NavigationItem[] {

    var navigation: NavigationItem[] = [];

    navigation.push(new NavigationItem('icon-speedometer', 'Dashboard', '/dashboard', ['Application.Dashboard'], null, null, new NavigationBadge('info', 'NEW')));

    navigation.push(new NavigationItem('icon-layers', 'Inception', '/inception', [], [

      new NavigationItem('icon-puzzle', 'Components', '/inception/components', [], [
        new NavigationItem('icon-puzzle', 'Buttons', '/inception/components/buttons', []),
        new NavigationItem('icon-puzzle', 'Cards', '/inception/components/cards', []),
        new NavigationItem('icon-puzzle', 'Charts', '/inception/components/charts', []),
        new NavigationItem('icon-puzzle', 'Expansion Panels', '/inception/components/expansion-panels', []),
        new NavigationItem('icon-puzzle', 'Grid List', '/inception/components/grid-list', []),
        new NavigationItem('icon-puzzle', 'Lists', '/inception/components/lists', []),
        new NavigationItem('icon-puzzle', 'Progress', '/inception/components/progress', []),
        new NavigationItem('icon-puzzle', 'Switches', '/inception/components/switches', []),
        new NavigationItem('icon-puzzle', 'Tabs', '/inception/components/tabs', []),
        new NavigationItem('icon-puzzle', 'Tooltips', '/inception/components/tooltips', [])
      ]),

      new NavigationItem('icon-note', 'Form', '/inception/form', [], [
        new NavigationItem('icon-note', 'Example Form', '/inception/form/example-form', [])
      ]),

      new NavigationItem('icon-table', 'Tables', '/inception/tables', [], [
        new NavigationItem('icon-table', 'Action List Table', '/inception/tables/action-list-table', []),
        new NavigationItem('icon-table', 'Action Menu Table', '/inception/tables/action-menu-table', []),
        new NavigationItem('icon-table', 'Basic Table', '/inception/tables/basic-table', []),
        new NavigationItem('icon-table', 'Filterable Table', '/inception/tables/filterable-table', []),
        new NavigationItem('icon-table', 'Pagination Table', '/inception/tables/pagination-table', []),
        new NavigationItem('icon-table', 'Sortable Table', '/inception/tables/sortable-table', [])
      ]),

      new NavigationItem('icon-drop', 'Theme', '/inception/theme', [], [
        new NavigationItem('icon-drop', 'Colors', '/inception/theme/colors', []),
        new NavigationItem('icon-drop', 'Typography', '/inception/theme/typography', [])
      ])

    ]));

    navigation.push(new NavigationTitle('Menus'));

    navigation.push(new NavigationItem('icon-doc', 'Menu 1', '/menu1', []));

    navigation.push(new NavigationItem('icon-doc', 'Menu 2', '/menu2', [], [
      new NavigationItem('icon-doc', 'Menu 2.1', '/menu2/menu21', []),
      new NavigationItem('icon-doc', 'Menu 2.2', '/menu2/menu22', [])
    ]));

    navigation.push(new NavigationItem('icon-doc', 'Menu 3', '/menu3', [], [
      new NavigationItem('icon-doc', 'Menu 3.1', '/menu3/menu31', [], [
        new NavigationItem('icon-doc', 'Menu 3.1.1', '/menu3/menu31/menu311', []),
        new NavigationItem('icon-doc', 'Menu 3.1.2', '/menu3/menu31/menu312', [])
      ]),
      new NavigationItem('icon-doc', 'Menu 3.2', '/menu3/menu32', [], [
        new NavigationItem('icon-doc', 'Menu 3.2.1', '/menu3/menu32/menu321', []),
        new NavigationItem('icon-doc', 'Menu 3.2.2', '/menu3/menu32/menu322', [])
      ])
    ]));

    return navigation;
  }
}
