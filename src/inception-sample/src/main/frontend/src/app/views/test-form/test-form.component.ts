import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {IOption} from 'ng-select';
import {patternValidator} from '../../inception/validators/pattern-validator';

@Component({
  templateUrl: 'test-form.component.html'
})
export class TestFormComponent {

  public static readonly MIN_DATE = new Date(1900, 1, 1);
  public static readonly MAX_DATE = Date.now();

  private testForm: FormGroup;

  public titles: Array<IOption> = [
    {label: 'Mr', value: 'Mr'},
    {label: 'Mrs', value: 'Mrs'},
    {label: 'Ms', value: 'Ms'}
  ];

  constructor(private formBuilder: FormBuilder) {

    this.testForm = this.formBuilder.group({
      // tslint:disable-next-line
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      title: ['', Validators.required],
      dateOfBirth: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      favouritePet: ['', Validators.required],
      employmentPeriod: ['', Validators.required],
      favouriteTime: ['', Validators.required],
      notes: ['', Validators.required]
    });
  }

  public onSubmit() {

  }
}
