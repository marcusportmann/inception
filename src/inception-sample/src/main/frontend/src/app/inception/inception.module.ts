// Import Angular decorators
import { NgModule } from '@angular/core';

// Import Angular modules
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

// Import Angular components
import { HashLocationStrategy, LocationStrategy } from '@angular/common';

// Import Inception directives
import { DirectivesModule } from "./directives/directives.module";

// Import Inception components
import {
  AdminContainerComponent,
  AsideComponent,
  BreadcrumbsComponent,
  ContainerComponent,
  ErrorModalComponent,
  FooterComponent,
  HeaderComponent,
  SidebarComponent,
  SidebarFooterComponent,
  SidebarFormComponent,
  SidebarHeaderComponent,
  SidebarMinimizerComponent,
  SidebarNavComponent,
  SidebarNavDropdownComponent,
  SidebarNavItemComponent,
  SidebarNavLinkComponent,
  SidebarNavTitleComponent,
  SimpleContainerComponent,
} from './components/layout';

// Import Inception services
import { ErrorService } from './services/error/error.service';
import { SecurityService } from './services/security/security.service';

// Import 3rd party components
import { BsDatepickerConfig, BsDaterangepickerConfig } from 'ngx-bootstrap';
import { BsDatepickerModule } from 'ngx-bootstrap';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { ChartsModule } from 'ng2-charts/ng2-charts';
import { ModalModule } from 'ngx-bootstrap/modal';
import { SelectModule } from 'ng-select';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { TimepickerModule } from 'ngx-bootstrap';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,

    BsDatepickerModule.forRoot(),
    BsDropdownModule.forRoot(),
    ChartsModule,
    ModalModule.forRoot(),
    SelectModule,
    TabsModule.forRoot(),
    TimepickerModule.forRoot(),

    DirectivesModule
  ],
  declarations: [
    AdminContainerComponent,
    AsideComponent,
    BreadcrumbsComponent,
    ContainerComponent,
    ErrorModalComponent,
    FooterComponent,
    HeaderComponent,
    SidebarComponent,
    SidebarFooterComponent,
    SidebarFormComponent,
    SidebarHeaderComponent,
    SidebarMinimizerComponent,
    SidebarNavComponent,
    SidebarNavDropdownComponent,
    SidebarNavItemComponent,
    SidebarNavLinkComponent,
    SidebarNavTitleComponent,
    SimpleContainerComponent
  ],
  exports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    RouterModule,

    BsDatepickerModule,
    BsDropdownModule,
    ChartsModule,
    SelectModule,
    TabsModule,
    TimepickerModule,

    DirectivesModule
  ],
  providers: [{
      provide: LocationStrategy,
      useClass: HashLocationStrategy,
    },
    ErrorService,
    SecurityService],
  bootstrap: [ErrorModalComponent]
})
export class InceptionModule {

  public static forgottenPasswordEnabled = false;

  public static registrationEnabled = false;

  public constructor(private bsDatepickerConfig: BsDatepickerConfig, private bsDaterangepickerConfig: BsDaterangepickerConfig) {
    this.bsDatepickerConfig.dateInputFormat = 'YYYY-MM-DD';
    this.bsDaterangepickerConfig.rangeInputFormat = 'YYYY-MM-DD';
  }
}


