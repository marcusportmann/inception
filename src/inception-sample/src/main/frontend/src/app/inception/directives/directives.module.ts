import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import {
  AppAsideTogglerDirective,
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
    AppAsideTogglerDirective,
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
    AppAsideTogglerDirective,
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

