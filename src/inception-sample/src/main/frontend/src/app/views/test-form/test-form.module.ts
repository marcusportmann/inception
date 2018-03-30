import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';

import { SelectModule } from 'ng-select';

import {DirectivesModule} from '../../inception/directives/directives.module';

import {TestFormComponent} from './test-form.component';
import {TestFormRoutingModule} from './test-form-routing.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,

    SelectModule,

    DirectivesModule,

    TestFormRoutingModule
  ],
  declarations: [
    TestFormComponent
  ],
  exports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class TestFormModule { }
