import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {
  AdminContainerComponent,
  NotFoundComponent,
  SimpleContainerComponent
} from './inception/components/layout';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'menu1/menu11',
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
    path: 'menu1',
    component: AdminContainerComponent,
    loadChildren: './views/menu1/menu1.module#Menu1Module',
    data: {
      title: 'Menu 1',
      icon: 'icon-doc',
      sidebarNav: true
    }
  },
  {
    path: 'menu2',
    component: AdminContainerComponent,
    loadChildren: './views/menu2/menu2.module#Menu2Module',
    data: {
      title: 'Menu 2',
      icon: 'icon-doc',
      sidebarNav: true
    }
  },


  // Administration routes
  {
    path: 'administration',
    component: AdminContainerComponent,
    data: {
      title: 'Administration',
      icon: 'fa fa-gear',
      sidebarNav: true
    },
    children: [
      {
        path: 'security',
        loadChildren: './inception/views/security/security.module#SecurityModule',
        data: {
          title: 'Security',
          icon: 'fa fa-lock',
          sidebarNav: true
        }
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


