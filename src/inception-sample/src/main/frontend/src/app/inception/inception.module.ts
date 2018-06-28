/*
 * Copyright 2018 Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Import Angular decorators
import {Injector, ModuleWithProviders, NgModule} from '@angular/core';

// Import Angular modules
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {ReactiveFormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';

// Import Angular components
import {HashLocationStrategy, LocationStrategy} from '@angular/common';

// Import Inception modules
import {DirectivesModule} from "./directives/directives.module";

// Import Inception components
import {
  AppHeaderComponent,
  BreadcrumbsComponent,
  ContainerComponent,
  ErrorModalComponent,
  AppFooterComponent,
  NotFoundComponent,
  AppSidebarComponent,
  AppSidebarFooterComponent,
  AppSidebarFormComponent,
  AppSidebarHeaderComponent,
  AppSidebarMinimizerComponent,
  AppSidebarNavComponent,
  AppSidebarNavDropdownComponent,
  AppSidebarNavItemComponent,
  AppSidebarNavLinkComponent,
  AppSidebarNavTitleComponent,
  SimpleContainerComponent,
  AppContainerComponent
} from './components/layout';

// Import Inception services
import {BreadcrumbsService} from "./services/breadcrumbs/breadcrumbs.service";
import {ErrorService} from './services/error/error.service';
import {NavigationService} from "./services/navigation/navigation.service";
import {SecurityService} from './services/security/security.service';
import {SessionService} from './services/session/session.service';

// Import 3rd party modules
import {BsDatepickerModule} from 'ngx-bootstrap';
import {BsDropdownModule} from 'ngx-bootstrap/dropdown';
import {ChartsModule} from 'ng2-charts/ng2-charts';
import {ModalModule} from 'ngx-bootstrap/modal';
import {NgSelectModule} from '@ng-select/ng-select';
import {PerfectScrollbarModule} from 'ngx-perfect-scrollbar';
import {StorageServiceModule} from "angular-webstorage-service";
import {TabsModule} from 'ngx-bootstrap/tabs';
import {TimepickerModule} from 'ngx-bootstrap';

// Import 3rd party components
import {BsDatepickerConfig, BsDaterangepickerConfig} from 'ngx-bootstrap';
import {SessionInterceptor} from "./services/session/session.interceptor";
import {CanActivateFunctionGuard} from "./routing/can-activate-function-guard";
import {setInceptionInjector} from "./inception-injector";

// Import perfect scrollbar dependencies
import {PERFECT_SCROLLBAR_CONFIG} from 'ngx-perfect-scrollbar';
import {PerfectScrollbarConfigInterface} from 'ngx-perfect-scrollbar';

const INCEPTION_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true
};

/**
 * The InceptionModule class implements the Inception framework module.
 *
 * @author Marcus Portmann
 */
@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,

    BsDatepickerModule.forRoot(),
    BsDropdownModule.forRoot(),
    ChartsModule,
    ModalModule.forRoot(),
    NgSelectModule,
    PerfectScrollbarModule,
    StorageServiceModule,
    TabsModule.forRoot(),
    TimepickerModule.forRoot(),

    DirectivesModule
  ],
  exports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    RouterModule,

    BsDatepickerModule,
    BsDropdownModule,
    ChartsModule,
    ModalModule,
    NgSelectModule,
    PerfectScrollbarModule,
    StorageServiceModule,
    TabsModule,
    TimepickerModule,

    DirectivesModule
  ],
  declarations: [
    AppHeaderComponent,
    BreadcrumbsComponent,
    ContainerComponent,
    ErrorModalComponent,
    AppFooterComponent,
    NotFoundComponent,
    AppSidebarComponent,
    AppSidebarFooterComponent,
    AppSidebarFormComponent,
    AppSidebarHeaderComponent,
    AppSidebarMinimizerComponent,
    AppSidebarNavComponent,
    AppSidebarNavDropdownComponent,
    AppSidebarNavItemComponent,
    AppSidebarNavLinkComponent,
    AppSidebarNavTitleComponent,
    SimpleContainerComponent,
    AppContainerComponent
  ],
  bootstrap: [AppContainerComponent, ErrorModalComponent, SimpleContainerComponent]
})
export class InceptionModule {

  public static forgottenPasswordEnabled = false;

  public static registrationEnabled = false;

  constructor(injector: Injector, bsDatepickerConfig: BsDatepickerConfig, bsDaterangepickerConfig: BsDaterangepickerConfig) {
    console.log('Initialising the Inception Module');
    setInceptionInjector(injector);
    bsDatepickerConfig.dateInputFormat = 'YYYY-MM-DD';
    bsDaterangepickerConfig.rangeInputFormat = 'YYYY-MM-DD';
  }

  static forRoot(): ModuleWithProviders
  {
    return {
      ngModule: InceptionModule,
      providers: [
        {
          provide: LocationStrategy,
          useClass: HashLocationStrategy,
        },

        {
          provide: HTTP_INTERCEPTORS,
          useClass: SessionInterceptor,
          multi: true
        },

        {
          provide: PERFECT_SCROLLBAR_CONFIG,
          useValue: INCEPTION_PERFECT_SCROLLBAR_CONFIG
        },

        CanActivateFunctionGuard,

        BreadcrumbsService,
        ErrorService,
        NavigationService,
        SecurityService,
        SessionService
      ]
    }
  }
}



