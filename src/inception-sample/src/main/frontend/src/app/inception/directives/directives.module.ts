import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import {
  AsideToggleDirective,
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
    AsideToggleDirective,
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
    AsideToggleDirective,
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

