import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {
  AdminContainerComponent, NotFoundComponent, SimpleContainerComponent
} from './inception/components/layout';
import {CanActivateFunctionGuard} from "./inception/routing/can-activate-function-guard";

export const routes: Routes = [{
  path: '',
  pathMatch: 'full',
  redirectTo: 'menu1',
},

  {
    path: '',
    component: AdminContainerComponent,
    children: [{
      path: 'dashboard',
      canActivate: [CanActivateFunctionGuard
      ],
      data: {
        title: 'Dashboard',
        authorities: ['ROLE_Administrator', 'FUNCTION_Application.Dashboard']
      },
      loadChildren: () => import('./views/dashboard/dashboard.module').then(m => m.DashboardModule)
    }, {
      path: 'inception',
      data: {
        title: 'Inception'
      },
      loadChildren: () => import('./views/inception/inception.module').then(m => m.InceptionModule)
    }, {
      path: 'menu1',
      data: {
        title: 'Menu 1'
      },
      loadChildren: () => import('./views/menu1/menu1.module').then(m => m.Menu1Module)
    }, {
      path: 'menu2',
      data: {
        title: 'Menu 2'
      },
      loadChildren: () => import('./views/menu2/menu2.module').then(m => m.Menu2Module)
    }, {
      path: 'menu3',
      data: {
        title: 'Menu 3'
      },
      loadChildren: () => import('./views/menu3/menu3.module').then(m => m.Menu3Module)
    },

      // Administration routes
      {
        path: 'administration',
        data: {
          title: 'Administration',
        },
        children: [{
          path: 'system',
          data: {
            title: 'System'
          },
          children: [{
            path: 'codes',
            data: {
              title: 'Codes'
            },
            loadChildren: () => import('./inception/views/codes/codes.module').then(m => m.CodesModule)
          }, {
            path: 'configuration',
            data: {
              title: 'Configuration'
            },
            loadChildren: () => import('./inception/views/configuration/configuration.module').then(m => m.ConfigurationModule)
          }
          ]
        }, {
          path: 'security',
          data: {
            title: 'Security'
          },
          loadChildren: () => import('./inception/views/security/security.module').then(m => m.SecurityModule)
        }
        ]
      }
    ]
  },

  // Login route
  {
    path: 'login',
    component: SimpleContainerComponent,
    loadChildren: () => import('./inception/views/login/login.module').then(m => m.LoginModule)
  },

  // Send Error Report route
  {
    path: 'error',
    component: SimpleContainerComponent,
    loadChildren: () => import('./inception/views/error/error.module').then(m => m.ErrorModule)
  },

  // Default route for invalid paths
  {
    path: '**',
    component: NotFoundComponent
  }
];

@NgModule({
  // Tracing should only be enabled for DEBUG purposes
  imports: [RouterModule.forRoot(routes, {enableTracing: false})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}




