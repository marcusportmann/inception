/*
 * Copyright 2019 Marcus Portmann
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
// Import Angular components
import {CommonModule, HashLocationStrategy, LocationStrategy} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {RouterModule} from '@angular/router';
// Import Inception modules
import {DirectivesModule} from "./directives/directives.module";
// Import Inception controls
import {
  CheckboxFormFieldComponent, FileUploadComponent,
  RadioGroupFormFieldComponent,
  TableFilterComponent
} from "./components/controls";
// Import Inception dialogs
import {
  ConfirmationDialogComponent,
  ErrorDialogComponent,
  InformationDialogComponent,
  WarningDialogComponent
} from "./components/dialogs";
// Import Inception layout components
import {
  AdminContainerComponent,
  AdminFooterComponent,
  AdminHeaderComponent,
  BreadcrumbsComponent,
  NotFoundComponent,
  SidebarComponent,
  SidebarFooterComponent,
  SidebarFormComponent,
  SidebarHeaderComponent,
  SidebarMinimizerComponent,
  SidebarNavComponent,
  SidebarNavDropdownComponent,
  SidebarNavItemComponent,
  SimpleContainerComponent,
  SpinnerComponent,
  TitleBarComponent
} from './components/layout';
// Import Inception interceptors
import {SessionInterceptor} from "./services/session/session.interceptor";
// Import Inception services
import {BreadcrumbsService} from "./services/layout/breadcrumbs.service";
import {CodesService} from "./services/codes/codes.service";
import {ConfigurationService} from "./services/configuration/configuration.service";
import {DialogService} from "./services/dialog/dialog.service";
import {ErrorService} from './services/error/error.service';
import {SpinnerService} from './services/layout/spinner.service'
import {TitleBarService} from './services/layout/title-bar.service';
import {NavigationService} from "./services/navigation/navigation.service";
import {SecurityService} from './services/security/security.service';
import {SessionService} from './services/session/session.service';
// Import Inception miscellaneous
import {CanActivateFunctionGuard} from "./routing/can-activate-function-guard";
import {setInceptionInjector} from "./inception-injector";
// Import 3rd party modules
// Import perfect scrollbar dependencies
import {
  PERFECT_SCROLLBAR_CONFIG, PerfectScrollbarConfigInterface, PerfectScrollbarModule
} from 'ngx-perfect-scrollbar';
// Import Material modules
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatButtonModule} from "@angular/material/button";
import {MatButtonToggleModule} from "@angular/material/button-toggle";
import {MatCardModule} from "@angular/material/card";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from "@angular/material/core";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatDialogModule} from "@angular/material/dialog";
import {MatExpansionModule} from "@angular/material/expansion";
import {MAT_FORM_FIELD_DEFAULT_OPTIONS, MatFormFieldModule} from "@angular/material/form-field";
import {MatGridListModule} from "@angular/material/grid-list";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {MatListModule} from "@angular/material/list";
import {MatMenuModule} from "@angular/material/menu";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatRadioModule} from "@angular/material/radio";
import {MatSelectModule} from "@angular/material/select";
import {MatSliderModule} from "@angular/material/slider";
import {MatSortModule} from "@angular/material/sort";
import {MatTableModule} from "@angular/material/table";
import {MatTabsModule} from "@angular/material/tabs";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatTooltipModule} from "@angular/material/tooltip";
// Import Material miscellaneous
import {MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter} from "@angular/material-moment-adapter";
import {DisabledFunctionGuard} from './routing/disabled-function-guard';
import {SchedulerService} from "./services/scheduler/scheduler.service";
import {ReportingService} from "./services/reporting/reporting.service";
import {MailService} from "./services/mail/mail.service";
import {SchedulerModule} from "./views/scheduler/scheduler.module";

// Import 3rd party components

const INCEPTION_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true
};

// See the Moment.js docs for the meaning of these formats:
// https://momentjs.com/docs/#/displaying/format/
export const INCEPTION_DATE_FORMATS = {
  parse: {
    dateInput: 'LL',
  },
  display: {
    dateInput: 'LL',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

/**
 * The InceptionModule class implements the Inception framework module.
 *
 * @author Marcus Portmann
 */
@NgModule({
  imports: [// Angular modules
    CommonModule, FormsModule, ReactiveFormsModule, RouterModule,

    // 3rd party modules
    //ChartsModule,
    PerfectScrollbarModule,

    // Material modules
    MatAutocompleteModule, MatButtonModule, MatButtonToggleModule, MatCardModule, MatCheckboxModule,
    MatDatepickerModule, MatDialogModule, MatExpansionModule, MatFormFieldModule, MatGridListModule,
    MatIconModule, MatInputModule, MatListModule, MatMenuModule, MatPaginatorModule,
    MatProgressBarModule, MatRadioModule, MatSelectModule, MatSliderModule, MatSortModule,
    MatTableModule, MatTabsModule, MatToolbarModule, MatTooltipModule,

    // Inception modules
    DirectivesModule
  ],
  exports: [// Angular modules
    CommonModule, FormsModule, HttpClientModule, ReactiveFormsModule, RouterModule,

    // 3rd party modules
    //ChartsModule,
    PerfectScrollbarModule,

    // Material modules
    MatAutocompleteModule, MatButtonModule, MatButtonToggleModule, MatCardModule, MatCheckboxModule,
    MatDatepickerModule, MatDialogModule, MatExpansionModule, MatFormFieldModule, MatGridListModule,
    MatIconModule, MatInputModule, MatListModule, MatMenuModule, MatPaginatorModule,
    MatProgressBarModule, MatRadioModule, MatSelectModule, MatSliderModule, MatSortModule,
    MatTableModule, MatTabsModule, MatToolbarModule, MatTooltipModule,

    // Inception modules
    DirectivesModule,

    // Inception control components
    CheckboxFormFieldComponent, FileUploadComponent, RadioGroupFormFieldComponent, TableFilterComponent,

    // Inception layout components
    AdminContainerComponent, NotFoundComponent, SimpleContainerComponent, SpinnerComponent
  ],
  declarations: [// Inception control components
    CheckboxFormFieldComponent, FileUploadComponent, RadioGroupFormFieldComponent, TableFilterComponent,

    // Inception layout components
    BreadcrumbsComponent, AdminContainerComponent, AdminFooterComponent, AdminHeaderComponent,
    NotFoundComponent, SidebarComponent, SidebarFooterComponent, SidebarFormComponent,
    SidebarHeaderComponent, SidebarMinimizerComponent, SidebarNavComponent,
    SidebarNavDropdownComponent, SidebarNavItemComponent, SimpleContainerComponent,
    SpinnerComponent, TitleBarComponent,

    // Inception dialogs
    ConfirmationDialogComponent, ErrorDialogComponent, InformationDialogComponent,
    WarningDialogComponent
  ],
  entryComponents: [SpinnerComponent, ConfirmationDialogComponent, ErrorDialogComponent, InformationDialogComponent, WarningDialogComponent],
  bootstrap: [AdminContainerComponent, SimpleContainerComponent
  ]
})
export class InceptionModule {

  public static forgottenPasswordEnabled = false;

  public static registrationEnabled = false;

  constructor(injector: Injector) {
    console.log('Initialising the Inception Module');
    setInceptionInjector(injector);
  }

  static forRoot(): ModuleWithProviders {
    return {
      ngModule: InceptionModule,
      providers: [{
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

        {
          provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
          useValue: {appearance: 'standard'}
        },

        {
          provide: DateAdapter,
          useClass: MomentDateAdapter,
          deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS]
        },

        {
          provide: MAT_DATE_FORMATS,
          useValue: INCEPTION_DATE_FORMATS
        },

        {
          provide: MAT_DATE_LOCALE,
          useValue: 'en-GB'
        },

        CanActivateFunctionGuard, DisabledFunctionGuard,

        BreadcrumbsService, CodesService, ConfigurationService, DialogService, ErrorService,
        MailService, NavigationService, ReportingService, SchedulerService, SecurityService,
        ReportingService, SessionService, SpinnerService, TitleBarService
      ]
    }
  }
}



