import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';

import { LoginComponent } from './login.component';
import { LoginRoutingModule } from './login-routing.module';


@NgModule({
  imports: [
    CommonModule,
    LoginRoutingModule,
    ReactiveFormsModule
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
