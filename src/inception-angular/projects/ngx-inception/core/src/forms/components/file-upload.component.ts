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
  Component,
  DoCheck,
  ElementRef,
  HostBinding,
  Input,
  OnDestroy,
  OnInit,
  Optional,
  Self,
  ViewChild
} from '@angular/core';
import {
  AbstractControl,
  ControlValueAccessor,
  FormGroupDirective,
  NgControl,
  NgForm
} from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { MatFormFieldControl } from '@angular/material/form-field';
import { Subject } from 'rxjs';

@Component({
  selector: 'file-upload',
  standalone: false,
  template: `
    <div
      class="file-upload"
      role="button"
      tabindex="0"
      (click)="open()"
      (keydown.enter)="open()"
      [class.disabled]="disabled"
      [class.mat-form-field-should-float]="shouldLabelFloat">
      <span class="filename">{{ fileNames || placeholder }}</span>
    </div>

    <input
      #input
      type="file"
      [attr.accept]="accept"
      [attr.multiple]="multiple ? '' : null"
      (change)="onFileSelected($event)"
      hidden />
  `,
  styles: [
    `
      .file-upload {
        display: inline-flex;
        align-items: center;
        cursor: pointer;
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
  ]
})
export class FileUploadComponent
  implements
    MatFormFieldControl<File[]>,
    ControlValueAccessor,
    OnInit,
    OnDestroy,
    DoCheck
{
  static nextId = 0;

  @Input() accept = '';

  controlType = 'file-upload';

  @HostBinding('attr.aria-describedby') describedBy = '';

  errorState = false;

  @Input() errorStateMatcher: ErrorStateMatcher;

  /** allow parent or consumer to control float behavior */
  @Input() floatLabel: 'auto' | 'always' | 'never' = 'auto';

  focused = false;

  @HostBinding() id = `file-upload-${FileUploadComponent.nextId++}`;

  @ViewChild('input', { static: true }) inputRef!: ElementRef<HTMLInputElement>;

  //@Input() placeholder = 'No file chosen';

  @Input() placeholder = '';

  stateChanges = new Subject<void>();

  private _explicitRequired = false;

  private _files: File[] | null = null;

  constructor(
    private fm: FocusMonitor,
    private _elementRef: ElementRef<HTMLElement>,
    @Optional() @Self() public ngControl: NgControl,
    @Optional() public _parentForm: NgForm,
    @Optional() public _parentFormGroup: FormGroupDirective,
    defaultErrorStateMatcher: ErrorStateMatcher
  ) {
    if (this.ngControl) {
      this.ngControl.valueAccessor = this;
    }
    this.errorStateMatcher = defaultErrorStateMatcher;

    this.fm
      .monitor(this._elementRef.nativeElement, true)
      .subscribe((origin) => {
        this.focused = !!origin;
        this.stateChanges.next();
      });
  }

  private _disabled = false;

  @Input() get disabled(): boolean {
    return this._disabled;
  }

  set disabled(v: boolean) {
    this._disabled = coerceBooleanProperty(v);
    this.setDisabledState(this._disabled);
    this.stateChanges.next();
  }

  private _hideRequiredMarker = false;

  /** Whether to hide the required‐field asterisk (matFormField reads this) */
  @Input() get hideRequiredMarker(): boolean {
    return this._hideRequiredMarker;
  }

  set hideRequiredMarker(v: boolean) {
    console.log(
      '[FileUploadComponent][hideRequiredMarker] Setting hide required = ',
      v
    );

    this._hideRequiredMarker = coerceBooleanProperty(v);
    this.stateChanges.next();
  }

  private _multiple = false;

  @Input() get multiple(): boolean {
    return this._multiple;
  }

  set multiple(v: boolean) {
    this._multiple = coerceBooleanProperty(v);
    this.stateChanges.next();
  }

  get empty(): boolean {
    return !this._files || this._files.length === 0;
  }

  /** Human-readable file names */
  get fileNames(): string {
    if (Array.isArray(this._files) && this._files.length) {
      return this._files.map((f) => f.name).join(', ');
    }
    return '';
  }

  /** Explicit required attribute */
  @Input() get required(): boolean {
    if (this._explicitRequired) {
      return true;
    }
    // auto-detect Validators.required on FormControl
    const control: AbstractControl | null = this.ngControl?.control ?? null;
    if (control && control.validator) {
      const errors = control.validator(control);
      if (errors?.['required']) {
        return true;
      }
    }
    return false;
  }

  set required(v: boolean) {
    this._explicitRequired = coerceBooleanProperty(v);
    this.stateChanges.next();
  }

  @HostBinding('class.mat-form-field-should-float') get shouldLabelFloat() {
    // always float
    if (this.floatLabel === 'always') {
      return true;
    }
    // never float
    if (this.floatLabel === 'never') {
      return false;
    }
    // auto (default): float on focus or when there's a value or when there's a placeholder
    return this.focused || !!this.fileNames || !!this.placeholder;
  }

  /** MatFormFieldControl.value */
  @Input() get value(): File[] | null {
    return this._files;
  }

  set value(files: File[] | null) {
    // only accept arrays, otherwise clear
    this._files = Array.isArray(files) ? files : null;
    this._onChange(this._files);
    this.stateChanges.next();
  }

  ngDoCheck() {
    const control = this.ngControl?.control;
    const newState = this.errorStateMatcher.isErrorState(
      control,
      this._parentForm || this._parentFormGroup
    );
    if (newState !== this.errorState) {
      this.errorState = newState;
      this.stateChanges.next();
    }
  }

  ngOnDestroy() {
    this.stateChanges.complete();
    this.fm.stopMonitoring(this._elementRef.nativeElement);
  }

  ngOnInit() {
    // no-op
  }

  onContainerClick(event: MouseEvent): void {
    if (!this.disabled) {
      this.open();
    }
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    this._files = input.files ? Array.from(input.files) : null;
    this._onChange(this._files);
    this.stateChanges.next();
  }

  open() {
    if (!this.disabled) {
      this.inputRef.nativeElement.click();
    }
  }

  registerOnChange(fn: any): void {
    this._onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this._onTouched = fn;
  }

  setDescribedByIds(ids: string[]): void {
    this.describedBy = ids.join(' ');
  }

  setDisabledState(isDisabled: boolean): void {
    this.inputRef.nativeElement.disabled = isDisabled;
    this.stateChanges.next();
  }

  writeValue(files: File[] | null): void {
    this._files = files;
    this.stateChanges.next();
  }

  private _onChange: (_: File[] | null) => void = () => {};

  private _onTouched = () => {};
}
