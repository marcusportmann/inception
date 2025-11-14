/*
 * Copyright Marcus Portmann
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

import { ObserversModule } from '@angular/cdk/observers';
import { CommonModule, formatDate, HashLocationStrategy, LocationStrategy } from '@angular/common';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { Injectable, Injector, ModuleWithProviders, NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatChipsModule } from '@angular/material/chips';
import { DateAdapter, MAT_DATE_FORMATS, NativeDateAdapter } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatExpansionModule } from '@angular/material/expansion';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS, MatFormFieldModule } from '@angular/material/form-field';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSliderModule } from '@angular/material/slider';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterModule } from '@angular/router';
import {
  PERFECT_SCROLLBAR_CONFIG, PerfectScrollbarConfigInterface, PerfectScrollbarModule
} from 'ngx-om-perfect-scrollbar';
import { CacheService } from './cache/services/cache.service';
import { DialogService } from './dialogs/services/dialog.service';
import { INCEPTION_CONFIG, InceptionConfig } from './inception-config';
import { setInceptionInjector } from './inception-injector';
import { BreadcrumbsService } from './layout/services/breadcrumbs.service';
import { NavigationService } from './layout/services/navigation.service';
import { SpinnerService } from './layout/services/spinner.service';
import { TitleBarService } from './layout/services/title-bar.service';
import { CanActivateFunctionGuard } from './routing/can-activate-function-guard';
import { DisabledFunctionGuard } from './routing/disabled-function-guard';
import { SessionInterceptor } from './session/services/session.interceptor';
import { SessionService } from './session/services/session.service';
//import '@angular/localize/init';

const INCEPTION_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true
};

export const INCEPTION_DATE_FORMATS = {
  parse: {
    dateInput: {
      month: 'short',
      year: 'numeric',
      day: 'numeric'
    }
  },
  display: {
    dateInput: 'input',
    monthYearLabel: {
      year: 'numeric',
      month: 'short'
    },
    dateA11yLabel: {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    },
    monthYearA11yLabel: {
      year: 'numeric',
      month: 'long'
    }
  }
};

@Injectable()
export class InceptionDateAdapter extends NativeDateAdapter {
  // eslint-disable-next-line @typescript-eslint/no-wrapper-object-types
  override format(date: Date, displayFormat: Object): string {
    // `displayFormat` will be 'input' when used with MAT_DATE_FORMATS
    if (displayFormat === 'input') {
      return formatDate(date, 'yyyy-MM-dd', this.locale);
    }

    // Fallback to the default implementation
    return super.format(date, displayFormat);
  }
}

/**
 * The Core module for the Inception framework.
 *
 * @author Marcus Portmann
 */
@NgModule({
  exports: [
    // Angular Modules
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    // Angular Material Modules
    MatAutocompleteModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDatepickerModule,
    MatDialogModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatRadioModule,
    MatSelectModule,
    MatSliderModule,
    MatSortModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    MatTooltipModule,
    // Third-Party Modules
    // ChartsModule,
    PerfectScrollbarModule

    // Inception Core Dialogs Components
    //ConfirmationDialogComponent,
    //ErrorDialogComponent,
    //InformationDialogComponent,
    //WarningDialogComponent,

    // Inception Core Forms Directives
    //AutocompleteSelectionRequiredDirective,
    //AutofocusDirective,
    //ValidatedFormDirective,

    // Inception Forms Components
    //FileUploadComponent,
    //GroupFormFieldComponent,
    //TableFilterComponent,

    // Inception Core Session Directives
    //HasAuthorityDirective
  ],
  imports: [
    // Angular Modules
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    // CDK Modules
    ObserversModule,
    // Angular Material Modules
    MatAutocompleteModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDatepickerModule,
    MatDialogModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatRadioModule,
    MatSelectModule,
    MatSliderModule,
    MatSortModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    MatTooltipModule,
    // Third-Party Modules
    // ChartsModule,
    PerfectScrollbarModule

    // Inception Core Dialogs Components
    //ConfirmationDialogComponent,
    //ErrorDialogComponent,
    //InformationDialogComponent,
    //WarningDialogComponent,
    // Inception Core Forms Directives
    //AutocompleteSelectionRequiredDirective,
    //AutofocusDirective,
    //ValidatedFormDirective,
    // Inception Forms Components
    //FileUploadComponent,
    //GroupFormFieldComponent,
    //TableFilterComponent,
    // Inception Core Session Directives
    //HasAuthorityDirective
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: SessionInterceptor,
      multi: true
    },
    provideHttpClient(withInterceptorsFromDi())
  ]
})
export class CoreModule {
  constructor(injector: Injector) {
    setInceptionInjector(injector);
  }

  static forRoot(config: InceptionConfig): ModuleWithProviders<CoreModule> {
    return {
      ngModule: CoreModule,
      providers: [
        {
          provide: INCEPTION_CONFIG,
          useValue: config
        },
        {
          provide: LocationStrategy,
          useClass: HashLocationStrategy
        },
        {
          provide: DateAdapter,
          useClass: InceptionDateAdapter
        },
        {
          provide: MAT_DATE_FORMATS,
          useValue: INCEPTION_DATE_FORMATS
        },
        {
          provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
          useValue: { appearance: 'fill' }
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

        // Cache Services
        CacheService,

        // Dialog Services
        DialogService,

        // Layout Services
        BreadcrumbsService,
        NavigationService,
        SpinnerService,
        TitleBarService,

        // Session Services
        SessionService,

        // Function Guards
        CanActivateFunctionGuard,
        DisabledFunctionGuard,

        provideHttpClient()
      ]
    };
  }
}
