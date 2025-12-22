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

import { AfterViewInit, Component, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  AdminContainerView, BackNavigation, CoreModule, ValidatedFormDirective
} from 'ngx-inception/core';
import { finalize, first } from 'rxjs/operators';
import { Code } from '../services/code';
import { CodesService } from '../services/codes.service';

/**
 * The EditCodeComponent class implements the edit code component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-codes-edit-code',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective],
  templateUrl: 'edit-code.component.html',
  styleUrls: ['edit-code.component.css']
})
export class EditCodeComponent extends AdminContainerView implements AfterViewInit {
  code: Code | null = null;

  codeCategoryId: string;

  codeId: string;

  editCodeForm: FormGroup;

  idControl: FormControl;

  nameControl: FormControl;

  readonly title = $localize`:@@codes_edit_code_title:Edit Code`;

  valueControl: FormControl;

  private readonly codesService = inject(CodesService);

  constructor() {
    super();

    // Retrieve and decode route parameters
    const codeCategoryId = this.activatedRoute.snapshot.paramMap.get('codeCategoryId');
    const codeId = this.activatedRoute.snapshot.paramMap.get('codeId');

    if (!codeCategoryId || !codeId) {
      throw new globalThis.Error('Required route parameters are missing');
    }

    this.codeCategoryId = codeCategoryId;
    this.codeId = codeId;

    // Initialize the form controls
    this.idControl = new FormControl(
      {
        value: '',
        disabled: true
      },
      [Validators.required, Validators.maxLength(100)]
    );
    this.nameControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.valueControl = new FormControl('', [Validators.required]);

    // Initialize the form
    this.editCodeForm = new FormGroup({
      id: this.idControl,
      name: this.nameControl,
      value: this.valueControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation($localize`:@@codes_edit_code_back_navigation:Codes`, ['.'], {
      relativeTo: this.activatedRoute.parent?.parent
    });
  }

  cancel(): void {
    void this.router.navigate(['.'], { relativeTo: this.activatedRoute.parent?.parent });
  }

  ngAfterViewInit(): void {
    this.spinnerService.showSpinner();

    this.codesService
      .getCode(this.codeCategoryId, this.codeId)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (code: Code) => {
          this.code = code;
          this.idControl.setValue(code.id);
          this.nameControl.setValue(code.name);
          this.valueControl.setValue(code.value);
        },
        error: (error: Error) =>
          this.handleError(error, true, ['.'], { relativeTo: this.activatedRoute.parent?.parent })
      });
  }

  ok(): void {
    if (this.code && this.editCodeForm.valid) {
      this.code.name = this.nameControl.value;
      this.code.value = this.valueControl.value;

      this.spinnerService.showSpinner();

      this.codesService
        .updateCode(this.code)
        .pipe(
          first(),
          finalize(() => this.spinnerService.hideSpinner())
        )
        .subscribe({
          next: () => {
            void this.router.navigate(['.'], { relativeTo: this.activatedRoute.parent?.parent });
          },
          error: (error: Error) => this.handleError(error, false)
        });
    }
  }
}
