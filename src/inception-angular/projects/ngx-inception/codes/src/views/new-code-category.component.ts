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

import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  AdminContainerView, BackNavigation, CoreModule, ValidatedFormDirective
} from 'ngx-inception/core';
import { finalize, first } from 'rxjs/operators';
import { CodeCategory } from '../services/code-category';
import { CodesService } from '../services/codes.service';

/**
 * The NewCodeCategoryComponent class implements the new code category component.
 *
 * @author Marcus
 */
@Component({
  selector: 'inception-codes-new-code-category',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective],
  templateUrl: 'new-code-category.component.html',
  styleUrls: ['new-code-category.component.css']
})
export class NewCodeCategoryComponent extends AdminContainerView implements OnInit {
  codeCategory: CodeCategory | null = null;

  readonly dataControl: FormControl<string | null>;

  readonly idControl: FormControl<string>;

  readonly nameControl: FormControl<string>;

  readonly newCodeCategoryForm: FormGroup<{
    data: FormControl<string | null>;
    id: FormControl<string>;
    name: FormControl<string>;
  }>;

  readonly title = $localize`:@@codes_new_code_category_title:New Code Category`;

  private readonly codesService = inject(CodesService);

  constructor() {
    super();

    // Initialize form controls
    this.dataControl = new FormControl<string | null>('');

    this.idControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(100)]
    });

    this.nameControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(100)]
    });

    // Initialize the form group
    this.newCodeCategoryForm = new FormGroup({
      data: this.dataControl,
      id: this.idControl,
      name: this.nameControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation(
      $localize`:@@codes_new_code_category_back_navigation:Code Categories`,
      ['.'],
      { relativeTo: this.activatedRoute.parent }
    );
  }

  cancel(): void {
    void this.router.navigate(['.'], { relativeTo: this.activatedRoute.parent });
  }

  ngOnInit(): void {
    // Initialize a new instance of CodeCategory
    this.codeCategory = new CodeCategory('', '');
  }

  ok(): void {
    if (this.codeCategory && this.newCodeCategoryForm.valid) {
      const data = this.dataControl.value?.trim();

      this.codeCategory.id = this.idControl.value?.trim();
      this.codeCategory.name = this.nameControl.value?.trim();
      this.codeCategory.data = data || null;

      this.newCodeCategoryForm.disable();

      this.spinnerService.showSpinner();

      this.codesService
        .createCodeCategory(this.codeCategory)
        .pipe(
          first(),
          finalize(() => {
            this.spinnerService.hideSpinner();

            this.newCodeCategoryForm.enable();
          })
        )
        .subscribe({
          next: () => this.router.navigate(['.'], { relativeTo: this.activatedRoute.parent }),
          error: (error: Error) => this.handleError(error, false)
        });
    }
  }
}
