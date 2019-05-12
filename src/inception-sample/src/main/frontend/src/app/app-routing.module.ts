import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {
  AdminContainerComponent,
  NotFoundComponent,
  SimpleContainerComponent
} from './inception/components/layout';
import {CanActivateFunctionGuard} from "./inception/routing/can-activate-function-guard";

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'menu1',
  },

  {
    path: '',
    component: AdminContainerComponent,
    children: [
      {
        path: 'dashboard',
        canActivate: [
          CanActivateFunctionGuard
        ],
        data: {
          title: 'Dashboard',
          functionCodes: ['Application.Dashboard']
        },
        loadChildren: './views/dashboard/dashboard.module#DashboardModule'
      },
      {
        path: 'inception',
        data: {
          title: 'Inception'
        },
        loadChildren: './views/inception/inception.module#InceptionModule'
      },
      {
        path: 'menu1',
        data: {
          title: 'Menu 1'
        },
        loadChildren: './views/menu1/menu1.module#Menu1Module'
      },
      {
        path: 'menu2',
        data: {
          title: 'Menu 2'
        },
        loadChildren: './views/menu2/menu2.module#Menu2Module'
      },
      {
        path: 'menu3',
        data: {
          title: 'Menu 3'
        },
        loadChildren: './views/menu3/menu3.module#Menu3Module'
      },

      // Administration routes
      {
        path: 'administration',
        data: {
          title: 'Administration',
        },
        children: [
          {
            path: 'system',
            data: {
              title: 'System'
            },
            children: [
              {
                path: 'code-categories',
                data: {
                  title: 'Code Categories'
                },
                loadChildren: './inception/views/codes/codes.module#CodesModule'
              },
              {
                path: 'configuration',
                data: {
                  title: 'Configuration'
                },
                loadChildren: './inception/views/configuration/configuration.module#ConfigurationModule'
              }
            ]
          },
          {
            path: 'security',
            data: {
              title: 'Security'
            },
            loadChildren: './inception/views/security/security.module#SecurityModule'
          }
        ]
      }
    ]
  },

  // Login route
  {
    path: 'login',
    component: SimpleContainerComponent,
    loadChildren: './inception/views/login/login.module#LoginModule'
  },

  // Send Error Report route
  {
    path: 'error',
    component: SimpleContainerComponent,
    loadChildren: './inception/views/error/error.module#ErrorModule'
  },

  // Default route for invalid paths
  {
    path: '**',
    component: NotFoundComponent
  }
];

@NgModule({
  // Tracing should only be enabled for DEBUG purposes
  imports: [RouterModule.forRoot(routes, {enableTracing: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}




