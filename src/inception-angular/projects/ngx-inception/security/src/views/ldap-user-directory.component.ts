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

import {Component, forwardRef} from '@angular/core';
import {
  AbstractControl, ControlValueAccessor, FormControl, FormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR,
  ValidationErrors, Validator, Validators
} from '@angular/forms';
import {UserDirectoryParameter} from '../services/user-directory-parameter';
import {UserDirectoryUtil} from '../services/user-directory-util';

@Component({
  selector: 'inception-ldap-user-directory',
  templateUrl: 'ldap-user-directory.component.html',
  styleUrls: ['ldap-user-directory.component.css'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => LdapUserDirectoryComponent),
      multi: true
    }, {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => LdapUserDirectoryComponent),
      multi: true
    }
  ],
  standalone: false
})
export class LdapUserDirectoryComponent implements ControlValueAccessor, Validator {

  baseDNControl: FormControl;

  bindDNControl: FormControl;

  bindPasswordControl: FormControl;

  groupBaseDNControl: FormControl;

  groupDescriptionAttributeControl: FormControl;

  groupMemberAttributeControl: FormControl;

  groupNameAttributeControl: FormControl;

  groupNamePrefixFilterControl: FormControl;

  groupObjectClassControl: FormControl;

  hostControl: FormControl;

  ldapUserDirectoryForm: FormGroup;

  maxFilteredGroupMembersControl: FormControl;

  maxFilteredGroupsControl: FormControl;

  maxFilteredUsersControl: FormControl;

  portControl: FormControl;

  supportsAdminChangePasswordControl: FormControl;

  supportsChangePasswordControl: FormControl;

  supportsGroupAdministrationControl: FormControl;

  supportsGroupMemberAdministrationControl: FormControl;

  supportsUserAdministrationControl: FormControl;

  useSSLControl: FormControl;

  userBaseDNControl: FormControl;

  userEmailAttributeControl: FormControl;

  userMobileNumberAttributeControl: FormControl;

  userNameAttributeControl: FormControl;

  userObjectClassControl: FormControl;

  userPhoneNumberAttributeControl: FormControl;

  userPreferredNameAttributeControl: FormControl;

  userUsernameAttributeControl: FormControl;

  constructor() {
    // Initialise the form controls
    this.baseDNControl = new FormControl('', [Validators.required, Validators.maxLength(200)]);
    this.bindDNControl = new FormControl('', [Validators.required, Validators.maxLength(200)]);
    this.bindPasswordControl = new FormControl('',
      [Validators.required, Validators.maxLength(100)]);
    this.groupBaseDNControl = new FormControl('', [Validators.required, Validators.maxLength(200)]);
    this.groupDescriptionAttributeControl = new FormControl('',
      [Validators.required, Validators.maxLength(100)]);
    this.groupMemberAttributeControl = new FormControl('',
      [Validators.required, Validators.maxLength(100)]);
    this.groupNameAttributeControl = new FormControl('',
      [Validators.required, Validators.maxLength(100)]);
    this.groupNamePrefixFilterControl = new FormControl('',
      [Validators.maxLength(100)]);
    this.groupObjectClassControl = new FormControl('',
      [Validators.required, Validators.maxLength(100)]);
    this.hostControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.maxFilteredGroupMembersControl = new FormControl('',
      [Validators.required, Validators.pattern('^\\d+$')]);
    this.maxFilteredGroupsControl = new FormControl('',
      [Validators.required, Validators.pattern('^\\d+$')]);
    this.maxFilteredUsersControl = new FormControl('',
      [Validators.required, Validators.pattern('^\\d+$')]);
    this.portControl = new FormControl('', [Validators.required, Validators.pattern('^\\d+$')]);
    this.supportsAdminChangePasswordControl =
      new FormControl('', [Validators.required, Validators.pattern('^(true|false)$')]);
    this.supportsChangePasswordControl =
      new FormControl('', [Validators.required, Validators.pattern('^(true|false)$')]);
    this.supportsGroupAdministrationControl =
      new FormControl('', [Validators.required, Validators.pattern('^(true|false)$')]);
    this.supportsGroupMemberAdministrationControl =
      new FormControl('', [Validators.required, Validators.pattern('^(true|false)$')]);
    this.supportsUserAdministrationControl =
      new FormControl('', [Validators.required, Validators.pattern('^(true|false)$')]);
    this.userBaseDNControl = new FormControl('', [Validators.required, Validators.maxLength(200)]);
    this.userObjectClassControl = new FormControl('',
      [Validators.required, Validators.maxLength(100)]);
    this.userEmailAttributeControl = new FormControl('',
      [Validators.required, Validators.maxLength(100)]);
    this.userNameAttributeControl = new FormControl('',
      [Validators.required, Validators.maxLength(100)]);
    this.userPreferredNameAttributeControl = new FormControl('',
      [Validators.required, Validators.maxLength(100)]);
    this.userMobileNumberAttributeControl = new FormControl('',
      [Validators.required, Validators.maxLength(100)]);
    this.userPhoneNumberAttributeControl = new FormControl('',
      [Validators.required, Validators.maxLength(100)]);
    this.userUsernameAttributeControl = new FormControl('',
      [Validators.required, Validators.maxLength(100)]);
    this.useSSLControl = new FormControl('',
      [Validators.required, Validators.pattern('^(true|false)$')]);

    // Initialise the form
    this.ldapUserDirectoryForm = new FormGroup({
      baseDN: this.baseDNControl,
      bindDN: this.bindDNControl,
      bindPassword: this.bindPasswordControl,
      groupBaseDN: this.groupBaseDNControl,
      groupDescriptionAttribute: this.groupDescriptionAttributeControl,
      groupMemberAttribute: this.groupMemberAttributeControl,
      groupNameAttribute: this.groupNameAttributeControl,
      groupNamePrefixFilter: this.groupNamePrefixFilterControl,
      groupObjectClass: this.groupObjectClassControl,
      host: this.hostControl,
      maxFilteredGroupMembers: this.maxFilteredGroupMembersControl,
      maxFilteredGroups: this.maxFilteredGroupsControl,
      maxFilteredUsers: this.maxFilteredUsersControl,
      port: this.portControl,
      supportsAdminChangePassword: this.supportsAdminChangePasswordControl,
      supportsChangePassword: this.supportsChangePasswordControl,
      supportsGroupAdministration: this.supportsGroupAdministrationControl,
      supportsGroupMemberAdministration: this.supportsGroupMemberAdministrationControl,
      supportsUserAdministration: this.supportsUserAdministrationControl,
      userBaseDN: this.userBaseDNControl,
      userObjectClass: this.userObjectClassControl,
      userEmailAttribute: this.userEmailAttributeControl,
      userNameAttribute: this.userNameAttributeControl,
      userPreferredNameAttribute: this.userPreferredNameAttributeControl,
      userMobileNumberAttribute: this.userMobileNumberAttributeControl,
      userPhoneNumberAttribute: this.userPhoneNumberAttributeControl,
      userUsernameAttribute: this.userUsernameAttributeControl,
      useSSL: this.useSSLControl
    });
  }

  getParameters(): UserDirectoryParameter[] {

    const parameters: UserDirectoryParameter[] = [];

    UserDirectoryUtil.setParameter(parameters, 'BaseDN', this.baseDNControl.value);
    UserDirectoryUtil.setParameter(parameters, 'BindDN', this.bindDNControl.value);
    UserDirectoryUtil.setParameter(parameters, 'BindPassword', this.bindPasswordControl.value);
    UserDirectoryUtil.setParameter(parameters, 'GroupBaseDN', this.groupBaseDNControl.value);
    UserDirectoryUtil.setParameter(parameters, 'GroupDescriptionAttribute',
      this.groupDescriptionAttributeControl.value);
    UserDirectoryUtil.setParameter(parameters, 'GroupMemberAttribute',
      this.groupMemberAttributeControl.value);
    UserDirectoryUtil.setParameter(parameters, 'GroupNameAttribute',
      this.groupNameAttributeControl.value);
    UserDirectoryUtil.setParameter(parameters, 'GroupNamePrefixFilter',
      this.groupNamePrefixFilterControl.value);
    UserDirectoryUtil.setParameter(parameters, 'GroupObjectClass',
      this.groupObjectClassControl.value);
    UserDirectoryUtil.setParameter(parameters, 'Host', this.hostControl.value);
    UserDirectoryUtil.setParameter(parameters, 'MaxFilteredGroupMembers',
      this.maxFilteredGroupMembersControl.value);
    UserDirectoryUtil.setParameter(parameters, 'MaxFilteredGroups',
      this.maxFilteredGroupsControl.value);
    UserDirectoryUtil.setParameter(parameters, 'MaxFilteredUsers',
      this.maxFilteredUsersControl.value);
    UserDirectoryUtil.setParameter(parameters, 'Port', this.portControl.value);
    UserDirectoryUtil.setParameter(parameters, 'SupportsAdminChangePassword',
      this.supportsAdminChangePasswordControl.value);
    UserDirectoryUtil.setParameter(parameters, 'SupportsChangePassword',
      this.supportsChangePasswordControl.value);
    UserDirectoryUtil.setParameter(parameters, 'SupportsGroupAdministration',
      this.supportsGroupAdministrationControl.value);
    UserDirectoryUtil.setParameter(parameters, 'SupportsGroupMemberAdministration',
      this.supportsGroupMemberAdministrationControl.value);
    UserDirectoryUtil.setParameter(parameters, 'SupportsUserAdministration',
      this.supportsUserAdministrationControl.value);
    UserDirectoryUtil.setParameter(parameters, 'UserBaseDN', this.userBaseDNControl.value);
    UserDirectoryUtil.setParameter(parameters, 'UserObjectClass',
      this.userObjectClassControl.value);
    UserDirectoryUtil.setParameter(parameters, 'UserEmailAttribute',
      this.userEmailAttributeControl.value);
    UserDirectoryUtil.setParameter(parameters, 'UserNameAttribute',
      this.userNameAttributeControl.value);
    UserDirectoryUtil.setParameter(parameters, 'UserPreferredNameAttribute',
      this.userPreferredNameAttributeControl.value);
    UserDirectoryUtil.setParameter(parameters, 'UserMobileNumberAttribute',
      this.userMobileNumberAttributeControl.value);
    UserDirectoryUtil.setParameter(parameters, 'UserPhoneNumberAttribute',
      this.userPhoneNumberAttributeControl.value);
    UserDirectoryUtil.setParameter(parameters, 'UserUsernameAttribute',
      this.userUsernameAttributeControl.value);
    UserDirectoryUtil.setParameter(parameters, 'UseSSL', this.useSSLControl.value);

    return parameters;
  }

  onTouched: () => void = () => {
  };

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  registerOnChange(fn: any): void {
    this.ldapUserDirectoryForm.valueChanges.subscribe(fn);
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    isDisabled ? this.ldapUserDirectoryForm.disable() : this.ldapUserDirectoryForm.enable();
  }

  setParameters(parameters: UserDirectoryParameter[]) {
    this.baseDNControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'BaseDN') ? UserDirectoryUtil.getParameter(
        parameters, 'BaseDN') : '');
    this.bindDNControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'BindDN') ? UserDirectoryUtil.getParameter(
        parameters, 'BindDN') : '');
    this.bindPasswordControl.setValue(UserDirectoryUtil.hasParameter(parameters, 'BindPassword') ?
      UserDirectoryUtil.getParameter(parameters, 'BindPassword') : '');
    this.groupBaseDNControl.setValue(UserDirectoryUtil.hasParameter(parameters, 'GroupBaseDN') ?
      UserDirectoryUtil.getParameter(parameters, 'GroupBaseDN') : '');
    this.groupDescriptionAttributeControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'GroupDescriptionAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'GroupDescriptionAttribute') : 'description');
    this.groupMemberAttributeControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'GroupMemberAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'GroupMemberAttribute') : 'member');
    this.groupNameAttributeControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'GroupNameAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'GroupNameAttribute') : 'cn');
    this.groupNamePrefixFilterControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'GroupNamePrefixFilter') ?
        UserDirectoryUtil.getParameter(parameters, 'GroupNamePrefixFilter') : '');
    this.groupObjectClassControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'GroupObjectClass') ?
        UserDirectoryUtil.getParameter(parameters, 'GroupObjectClass') : 'groupOfNames');
    this.hostControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'Host') ? UserDirectoryUtil.getParameter(
        parameters, 'Host') : '');
    this.maxFilteredGroupMembersControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'MaxFilteredGroupMembers') ?
        UserDirectoryUtil.getParameter(parameters, 'MaxFilteredGroupMembers') : '100');
    this.maxFilteredGroupsControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'MaxFilteredGroups') ?
        UserDirectoryUtil.getParameter(parameters, 'MaxFilteredGroups') : '100');
    this.maxFilteredUsersControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'MaxFilteredUsers') ?
        UserDirectoryUtil.getParameter(parameters, 'MaxFilteredUsers') : '100');
    this.portControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'Port') ? UserDirectoryUtil.getParameter(
        parameters, 'Port') : '');
    this.supportsAdminChangePasswordControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'SupportsAdminChangePassword') ?
        UserDirectoryUtil.getParameter(parameters, 'SupportsAdminChangePassword') : '');
    this.supportsChangePasswordControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'SupportsChangePassword') ?
        UserDirectoryUtil.getParameter(parameters, 'SupportsChangePassword') : '');
    this.supportsGroupAdministrationControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'SupportsGroupAdministration') ?
        UserDirectoryUtil.getParameter(parameters, 'SupportsGroupAdministration') : '');
    this.supportsGroupMemberAdministrationControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'SupportsGroupMemberAdministration') ?
        UserDirectoryUtil.getParameter(parameters, 'SupportsGroupMemberAdministration') : '');
    this.supportsUserAdministrationControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'SupportsUserAdministration') ?
        UserDirectoryUtil.getParameter(parameters, 'SupportsUserAdministration') : '');
    this.userBaseDNControl.setValue(UserDirectoryUtil.hasParameter(parameters, 'UserBaseDN') ?
      UserDirectoryUtil.getParameter(parameters, 'UserBaseDN') : '');
    this.userObjectClassControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'UserObjectClass') ?
        UserDirectoryUtil.getParameter(parameters, 'UserObjectClass') : 'inetOrgPerson');
    this.userEmailAttributeControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'UserEmailAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'UserEmailAttribute') : 'mail');
    this.userNameAttributeControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'UserNameAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'UserNameAttribute') : 'Name');
    this.userPreferredNameAttributeControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'UserPreferredNameAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'UserPreferredNameAttribute') : 'nickName');
    this.userMobileNumberAttributeControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'UserMobileNumberAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'UserMobileNumberAttribute') : 'mobile');
    this.userPhoneNumberAttributeControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'UserPhoneNumberAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'UserPhoneNumberAttribute') : 'telephoneNumber');
    this.userUsernameAttributeControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'UserUsernameAttribute') ?
        UserDirectoryUtil.getParameter(parameters, 'UserUsernameAttribute') : 'uid');
    this.useSSLControl.setValue(
      UserDirectoryUtil.hasParameter(parameters, 'UseSSL') ? UserDirectoryUtil.getParameter(
          parameters, 'UseSSL') :
        'false');
  }

  validate(c: AbstractControl): ValidationErrors | null {
    return this.ldapUserDirectoryForm.valid ? null : {
      invalidForm: {
        valid: false,
        message: 'ldapUserDirectoryForm fields are invalid'
      }
    };
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  writeValue(val: any): void {
    if (val) {
      this.ldapUserDirectoryForm.setValue(val, {emitEvent: false});
    }
  }
}
