/*
 * Copyright 2018 Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * The NavItem class holds the information for a navigation item.
 *
 * @author Marcus Portmann
 */
export class NavItem {


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
