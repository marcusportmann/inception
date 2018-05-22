import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {SimpleContainerComponent} from './components/layout';


export const routes: Routes = [
  {
    path: 'login',
    component: SimpleContainerComponent,
    loadChildren: './views/login/login.module#LoginModule',
    data: {
      title: 'Login'
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class InceptionRoutingModule {
}
