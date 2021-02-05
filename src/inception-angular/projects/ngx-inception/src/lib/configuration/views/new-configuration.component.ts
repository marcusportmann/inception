/*
 * Copyright 2020 Marcus Portmann
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
import {first} from 'rxjs/operators';
import {ActivatedRoute, Router} from '@angular/router';
import {AdminContainerView} from '../../layout/components/admin-container-view';
import {Configuration} from '../services/configuration';
import {ConfigurationService} from '../services/configuration.service';
import {DialogService} from '../../dialog/services/dialog.service';
import {SpinnerService} from '../../layout/services/spinner.service';
import {AccessDeniedError} from '../../core/errors/access-denied-error';
import {ServiceUnavailableError} from '../../core/errors/service-unavailable-error';
import {Error} from '../../core/errors/error';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {BackNavigation} from '../../layout/components/back-navigation';

/**
 * The NewConfigurationComponent class implements the new configuration component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'new-configuration.component.html',
  styleUrls: ['new-configuration.component.css'],
})
export class NewConfigurationComponent extends AdminContainerView implements AfterViewInit {

  configuration?: Configuration;

  descriptionFormControl: FormControl;

  keyFormControl: FormControl;

  newConfigurationForm: FormGroup;

  valueFormControl: FormControl;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private configurationService: ConfigurationService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Initialise the form controls
    this.descriptionFormControl = new FormControl('', [Validators.maxLength(100)]);
    this.keyFormControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.valueFormControl = new FormControl('', [Validators.maxLength(4000)]);

    // Initialise the form
    this.newConfigurationForm = new FormGroup({
      description: this.descriptionFormControl,
      key: this.keyFormControl,
      value: this.valueFormControl
    });
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation($localize`:@@configuration_new_configuration_back_navigation:Configuration`,
      ['..'], {relativeTo: this.activatedRoute});
  }

  get title(): string {
    return $localize`:@@configuration_new_configuration_title:New Configuration`
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    this.configuration = new Configuration('', '', '');
  }

  ok(): void {
    if (this.configuration && this.newConfigurationForm.valid) {
      this.configuration.description = this.descriptionFormControl.value;
      this.configuration.key = this.keyFormControl.value;
      this.configuration.value = this.valueFormControl.value;

      this.spinnerService.showSpinner();

      this.configurationService.saveConfiguration(this.configuration)
        .pipe(first())
        .subscribe(() => {
          this.spinnerService.hideSpinner();

          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['..'], {relativeTo: this.activatedRoute});
        }, (error: Error) => {
          this.spinnerService.hideSpinner();
          // noinspection SuspiciousTypeOfGuard
          if ((error instanceof AccessDeniedError) || (error instanceof ServiceUnavailableError)) {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigateByUrl('/error/send-error-report', {state: {error}});
          } else {
            this.dialogService.showErrorDialog(error);
          }
        });
    }
  }
}
