/*
 * Copyright 2019 Marcus Portmann
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
import {NgModule} from '@angular/core';
// Import Angular classes
import {RouterModule, Routes} from '@angular/router';
// Import Inception components
import {AdministrationComponent} from './administration.component';
import {AdministrationTitleResolver} from './administration-title-resolver';
import {SystemTitleResolver} from './system-title-resolver';
import {CodeCategoriesTitleResolver} from '../../inception/views/codes/code-categories-title-resolver';
import {ConfigurationsTitleResolver} from '../../inception/views/configuration/configurations-title-resolver';
import {SecurityTitleResolver} from './security-title-resolver';
import {JobsTitleResolver} from "../../inception/views/scheduler/jobs-title-resolver";

const routes: Routes = [{
  path: '',
  pathMatch: 'full',
  component: AdministrationComponent,
  resolve: {
    title: AdministrationTitleResolver
  }
}, {
  path: 'system',
  resolve: {
    title: SystemTitleResolver
  },
  children: [{
    path: 'code-categories',
    resolve: {
      title: CodeCategoriesTitleResolver
    },
    loadChildren: () => import('../../inception/views/codes/codes.module').then(m => m.CodesModule)
  }, {
    path: 'configuration',
    resolve: {
      title: ConfigurationsTitleResolver
    },
    loadChildren: () => import('../../inception/views/configuration/configuration.module').then(
      m => m.ConfigurationModule)
  }, {
    path: 'jobs',
    resolve: {
      title: JobsTitleResolver
    },
    loadChildren: () => import('../../inception/views/scheduler/scheduler.module').then(
      m => m.SchedulerModule)
  }
  ]
}, {
  path: 'security',
  resolve: {
    title: SecurityTitleResolver
  },
  loadChildren: () => import('../../inception/views/security/security.module').then(
    m => m.SecurityModule)
}
];

@NgModule({
  imports: [CommonModule, RouterModule.forChild(routes)
  ],
  declarations: [AdministrationComponent],
  providers: [AdministrationTitleResolver, CodeCategoriesTitleResolver, ConfigurationsTitleResolver,
    JobsTitleResolver, SecurityTitleResolver, SystemTitleResolver
  ]
})
export class AdministrationModule {
}
