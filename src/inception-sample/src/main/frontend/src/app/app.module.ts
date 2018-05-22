import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { AppRoutingModule } from './app.routing';

import { InceptionModule } from './inception/inception.module';
import {NavigationService} from "./inception/services/navigation/navigation.service";
import {NavigationItemBadge} from "./inception/services/navigation/navigation-item-badge";
import {NavigationItem} from "./inception/services/navigation/navigation-item";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    AppRoutingModule,

    BrowserModule,

    InceptionModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {

  constructor(navigationService: NavigationService) {
    console.log('Initialising AppModule');
    console.log('Registration enabled = ' + InceptionModule.registrationEnabled);

    // Initialise the navigation for the application
    this.initNavigation(navigationService);
  }

  /**
   * Initialise the navigation for the application.
   *
   * @param {NavigationService} navigationService The Navigation Service.
   */
  initNavigation(navigationService: NavigationService ) {

    //navigationService.addNavigationItem(new NavigationItem('Dashboard', '/dashboard', 'icon-speedometer', 'dashboard', './views/dashboard/dashboard.module#DashboardModule', new NavigationItemBadge('info', 'NEW')));
    //navigationService.addNavigationItem(new NavigationItem('Test Form', '/test-form', 'icon-note', 'test-form', './views/test-form/test-form.module#TestFormModule'));
  }
}


