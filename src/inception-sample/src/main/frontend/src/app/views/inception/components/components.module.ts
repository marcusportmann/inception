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
import {InceptionModule} from '../../../inception/inception.module';

// Buttons component
import {ButtonsComponent} from './buttons.component';

// Cards component
import {CardsComponent} from './cards.component';

// Charts Component
import {ChartsComponent} from './charts.component';

// Collapse Component
import {CollapsesComponent} from './collapses.component';

// Pagination Component
import {PaginationComponent} from './pagination.component';

// Popovers Component
import {PopoversComponent} from './popovers.component';

// Progress Component
import {ProgressComponent} from './progress.component';

// Switches Component
import {SwitchesComponent} from './switches.component';

// Tabs Component
import {TabsComponent} from './tabs.component';

// Text Editors Components
import {QuillModule} from 'ngx-quill';
import {TextEditorsComponent} from './text-editors.component';

// Tooltips Component
import {TooltipsComponent} from './tooltips.component';


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
    path: 'charts',
    component: ChartsComponent,
    data: {
      title: 'Charts',
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
    path: 'pagination',
    component: PaginationComponent,
    data: {
      title: 'Pagination',
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
    path: 'text-editors',
    component: TextEditorsComponent,
    data: {
      title: 'Text Editors',
    }
  },
  {
    path: 'tooltips',
    component: TooltipsComponent,
    data: {
      title: 'Tooltips',
    }
  }
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,

    // CollapseModule.forRoot(),
    // PaginationModule.forRoot(),
    // PopoverModule.forRoot(),
    // ProgressbarModule.forRoot(),
    QuillModule,
    // TabsModule.forRoot(),
    // TooltipModule.forRoot(),

    InceptionModule,

    RouterModule.forChild(routes)
  ],
  declarations: [ButtonsComponent, CardsComponent, ChartsComponent, CollapsesComponent, PaginationComponent, PopoversComponent, ProgressComponent, SwitchesComponent, TabsComponent, TextEditorsComponent, TooltipsComponent]
})
export class ComponentsModule {
}
