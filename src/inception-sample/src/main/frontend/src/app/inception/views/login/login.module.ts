import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';

import {DirectivesModule} from "../../directives/directives.module";

import { LoginComponent } from './login.component';
import { LoginRoutingModule } from './login-routing.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,

    DirectivesModule,

    LoginRoutingModule
  ],
  declarations: [
    LoginComponent
  ],
  exports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class LoginModule { }
