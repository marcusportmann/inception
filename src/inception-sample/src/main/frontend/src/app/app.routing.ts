import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {AdminContainerComponent, NotFoundComponent} from './inception/components/layout';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: 'dashboard',
    component: AdminContainerComponent,
    loadChildren: './views/dashboard/dashboard.module#DashboardModule',
    data: {
      title: 'Dashboard',
      icon: 'icon-speedometer',
      sidebarNav: true
    }
  },
  {
    path: 'test-form',
    component: AdminContainerComponent,
    loadChildren: './views/test-form/test-form.module#TestFormModule',
    data: {
      title: 'Test Form',
      icon: 'icon-note',
      sidebarNav: true
    }
  },
  {
    path: 'level1',
    component: AdminContainerComponent,
    loadChildren: './views/level1/level1.module#Level1Module',
    data: {
      title: 'Level 1',
      icon: 'icon-doc',
      sidebarNav: true
    }
  },



  {
    path: '',
    loadChildren: './inception/inception.module#InceptionModule',
  },
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


