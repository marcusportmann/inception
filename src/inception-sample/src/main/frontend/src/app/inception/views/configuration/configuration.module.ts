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
import {FormsModule} from '@angular/forms';
import {NgModule} from '@angular/core';
// Import Inception module
import {InceptionModule} from '../../inception.module';
// Import Angular classes
import {RouterModule, Routes} from '@angular/router';
// Import Inception components
import {ConfigurationsComponent} from './configurations.component';
import {EditConfigurationComponent} from './edit-configuration.component';
import {NewConfigurationComponent} from './new-configuration.component';
import {CanActivateFunctionGuard} from '../../routing/can-activate-function-guard';
import {ConfigurationsTitleResolver} from './configurations-title-resolver';
import {NewConfigurationTitleResolver} from './new-configuration-title-resolver';
import {EditConfigurationTitleResolver} from './edit-configuration-title-resolver';

const routes: Routes = [{
  path: 'configuration',
  canActivate: [CanActivateFunctionGuard],
  component: ConfigurationsComponent,
  data: {
    authorities: ['ROLE_Administrator', 'FUNCTION_Configuration.ConfigurationAdministration']
  },
  resolve: {
    title: ConfigurationsTitleResolver
  }
}, {
  path: 'configuration/:key/edit',
  canActivate: [CanActivateFunctionGuard],
  component: EditConfigurationComponent,
  data: {
    authorities: ['ROLE_Administrator', 'FUNCTION_Configuration.ConfigurationAdministration']
  },
  resolve: {
    title: EditConfigurationTitleResolver
  }
}, {
  path: 'new-configuration',
  canActivate: [CanActivateFunctionGuard],
  component: NewConfigurationComponent,
  data: {
    authorities: ['ROLE_Administrator', 'FUNCTION_Configuration.ConfigurationAdministration']
  },
  resolve: {
    title: NewConfigurationTitleResolver
  }
}
];

@NgModule({
  imports: [CommonModule, FormsModule, InceptionModule,

    RouterModule.forChild(routes)
  ],
  declarations: [ConfigurationsComponent, EditConfigurationComponent, NewConfigurationComponent],
  providers: [ConfigurationsTitleResolver, EditConfigurationTitleResolver, NewConfigurationTitleResolver]
})
export class ConfigurationModule {
}
