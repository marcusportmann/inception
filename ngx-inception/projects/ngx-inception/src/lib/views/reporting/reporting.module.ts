/*
 * Copyright 2019 Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Import Angular modules
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {NgModule} from '@angular/core';
// Import Inception module
import {InceptionModule} from '../../inception.module';
// Import Angular classes
import {RouterModule, Routes} from '@angular/router';
// Import Inception components
import {CanActivateFunctionGuard} from '../../routing/can-activate-function-guard';
import {ReportDefinitionsComponent} from './report-definitions.component';
import {NewReportDefinitionComponent} from './new-report-definition.component';
import {EditReportDefinitionComponent} from './edit-report-definition.component';
import {EditReportDefinitionTitleResolver} from './edit-report-definition-title-resolver';
import {ReportDefinitionsTitleResolver} from './report-definitions-title-resolver';
import {NewReportDefinitionTitleResolver} from './new-report-definition-title-resolver';
import {ReportDefinitionTitleResolver} from './report-definition-title-resolver';


const routes: Routes = [{
  path: 'report-definitions',
  resolve: {
    title: ReportDefinitionsTitleResolver
  },
  children: [{
    path: '',
    canActivate: [CanActivateFunctionGuard],
    component: ReportDefinitionsComponent,
    data: {
      authorities: ['ROLE_Administrator', 'FUNCTION_Reporting.ReportingAdministration', 'FUNCTION_Reporting.ReportDefinitionAdministration'
      ]
    }
  }, {
    path: 'new',
    pathMatch: 'full',
    canActivate: [CanActivateFunctionGuard],
    component: NewReportDefinitionComponent,
    data: {
      authorities: ['ROLE_Administrator', 'FUNCTION_Reporting.ReportingAdministration', 'FUNCTION_Reporting.ReportDefinitionAdministration'
      ]
    },
    resolve: {
      title: NewReportDefinitionTitleResolver
    }
  }, {
    path: ':reportDefinitionId',
    pathMatch: 'full',
    redirectTo: ':reportDefinitionId/edit'
  },

    {
      path: ':reportDefinitionId',
      resolve: {
        title: ReportDefinitionTitleResolver
      },
      children: [{
        path: 'edit',
        canActivate: [CanActivateFunctionGuard],
        component: EditReportDefinitionComponent,
        data: {
          authorities: ['ROLE_Administrator', 'FUNCTION_Reporting.ReportingAdministration',
            'FUNCTION_Reporting.ReportDefinitionAdministration'
          ]
        },
        resolve: {
          title: EditReportDefinitionTitleResolver
        }
      }
      ]
    }
  ]
}
];

@NgModule({
  imports: [CommonModule, FormsModule, InceptionModule,

    RouterModule.forChild(routes)
  ],
  declarations: [EditReportDefinitionComponent, ReportDefinitionsComponent, NewReportDefinitionComponent
  ],
  providers: [ReportDefinitionsTitleResolver, ReportDefinitionTitleResolver, EditReportDefinitionTitleResolver,
    NewReportDefinitionTitleResolver
  ]
})
export class ReportingModule {
}



