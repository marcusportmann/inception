import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {IOption} from 'ng-select';
import {patternValidator} from '../../inception/validators/pattern-validator';

@Component({
  templateUrl: 'test-form.component.html'
})
export class TestFormComponent {

  private testForm: FormGroup;

  public titles: Array<IOption> = [
    {label: 'Mr', value: 'Mr'},
    {label: 'Mrs', value: 'Mrs'},
    {label: 'Ms', value: 'Ms'}
  ];

  constructor(private formBuilder: FormBuilder) {

    this.testForm = this.formBuilder.group({
      // tslint:disable-next-line
      //username: [{value: ''}, [Validators.required, patternValidator(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)]],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      title: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
    });
  }

  public onSubmit() {

  }
}
