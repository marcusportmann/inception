import { NgModule } from '@angular/core';

import { CommonModule, HashLocationStrategy, LocationStrategy } from '@angular/common';

// Import components
import {
  AdminContainerComponent,
  AsideComponent,
  BreadcrumbsComponent,
  FooterComponent,
  HeaderComponent,
  SidebarComponent,
  SidebarFooterComponent,
  SidebarFormComponent,
  SidebarHeaderComponent,
  SidebarMinimizerComponent,
  SimpleContainerComponent,
  INCEPTION_SIDEBAR_NAV_COMPONENTS
} from './components/layout';

const INCEPTION_COMPONENTS = [
  AdminContainerComponent,
  AsideComponent,
  BreadcrumbsComponent,
  FooterComponent,
  HeaderComponent,
  SidebarComponent,
  SidebarFooterComponent,
  SidebarFormComponent,
  SidebarHeaderComponent,
  SidebarMinimizerComponent,
  SimpleContainerComponent,
  INCEPTION_SIDEBAR_NAV_COMPONENTS
];

// Import directives
import {
  AsideToggleDirective,
  NAV_DROPDOWN_DIRECTIVES,
  ReplaceDirective,
  SIDEBAR_TOGGLE_DIRECTIVES
} from './directives';

const INCEPTION_DIRECTIVES = [
  AsideToggleDirective,
  NAV_DROPDOWN_DIRECTIVES,
  ReplaceDirective,
  SIDEBAR_TOGGLE_DIRECTIVES
];

// Import routing module
import { AppRoutingModule } from '../app.routing';

// Import 3rd party components
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { ChartsModule } from 'ng2-charts/ng2-charts';

@NgModule({
  imports: [
    CommonModule,
    AppRoutingModule,
    BsDropdownModule.forRoot(),
    TabsModule.forRoot(),
    ChartsModule
  ],
  declarations: [
    ...INCEPTION_COMPONENTS,
    ...INCEPTION_DIRECTIVES
  ],
  providers: [{
    provide: LocationStrategy,
    useClass: HashLocationStrategy
  }]
})

export class InceptionModule {

  public static forgottenPasswordEnabled = false;

  public static registrationEnabled = false;

  public constructor() {
  }
}


