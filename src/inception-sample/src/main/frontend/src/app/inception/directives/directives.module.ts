import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import {
  AsideToggleDirective,
  AutofocusDirective,
  ReplaceDirective,
  NavDropdownDirective,
  NavDropdownToggleDirective,
  SidebarToggleDirective,
  SidebarMinimizeDirective,
  BrandMinimizeDirective,
  SidebarOffCanvasCloseDirective,
  MobileSidebarToggleDirective,
  ValidatedFormDirective
} from './'
import {FormsModule, ReactiveFormsModule} from "@angular/forms";


@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  exports: [
    AsideToggleDirective,
    AutofocusDirective,
    ReplaceDirective,
    NavDropdownDirective,
    NavDropdownToggleDirective,
    SidebarToggleDirective,
    SidebarMinimizeDirective,
    BrandMinimizeDirective,
    SidebarOffCanvasCloseDirective,
    MobileSidebarToggleDirective,
    ValidatedFormDirective
  ],
  declarations: [
    AsideToggleDirective,
    AutofocusDirective,
    ReplaceDirective,
    NavDropdownDirective,
    NavDropdownToggleDirective,
    SidebarToggleDirective,
    SidebarMinimizeDirective,
    BrandMinimizeDirective,
    SidebarOffCanvasCloseDirective,
    MobileSidebarToggleDirective,
    ValidatedFormDirective
  ]
})
export class DirectivesModule { }

