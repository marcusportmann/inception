/*
 * Copyright 2018 Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Import Angular modules
import {CommonModule} from '@angular/common';
import {FormsModule} from "@angular/forms";
import {NgModule} from '@angular/core';

// Import Angular classes
import {RouterModule, Routes} from "@angular/router";

// Import Inception module
import {InceptionModule} from '../../inception/inception.module';

// Buttons component
import {ButtonsComponent} from './buttons.component';

// Cards component
import {CardsComponent} from './cards.component';

// Collapse Component
import {CollapseModule} from 'ngx-bootstrap/collapse';
import {CollapsesComponent} from './collapses.component';

// Colors component
import {ColorsComponent} from './colors.component';

// Dropdowns component
import {BsDropdownModule} from 'ngx-bootstrap/dropdown';
import {DropdownsComponent} from './dropdowns.component';

// Loading Buttons component
import {LoadingButtonsComponent} from './loading-buttons.component';

// Pagination Component
import {PaginationModule} from 'ngx-bootstrap/pagination';
import {PaginationsComponent} from './paginations.component';

// Popovers Component
import {PopoverModule} from 'ngx-bootstrap/popover';
import {PopoversComponent} from './popovers.component';

// Progress Component
import {ProgressbarModule} from 'ngx-bootstrap/progressbar';
import {ProgressComponent} from './progress.component';

// Switches Component
import {SwitchesComponent} from './switches.component';

// Tabs Component
import {TabsModule} from 'ngx-bootstrap/tabs';
import {TabsComponent} from './tabs.component';

// Tooltips Component
import {TooltipModule} from 'ngx-bootstrap/tooltip';
import {TooltipsComponent} from './tooltips.component';

// Typography Component
import {TypographyComponent} from './typography.component';


const routes: Routes = [
  {
    path: '',
    redirectTo: 'buttons',
    pathMatch: 'full',
  },
  {
    path: 'buttons',
    component: ButtonsComponent,
    data: {
      title: 'Buttons',
    }
  },
  {
    path: 'cards',
    component: CardsComponent,
    data: {
      title: 'Cards',
    }
  },
  {
    path: 'collapses',
    component: CollapsesComponent,
    data: {
      title: 'Collapses',
    }
  },
  {
    path: 'colors',
    component: ColorsComponent,
    data: {
      title: 'Colors',
    }
  },
  {
    path: 'dropdowns',
    component: DropdownsComponent,
    data: {
      title: 'Dropdowns',
    }
  },
  {
    path: 'loading-buttons',
    component: LoadingButtonsComponent,
    data: {
      title: 'Loading Buttons',
    }
  },
  {
    path: 'paginations',
    component: PaginationsComponent,
    data: {
      title: 'Paginations',
    }
  },
  {
    path: 'popovers',
    component: PopoversComponent,
    data: {
      title: 'Popovers',
    }
  },
  {
    path: 'progress',
    component: ProgressComponent,
    data: {
      title: 'Progress',
    }
  },
  {
    path: 'switches',
    component: SwitchesComponent,
    data: {
      title: 'Switches',
    }
  },
  {
    path: 'tabs',
    component: TabsComponent,
    data: {
      title: 'Tabs',
    }
  },
  {
    path: 'tooltips',
    component: TooltipsComponent,
    data: {
      title: 'Tooltips',
    }
  },
  {
    path: 'typography',
    component: TypographyComponent,
    data: {
      title: 'Typography',
    }
  }
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,

    BsDropdownModule.forRoot(),
    CollapseModule.forRoot(),
    PaginationModule.forRoot(),
    PopoverModule.forRoot(),
    ProgressbarModule.forRoot(),
    TabsModule.forRoot(),
    TooltipModule.forRoot(),

    InceptionModule,

    RouterModule.forChild(routes)
  ],
  declarations: [ButtonsComponent, CardsComponent, CollapsesComponent, ColorsComponent, DropdownsComponent, LoadingButtonsComponent, PaginationsComponent, PopoversComponent, ProgressComponent, SwitchesComponent, TabsComponent, TooltipsComponent, TypographyComponent]
})
export class ComponentsModule {
}
