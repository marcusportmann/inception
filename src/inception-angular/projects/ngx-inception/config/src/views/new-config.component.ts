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

import {AfterViewInit, Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {
  AccessDeniedError, AdminContainerView, BackNavigation, DialogService, Error, InvalidArgumentError,
  ServiceUnavailableError, SpinnerService
} from 'ngx-inception/core';
import {finalize, first} from 'rxjs/operators';
import {Config} from '../services/config';
import {ConfigService} from '../services/config.service';

/**
 * The NewConfigComponent class implements the new config component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'new-config.component.html',
  styleUrls: ['new-config.component.css'],
})
export class NewConfigComponent extends AdminContainerView implements AfterViewInit {

  config: Config;

  descriptionControl: FormControl;

  idControl: FormControl;

  newConfigForm: FormGroup;

  valueControl: FormControl;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private configService: ConfigService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Initialize the form controls
    this.descriptionControl = new FormControl('', [Validators.maxLength(100)]);
    this.idControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.valueControl = new FormControl('', [Validators.maxLength(4000)]);

    // Initialize the form
    this.newConfigForm = new FormGroup({
      description: this.descriptionControl,
      id: this.idControl,
      value: this.valueControl,
    });

    // Initialize the config object
    this.config = new Config('', '', '');
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation($localize`:@@config_new_config_back_navigation:Config`, ['..'],
      {relativeTo: this.activatedRoute});
  }

  get title(): string {
    return $localize`:@@config_new_config_title:New Config`;
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    // Ensure config object is ready (already initialized in the constructor)
  }

  ok(): void {
    if (this.newConfigForm.valid) {
      // Assign form values to the config object
      this.config.description = this.descriptionControl.value ?? '';
      this.config.id = this.idControl.value ?? '';
      this.config.value = this.valueControl.value ?? '';

      // Show spinner while saving config
      this.spinnerService.showSpinner();

      this.configService.saveConfig(this.config)
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()) // Ensure spinner is hidden after the
        // operation
      )
      .subscribe({
        next: () => {
          // Navigate back on successful save
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['..'], {relativeTo: this.activatedRoute});
        },
        error: (error: Error) => this.handleError(error)
      });
    }
  }

  private handleError(error: Error): void {
    // Centralized error handling
    if (error instanceof AccessDeniedError || error instanceof InvalidArgumentError || error instanceof ServiceUnavailableError) {
      // noinspection JSIgnoredPromiseFromCall
      this.router.navigateByUrl('/error/send-error-report', {state: {error}});
    } else {
      this.dialogService.showErrorDialog(error);
    }
  }
}
