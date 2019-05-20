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

import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from "@angular/router";
import {DialogService} from "../../services/dialog/dialog.service";
import {SpinnerService} from "../../services/layout/spinner.service";
import {I18n} from "@ngx-translate/i18n-polyfill";
import {Error} from "../../errors/error";
import {first} from "rxjs/operators";
import {SystemUnavailableError} from "../../errors/system-unavailable-error";
import {AccessDeniedError} from "../../errors/access-denied-error";
import {ConfigurationService} from "../../services/configuration/configuration.service";
import {Configuration} from "../../services/configuration/configuration";
import {ConfigurationServiceError} from "../../services/configuration/configuration.service.errors";

/**
 * The NewConfigurationComponent class implements the new configuration component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'new-configuration.component.html',
  styleUrls: ['new-configuration.component.css'],
})
export class NewConfigurationComponent implements OnInit {

  newConfigurationForm: FormGroup;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private configurationService: ConfigurationService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    this.newConfigurationForm = this.formBuilder.group({
      // tslint:disable-next-line
      key: ['', [Validators.required, Validators.maxLength(4000)]],
      value: ['', [Validators.required, Validators.maxLength(4000)]],
      description: [''],
    });
  }

  get descriptionFormControl(): AbstractControl {
    return this.newConfigurationForm.get('description');
  }

  get keyFormControl(): AbstractControl {
    return this.newConfigurationForm.get('key');
  }

  get valueFormControl(): AbstractControl {
    return this.newConfigurationForm.get('value');
  }

  ngOnInit(): void {
  }

  onCancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], {relativeTo: this.activatedRoute});
  }

  onOK(): void {
    if (this.newConfigurationForm.valid) {

      let configuration: Configuration = new Configuration(this.keyFormControl.value,
        this.valueFormControl.value, this.descriptionFormControl.value);

      this.spinnerService.showSpinner();

      this.configurationService.saveConfiguration(configuration).pipe(first()).subscribe(() => {
        this.spinnerService.hideSpinner();

        // noinspection JSIgnoredPromiseFromCall
        this.router.navigate(['..'], {relativeTo: this.activatedRoute});
      }, (error: Error) => {
        this.spinnerService.hideSpinner();

        if ((error instanceof ConfigurationServiceError) || (error instanceof AccessDeniedError) ||
          (error instanceof SystemUnavailableError)) {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigateByUrl('/error/send-error-report', {state: {error: error}});
        } else {
          this.dialogService.showErrorDialog(error);
        }
      });
    }
  }
}
