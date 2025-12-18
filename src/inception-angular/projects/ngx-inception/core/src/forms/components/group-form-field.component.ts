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

import { Directionality } from '@angular/cdk/bidi';
import { BooleanInput, coerceBooleanProperty } from '@angular/cdk/coercion';
import { CdkObserveContent } from '@angular/cdk/observers';
import { Platform } from '@angular/cdk/platform';
import { NgTemplateOutlet } from '@angular/common';
import { AfterContentChecked, AfterContentInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, ContentChild, ContentChildren, ElementRef, InjectionToken, Input, NgZone, OnDestroy, QueryList, ViewChild, ViewEncapsulation, inject } from '@angular/core';
import { MatCheckbox } from '@angular/material/checkbox';
import {
  FloatLabelType, getMatFormFieldDuplicatedHintError, MAT_ERROR, MAT_FORM_FIELD_DEFAULT_OPTIONS,
  MAT_PREFIX, MAT_SUFFIX, MatError, MatFormFieldAppearance, MatFormFieldDefaultOptions,
  MatFormFieldModule, MatHint, MatLabel, MatPrefix, MatSuffix
} from '@angular/material/form-field';
import { MatRadioButton, MatRadioGroup } from '@angular/material/radio';
import { merge, startWith, Subject, takeUntil } from 'rxjs';
import {
  GroupFormFieldNotchedOutlineComponent
} from './group-form-field-notched-outline.component';

let nextUniqueId = 0;
const floatingLabelScale = 0.75;
const outlineGapPadding = 5;

/**
 * Injection token that can be used to inject instances of `GroupFormFieldComponent`.
 * This avoids retaining the component class type and its metadata unnecessarily.
 */
export const GROUP_FORM_FIELD_COMPONENT = new InjectionToken<GroupFormFieldComponent>(
  'GroupFormFieldComponent'
);

/** Container for form controls that applies Material Design styling and behavior. */
@Component({
  selector: 'inception-core-group-form-field',
  standalone: true,
  imports: [
    CdkObserveContent,
    GroupFormFieldNotchedOutlineComponent,
    MatFormFieldModule,
    NgTemplateOutlet
  ],
  exportAs: 'groupFormField',
  templateUrl: 'group-form-field.component.html',
  styleUrls: ['group-form-field.component.scss'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [
    {
      provide: GROUP_FORM_FIELD_COMPONENT,
      useExisting: GroupFormFieldComponent
    }
  ],
  host: {
    class:
      'group-form-field mat-mdc-form-field mat-mdc-form-field-type-mat-input mat-form-field-hide-placeholder',
    '[class.mat-form-field-appearance-fill]': 'appearance === "fill"',
    '[class.mat-form-field-appearance-outline]': 'appearance === "outline"',
    '[class.mat-form-field-invalid]': '_hasError()',
    '[class.mat-form-field-disabled]': '_isDisabled()',
    '[class.mat-focused]': '_isFocused()',
    '[class.mat-primary]': 'color !== "accent" && color !== "warn"',
    '[class.mat-accent]': 'color === "accent"',
    '[class.mat-warn]': 'color === "warn"'
  }
})
export class GroupFormFieldComponent implements AfterContentInit, AfterContentChecked, OnDestroy {
  _elementRef = inject<ElementRef<HTMLElement>>(ElementRef);
  private readonly _changeDetectorRef = inject(ChangeDetectorRef);
  private readonly _dir = inject(Directionality, { optional: true });
  private readonly _defaults = inject<MatFormFieldDefaultOptions | null>(MAT_FORM_FIELD_DEFAULT_OPTIONS, { optional: true });
  private readonly _platform = inject(Platform);
  private readonly _ngZone = inject(NgZone);

  static ngAcceptInputType_hideRequiredMarker: BooleanInput;

  @ContentChildren(MatCheckbox, { descendants: true })
  _checkboxChildren!: QueryList<MatCheckbox>;

  @ViewChild('connectionContainer', { static: true })
  _connectionContainerRef!: ElementRef<HTMLElement>;

  @ContentChildren(MAT_ERROR, { descendants: true })
  _errorChildren!: QueryList<MatError>;

  @ContentChildren(MatHint, { descendants: true })
  _hintChildren!: QueryList<MatHint>;

  /** Unique id for the hint label. */
  readonly _hintLabelId: string = `mat-hint-${nextUniqueId++}`;

  @ContentChild(MatLabel) _labelChildNonStatic?: MatLabel;

  @ContentChild(MatLabel, { static: true }) _labelChildStatic!: MatLabel;

  /** Unique id for the internal form field label. */
  readonly _labelId = `group-form-field-label-${nextUniqueId++}`;

  @ContentChildren(MAT_PREFIX, { descendants: true })
  _prefixChildren!: QueryList<MatPrefix>;

  @ContentChild(MatRadioGroup) _radioGroupChildNonStatic?: MatRadioGroup;

  @ContentChild(MatRadioGroup, { static: true })
  _radioGroupChildStatic!: MatRadioGroup;

  @ContentChildren(MAT_SUFFIX, { descendants: true })
  _suffixChildren!: QueryList<MatSuffix>;

  @Input() color: 'primary' | 'accent' | 'warn' = 'primary';

  /** Emits when the component is being destroyed. */
  private readonly _destroyed = new Subject<void>();

  @ViewChild('label') private _label!: ElementRef<HTMLElement>;

  /**
   * Whether the outline gap needs to be calculated immediately on the next change detection run.
   */
  private _outlineGapCalculationNeededImmediately = false;

  /** Whether the outline gap needs to be calculated next time the zone has stabilized. */
  private _outlineGapCalculationNeededOnStable = false;

  constructor() {
    this.floatLabel = this._getDefaultFloatLabelState();
  }

  private _appearance!: MatFormFieldAppearance;

  /** The form-field appearance style. */
  @Input()
  get appearance(): MatFormFieldAppearance {
    return this._appearance;
  }

  set appearance(value: MatFormFieldAppearance) {
    const oldValue = this._appearance;

    this._appearance = value ?? this._defaults?.appearance ?? ('outline' as MatFormFieldAppearance);

    if (this._appearance === 'outline' && oldValue !== value) {
      this._outlineGapCalculationNeededOnStable = true;
    }
  }

  private _floatLabel: FloatLabelType = this._getDefaultFloatLabelState();

  /**
   * Whether the label should always float, never float or float as the user types.
   */
  @Input()
  get floatLabel(): FloatLabelType {
    return this._floatLabel;
  }

  set floatLabel(value: FloatLabelType) {
    if (value !== this._floatLabel) {
      this._floatLabel = value ?? this._getDefaultFloatLabelState();
      this._changeDetectorRef.markForCheck();
    }
  }

  private _hideRequiredMarker = false;

  /** Whether the required marker should be hidden. */
  @Input()
  get hideRequiredMarker(): boolean {
    return this._hideRequiredMarker;
  }

  set hideRequiredMarker(value: boolean) {
    this._hideRequiredMarker = coerceBooleanProperty(value);
  }

  private _hintLabel = '';

  /** Text for the form field hint. */
  @Input()
  get hintLabel(): string {
    return this._hintLabel;
  }
  set hintLabel(value: string) {
    this._hintLabel = value;
    this._processHints();
    // this._syncDescribedByIds();
  }

  get _labelChild(): MatLabel {
    return this._labelChildNonStatic || this._labelChildStatic;
  }

  get _radioGroupChild(): MatRadioGroup | undefined {
    return this._radioGroupChildNonStatic || this._radioGroupChildStatic;
  }

  _controlId(): string | undefined {
    const radioGroup = this._radioGroupChild;

    if (radioGroup && radioGroup._radios.length > 0) {
      return radioGroup._radios.get(0)?.id;
    }

    const firstCheckbox = this._checkboxChildren?.get(0);
    return firstCheckbox?.id;
  }

  /** Determines whether to display hints or errors. */
  _getDisplayedMessages(): 'error' | 'hint' {
    return this._errorChildren?.length ? 'error' : 'hint';
  }

  /** Whether there are one or more errors associated with the group form field. */
  _hasError(): boolean {
    return !!this._errorChildren?.length;
  }

  _hasFloatingLabel(): boolean {
    return this._hasLabel();
  }

  _hasLabel(): boolean {
    return !!this._labelChild;
  }

  _hasOutline(): boolean {
    return this.appearance === 'outline';
  }

  _isDisabled(): boolean {
    const radioGroup = this._radioGroupChild;
    if (radioGroup) {
      return radioGroup.disabled;
    }

    if (!this._checkboxChildren?.length) {
      return false;
    }

    // disabled if *all* checkboxes are disabled
    return this._checkboxChildren.toArray().every((cb) => cb.disabled);
  }

  _isFocused(): boolean {
    // Group container itself is not focusable; this can be extended if you track focus manually.
    return false;
  }

  _isRequired(): boolean {
    return !!this._radioGroupChild?.required;
  }

  /**
   * Updates the width and position of the gap in the outline. Only relevant for the outline
   * appearance.
   */
  _updateOutlineGap(): void {
    const labelEl = this._label?.nativeElement ?? null;

    if (
      this.appearance !== 'outline' ||
      !labelEl ||
      !labelEl.children.length ||
      !labelEl.textContent?.trim()
    ) {
      return;
    }

    if (!this._platform.isBrowser) {
      // getBoundingClientRect isn't available on the server.
      return;
    }

    if (!this._isAttachedToDOM()) {
      this._outlineGapCalculationNeededImmediately = true;
      return;
    }

    let startWidth = 0;
    let gapWidth = 0;

    const container = this._connectionContainerRef.nativeElement;
    const startEls = container.querySelectorAll<HTMLElement>('.mat-form-field-outline-start');
    const gapEls = container.querySelectorAll<HTMLElement>('.mat-form-field-outline-gap');

    if (labelEl.children.length) {
      const containerRect = container.getBoundingClientRect();

      // If the container is invisible we can't calculate the outline gap yet.
      if (containerRect.width === 0 && containerRect.height === 0) {
        this._outlineGapCalculationNeededOnStable = true;
        this._outlineGapCalculationNeededImmediately = false;
        return;
      }

      const containerStart = this._getStartEnd(containerRect);
      const labelStart = this._getStartEnd(labelEl.children[0].getBoundingClientRect());

      let labelWidth = 0;
      for (const child of Array.from(labelEl.children)) {
        labelWidth += (child as HTMLElement).offsetWidth;
      }

      startWidth = Math.abs(labelStart - containerStart) - outlineGapPadding;
      gapWidth = labelWidth > 0 ? labelWidth * floatingLabelScale + outlineGapPadding * 2 : 0;
    }

    startEls.forEach((item) => (item.style.width = `${startWidth}px`));
    gapEls.forEach((item) => (item.style.width = `${gapWidth}px`));

    this._outlineGapCalculationNeededOnStable =
      this._outlineGapCalculationNeededImmediately = false;
  }

  /**
   * Gets an ElementRef for the element that an overlay attached to the form-field should be
   * positioned relative to.
   */
  getConnectedOverlayOrigin(): ElementRef<HTMLElement> {
    return this._connectionContainerRef || this._elementRef;
  }

  ngAfterContentChecked(): void {
    this._validateControlChildren();

    if (this._outlineGapCalculationNeededImmediately) {
      this._updateOutlineGap();
    }
  }

  ngAfterContentInit(): void {
    this._validateControlChildren();

    // Run outside Angular to avoid infinite loops if `zone-patch-rxjs` is included.
    this._ngZone.runOutsideAngular(() => {
      this._ngZone.onStable
        .asObservable()
        .pipe(takeUntil(this._destroyed))
        .subscribe(() => {
          if (this._outlineGapCalculationNeededOnStable) {
            this._updateOutlineGap();
          }
        });
    });

    // Run change detection and update the outline if the suffix or prefix changes.
    merge(this._prefixChildren.changes, this._suffixChildren.changes)
      .pipe(takeUntil(this._destroyed))
      .subscribe(() => {
        this._outlineGapCalculationNeededOnStable = true;
        this._changeDetectorRef.markForCheck();
      });

    // Re-validate when the number of hints changes.
    this._hintChildren.changes.pipe(startWith(null), takeUntil(this._destroyed)).subscribe(() => {
      this._processHints();
      this._changeDetectorRef.markForCheck();
    });

    // Update when the number of errors changes.
    this._errorChildren.changes.pipe(startWith(null), takeUntil(this._destroyed)).subscribe(() => {
      // this._syncDescribedByIds();
      this._changeDetectorRef.markForCheck();
    });

    if (this._dir) {
      this._dir.change.pipe(takeUntil(this._destroyed)).subscribe(() => {
        if (typeof requestAnimationFrame === 'function') {
          this._ngZone.runOutsideAngular(() => {
            requestAnimationFrame(() => this._updateOutlineGap());
          });
        } else {
          this._updateOutlineGap();
        }
      });
    }

    if (this._labelChild) {
      if (this._radioGroupChild) {
        this._radioGroupChild._radios.forEach((radioButton: MatRadioButton) => {
          radioButton.ariaLabelledby = this._labelId;
        });
      } else if (this._checkboxChildren?.length) {
        this._checkboxChildren.forEach((checkbox: MatCheckbox) => {
          checkbox.ariaLabelledby = this._labelId;
        });
      }
    }
  }

  ngOnDestroy(): void {
    this._destroyed.next();
    this._destroyed.complete();
  }

  /** Gets the default float label state. */
  private _getDefaultFloatLabelState(): FloatLabelType {
    return this._defaults?.floatLabel ?? ('auto' as FloatLabelType);
  }

  /** Gets the start/end coordinate of the rect considering the current directionality. */
  private _getStartEnd(rect: ClientRect | DOMRect): number {
    return this._dir?.value === 'rtl' ? rect.right : rect.left;
  }

  /** Checks whether the form field is attached to the DOM. */
  private _isAttachedToDOM(): boolean {
    const element: HTMLElement = this._elementRef.nativeElement;

    if (element.getRootNode) {
      const rootNode = element.getRootNode();
      // If the element is inside the DOM, the root node will be the document or a shadow root.
      return !!rootNode && rootNode !== element;
    }

    // Fallback if `getRootNode` isn't supported.
    return document.documentElement.contains(element);
  }

  /** Does any extra processing that is required when handling the hints. */
  private _processHints(): void {
    this._validateHints();
  }

  /** Throws an error if there are no valid child controls for the group form field. */
  private _validateControlChildren(): void {
    // TODO: Validate that there is at least one MatRadioGroup, MatRadioButton or MatCheckbox.
    // if (!this._control) { throw getGroupFormFieldMissingControlError(); }
  }

  /**
   * Ensure that there is a maximum of one of each `<mat-hint>` alignment specified, with the
   * attribute being considered as `align="start"`.
   */
  private _validateHints(): void {
    if (!this._hintChildren?.length) {
      return;
    }

    let startHint: MatHint | undefined;
    let endHint: MatHint | undefined;

    this._hintChildren.forEach((hint: MatHint) => {
      if (hint.align === 'start') {
        if (startHint || this.hintLabel) {
          throw getMatFormFieldDuplicatedHintError('start');
        }
        startHint = hint;
      } else if (hint.align === 'end') {
        if (endHint) {
          throw getMatFormFieldDuplicatedHintError('end');
        }
        endHint = hint;
      }
    });
  }
}
