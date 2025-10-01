/*
 * Copyright Marcus Portmann
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

import { CommonModule } from '@angular/common';
import {
  provideHttpClient,
  withInterceptorsFromDi
} from '@angular/common/http';
import { ModuleWithProviders, NgModule } from '@angular/core';
import { ReportingService } from './reporting.service';

/**
 * The ReportingServicesModule class implements the Inception Reporting Services Module.
 *
 * @author Marcus Portmann
 */
@NgModule({
  declarations: [],
  exports: [],
  imports: [
    // Angular modules
    CommonModule
  ],
  providers: [provideHttpClient(withInterceptorsFromDi())]
})
export class ReportingServicesModule {
  constructor() {}

  static forRoot(): ModuleWithProviders<ReportingServicesModule> {
    return {
      ngModule: ReportingServicesModule,
      providers: [ReportingService]
    };
  }
}
