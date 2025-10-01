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

import { AfterViewInit, Component, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {
  AccessDeniedError,
  AdminContainerView,
  BackNavigation,
  DialogService,
  Error,
  InvalidArgumentError,
  ServiceUnavailableError,
  SpinnerService
} from 'ngx-inception/core';
import { Subject } from 'rxjs';
import { finalize, first, takeUntil } from 'rxjs/operators';
import { Config } from '../services/config';
import { ConfigService } from '../services/config.service';

/**
 * The EditConfigComponent class implements the edit config component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'edit-config.component.html',
  styleUrls: ['edit-config.component.css'],
  standalone: false
})
export class EditConfigComponent
  extends AdminContainerView
  implements AfterViewInit, OnDestroy
{
  config: Config | null = null;

  descriptionControl: FormControl<string | null>;

  editConfigForm: FormGroup;

  id: string;

  idControl: FormControl<string | null>;

  valueControl: FormControl<string | null>;

  private destroy$ = new Subject<void>();

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private configService: ConfigService,
    private dialogService: DialogService,
    private spinnerService: SpinnerService
  ) {
    super();

    // Retrieve the route parameters
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    if (!id) {
      throw new Error('No id route parameter found');
    }
    this.id = decodeURIComponent(id);

    // Initialize form controls
    this.descriptionControl = new FormControl('', [Validators.maxLength(100)]);
    this.idControl = new FormControl(
      {
        value: '',
        disabled: true
      },
      [Validators.required, Validators.maxLength(100)]
    );
    this.valueControl = new FormControl('', [Validators.maxLength(4000)]);

    // Initialize the form
    this.editConfigForm = new FormGroup({
      description: this.descriptionControl,
      id: this.idControl,
      value: this.valueControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation(
      $localize`:@@config_edit_config_back_navigation:Config`,
      ['../..'],
      {
        relativeTo: this.activatedRoute
      }
    );
  }

  get title(): string {
    return $localize`:@@config_edit_config_title:Edit Config`;
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../..'], { relativeTo: this.activatedRoute });
  }

  ngAfterViewInit(): void {
    // Retrieve the existing config and initialize the form controls
    this.spinnerService.showSpinner();
    this.configService
      .getConfig(this.id)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner()),
        takeUntil(this.destroy$)
      )
      .subscribe({
        next: (config: Config) => {
          this.config = config;
          this.idControl.setValue(config.id);
          this.valueControl.setValue(config.value);
          this.descriptionControl.setValue(config.description);
        },
        error: (error) => this.handleError(error, () => this.cancel())
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  ok(): void {
    if (this.config && this.editConfigForm.valid) {
      this.config.description = this.descriptionControl.value ?? '';
      this.config.value = this.valueControl.value ?? '';

      this.spinnerService.showSpinner();
      this.configService
        .saveConfig(this.config)
        .pipe(
          first(),
          finalize(() => this.spinnerService.hideSpinner()),
          takeUntil(this.destroy$)
        )
        .subscribe({
          next: () => {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigate(['../..'], {
              relativeTo: this.activatedRoute
            });
          },
          error: (error) => this.handleError(error)
        });
    }
  }

  private handleError(error: Error, onClose?: () => void): void {
    if (
      error instanceof AccessDeniedError ||
      error instanceof InvalidArgumentError ||
      error instanceof ServiceUnavailableError
    ) {
      // noinspection JSIgnoredPromiseFromCall
      this.router.navigateByUrl('/error/send-error-report', {
        state: { error }
      });
    } else {
      this.dialogService
        .showErrorDialog(error)
        .afterClosed()
        .pipe(first(), takeUntil(this.destroy$))
        .subscribe(() => {
          if (onClose) onClose();
        });
    }
  }
}
