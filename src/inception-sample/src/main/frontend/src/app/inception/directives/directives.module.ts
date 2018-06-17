import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import {
  AppAsideToggleDirective,
  AutofocusDirective,
  BrandMinimizeDirective,


  MobileSidebarToggleDirective,
  SidebarToggleDirective,
  SidebarMinimizeDirective,
  SidebarOffCanvasCloseDirective,

  ValidatedFormDirective




  //ReplaceDirective,


  //NavigationDropdownDirective,
  //NavigationDropdownToggleDirective,
  //SidebarToggleDirective,
  //SidebarMinimizeDirective,
  //SidebarOffCanvasCloseDirective,
  //MobileSidebarToggleDirective,

} from './'
import {FormsModule, ReactiveFormsModule} from "@angular/forms";


@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  exports: [
    AppAsideToggleDirective,
    AutofocusDirective,
    BrandMinimizeDirective,

    MobileSidebarToggleDirective,
    SidebarToggleDirective,
    SidebarMinimizeDirective,
    SidebarOffCanvasCloseDirective,

    ValidatedFormDirective



    //ReplaceDirective,
    //NavigationDropdownDirective,
    //NavigationDropdownToggleDirective,
    // SidebarToggleDirective,
    // SidebarMinimizeDirective,
    // SidebarOffCanvasCloseDirective,
    // MobileSidebarToggleDirective,
    // ValidatedFormDirective
  ],
  declarations: [
    AppAsideToggleDirective,
    AutofocusDirective,
    BrandMinimizeDirective,

    MobileSidebarToggleDirective,
    SidebarToggleDirective,
    SidebarMinimizeDirective,
    SidebarOffCanvasCloseDirective,

    ValidatedFormDirective



    //ReplaceDirective,
    //NavigationDropdownDirective,
    //NavigationDropdownToggleDirective,
    // SidebarToggleDirective,
    // SidebarMinimizeDirective,
    // SidebarOffCanvasCloseDirective,
    // MobileSidebarToggleDirective,
    // ValidatedFormDirective
  ]
})
export class DirectivesModule { }

