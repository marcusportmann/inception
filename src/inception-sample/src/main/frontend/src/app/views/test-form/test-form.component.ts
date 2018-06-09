import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {IOption} from 'ng-select';
import {patternValidator} from '../../inception/validators/pattern-validator';
import {NavigationService} from "../../inception/services/navigation/navigation.service";

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

  constructor(private formBuilder: FormBuilder, private navigationService: NavigationService) {

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
      inlineRadio: ['', Validators.required],
      inlineCheckbox1: [''],
      inlineCheckbox2: [''],
      inlineCheckbox3: [''],
      radio: ['', Validators.required],
      checkbox1: [''],
      checkbox2: [''],
      checkbox3: [''],
      favouriteTime: ['', Validators.required],
      notes: ['', Validators.required]
    });
  }

  public onSubmit() {

  }
}
