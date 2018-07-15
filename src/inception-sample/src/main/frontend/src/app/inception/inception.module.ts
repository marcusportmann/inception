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
  BreadcrumbsComponent,
  AdminContainerComponent,
  AdminFooterComponent,
  AdminHeaderComponent,
  SidebarComponent,
  SidebarFooterComponent,
  SidebarFormComponent,
  SidebarHeaderComponent,
  SidebarMinimizerComponent,
  SidebarNavComponent,
  SidebarNavDropdownComponent,
  SidebarNavItemComponent,
  SidebarNavLinkComponent,
  SidebarNavTitleComponent,
  NotFoundComponent,
  SimpleContainerComponent, SpinnerComponent
} from './components/layout';
import { ErrorReportDialog } from './components/error';

// Import Inception services
import {BreadcrumbsService} from "./services/breadcrumbs/breadcrumbs.service";
import {ErrorService} from './services/error/error.service';
import {NavigationService} from "./services/navigation/navigation.service";
import {SecurityService} from './services/security/security.service';
import {SessionService} from './services/session/session.service';

// Import 3rd party modules
//import {BsDatepickerModule} from 'ngx-bootstrap';
//import {BsDropdownModule} from 'ngx-bootstrap/dropdown';
//import {LaddaModule} from 'angular2-ladda';
//import {ModalModule} from 'ngx-bootstrap/modal';
import {PerfectScrollbarModule} from 'ngx-perfect-scrollbar';
import {StorageServiceModule} from "angular-webstorage-service";
//import {TabsModule} from 'ngx-bootstrap/tabs';
//import {TimepickerModule} from 'ngx-bootstrap';

// Import 3rd party components
//import {BsDatepickerConfig, BsDaterangepickerConfig} from 'ngx-bootstrap';
import {SessionInterceptor} from "./services/session/session.interceptor";
import {CanActivateFunctionGuard} from "./routing/can-activate-function-guard";
import {setInceptionInjector} from "./inception-injector";

// Import perfect scrollbar dependencies
import {PERFECT_SCROLLBAR_CONFIG} from 'ngx-perfect-scrollbar';
import {PerfectScrollbarConfigInterface} from 'ngx-perfect-scrollbar';

const INCEPTION_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true
};


// Import Material modules
import {MatCardModule} from "@angular/material";
import {MatDialogModule} from "@angular/material";
import {MatFormFieldModule} from "@angular/material";
import {MatIconModule} from "@angular/material";
import {MatInputModule} from "@angular/material";
import {MatMenuModule} from "@angular/material";
import {MatToolbarModule} from "@angular/material";

/**
 * The InceptionModule class implements the Inception framework module.
 *
 * @author Marcus Portmann
 */
@NgModule({
  imports: [
    // Angular modules
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,

    // 3rd party modules
    //BsDatepickerModule.forRoot(),
    //BsDropdownModule.forRoot(),
    //ChartsModule,
    //LaddaModule.forRoot({}),
    //ModalModule.forRoot(),
    //NgSelectModule,
    PerfectScrollbarModule,
    StorageServiceModule,
    //TabsModule.forRoot(),
    //TimepickerModule.forRoot(),


    // Material modules
    MatCardModule,
    MatDialogModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatMenuModule,
    MatToolbarModule,


    // Inception modules
    DirectivesModule
  ],
  exports: [
    // Angular modules
    CommonModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    RouterModule,

    // 3rd party modules
    //BsDatepickerModule,
    //BsDropdownModule,
    //ChartsModule,
    //LaddaModule,
    //ModalModule,
    //NgSelectModule,
    PerfectScrollbarModule,
    StorageServiceModule,
    //TabsModule,
    //TimepickerModule,


    // Material modules
    MatCardModule,
    MatDialogModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatMenuModule,
    MatToolbarModule,




    // Inception modules
    DirectivesModule,

    // Inception  components
    AdminContainerComponent,
    NotFoundComponent,
    SimpleContainerComponent,
    SpinnerComponent
  ],
  declarations: [
    // Inception layout components
    BreadcrumbsComponent,
    AdminContainerComponent,
    AdminFooterComponent,
    AdminHeaderComponent,
    SidebarComponent,
    SidebarFooterComponent,
    SidebarFormComponent,
    SidebarHeaderComponent,
    SidebarMinimizerComponent,
    SidebarNavComponent,
    SidebarNavDropdownComponent,
    SidebarNavItemComponent,
    SidebarNavLinkComponent,
    SidebarNavTitleComponent,
    NotFoundComponent,
    SimpleContainerComponent,
    SpinnerComponent,

    // Inception error components
    ErrorReportDialog
  ],
  bootstrap: [AdminContainerComponent, ErrorReportDialog, SimpleContainerComponent]
})
export class InceptionModule {

  public static forgottenPasswordEnabled = false;

  public static registrationEnabled = false;

  // constructor(injector: Injector, bsDatepickerConfig: BsDatepickerConfig, bsDaterangepickerConfig: BsDaterangepickerConfig) {
  //   console.log('Initialising the Inception Module');
  //   setInceptionInjector(injector);
  //   bsDatepickerConfig.dateInputFormat = 'YYYY-MM-DD';
  //   bsDaterangepickerConfig.rangeInputFormat = 'YYYY-MM-DD';
  // }

  constructor(injector: Injector) {
    console.log('Initialising the Inception Module');
    setInceptionInjector(injector);
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



