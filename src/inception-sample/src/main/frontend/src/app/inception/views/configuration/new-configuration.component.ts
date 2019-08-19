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

import {AfterViewInit, Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {DialogService} from '../../services/dialog/dialog.service';
import {SpinnerService} from '../../services/layout/spinner.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {Error} from '../../errors/error';
import {first} from 'rxjs/operators';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {AccessDeniedError} from '../../errors/access-denied-error';
import {ConfigurationService} from '../../services/configuration/configuration.service';
import {Configuration} from '../../services/configuration/configuration';
import {ConfigurationServiceError} from '../../services/configuration/configuration.service.errors';
import {AdminContainerView} from '../../components/layout/admin-container-view';
import {BackNavigation} from '../../components/layout/back-navigation';

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
              private formBuilder: FormBuilder, private i18n: I18n,
              private configurationService: ConfigurationService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    // Initialise form controls
    this.descriptionFormControl = new FormControl('', [Validators.maxLength(4000)]);

    this.keyFormControl = new FormControl('', [Validators.required, Validators.maxLength(4000)]);

    this.valueFormControl = new FormControl('', [Validators.maxLength(4000)]);

    // Initialise form
    this.newConfigurationForm = new FormGroup({
      description: this.descriptionFormControl,
      key: this.keyFormControl,
      value: this.valueFormControl
    });
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@new_configuration_component_back_title',
      value: 'Configuration'
    }), ['..'], {relativeTo: this.activatedRoute});
  }

  get title(): string {
    return this.i18n({
      id: '@@new_configuration_component_title',
      value: 'New Configuration'
    })
  }

  ngAfterViewInit(): void {
    // Construct the new configuration
    this.configuration = new Configuration('', '', '');

    // Initialise the form controls
  }

  onCancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], {relativeTo: this.activatedRoute});
  }

  onOK(): void {
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
          if ((error instanceof ConfigurationServiceError) || (error instanceof AccessDeniedError) ||
            (error instanceof SystemUnavailableError)) {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigateByUrl('/error/send-error-report', {state: {error}});
          } else {
            this.dialogService.showErrorDialog(error);
          }
        });
    }
  }
}
