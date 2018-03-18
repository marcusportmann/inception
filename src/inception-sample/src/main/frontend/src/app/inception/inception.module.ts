import { NgModule } from '@angular/core';

import { CommonModule } from '@angular/common';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';

// Import layouts
import {
  AdminLayoutComponent,
  SimpleLayoutComponent
} from './layouts';

const INCEPTION_LAYOUTS = [
  AdminLayoutComponent,
  SimpleLayoutComponent
]

// Import components
import {
  InceptionAsideComponent,
  InceptionBreadcrumbsComponent,
  InceptionFooterComponent,
  InceptionHeaderComponent,
  InceptionSidebarComponent,
  InceptionSidebarFooterComponent,
  InceptionSidebarFormComponent,
  InceptionSidebarHeaderComponent,
  InceptionSidebarMinimizerComponent,
  APP_SIDEBAR_NAV
} from './components';

const INCEPTION_COMPONENTS = [
  InceptionAsideComponent,
  InceptionBreadcrumbsComponent,
  InceptionFooterComponent,
  InceptionHeaderComponent,
  InceptionSidebarComponent,
  InceptionSidebarFooterComponent,
  InceptionSidebarFormComponent,
  InceptionSidebarHeaderComponent,
  InceptionSidebarMinimizerComponent,
  APP_SIDEBAR_NAV
]

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
]

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
    ...INCEPTION_LAYOUTS,
    ...INCEPTION_COMPONENTS,
    ...INCEPTION_DIRECTIVES
  ],
  providers: [{
    provide: LocationStrategy,
    useClass: HashLocationStrategy
  }]
})

export class InceptionModule
{
  public static registrationEnabled = true;
}
