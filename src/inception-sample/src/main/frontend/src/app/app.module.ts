import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';

import { InceptionModule } from './inception/inception.module';
import {InceptionAppModule} from "./inception/inception-app.module";
import {NavigationItem} from "./inception/services/navigation/navigation-item";
import {NavigationService} from "./inception/services/navigation/navigation.service";
import {NavigationBadge} from "./inception/services/navigation/navigation-badge";

@NgModule({
  imports: [
    AppRoutingModule,

    BrowserModule,

    InceptionModule.forRoot()
  ],
  exports: [
    InceptionModule
  ],
  declarations: [
    AppComponent
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule extends InceptionAppModule {

  constructor() {

    super();

    console.log('Initialising the Application Module');
    //console.log('Registration enabled = ' + InceptionModule.registrationEnabled);
  }

  /**
   * Initialise the navigation for the application.
   *
   * @returns {NavigationItem[]}
   */
  protected initNavigation(): NavigationItem[] {

    var navigation: NavigationItem[] = [];

    navigation.push(new NavigationItem('icon-speedometer', 'Dashboard', '/dashboard', ['Application.Dashboard'], null, new NavigationBadge('info', 'NEW')));

    navigation.push(new NavigationItem('icon-puzzle', 'Base', '/base', [], [
      new NavigationItem('icon-puzzle', 'Cards', '/base/cards', []),
      new NavigationItem('icon-puzzle', 'Collapses', '/base/collapses', []),
      new NavigationItem('icon-puzzle', 'Paginations', '/base/paginations', []),
      new NavigationItem('icon-puzzle', 'Popovers', '/base/popovers', [])
    ]));

    navigation.push(new NavigationItem('icon-note', 'Test Form', '/test-form', []));

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

    // icon-login

    return navigation;
  }
}





/*

export const navigation = [
  {
    name: 'Dashboard',
    url: '/dashboard',
    icon: 'icon-speedometer',
    badge: {
      variant: 'info',
      text: 'NEW'
    }
  },
  {
    title: true,
    name: 'Theme'
  },
  {
    name: 'Colors',
    url: '/theme/colors',
    icon: 'icon-drop'
  },
  {
    name: 'Typography',
    url: '/theme/typography',
    icon: 'icon-pencil'
  },
  {
    title: true,
    name: 'Components'
  },
  {
    name: 'Base',
    url: '/base',
    icon: 'icon-puzzle',
    children: [
      {
        name: 'Cards',
        url: '/base/cards',
        icon: 'icon-puzzle'
      },
      {
        name: 'Carousels',
        url: '/base/carousels',
        icon: 'icon-puzzle'
      },
      {
        name: 'Collapses',
        url: '/base/collapses',
        icon: 'icon-puzzle'
      },
      {
        name: 'Pagination',
        url: '/base/paginations',
        icon: 'icon-puzzle'
      },
      {
        name: 'Popovers',
        url: '/base/popovers',
        icon: 'icon-puzzle'
      },
      {
        name: 'Progress',
        url: '/base/progress',
        icon: 'icon-puzzle'
      },
      {
        name: 'Switches',
        url: '/base/switches',
        icon: 'icon-puzzle'
      },
      {
        name: 'Tabs',
        url: '/base/tabs',
        icon: 'icon-puzzle'
      },
      {
        name: 'Tooltips',
        url: '/base/tooltips',
        icon: 'icon-puzzle'
      }
    ]
  },
  {
    name: 'Buttons',
    url: '/buttons',
    icon: 'icon-cursor',
    children: [
      {
        name: 'Buttons',
        url: '/buttons/buttons',
        icon: 'icon-cursor'
      },
      {
        name: 'Dropdowns',
        url: '/buttons/dropdowns',
        icon: 'icon-cursor'
      },
      {
        name: 'Loading Buttons',
        url: '/buttons/loading-buttons',
        icon: 'icon-cursor'
      },
      {
        name: 'Social Buttons',
        url: '/buttons/social-buttons',
        icon: 'icon-cursor'
      }
    ]
  },
  {
    name: 'Charts',
    url: '/charts',
    icon: 'icon-pie-chart'
  },
  {
    name: 'Editors',
    url: '/editors',
    icon: 'fa fa-code',
    children: [
      {
        name: 'Text Editors',
        url: '/editors/text-editors',
        icon: 'icon-note'
      },
      {
        name: 'Code Editors',
        url: '/editors/code-editors',
        icon: 'fa fa-code'
      }
    ]
  },
  {
    name: 'Forms',
    url: '/forms',
    icon: 'icon-note',
    children: [
      {
        name: 'Basic Forms',
        url: '/forms/basic-forms',
        icon: 'icon-note'
      },
      {
        name: 'Advanced Forms',
        url: '/forms/advanced-forms',
        icon: 'icon-note'
      },
    ]
  },
  {
    name: 'Google Maps',
    url: '/google-maps',
    icon: 'icon-map',
    badge: {
      variant: 'info',
      text: 'NEW'
    }
  },
  {
    name: 'Icons',
    url: '/icons',
    icon: 'icon-star',
    children: [
      {
        name: 'Flags',
        url: '/icons/flags',
        icon: 'icon-star',
        badge: {
          variant: 'success',
          text: 'NEW'
        }
      },
      {
        name: 'Font Awesome',
        url: '/icons/font-awesome',
        icon: 'icon-star',
        badge: {
          variant: 'secondary',
          text: '4.7'
        }
      },
      {
        name: 'Simple Line Icons',
        url: '/icons/simple-line-icons',
        icon: 'icon-star'
      }
    ]
  },
  {
    name: 'Notifications',
    url: '/notifications',
    icon: 'icon-bell',
    children: [
      {
        name: 'Alerts',
        url: '/notifications/alerts',
        icon: 'icon-bell'
      },
      {
        name: 'Modals',
        url: '/notifications/modals',
        icon: 'icon-bell'
      },
      {
        name: 'Toastr',
        url: '/notifications/toastr',
        icon: 'icon-bell'
      }
    ]
  },
  {
    name: 'Plugins',
    url: '/plugins',
    icon: 'icon-energy',
    children: [
      {
        name: 'Calendar',
        url: '/plugins/calendar',
        icon: 'icon-calendar'
      },

      {
        name: 'Draggable Cards',
        url: '/plugins/draggable-cards',
        icon: 'icon-cursor-move'
      },
      {
        name: 'Spinners',
        url: '/plugins/spinners',
        icon: 'fa fa-spinner'
      }
    ]
  },
  {
    name: 'Tables',
    url: '/tables',
    icon: 'icon-list',
    children: [
      {
        name: 'DataTable',
        url: '/tables/datatable',
        icon: 'icon-list'
      },
      {
        name: 'Standard Tables',
        url: '/tables/tables',
        icon: 'icon-list'
      },
    ]
  },
  {
    name: 'Widgets',
    url: '/widgets',
    icon: 'icon-calculator',
    badge: {
      variant: 'info',
      text: 'NEW'
    }
  },
  {
    divider: true
  },
  {
    title: true,
    name: 'Extras',
  },
  {
    name: 'Pages',
    url: '/pages',
    icon: 'icon-star',
    children: [
      {
        name: 'Login',
        url: '/pages/login',
        icon: 'icon-star'
      },
      {
        name: 'Register',
        url: '/pages/register',
        icon: 'icon-star'
      },
      {
        name: 'Error 404',
        url: '/pages/404',
        icon: 'icon-star'
      },
      {
        name: 'Error 500',
        url: '/pages/500',
        icon: 'icon-star'
      }
    ]
  },
  {
    name: 'UI Kits',
    url: '/uikits',
    icon: 'icon-layers',
    children: [
      {
        name: 'Invoicing',
        url: '/uikits/invoicing',
        icon: 'icon-speech',
        children: [
          {
            name: 'Invoice',
            url: '/uikits/invoicing/invoice',
            icon: 'icon-speech'
          }
        ]
      },
      {
        name: 'Email',
        url: '/uikits/email',
        icon: 'icon-speech',
        children: [
          {
            name: 'Inbox',
            url: '/uikits/email/inbox',
            icon: 'icon-speech'
          },
          {
            name: 'Message',
            url: '/uikits/email/message',
            icon: 'icon-speech'
          },
          {
            name: 'Compose',
            url: '/uikits/email/compose',
            icon: 'icon-speech'
          }
        ]
      }
    ]
  }
];




 */
