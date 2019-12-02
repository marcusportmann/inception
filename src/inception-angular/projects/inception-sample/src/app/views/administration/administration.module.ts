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


import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';

import {RouterModule, Routes} from '@angular/router';

import {AdministrationComponent} from './administration.component';
import {AdministrationTitleResolver} from './administration-title-resolver';
import {SystemTitleResolver} from './system-title-resolver';
import {CodeCategoriesTitleResolver} from 'ngx-inception';
import {ConfigurationsTitleResolver} from 'ngx-inception';
// import {SecurityTitleResolver} from 'ngx-inception';
// import {MailTitleResolver} from 'ngx-inception';
// import {SchedulerTitleResolver} from 'ngx-inception';
// import {ReportingTitleResolver} from 'ngx-inception';

const routes: Routes = [{
  path: '',
  pathMatch: 'full',
  component: AdministrationComponent,
  resolve: {
    title: AdministrationTitleResolver
  }
}
/*
, {
  path: 'security',
  resolve: {
    title: SecurityTitleResolver
  },
  loadChildren: () => import('../wrappers/security-views-wrapper.module').then(
    m => m.SecurityViewsWrapperModule)
}
*/

, {
  path: 'system',
  resolve: {
    title: SystemTitleResolver
  },
  children: [{
    path: 'code-categories',
    resolve: {
      title: CodeCategoriesTitleResolver
    },
    loadChildren: () => import('../wrappers/codes-views-wrapper.module').then(m => m.CodesViewsWrapperModule)
  }


  , {
    path: 'configuration',
    resolve: {
      title: ConfigurationsTitleResolver
    },
    loadChildren: () => import('../wrappers/configuration-views-wrapper.module').then(
      m => m.ConfigurationViewsWrapperModule)
  }


  /*
  ,   {
      path: 'mail',
      resolve: {
        title: MailTitleResolver
      },
      loadChildren: () => import('../wrappers/mail-views-wrapper.module').then(m => m.MailViewsWrapperModule)
    }
    */

  /*
    , {
      path: 'scheduler',
      resolve: {
        title: SchedulerTitleResolver
      },
      loadChildren: () => import('../wrappers/scheduler-views-wrapper.module').then(
        m => m.SchedulerViewsWrapperModule)
    }
    */

  /*
    , {
      path: 'reporting',
      resolve: {
        title: ReportingTitleResolver
      },
      loadChildren: () => import('../wrappers/reporting-views-wrapper.module').then(
        m => m.ReportingViewsWrapperModule)
    }
    */

  ]
}
];

@NgModule({
  imports: [CommonModule, RouterModule.forChild(routes)
  ],
  declarations: [AdministrationComponent],
  providers: [

    // Resolvers
    CodeCategoriesTitleResolver, ConfigurationsTitleResolver,


    // ConfigurationsTitleResolver,
    // MailTitleResolver, ReportingTitleResolver, SchedulerTitleResolver, SecurityTitleResolver,
    SystemTitleResolver
  ]
})
export class AdministrationModule {
}
