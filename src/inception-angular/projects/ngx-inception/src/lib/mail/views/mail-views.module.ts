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

import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MailTemplatesComponent} from './mail-templates.component';
import {NewMailTemplateComponent} from './new-mail-template.component';
import {NewMailTemplateTitleResolver} from './new-mail-template-title-resolver';
import {MailTemplateTitleResolver} from './mail-template-title-resolver';
import {EditMailTemplateComponent} from './edit-mail-template.component';
import {EditMailTemplateTitleResolver} from './edit-mail-template-title-resolver';
import {MailTemplatesTitleResolver} from './mail-templates-title-resolver';
import {CoreModule} from '../../core/core.module';
import {DialogModule} from '../../dialog/dialog.module';
import {LayoutModule} from '../../layout/layout.module';
import {MailModule} from '../mail.module';
import {CanActivateFunctionGuard} from '../../security/routing/can-activate-function-guard';

const routes: Routes = [{
  path: 'mail-templates',
  resolve: {
    title: MailTemplatesTitleResolver
  },
  children: [{
    path: '',
    canActivate: [CanActivateFunctionGuard],
    component: MailTemplatesComponent,
    data: {
      authorities: ['ROLE_Administrator', 'FUNCTION_Mail.MailAdministration', 'FUNCTION_Mail.MailTemplateAdministration'
      ]
    }
  }, {
    path: 'new',
    pathMatch: 'full',
    canActivate: [CanActivateFunctionGuard],
    component: NewMailTemplateComponent,
    data: {
      authorities: ['ROLE_Administrator', 'FUNCTION_Mail.MailAdministration', 'FUNCTION_Mail.MailTemplateAdministration'
      ]
    },
    resolve: {
      title: NewMailTemplateTitleResolver
    }
  }, {
    path: ':mailTemplateId',
    pathMatch: 'full',
    redirectTo: ':mailTemplateId/edit'
  }, {
    path: ':mailTemplateId',
    resolve: {
      title: MailTemplateTitleResolver
    },
    children: [{
      path: 'edit',
      canActivate: [CanActivateFunctionGuard],
      component: EditMailTemplateComponent,
      data: {
        authorities: ['ROLE_Administrator', 'FUNCTION_Mail.MailAdministration',
          'FUNCTION_Mail.MailTemplateAdministration'
        ]
      },
      resolve: {
        title: EditMailTemplateTitleResolver
      }
    }
    ]
  }
  ]
}
];

@NgModule({
  declarations: [

    // Components
    EditMailTemplateComponent, MailTemplatesComponent, NewMailTemplateComponent
  ],
  imports: [

    // Angular modules
    CommonModule, FormsModule, ReactiveFormsModule, RouterModule.forChild(routes),

    // Inception modules
    CoreModule.forRoot(), DialogModule.forRoot(), LayoutModule.forRoot(), MailModule.forRoot()
  ],
  providers: [

    // Resolvers
    MailTemplatesTitleResolver, MailTemplateTitleResolver, EditMailTemplateTitleResolver, NewMailTemplateTitleResolver
  ]
})
export class MailViewsModule {
}



