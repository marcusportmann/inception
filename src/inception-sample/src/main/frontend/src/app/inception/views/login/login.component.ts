import {Component, OnInit} from '@angular/core';

import {ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators, FormBuilder} from '@angular/forms';

import {Session} from '../../models/session';
import {InceptionModule} from '../../inception.module';

@Component({
  templateUrl: 'login.component.html'
})
export class LoginComponent implements OnInit {

  private loginForm: FormGroup;
  private username: FormControl;
  private password: FormControl;

  constructor() { }

  ngOnInit(): void {
    this.createFormControls();
    this.createForm();
  }

  public isForgottenPasswordEnabled(): boolean {

    return InceptionModule.forgottenPasswordEnabled;
  }

  public isRegistrationEnabled(): boolean {

    return InceptionModule.registrationEnabled;
  }

  public onCancel() {
    console.log('Cancel clicked!');
  }

  public onLogin() {
    console.log('Login clicked!');
  }

  public onSubmit() {
    console.log('Form submitted (' + this.loginForm.valid + ')!');

    if (this.loginForm.valid)
    {

    }
    else
    {
      //this.loginForm.reset();
    }
  }

  private createFormControls() {
    this.username = new FormControl('', Validators.required);
    this.password = new FormControl('', Validators.required);
  }

  private createForm() {
    this.loginForm = new FormGroup({
      username: this.username,
      password: this.password
    });
  }
}
