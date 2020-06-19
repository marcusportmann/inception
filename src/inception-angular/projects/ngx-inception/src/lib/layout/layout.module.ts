/*
 * Copyright 2020 Marcus Portmann
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
import {DialogModule} from '../dialog/dialog.module';
import {RouterModule} from '@angular/router';
import {SecurityModule} from '../security/security.module';
import {AdminContainerComponent} from './components/admin-container.component';
import {AdminFooterComponent} from './components/admin-footer.component';
import {AdminHeaderComponent} from './components/admin-header.component';
import {BreadcrumbsComponent} from './components/breadcrumbs.component';
import {NotFoundComponent} from './components/not-found.component';
import {SidebarComponent} from './components/sidebar.component';
import {SidebarFooterComponent} from './components/sidebar-footer.component';
import {SidebarNavDropdownComponent} from './components/sidebar-nav-dropdown.component';
import {SidebarNavItemComponent} from './components/sidebar-nav-item.component';
import {SidebarMinimizerComponent} from './components/sidebar-minimizer.component';
import {SidebarNavComponent} from './components/sidebar-nav.component';
import {SpinnerComponent} from './components/spinner.component';
import {TitleBarComponent} from './components/title-bar.component';
import {SidebarFormComponent} from './components/sidebar-form.component';
import {SidebarHeaderComponent} from './components/sidebar-header.component';
import {SimpleContainerComponent} from './components/simple-container.component';
import {BrandMinimizerDirective} from './directives/brand-minimizer.directive';
import {MobileSidebarTogglerDirective} from './directives/mobile-sidebar-toggler.directive';
import {SidebarMinimizerDirective} from './directives/sidebar-minimizer.directive';
import {SidebarNavDropdownDirective} from './directives/sidebar-nav-dropdown.directive';
import {SidebarNavDropdownTogglerDirective} from './directives/sidebar-nav-dropdown-toggler.directive';
import {SidebarOffCanvasCloseDirective} from './directives/sidebar-off-canvas-close.directive';
import {SidebarTogglerDirective} from './directives/sidebar-toggler.directive';

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
  ],
  entryComponents: [SpinnerComponent]
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
      }
      ]
    };
  }
}
