/*
 * Copyright 2021 Marcus Portmann
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

import '@angular/localize/init';

import {ObserversModule} from '@angular/cdk/observers';
import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatButtonModule} from '@angular/material/button';
import {MatButtonToggleModule} from "@angular/material/button-toggle";
import {MatCardModule} from "@angular/material/card";
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatDialogModule} from "@angular/material/dialog";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatGridListModule} from "@angular/material/grid-list";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {MatListModule} from "@angular/material/list";
import {MatMenuModule} from "@angular/material/menu";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatRadioModule} from '@angular/material/radio';
import {MatSelectModule} from "@angular/material/select";
import {MatSliderModule} from "@angular/material/slider";
import {MatSortModule} from "@angular/material/sort";
import {MatTableModule} from "@angular/material/table";
import {MatTabsModule} from "@angular/material/tabs";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatTooltipModule} from "@angular/material/tooltip";
import {FileUploadComponent} from './components/file-upload.component';
import {GroupFormFieldComponent} from "./components/group-form-field.component";
import {TableFilterComponent} from './components/table-filter.component';
import {AutocompleteSelectionRequiredDirective} from "./directives/autocomplete-selection-required.directive";
import {AutofocusDirective} from './directives/autofocus.directive';
import {ValidatedFormDirective} from './directives/validated-form.directive';

/**
 * The InceptionModule class implements the Inception framework module.
 *
 * @author Marcus Portmann
 */
@NgModule({
  declarations: [
    // Components
    FileUploadComponent, GroupFormFieldComponent, TableFilterComponent,

    // Directives
    AutocompleteSelectionRequiredDirective, AutofocusDirective, ValidatedFormDirective
  ],
  imports: [
    // Angular modules
    CommonModule, FormsModule,

    // CDK modules
    ObserversModule,

    // Material modules
    MatAutocompleteModule, MatButtonModule, MatButtonToggleModule, MatCardModule, MatCheckboxModule,
    MatDatepickerModule, MatDialogModule, MatExpansionModule, MatFormFieldModule, MatGridListModule,
    MatIconModule, MatInputModule, MatListModule, MatMenuModule, MatPaginatorModule,
    MatProgressBarModule, MatRadioModule, MatSelectModule, MatSliderModule, MatSortModule,
    MatTableModule, MatTabsModule, MatToolbarModule, MatTooltipModule
  ],
  exports: [
    // Angular modules
    CommonModule, FormsModule,

    // Material modules
    MatAutocompleteModule, MatButtonModule, MatButtonToggleModule, MatCardModule, MatCheckboxModule,
    MatDatepickerModule, MatDialogModule, MatExpansionModule, MatFormFieldModule, MatGridListModule,
    MatIconModule, MatInputModule, MatListModule, MatMenuModule, MatPaginatorModule,
    MatProgressBarModule, MatRadioModule, MatSelectModule, MatSliderModule, MatSortModule,
    MatTableModule, MatTabsModule, MatToolbarModule, MatTooltipModule,

    // Components
    FileUploadComponent, GroupFormFieldComponent, TableFilterComponent,

    // Directives
    AutocompleteSelectionRequiredDirective, AutofocusDirective, ValidatedFormDirective,
  ]
})
export class CoreModule {
  constructor() {
  }
}
