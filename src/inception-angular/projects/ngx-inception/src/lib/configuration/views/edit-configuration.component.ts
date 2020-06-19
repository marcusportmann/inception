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
import {finalize, first} from 'rxjs/operators';
import {ActivatedRoute, Router} from '@angular/router';
import {AdminContainerView} from '../../layout/components/admin-container-view';
import {Configuration} from '../services/configuration';
import {ConfigurationService} from '../services/configuration.service';
import {DialogService} from '../../dialog/services/dialog.service';
import {SpinnerService} from '../../layout/services/spinner.service';
import {ConfigurationServiceError} from '../services/configuration.service.errors';
import {AccessDeniedError} from '../../core/errors/access-denied-error';
import {SystemUnavailableError} from '../../core/errors/system-unavailable-error';
import {Error} from '../../core/errors/error';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {BackNavigation} from '../../layout/components/back-navigation';

/**
 * The EditConfigurationComponent class implements the edit configuration component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'edit-configuration.component.html',
  styleUrls: ['edit-configuration.component.css'],
})
export class EditConfigurationComponent extends AdminContainerView implements AfterViewInit {

  configuration?: Configuration;

  descriptionFormControl: FormControl;

  editConfigurationForm: FormGroup;

  key: string;

  keyFormControl: FormControl;

  valueFormControl: FormControl;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private configurationService: ConfigurationService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Retrieve the route parameters
    const key = this.activatedRoute.snapshot.paramMap.get('key');

    if (key == null) {
      throw(new Error('No key route parameter found'));
    }

    this.key = decodeURIComponent(key);

    // Initialise the form controls
    this.descriptionFormControl = new FormControl('', [Validators.maxLength(100)]);
    this.keyFormControl = new FormControl({
      value: '',
      disabled: true
    }, [Validators.required, Validators.maxLength(100)]);
    this.valueFormControl = new FormControl('', [Validators.maxLength(4000)]);

    // Initialise the form
    this.editConfigurationForm = new FormGroup({
      description: this.descriptionFormControl,
      key: this.keyFormControl,
      value: this.valueFormControl
    });
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation('Configuration', ['../..'], {relativeTo: this.activatedRoute});
  }

  get title(): string {
    return 'Edit Configuration';
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../..'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    // Retrieve the existing configuration and initialise the form controls
    this.spinnerService.showSpinner();

    this.configurationService.getConfiguration(this.key)
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((configuration: Configuration) => {
        this.configuration = configuration;

        this.keyFormControl.setValue(configuration.key);
        this.valueFormControl.setValue(configuration.value);
        this.descriptionFormControl.setValue(configuration.description);
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

  ok(): void {
    if (this.configuration && this.editConfigurationForm.valid) {
      this.configuration.description = this.descriptionFormControl.value;
      this.configuration.value = this.valueFormControl.value;

      this.spinnerService.showSpinner();

      this.configurationService.saveConfiguration(this.configuration)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['../..'], {relativeTo: this.activatedRoute});
        }, (error: Error) => {
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
