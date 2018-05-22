import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {
  AdminContainerComponent,
  NotFoundComponent,
  SimpleContainerComponent
} from './inception/components/layout';


export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
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
  },
  {
    path: '**',
    component: NotFoundComponent
  }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
