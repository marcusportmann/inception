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
    redirectTo: 'menu1',
    pathMatch: 'full',
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
        loadChildren: './views/dashboard/dashboard.module#DashboardModule',
        data: {
          title: 'Dashboard',
          functionCodes: ['Application.Dashboard']
        }
      },

      {
        path: 'test-form',
        loadChildren: './views/test-form/test-form.module#TestFormModule',
        data: {
          title: 'Test Form'
        },
      },

      {
        path: 'menu1',
        loadChildren: './views/menu1/menu1.module#Menu1Module',
        data: {
          title: 'Menu 1'
        },
      },

      {
        path: 'menu2',
        loadChildren: './views/menu2/menu2.module#Menu2Module',
        data: {
          title: 'Menu 2'
        },
      },

      {
        path: 'menu3',
        loadChildren: './views/menu3/menu3.module#Menu3Module',
        data: {
          title: 'Menu 3'
        },
      },

      // Administration routes
      {
        path: 'administration',
        data: {
          title: 'Administration',
        },
        children: [
          {
            path: 'security',
            loadChildren: './inception/views/security/security.module#SecurityModule',
            data: {
              title: 'Security'
            }
          }
        ]
      }
    ]
  },

  // Login route
  {
    path: 'login',
    component: SimpleContainerComponent,
    loadChildren: './inception/views/login/login.module#LoginModule',
    data: {
      title: 'Login'
    }
  },

  // Default route for invalid paths
  {
    path: '**',
    component: NotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}




