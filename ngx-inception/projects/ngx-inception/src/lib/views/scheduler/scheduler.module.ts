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
import {JobsComponent} from './jobs.component';
import {JobsTitleResolver} from './jobs-title-resolver';
import {JobTitleResolver} from './job-title-resolver';
import {EditJobTitleResolver} from './edit-job-title-resolver';
import {NewJobTitleResolver} from './new-job-title-resolver';
import {EditJobComponent} from './edit-job.component';
import {NewJobComponent} from './new-job.component';
import {JobParameterDialogComponent} from './job-parameter-dialog.component';

const routes: Routes = [{
  path: 'jobs',
  resolve: {
    title: JobsTitleResolver
  },
  children: [{
    path: '',
    canActivate: [CanActivateFunctionGuard],
    component: JobsComponent,
    data: {
      authorities: ['ROLE_Administrator', 'FUNCTION_Scheduler.SchedulerAdministration', 'FUNCTION_Scheduler.JobAdministration'
      ]
    }
  },


    {
      path: 'new',
      pathMatch: 'full',
      canActivate: [CanActivateFunctionGuard],
      component: NewJobComponent,
      data: {
        authorities: ['ROLE_Administrator', 'FUNCTION_Scheduler.SchedulerAdministration', 'FUNCTION_Scheduler.JobAdministration'
        ]
      },
      resolve: {
        title: NewJobTitleResolver
      }
    }, {
      path: ':jobId',
      pathMatch: 'full',
      redirectTo: ':jobId/edit'
    }, {
      path: ':jobId',
      resolve: {
        title: JobTitleResolver
      },
      children: [{
        path: 'edit',
        canActivate: [CanActivateFunctionGuard],
        component: EditJobComponent,
        data: {
          authorities: ['ROLE_Administrator', 'FUNCTION_Scheduler.SchedulerAdministration', 'FUNCTION_Scheduler.JobAdministration'
          ]
        },
        resolve: {
          title: EditJobTitleResolver
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
  entryComponents: [JobParameterDialogComponent],
  declarations: [EditJobComponent, JobParameterDialogComponent, JobsComponent, NewJobComponent
  ],
  providers: [JobsTitleResolver, JobTitleResolver, EditJobTitleResolver, NewJobTitleResolver]
})
export class SchedulerModule {
}



