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

import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  AdminContainerView, BackNavigation, CoreModule, Error, ValidatedFormDirective
} from 'ngx-inception/core';
import { finalize, first } from 'rxjs/operators';
import { Config } from '../services/config';
import { ConfigService } from '../services/config.service';

/**
 * The NewConfigComponent class implements the new config component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-config-new-config',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective],
  templateUrl: 'new-config.component.html',
  styleUrls: ['new-config.component.css']
})
export class NewConfigComponent extends AdminContainerView {
  config: Config;

  descriptionControl: FormControl;

  idControl: FormControl;

  newConfigForm: FormGroup;

  readonly title = $localize`:@@config_new_config_title:New Config`;

  valueControl: FormControl;

  private configService = inject(ConfigService);

  constructor() {
    super();

    // Initialize the form controls
    this.descriptionControl = new FormControl('', [Validators.maxLength(100)]);
    this.idControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.valueControl = new FormControl('', [Validators.maxLength(4000)]);

    // Initialize the form
    this.newConfigForm = new FormGroup({
      description: this.descriptionControl,
      id: this.idControl,
      value: this.valueControl
    });

    // Initialize the config object
    this.config = new Config('', '', '');
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation($localize`:@@config_new_config_back_navigation:Config`, ['..'], {
      relativeTo: this.activatedRoute
    });
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], { relativeTo: this.activatedRoute });
  }

  ok(): void {
    if (this.newConfigForm.valid) {
      // Assign form values to the config object
      this.config.description = this.descriptionControl.value ?? '';
      this.config.id = this.idControl.value ?? '';
      this.config.value = this.valueControl.value ?? '';

      // Show spinner while saving config
      this.spinnerService.showSpinner();

      this.configService
        .saveConfig(this.config)
        .pipe(
          first(),
          finalize(() => this.spinnerService.hideSpinner())
        )
        .subscribe({
          next: () => {
            // Navigate back on successful save
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigate(['..'], { relativeTo: this.activatedRoute });
          },
          error: (error: Error) => this.handleError(error, false)
        });
    }
  }
}
