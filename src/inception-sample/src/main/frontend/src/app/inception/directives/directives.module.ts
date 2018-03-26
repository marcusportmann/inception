import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import {
  AsideToggleDirective,
  AutofocusDirective,
  FormControlDomDirective,
  ReplaceDirective,
  NavDropdownDirective,
  NavDropdownToggleDirective,
  SidebarToggleDirective,
  SidebarMinimizeDirective,
  BrandMinimizeDirective,
  SidebarOffCanvasCloseDirective,
  MobileSidebarToggleDirective
} from './'


@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    AsideToggleDirective,
    AutofocusDirective,
    FormControlDomDirective,
    ReplaceDirective,
    NavDropdownDirective,
    NavDropdownToggleDirective,
    SidebarToggleDirective,
    SidebarMinimizeDirective,
    BrandMinimizeDirective,
    SidebarOffCanvasCloseDirective,
    MobileSidebarToggleDirective
  ],
  exports: [
    AsideToggleDirective,
    AutofocusDirective,
    FormControlDomDirective,
    ReplaceDirective,
    NavDropdownDirective,
    NavDropdownToggleDirective,
    SidebarToggleDirective,
    SidebarMinimizeDirective,
    BrandMinimizeDirective,
    SidebarOffCanvasCloseDirective,
    MobileSidebarToggleDirective
  ]
})
export class DirectivesModule { }

