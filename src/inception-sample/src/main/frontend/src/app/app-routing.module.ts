import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {
  AdminContainerComponent, NotFoundComponent, SimpleContainerComponent
} from './inception/components/layout';
import {CanActivateFunctionGuard} from './inception/routing/can-activate-function-guard';
import {AdministrationTitleResolver} from './views/administration/administration-title-resolver';

export const routes: Routes = [{
  path: '',
  pathMatch: 'full',
  redirectTo: 'menu1',
}, {
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
  }, {
    path: 'administration',
    resolve: {
      title: AdministrationTitleResolver
    },
    loadChildren: () => import('./views/administration/administration.module').then(m => m.AdministrationModule)
  }]
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
}];

@NgModule({
  // Tracing should only be enabled for DEBUG purposes
  imports: [RouterModule.forRoot(routes, {enableTracing: false})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}




