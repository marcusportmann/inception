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

import {ModuleWithProviders, NgModule} from '@angular/core';
import {PERFECT_SCROLLBAR_CONFIG, PerfectScrollbarConfigInterface, PerfectScrollbarModule} from 'ngx-perfect-scrollbar';
import {CommonModule} from '@angular/common';
import {
  MatAutocompleteModule,
  MatButtonModule,
  MatButtonToggleModule,
  MatCardModule,
  MatCheckboxModule,
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
  MatTooltipModule
} from '@angular/material';
import {CoreModule} from '../core/core.module';
import {BreadcrumbsService} from './services/breadcrumbs.service';
import {SpinnerService} from './services/spinner.service';
import {TitleBarService} from './services/title-bar.service';
import {NavigationService} from './services/navigation.service';
import {
  BrandMinimizerDirective,
  MobileSidebarTogglerDirective,
  SidebarMinimizerDirective,
  SidebarNavDropdownDirective,
  SidebarNavDropdownTogglerDirective,
  SidebarOffCanvasCloseDirective,
  SidebarTogglerDirective
} from './directives';
import {DialogModule} from '../dialog/dialog.module';
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
} from './components';
import {RouterModule} from '@angular/router';
import {SecurityModule} from '../security/security.module';

const INCEPTION_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true
};

/**
 * The LayoutModule class implements the Inception Layout Module.
 *
 * @author Marcus Portmann
 */
@NgModule({
  declarations: [

    // Components
    AdminContainerComponent, AdminFooterComponent, AdminHeaderComponent, BreadcrumbsComponent, NotFoundComponent,
    SidebarComponent, SidebarFooterComponent, SidebarFormComponent, SidebarHeaderComponent, SidebarMinimizerComponent,
    SidebarNavComponent, SidebarNavDropdownComponent, SidebarNavItemComponent, SimpleContainerComponent,
    SpinnerComponent, TitleBarComponent,

    // Directives
    BrandMinimizerDirective, MobileSidebarTogglerDirective, SidebarMinimizerDirective, SidebarNavDropdownDirective,
    SidebarNavDropdownTogglerDirective, SidebarOffCanvasCloseDirective, SidebarTogglerDirective
  ],
  imports: [

    // Angular modules
    CommonModule, RouterModule,

    // 3rd party modules
    // ChartsModule,
    PerfectScrollbarModule,

    // Material modules
    MatAutocompleteModule, MatButtonModule, MatButtonToggleModule, MatCardModule, MatCheckboxModule,
    MatDatepickerModule, MatDialogModule, MatExpansionModule, MatFormFieldModule, MatGridListModule, MatIconModule,
    MatInputModule, MatListModule, MatMenuModule, MatPaginatorModule, MatProgressBarModule, MatRadioModule,
    MatSelectModule, MatSliderModule, MatSortModule, MatTableModule, MatTabsModule, MatToolbarModule, MatTooltipModule,

    // Inception modules
    CoreModule.forRoot(), DialogModule.forRoot(), SecurityModule.forRoot()
  ],
  exports: [

    // Angular modules
    CommonModule, RouterModule,

    // 3rd party modules
    // ChartsModule,
    PerfectScrollbarModule,

    // Material modules
    MatAutocompleteModule, MatButtonModule, MatButtonToggleModule, MatCardModule, MatCheckboxModule,
    MatDatepickerModule, MatDialogModule, MatExpansionModule, MatFormFieldModule, MatGridListModule, MatIconModule,
    MatInputModule, MatListModule, MatMenuModule, MatPaginatorModule, MatProgressBarModule, MatRadioModule,
    MatSelectModule, MatSliderModule, MatSortModule, MatTableModule, MatTabsModule, MatToolbarModule, MatTooltipModule,

    // Inception modules
    CoreModule, DialogModule, SecurityModule,

    // Components
    AdminContainerComponent, AdminFooterComponent, AdminHeaderComponent, BreadcrumbsComponent, NotFoundComponent,
    SidebarComponent, SidebarFooterComponent, SidebarFormComponent, SidebarHeaderComponent, SidebarMinimizerComponent,
    SidebarNavComponent, SidebarNavDropdownComponent, SidebarNavItemComponent, SimpleContainerComponent,
    SpinnerComponent, TitleBarComponent,

    // Directives
    BrandMinimizerDirective, MobileSidebarTogglerDirective, SidebarMinimizerDirective, SidebarNavDropdownDirective,
    SidebarNavDropdownTogglerDirective, SidebarOffCanvasCloseDirective, SidebarTogglerDirective
  ]
})
export class LayoutModule {
  constructor() {
    console.log('Initializing the Inception Layout Module');
  }

  static forRoot(): ModuleWithProviders {
    return {
      ngModule: LayoutModule,
      providers: [{
        provide: PERFECT_SCROLLBAR_CONFIG,
        useValue: INCEPTION_PERFECT_SCROLLBAR_CONFIG
      },

        // Services
        BreadcrumbsService, NavigationService, SpinnerService, TitleBarService
      ]
    };
  }
}
