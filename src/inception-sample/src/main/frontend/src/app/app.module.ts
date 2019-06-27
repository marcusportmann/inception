import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

import {NgModule, TRANSLATIONS, TRANSLATIONS_FORMAT} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';

import 'hammerjs';

import {InceptionModule} from './inception/inception.module';
import {InceptionAppModule} from "./inception/inception-app.module";
import {NavigationItem} from "./inception/services/navigation/navigation-item";
import {NavigationBadge} from "./inception/services/navigation/navigation-badge";
import {NavigationTitle} from "./inception/services/navigation/navigation-title";
// TODO: Remove the lines below when Angular provides support for I18N in source natively -- MARCUS
import {I18n} from '@ngx-translate/i18n-polyfill';

// Use the require method provided by webpack
declare const require;

// We use the webpack raw-loader to return the content as a string
const translations = require('raw-loader!../locale/messages.en.xlf');
export {translations};

// TODO: Remove the lines above when Angular provides support for I18N in source natively -- MARCUS


@NgModule({

  declarations: [AppComponent
  ],
  exports: [InceptionModule
  ],
  imports: [AppRoutingModule,

    BrowserModule, BrowserAnimationsModule,

    InceptionModule.forRoot()
  ],
  providers: [{
    provide: TRANSLATIONS,
    useValue: translations
  }, {
    provide: TRANSLATIONS_FORMAT,
    useValue: 'xlf2'
  }, I18n
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

    navigation.push(new NavigationItem('fa fa-tachometer-alt', 'Dashboard', '/dashboard',
      ['Application.Dashboard'], null, null, null, new NavigationBadge('info', 'NEW')));

    navigation.push(new NavigationItem('fa fa-layer-group', 'Inception', '/inception', [], [

      new NavigationItem('fa fa-puzzle-piece', 'Components', '/inception/components', [],
        [new NavigationItem('fa fa-puzzle-piece', 'Buttons', '/inception/components/buttons', []),
          new NavigationItem('fa fa-puzzle-piece', 'Cards', '/inception/components/cards', []),
          new NavigationItem('fa fa-puzzle-piece', 'Charts', '/inception/components/charts', []),
          new NavigationItem('fa fa-puzzle-piece', 'Expansion Panels',
            '/inception/components/expansion-panels', []),
          new NavigationItem('fa fa-puzzle-piece', 'Grid List', '/inception/components/grid-list',
            []),
          new NavigationItem('fa fa-puzzle-piece', 'Lists', '/inception/components/lists', []),
          new NavigationItem('fa fa-puzzle-piece', 'Progress', '/inception/components/progress',
            []),
          new NavigationItem('fa fa-puzzle-piece', 'Switches', '/inception/components/switches',
            []), new NavigationItem('fa fa-puzzle-piece', 'Tabs', '/inception/components/tabs', []),
          new NavigationItem('fa fa-puzzle-piece', 'Tooltips', '/inception/components/tooltips', [])
        ]),

      new NavigationItem('fa fa-file-alt', 'Form', '/inception/form', [],
        [new NavigationItem('fa fa-file-alt', 'Example Form', '/inception/form/example-form', [])
        ]),

      new NavigationItem('fa fa-table', 'Tables', '/inception/tables', [],
        [new NavigationItem('fa fa-table', 'Action List Table',
          '/inception/tables/action-list-table', []),
          new NavigationItem('fa fa-table', 'Action Menu Table',
            '/inception/tables/action-menu-table', []),
          new NavigationItem('fa fa-table', 'Basic Table', '/inception/tables/basic-table', []),
          new NavigationItem('fa fa-table', 'Filterable Table',
            '/inception/tables/filterable-table', []),
          new NavigationItem('fa fa-table', 'Pagination Table',
            '/inception/tables/pagination-table', []),
          new NavigationItem('fa fa-table', 'Sortable Table', '/inception/tables/sortable-table',
            [])
        ]),

      new NavigationItem('fa fa-tint', 'Theme', '/inception/theme', [],
        [new NavigationItem('fa fa-palette', 'Colors', '/inception/theme/colors', []),
          new NavigationItem('fa fa-font', 'Typography', '/inception/theme/typography', [])
        ])
    ]));

    navigation.push(new NavigationItem('fa fa-cogs', 'Administration', '/administration', ['Codes.CodeAdministration', 'Configuration.ConfigurationAdministration', 'Security.GroupAdministration', 'Security.OrganizationAdministration', 'Security.ResetUserPassword', 'Security.UserAdministration', 'Security.UserGroups'],
      [new NavigationItem('fa fa-shield-alt', 'Security', '/administration/security', ['Security.GroupAdministration', 'Security.OrganizationAdministration', 'Security.ResetUserPassword', 'Security.UserAdministration', 'Security.UserGroups'],
        [new NavigationItem('far fa-building', 'Organizations',
          '/administration/security/organizations', ['Security.OrganizationAdministration']),
          new NavigationItem('fas fa-users', 'Users',
            '/administration/security/users', ['Security.ResetUserPassword', 'Security.UserAdministration', 'Security.UserGroups'])
        ]), new NavigationItem('fa fa-cog', 'System', '/administration/system',
        ['Codes.CodeAdministration', 'Configuration.ConfigurationAdministration'],
        [new NavigationItem('fa fa-list', 'Codes', '/administration/system/code-categories',
          ['Codes.CodeAdministration']),
          new NavigationItem('fa fa-list', 'Configuration', '/administration/system/configuration',
            ['Configuration.ConfigurationAdministration'])
        ])
      ]));

    navigation.push(new NavigationTitle('Menus'));

    navigation.push(new NavigationItem('far fa-file', 'Menu 1', '/menu1', []));

    navigation.push(new NavigationItem('far fa-file', 'Menu 2', '/menu2', [],
      [new NavigationItem('far fa-file', 'Menu 2.1', '/menu2/menu21', []),
        new NavigationItem('far fa-file', 'Menu 2.2', '/menu2/menu22', [])
      ]));

    navigation.push(new NavigationItem('far fa-file', 'Menu 3', '/menu3', [],
      [new NavigationItem('far fa-file', 'Menu 3.1', '/menu3/menu31', [],
        [new NavigationItem('far fa-file', 'Menu 3.1.1', '/menu3/menu31/menu311', []),
          new NavigationItem('far fa-file', 'Menu 3.1.2', '/menu3/menu31/menu312', [])
        ]), new NavigationItem('far fa-file', 'Menu 3.2', '/menu3/menu32', [],
        [new NavigationItem('far fa-file', 'Menu 3.2.1', '/menu3/menu32/menu321', []),
          new NavigationItem('far fa-file', 'Menu 3.2.2', '/menu3/menu32/menu322', [])
        ])
      ]));

    return navigation;
  }
}
