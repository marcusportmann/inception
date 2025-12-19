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

import { FocusMonitor } from '@angular/cdk/a11y';
import { coerceBooleanProperty } from '@angular/cdk/coercion';
import {
  ChangeDetectionStrategy, Component, DoCheck, ElementRef, HostBinding, inject, Input, OnDestroy,
  ViewChild
} from '@angular/core';
import {
  AbstractControl, ControlValueAccessor, FormGroupDirective, NgControl, NgForm
} from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { MatFormFieldControl } from '@angular/material/form-field';
import { Subject } from 'rxjs';

@Component({
  selector: 'inception-core-file-upload',
  standalone: true,
  template: `
    <div
      class="file-upload"
      role="button"
      tabindex="0"
      (click)="open()"
      (keydown.enter)="handleKeydown($event)"
      (keydown.space)="handleKeydown($event)"
      [class.disabled]="disabled"
      [class.mat-form-field-should-float]="shouldLabelFloat">
      <span class="filename">{{ fileNames || placeholder }}</span>
    </div>

    <input
      #input
      type="file"
      [attr.accept]="accept || null"
      [attr.multiple]="multiple ? '' : null"
      (change)="onFileSelected($event)"
      hidden />
  `,
  styles: [
    `
      :host {
        display: inline-block;
        width: 100%;
      }

      .file-upload {
        display: inline-flex;
        align-items: center;
        cursor: pointer;
        width: 100%;
      }

      .file-upload.disabled {
        cursor: default;
        opacity: 0.5;
      }

      .filename {
        margin-left: 8px;
        margin-top: 2px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    `
  ],
  providers: [
    {
      provide: MatFormFieldControl,
      useExisting: FileUploadComponent
    }
  ],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FileUploadComponent
  implements MatFormFieldControl<File[]>, ControlValueAccessor, OnDestroy, DoCheck
{
  /** Static counter for unique id */
  private static nextId = 0;

  /** Accept attribute for input[type=file] */
  @Input() accept = '';

  /** Type for mat-form-field */
  readonly controlType = 'file-upload';

  @HostBinding('attr.aria-describedby')
  describedBy = '';

  /** MatFormFieldControl-errorState */
  errorState = false;

  /** Error state matcher (fallback to default) */
  @Input() errorStateMatcher: ErrorStateMatcher;

  /** Control float behavior of mat-label */
  @Input() floatLabel: 'auto' | 'always' | 'never' = 'auto';

  /** MatFormFieldControl-focused */
  focused = false;

  @HostBinding('attr.id')
  id = `file-upload-${FileUploadComponent.nextId++}`;

  @ViewChild('input', { static: true })
  inputRef!: ElementRef<HTMLInputElement>;

  ngControl = inject(NgControl, { optional: true, self: true });

  /** Placeholder shown when no files are selected */
  @Input() placeholder = '';

  /** MatFormFieldControl required */
  readonly stateChanges = new Subject<void>();

  private readonly _elementRef = inject<ElementRef<HTMLElement>>(ElementRef);

  private _explicitRequired = false;

  private _files: File[] | null = null;

  private readonly _parentForm = inject(NgForm, { optional: true });

  private readonly _parentFormGroup = inject(FormGroupDirective, { optional: true });

  private fm = inject(FocusMonitor);

  constructor() {
    const defaultErrorStateMatcher = inject(ErrorStateMatcher);

    if (this.ngControl != null) {
      this.ngControl.valueAccessor = this;
    }

    this.errorStateMatcher = defaultErrorStateMatcher;

    this.fm.monitor(this._elementRef.nativeElement, true).subscribe((origin) => {
      this.focused = !!origin;
      if (!origin) {
        // blur
        this._onTouched();
      }
      this.stateChanges.next();
    });
  }

  private _disabled = false;

  @Input()
  get disabled(): boolean {
    return this._disabled;
  }

  set disabled(value: boolean) {
    const newValue = coerceBooleanProperty(value);
    if (newValue !== this._disabled) {
      this._disabled = newValue;
      this.setDisabledState(this._disabled);
    }
  }

  private _hideRequiredMarker = false;

  /** Whether to hide the required marker (read by mat-form-field) */
  @Input()
  get hideRequiredMarker(): boolean {
    return this._hideRequiredMarker;
  }

  set hideRequiredMarker(value: boolean) {
    const newValue = coerceBooleanProperty(value);
    if (newValue !== this._hideRequiredMarker) {
      this._hideRequiredMarker = newValue;
      this.stateChanges.next();
    }
  }

  private _multiple = false;

  @Input()
  get multiple(): boolean {
    return this._multiple;
  }

  set multiple(value: boolean) {
    const newValue = coerceBooleanProperty(value);
    if (newValue !== this._multiple) {
      this._multiple = newValue;
      this.stateChanges.next();
    }
  }

  @HostBinding('attr.aria-disabled')
  get ariaDisabled() {
    return this.disabled.toString();
  }

  /** MatFormFieldControl.empty */
  get empty(): boolean {
    return !this._files || this._files.length === 0;
  }

  /** Human-readable file names */
  get fileNames(): string {
    return Array.isArray(this._files) && this._files.length
      ? this._files.map((f) => f.name).join(', ')
      : '';
  }

  /** Explicit required attribute */
  @Input()
  get required(): boolean {
    if (this._explicitRequired) {
      return true;
    }

    // auto-detect Validators.required on FormControl
    const control: AbstractControl | null = this.ngControl?.control ?? null;
    if (control?.validator) {
      const errors = control.validator(control);
      return !!errors?.['required'];
    }

    return false;
  }

  set required(value: boolean) {
    const newValue = coerceBooleanProperty(value);
    if (newValue !== this._explicitRequired) {
      this._explicitRequired = newValue;
      this.stateChanges.next();
    }
  }

  /** MatFormFieldControl.shouldLabelFloat */
  @HostBinding('class.mat-form-field-should-float')
  get shouldLabelFloat(): boolean {
    if (this.floatLabel === 'always') {
      return true;
    }
    if (this.floatLabel === 'never') {
      return false;
    }
    // auto
    return this.focused || !!this.fileNames || !!this.placeholder;
  }

  /** MatFormFieldControl.value */
  @Input()
  get value(): File[] | null {
    return this._files;
  }

  set value(files: File[] | null) {
    this._files = Array.isArray(files) ? files : null;
    this._onChange(this._files);
    this.stateChanges.next();
  }

  handleKeydown(event: KeyboardEvent | Event): void {
    if (this.disabled) {
      return;
    }
    event.preventDefault(); // prevent scrolling / button click quirks
    this.open();
  }

  ngDoCheck(): void {
    const control: AbstractControl | null = this.ngControl?.control ?? null;
    const newState = this.errorStateMatcher.isErrorState(
      control,
      this._parentForm || this._parentFormGroup
    );

    if (newState !== this.errorState) {
      this.errorState = newState;
      this.stateChanges.next();
    }
  }

  ngOnDestroy(): void {
    this.stateChanges.complete();
    this.fm.stopMonitoring(this._elementRef.nativeElement);
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  onContainerClick(_: MouseEvent): void {
    if (!this.disabled) {
      this.open();
    }
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this._files = input.files ? Array.from(input.files) : null;
    this._onChange(this._files);

    // Allow selecting the same file again
    input.value = '';

    this.stateChanges.next();
  }

  open(): void {
    if (!this.disabled && this.inputRef?.nativeElement) {
      this.inputRef.nativeElement.click();
    }
  }

  registerOnChange(fn: (files: File[] | null) => void): void {
    this._onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this._onTouched = fn;
  }

  setDescribedByIds(ids: string[]): void {
    this.describedBy = ids.join(' ');
  }

  setDisabledState(isDisabled: boolean): void {
    if (this.inputRef?.nativeElement) {
      this.inputRef.nativeElement.disabled = isDisabled;
    }
    this.stateChanges.next();
  }

  writeValue(files: File[] | null): void {
    this._files = Array.isArray(files) ? files : null;

    // We cannot programmatically set input.files, but we can clear it
    if (!this._files && this.inputRef?.nativeElement) {
      this.inputRef.nativeElement.value = '';
    }

    this.stateChanges.next();
  }

  private _onChange: (files: File[] | null) => void = () => {
    /* empty */
  };
  private _onTouched: () => void = () => {
    /* empty */
  };
}
