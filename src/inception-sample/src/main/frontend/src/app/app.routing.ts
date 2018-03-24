import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AdminContainerComponent, SimpleContainerComponent} from './inception/components/layout';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  },
  {
    path: '',
    component: AdminContainerComponent,
    data: {
      title: 'Home'
    },
    children: [
      {
        path: 'dashboard',
        loadChildren: './views/dashboard/dashboard.module#DashboardModule'
      },
    ]
  },
  {
    path: 'login',
    component: SimpleContainerComponent,
    data: {
      title: 'Login'
    },
    children: [
      {
        path: '',
        loadChildren: './inception/views/login/login.module#LoginModule'
      }
    ]
  }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
