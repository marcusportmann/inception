import {Component, ElementRef, OnInit, QueryList, ViewChildren} from '@angular/core';
import {FormGroup, FormControl, Validators} from '@angular/forms';

import {InceptionModule} from '../../inception.module';
import {Session} from '../../models/session';

import {patternValidator} from "../../validators/pattern-validator";
import {ValidatedFormGroup} from '../../directives/validated-form';

@Component({
  templateUrl: 'login.component.html'
})
export class LoginComponent implements OnInit {

  private loginForm: ValidatedFormGroup;

  private username: string;
  private password: string;

  constructor() {
  }

  ngOnInit(): void {
    this.loginForm = new ValidatedFormGroup({
      // tslint:disable-next-line
      'username': new FormControl('', [patternValidator(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)]),
      'password': new FormControl('', Validators.required),
    });
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

  public onSubmit() {

    // Mark all controls as touched
    for (let controlName in this.loginForm.controls) {

      let yyy = this.loginForm.controls[controlName];


      let zzz = (<any>this.loginForm.controls[controlName]).nativeElement;

      this.loginForm.controls[controlName].markAsTouched();
    }

    if (this.loginForm.valid)
    {
      console.log('username = ' + this.username);
      console.log('password = ' + this.password);
    }
    else
    {
      // Mark all controls as touched
      for (let controlName in this.loginForm.controls) {
        this.loginForm.controls[controlName]
      }


      //this.loginForm.reset();
    }
  }
}
