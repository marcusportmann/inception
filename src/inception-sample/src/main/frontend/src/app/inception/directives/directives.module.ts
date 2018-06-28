import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import {
  AppBrandMinimizerDirective,
  AppMobileSidebarTogglerDirective,

  AppSidebarMinimizerDirective,
  AppSidebarNavDropdownDirective,
  AppSidebarNavDropdownTogglerDirective,
  AppSidebarOffCanvasCloseDirective,
  AppSidebarTogglerDirective,



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

    AppSidebarMinimizerDirective,
    AppSidebarNavDropdownDirective,
    AppSidebarNavDropdownTogglerDirective,
    AppSidebarOffCanvasCloseDirective,
    AppSidebarTogglerDirective,

    AutofocusDirective,

    ValidatedFormDirective

  ],
  declarations: [
    AppBrandMinimizerDirective,
    AppMobileSidebarTogglerDirective,

    AppSidebarMinimizerDirective,
    AppSidebarNavDropdownDirective,
    AppSidebarNavDropdownTogglerDirective,
    AppSidebarOffCanvasCloseDirective,
    AppSidebarTogglerDirective,

    AutofocusDirective,

    ValidatedFormDirective
  ]
})
export class DirectivesModule { }

