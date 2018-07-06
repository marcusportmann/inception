import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import {
  AppBrandMinimizerDirective,
  AppMobileSidebarTogglerDirective,

  SidebarMinimizerDirective,
  SidebarNavDropdownDirective,
  SidebarNavDropdownTogglerDirective,
  SidebarOffCanvasCloseDirective,
  SidebarTogglerDirective,

  AutofocusDirective,

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
    AppBrandMinimizerDirective,
    AppMobileSidebarTogglerDirective,

    SidebarMinimizerDirective,
    SidebarNavDropdownDirective,
    SidebarNavDropdownTogglerDirective,
    SidebarOffCanvasCloseDirective,
    SidebarTogglerDirective,

    AutofocusDirective,

    ValidatedFormDirective
  ],
  declarations: [
    AppBrandMinimizerDirective,
    AppMobileSidebarTogglerDirective,

    SidebarMinimizerDirective,
    SidebarNavDropdownDirective,
    SidebarNavDropdownTogglerDirective,
    SidebarOffCanvasCloseDirective,
    SidebarTogglerDirective,

    AutofocusDirective,

    ValidatedFormDirective
  ]
})
export class DirectivesModule { }

