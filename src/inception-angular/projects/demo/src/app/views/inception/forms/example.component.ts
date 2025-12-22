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

import { Component, inject, OnDestroy } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { CoreModule, GroupFormFieldComponent, ValidatedFormDirective } from 'ngx-inception/core';
import { Country, CountryInputComponent } from 'ngx-inception/reference';
import { Subscription } from 'rxjs';

/**
 * The Title class holds title information for the example form component.
 */
class Title {
  name: string;
  value: string;

  constructor(name: string, value: string) {
    this.name = name;
    this.value = value;
  }
}

interface ExampleForm {
  confirmPassword: FormControl<string | null>;
  dateOfBirth: FormControl<Date | null>;
  favoriteColor: FormControl<string | null>;
  favoriteCountry: FormControl<Country | null>;
  favoritePetCat: FormControl<boolean | null>;
  favoritePetDog: FormControl<boolean | null>;
  favoritePetFish: FormControl<boolean | null>;
  grossIncome: FormControl<number | null>;
  name: FormControl<string | null>;
  notes: FormControl<string | null>;
  password: FormControl<string | null>;
  preferredName: FormControl<string | null>;
  title: FormControl<string | null>;
}

/**
 * The ExampleComponent class.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'app-forms',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective, CountryInputComponent, GroupFormFieldComponent],
  templateUrl: 'example.component.html'
})
export class ExampleComponent implements OnDestroy {
  static readonly MAX_DATE = Date.now();

  static readonly MIN_DATE = new Date(1900, 1, 1);

  confirmPasswordControl: FormControl<string | null>;

  dateOfBirthControl: FormControl<Date | null>;

  exampleForm: FormGroup<ExampleForm>;

  favoriteColorControl: FormControl<string | null>;

  favoriteCountryControl: FormControl<Country | null>;

  grossIncomeControl: FormControl<number | null>;

  nameControl: FormControl<string | null>;

  passwordControl: FormControl<string | null>;

  preferredNameControl: FormControl<string | null>;

  titleControl: FormControl<string | null>;

  titles: Title[] = [new Title('Mr', 'Mr'), new Title('Mrs', 'Mrs'), new Title('Ms', 'Ms')];

  private readonly formBuilder = inject(FormBuilder);

  private subscriptions: Subscription = new Subscription();

  constructor() {
    // Initialize the form controls with proper types
    this.confirmPasswordControl = new FormControl<string | null>(null, Validators.required);
    this.dateOfBirthControl = new FormControl<Date | null>(null, Validators.required);
    this.favoriteColorControl = new FormControl<string | null>(null, Validators.required);
    this.favoriteCountryControl = new FormControl<Country | null>(null, Validators.required);
    this.grossIncomeControl = new FormControl<number | null>(null, Validators.required);
    this.nameControl = new FormControl<string | null>(null, Validators.required);
    this.passwordControl = new FormControl<string | null>(null, Validators.required);
    this.preferredNameControl = new FormControl<string | null>(null, Validators.required);
    this.titleControl = new FormControl<string | null>(null, Validators.required);

    // Initialize the form as a typed FormGroup<ExampleForm>
    this.exampleForm = this.formBuilder.group({
      name: this.nameControl,
      preferredName: this.preferredNameControl,
      title: this.titleControl,
      dateOfBirth: this.dateOfBirthControl,
      password: this.passwordControl,
      confirmPassword: this.confirmPasswordControl,
      favoriteCountry: this.favoriteCountryControl,
      grossIncome: this.grossIncomeControl,
      favoriteColor: this.favoriteColorControl,
      favoritePetDog: new FormControl<boolean | null>(false),
      favoritePetCat: new FormControl<boolean | null>(true),
      favoritePetFish: new FormControl<boolean | null>(false),
      notes: new FormControl<string | null>(null)
    }) as FormGroup<ExampleForm>;
  }

  displayCountry(country: Country): string {
    if (country) {
      return country.shortName;
    } else {
      return '';
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  onSubmit(): void {
    console.log('favorite color = ', this.favoriteColorControl.value);
  }
}
