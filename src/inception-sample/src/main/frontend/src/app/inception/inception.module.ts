// Import Angular decorators
import {NgModule} from '@angular/core';

// Import Angular modules
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {ReactiveFormsModule} from '@angular/forms';
import {Router, RouterModule} from '@angular/router';

// Import Angular components
import {HashLocationStrategy, LocationStrategy} from '@angular/common';

// Import Inception modules
import {DirectivesModule} from "./directives/directives.module";

// Import Inception components
import {
  AdminContainerComponent,
  AsideComponent,
  BreadcrumbsComponent,
  ContainerComponent,
  ErrorModalComponent,
  FooterComponent,
  HeaderComponent, NotFoundComponent,
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
import {ErrorService} from './services/error/error.service';
import {NavigationService} from "./services/navigation/navigation.service";
import {SecurityService} from './services/security/security.service';
import {SessionService} from './services/session/session.service';

// Import 3rd party modules
import {BsDatepickerModule} from 'ngx-bootstrap';
import {BsDropdownModule} from 'ngx-bootstrap/dropdown';
import {ChartsModule} from 'ng2-charts/ng2-charts';
import {ModalModule} from 'ngx-bootstrap/modal';
import {SelectModule} from 'ng-select';
import {StorageServiceModule} from "angular-webstorage-service";
import {TabsModule} from 'ngx-bootstrap/tabs';
import {TimepickerModule} from 'ngx-bootstrap';

// Import 3rd party components
import {BsDatepickerConfig, BsDaterangepickerConfig} from 'ngx-bootstrap';
import {SessionInterceptor} from "./services/session/session.interceptor";

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
    StorageServiceModule,
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
    NotFoundComponent,
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
    ModalModule,
    SelectModule,
    StorageServiceModule,
    TabsModule,
    TimepickerModule,

    DirectivesModule
  ],
  providers: [{
      provide: LocationStrategy,
      useClass: HashLocationStrategy,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: SessionInterceptor,
      multi: true
    },
    ErrorService,
    NavigationService,
    SecurityService,
    SessionService],
  bootstrap: [AdminContainerComponent, ErrorModalComponent, SimpleContainerComponent]
})
export class InceptionModule {

  public static forgottenPasswordEnabled = false;

  public static registrationEnabled = false;

  constructor(private router: Router, private navigationService: NavigationService, private bsDatepickerConfig: BsDatepickerConfig, private bsDaterangepickerConfig: BsDaterangepickerConfig) {
    console.log('Initialising InceptionModule');
    this.bsDatepickerConfig.dateInputFormat = 'YYYY-MM-DD';
    this.bsDaterangepickerConfig.rangeInputFormat = 'YYYY-MM-DD';

    // Initialise the default routes for the Inception module
    this.initDefaultRoutes();
  }

  /**
   * Initialise the default routes for the Inception module.
   */
  private initDefaultRoutes() {
    this.router.config.unshift({path: 'login', component: SimpleContainerComponent, loadChildren: './views/login/login.module#LoginModule' });
  }
}



