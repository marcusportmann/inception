/*
 * Copyright 2019 Marcus Portmann
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

import {Component, forwardRef} from '@angular/core';
import {
  AbstractControl,
  ControlValueAccessor,
  FormControl,
  FormGroup,
  NG_VALIDATORS,
  NG_VALUE_ACCESSOR,
  ValidationErrors,
  Validator,
  Validators
} from '@angular/forms';
import {UserDirectoryParameter} from '../../services/security/user-directory-parameter';
import {UserDirectoryUtil} from '../../services/security/user-directory-util';

@Component({
  selector: 'ldap-user-directory',
  templateUrl: 'ldap-user-directory.component.html',
  styleUrls: ['ldap-user-directory.component.css'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => LdapUserDirectoryComponent),
    multi: true
  }, {
    provide: NG_VALIDATORS,
    useExisting: forwardRef(() => LdapUserDirectoryComponent),
    multi: true
  }
  ]
})
export class LdapUserDirectoryComponent implements ControlValueAccessor, Validator {

  ldapUserDirectoryForm: FormGroup;

  constructor() {
    // Initialise the form
    this.ldapUserDirectoryForm = new FormGroup({
      baseDN: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      bindDN: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      bindPassword: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      groupBaseDN: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      groupDescriptionAttribute: new FormControl('',
        [Validators.required, Validators.maxLength(100)]),
      groupMemberAttribute: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      groupNameAttribute: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      groupObjectClass: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      host: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      maxFilteredGroups: new FormControl('', [Validators.required, Validators.pattern('^\\d+$')]),
      maxFilteredUsers: new FormControl('', [Validators.required, Validators.pattern('^\\d+$')]),
      port: new FormControl('', [Validators.required, Validators.pattern('^\\d+$')]),
      userBaseDN: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      userObjectClass: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      userEmailAttribute: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      userFirstNameAttribute: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      userFullNameAttribute: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      userLastNameAttribute: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      userMobileNumberAttribute: new FormControl('',
        [Validators.required, Validators.maxLength(100)]),
      userPhoneNumberAttribute: new FormControl('',
        [Validators.required, Validators.maxLength(100)]),
      userUsernameAttribute: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      useSSL: new FormControl('', [Validators.required, Validators.pattern('^(true|false)$')])
    });
  }

  getParameters(): UserDirectoryParameter[] {

    const parameters: UserDirectoryParameter[] = [];

    UserDirectoryUtil.setParameter(parameters, 'BaseDN',
      this.ldapUserDirectoryForm.get('baseDN')!.value);
    UserDirectoryUtil.setParameter(parameters, 'BindDN',
      this.ldapUserDirectoryForm.get('bindDN')!.value);
    UserDirectoryUtil.setParameter(parameters, 'BindPassword',
      this.ldapUserDirectoryForm.get('bindPassword')!.value);
    UserDirectoryUtil.setParameter(parameters, 'GroupBaseDN',
      this.ldapUserDirectoryForm.get('groupBaseDN')!.value);
    UserDirectoryUtil.setParameter(parameters, 'GroupDescriptionAttribute',
      this.ldapUserDirectoryForm.get('groupDescriptionAttribute')!.value);
    UserDirectoryUtil.setParameter(parameters, 'GroupMemberAttribute',
      this.ldapUserDirectoryForm.get('groupMemberAttribute')!.value);
    UserDirectoryUtil.setParameter(parameters, 'GroupNameAttribute',
      this.ldapUserDirectoryForm.get('groupNameAttribute')!.value);
    UserDirectoryUtil.setParameter(parameters, 'GroupObjectClass',
      this.ldapUserDirectoryForm.get('groupObjectClass')!.value);
    UserDirectoryUtil.setParameter(parameters, 'Host',
      this.ldapUserDirectoryForm.get('host')!.value);
    UserDirectoryUtil.setParameter(parameters, 'MaxFilteredGroups',
      this.ldapUserDirectoryForm.get('maxFilteredGroups')!.value);
    UserDirectoryUtil.setParameter(parameters, 'MaxFilteredUsers',
      this.ldapUserDirectoryForm.get('maxFilteredUsers')!.value);
    UserDirectoryUtil.setParameter(parameters, 'Port',
      this.ldapUserDirectoryForm.get('port')!.value);
    UserDirectoryUtil.setParameter(parameters, 'UserBaseDN',
      this.ldapUserDirectoryForm.get('userBaseDN')!.value);
    UserDirectoryUtil.setParameter(parameters, 'UserObjectClass',
      this.ldapUserDirectoryForm.get('userObjectClass')!.value);
    UserDirectoryUtil.setParameter(parameters, 'UserEmailAttribute',
      this.ldapUserDirectoryForm.get('userEmailAttribute')!.value);
    UserDirectoryUtil.setParameter(parameters, 'UserFirstNameAttribute',
      this.ldapUserDirectoryForm.get('userFirstNameAttribute')!.value);
    UserDirectoryUtil.setParameter(parameters, 'UserFullNameAttribute',
      this.ldapUserDirectoryForm.get('userFullNameAttribute')!.value);
    UserDirectoryUtil.setParameter(parameters, 'UserLastNameAttribute',
      this.ldapUserDirectoryForm.get('userLastNameAttribute')!.value);
    UserDirectoryUtil.setParameter(parameters, 'UserMobileNumberAttribute',
      this.ldapUserDirectoryForm.get('userMobileNumberAttribute')!.value);
    UserDirectoryUtil.setParameter(parameters, 'UserPhoneNumberAttribute',
      this.ldapUserDirectoryForm.get('userPhoneNumberAttribute')!.value);
    UserDirectoryUtil.setParameter(parameters, 'UserUsernameAttribute',
      this.ldapUserDirectoryForm.get('userUsernameAttribute')!.value);
    UserDirectoryUtil.setParameter(parameters, 'UseSSL',
      this.ldapUserDirectoryForm.get('useSSL')!.value);

    return parameters;
  }

  onTouched: () => void = () => {
  };

  // tslint:disable-next-line:no-any
  registerOnChange(fn: any): void {
    this.ldapUserDirectoryForm.valueChanges.subscribe(fn);
  }

  // tslint:disable-next-line:no-any
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    isDisabled ? this.ldapUserDirectoryForm.disable() : this.ldapUserDirectoryForm.enable();
  }

  setParameters(parameters: UserDirectoryParameter[]) {
    this.ldapUserDirectoryForm.get('baseDN')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'BaseDN') ?
        UserDirectoryUtil.getParameter(parameters, 'BaseDN') : '');
    this.ldapUserDirectoryForm.get('bindDN')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'BindDN') ?
        UserDirectoryUtil.getParameter(parameters, 'BindDN') : '');
    this.ldapUserDirectoryForm.get('bindPassword')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'BindPassword') ?
        UserDirectoryUtil.getParameter(parameters, 'BindPassword') : '');
    this.ldapUserDirectoryForm.get('groupBaseDN')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'GroupBaseDN') ?
        UserDirectoryUtil.getParameter(parameters, 'GroupBaseDN') : '');
    this.ldapUserDirectoryForm.get('groupDescriptionAttribute')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'GroupDescriptionAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'GroupDescriptionAttribute') : 'description');
    this.ldapUserDirectoryForm.get('groupMemberAttribute')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'GroupMemberAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'GroupMemberAttribute') : 'member');
    this.ldapUserDirectoryForm.get('groupNameAttribute')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'GroupNameAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'GroupNameAttribute') : 'cn');
    this.ldapUserDirectoryForm.get('groupObjectClass')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'GroupObjectClass') ?
        UserDirectoryUtil.getParameter(parameters, 'GroupObjectClass') : 'groupOfNames');
    this.ldapUserDirectoryForm.get('host')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'Host') ?
        UserDirectoryUtil.getParameter(parameters, 'Host') : '');
    this.ldapUserDirectoryForm.get('maxFilteredGroups')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'MaxFilteredGroups') ?
        UserDirectoryUtil.getParameter(parameters, 'MaxFilteredGroups') : '100');
    this.ldapUserDirectoryForm.get('maxFilteredUsers')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'MaxFilteredUsers') ?
        UserDirectoryUtil.getParameter(parameters, 'MaxFilteredUsers') : '100');
    this.ldapUserDirectoryForm.get('port')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'Port') ?
        UserDirectoryUtil.getParameter(parameters, 'Port') : '');
    this.ldapUserDirectoryForm.get('userBaseDN')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'UserBaseDN') ?
        UserDirectoryUtil.getParameter(parameters, 'UserBaseDN') : '');
    this.ldapUserDirectoryForm.get('userObjectClass')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'UserObjectClass') ?
        UserDirectoryUtil.getParameter(parameters, 'UserObjectClass') : 'inetOrgPerson');
    this.ldapUserDirectoryForm.get('userEmailAttribute')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'UserEmailAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'UserEmailAttribute') : 'mail');
    this.ldapUserDirectoryForm.get('userFirstNameAttribute')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'UserFirstNameAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'UserFirstNameAttribute') : 'givenName');
    this.ldapUserDirectoryForm.get('userFullNameAttribute')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'UserFullNameAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'UserFullNameAttribute') : 'cn');
    this.ldapUserDirectoryForm.get('userLastNameAttribute')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'UserLastNameAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'UserLastNameAttribute') : 'sn');
    this.ldapUserDirectoryForm.get('userMobileNumberAttribute')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'UserMobileNumberAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'UserMobileNumberAttribute') : 'mobile');
    this.ldapUserDirectoryForm.get('userPhoneNumberAttribute')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'UserPhoneNumberAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'UserPhoneNumberAttribute') : 'telephoneNumber');
    this.ldapUserDirectoryForm.get('userUsernameAttribute')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'UserUsernameAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'UserUsernameAttribute') : 'uid');
    this.ldapUserDirectoryForm.get('useSSL')!.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'UseSSL') ?
        UserDirectoryUtil.getParameter(parameters, 'UseSSL') : 'false');
  }

  validate(c: AbstractControl): ValidationErrors | null {
    return this.ldapUserDirectoryForm.valid ? null : {
      invalidForm: {
        valid: false,
        message: 'ldapUserDirectoryForm fields are invalid'
      }
    };
  }


  // tslint:disable-next-line:no-any
  writeValue(val: any): void {
    if (val) {
      this.ldapUserDirectoryForm.setValue(val, {emitEvent: false});
    }
  }
}
