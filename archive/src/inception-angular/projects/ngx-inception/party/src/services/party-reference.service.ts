/*
 * Copyright 2022 Marcus Portmann
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

import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {Inject, Injectable, LOCALE_ID} from '@angular/core';
import {
  AccessDeniedError, CacheService, CommunicationError, INCEPTION_CONFIG, InceptionConfig,
  ServiceUnavailableError
} from 'ngx-inception/core';
import {Observable, of, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {AssociationPropertyType} from "./association-property-type";
import {AssociationType} from "./association-type";
import {AttributeType} from "./attribute-type";
import {AttributeTypeCategory} from "./attribute-type-category";
import {ConsentType} from "./consent-type";
import {ContactMechanismPurpose} from "./contact-mechanism-purpose";
import {ContactMechanismRole} from './contact-mechanism-role';
import {ContactMechanismType} from './contact-mechanism-type';
import {EmploymentStatus} from './employment-status';
import {EmploymentType} from './employment-type';
import {ExternalReferenceType} from "./external-reference-type";
import {FieldOfStudy} from "./field-of-study";
import {Gender} from './gender';
import {IdentityDocumentType} from './identity-document-type';
import {IndustryClassification} from './industry-classification';
import {IndustryClassificationCategory} from './industry-classification-category';
import {IndustryClassificationSystem} from './industry-classification-system';
import {LockType} from "./lock-type";
import {LockTypeCategory} from "./lock-type-category";
import {MandataryRole} from './mandatary-role';
import {MandatePropertyType} from "./mandate-property-type";
import {MandateType} from './mandate-type';
import {MaritalStatus} from './marital-status';
import {MarriageType} from './marriage-type';
import {NextOfKinType} from './next-of-kin-type';
import {Occupation} from './occupation';
import {PhysicalAddressPurpose} from "./physical-address-purpose";
import {PhysicalAddressRole} from "./physical-address-role";
import {PhysicalAddressType} from "./physical-address-type";
import {PreferenceType} from "./preference-type";
import {PreferenceTypeCategory} from "./preference-type-category";
import {QualificationType} from "./qualification-type";
import {Race} from './race';
import {ResidencePermitType} from './residence-permit-type';
import {ResidencyStatus} from './residency-status';
import {ResidentialType} from './residential-type';
import {RolePurpose} from "./role-purpose";
import {RoleType} from "./role-type";
import {RoleTypeAttributeTypeConstraint} from "./role-type-attribute-type-constraint";
import {RoleTypePreferenceTypeConstraint} from "./role-type-preference-type-constraint";
import {Segment} from "./segment";
import {SegmentationType} from "./segmentation-type";
import {SkillType} from './skill-type';
import {SourceOfFundsType} from './source-of-funds-type';
import {SourceOfWealthType} from "./source-of-wealth-type";
import {StatusType} from "./status-type";
import {StatusTypeCategory} from "./status-type-category";
import {TaxNumberType} from './tax-number-type';
import {TimeToContact} from "./time-to-contact";
import {Title} from './title';

/**
 * The Party Reference Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class PartyReferenceService {

  /**
   * Constructs a new ReferenceService.
   *
   * @param config     The Inception configuration.
   * @param localeId   The Unicode locale identifier for the locale for the application.
   * @param httpClient The HTTP client.
   */
  constructor(@Inject(INCEPTION_CONFIG) private config: InceptionConfig,
              @Inject(LOCALE_ID) private localeId: string, private httpClient: HttpClient,
              private cacheService: CacheService) {
    console.log('Initializing the Party Reference Service (' + localeId + ')');
  }

  /**
   * Retrieve the association property types.
   *
   * @return The association property types.
   */
  getAssociationPropertyTypes(): Observable<Map<string, AssociationPropertyType>> {
    let cachedAssociationPropertyTypes: Map<string, AssociationPropertyType> = this.cacheService.get('associationPropertyTypes');

    if (cachedAssociationPropertyTypes !== undefined) {
      return of(cachedAssociationPropertyTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<AssociationPropertyType[]>(this.config.apiUrlPrefix + '/party/reference/association-property-types',
        {params, reportProgress: true})
      .pipe(map((associationPropertyTypes: AssociationPropertyType[]) => {
        cachedAssociationPropertyTypes = new Map<string, AssociationPropertyType>();

        for (const associationPropertyType of associationPropertyTypes) {
          cachedAssociationPropertyTypes.set(associationPropertyType.code, associationPropertyType);
        }

        this.cacheService.set('associationPropertyTypes', cachedAssociationPropertyTypes);
        return cachedAssociationPropertyTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the association property types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the association types.
   *
   * @return The association types.
   */
  getAssociationTypes(): Observable<Map<string, AssociationType>> {
    let cachedAssociationTypes: Map<string, AssociationType> = this.cacheService.get('associationTypes');

    if (cachedAssociationTypes !== undefined) {
      return of(cachedAssociationTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<AssociationType[]>(this.config.apiUrlPrefix + '/party/reference/association-types',
        {params, reportProgress: true})
      .pipe(map((associationTypes: AssociationType[]) => {
        cachedAssociationTypes = new Map<string, AssociationType>();

        for (const associationType of associationTypes) {
          cachedAssociationTypes.set(associationType.code, associationType);
        }

        this.cacheService.set('associationTypes', cachedAssociationTypes);
        return cachedAssociationTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the association types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the attribute type categories.
   *
   * @return The attribute type categories.
   */
  getAttributeTypeCategories(): Observable<Map<string, AttributeTypeCategory>> {
    let cachedAttributeTypeCategories: Map<string, AttributeTypeCategory> = this.cacheService.get('attributeTypeCategories');

    if (cachedAttributeTypeCategories !== undefined) {
      return of(cachedAttributeTypeCategories);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<AttributeTypeCategory[]>(this.config.apiUrlPrefix + '/party/reference/attribute-type-categories',
        {params, reportProgress: true})
      .pipe(map((attributeTypeCategories: AttributeTypeCategory[]) => {
        cachedAttributeTypeCategories = new Map<string, AttributeTypeCategory>();

        for (const attributeTypeCategory of attributeTypeCategories) {
          cachedAttributeTypeCategories.set(attributeTypeCategory.code, attributeTypeCategory);
        }

        this.cacheService.set('attributeTypeCategories', cachedAttributeTypeCategories);
        return cachedAttributeTypeCategories;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the attribute type categories.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the attribute types.
   *
   * @return The attribute types.
   */
  getAttributeTypes(): Observable<Map<string, AttributeType>> {
    let cachedAttributeTypes: Map<string, AttributeType> = this.cacheService.get('attributeTypes');

    if (cachedAttributeTypes !== undefined) {
      return of(cachedAttributeTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<AttributeType[]>(this.config.apiUrlPrefix + '/party/reference/attribute-types',
        {params, reportProgress: true})
      .pipe(map((attributeTypes: AttributeType[]) => {
        cachedAttributeTypes = new Map<string, AttributeType>();

        for (const attributeType of attributeTypes) {
          cachedAttributeTypes.set(attributeType.code, attributeType);
        }

        this.cacheService.set('attributeTypes', cachedAttributeTypes);
        return cachedAttributeTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the attribute types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the consent types.
   *
   * @return The consent types.
   */
  getConsentTypes(): Observable<Map<string, ConsentType>> {
    let cachedConsentTypes: Map<string, ConsentType> = this.cacheService.get('consentTypes');

    if (cachedConsentTypes !== undefined) {
      return of(cachedConsentTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<ConsentType[]>(this.config.apiUrlPrefix + '/party/reference/consent-types',
        {params, reportProgress: true})
      .pipe(map((consentTypes: ConsentType[]) => {
        cachedConsentTypes = new Map<string, ConsentType>();

        for (const consentType of consentTypes) {
          cachedConsentTypes.set(consentType.code, consentType);
        }

        this.cacheService.set('consentTypes', cachedConsentTypes);
        return cachedConsentTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the consent types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the contact mechanism purposes.
   *
   * @return The contact mechanism purposes.
   */
  getContactMechanismPurposes(): Observable<Map<string, ContactMechanismPurpose>> {
    let cachedContactMechanismPurposes: Map<string, ContactMechanismPurpose> = this.cacheService.get('contactMechanismPurposes');

    if (cachedContactMechanismPurposes !== undefined) {
      return of(cachedContactMechanismPurposes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<ContactMechanismPurpose[]>(this.config.apiUrlPrefix + '/party/reference/contact-mechanism-purposes',
        {params, reportProgress: true})
      .pipe(map((contactMechanismPurposes: ContactMechanismPurpose[]) => {
        cachedContactMechanismPurposes = new Map<string, ContactMechanismPurpose>();

        for (const contactMechanismPurpose of contactMechanismPurposes) {
          cachedContactMechanismPurposes.set(contactMechanismPurpose.code, contactMechanismPurpose);
        }

        this.cacheService.set('contactMechanismPurposes', cachedContactMechanismPurposes);
        return cachedContactMechanismPurposes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the contact mechanism purposes.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the contact mechanism roles.
   *
   * @return The contact mechanism roles.
   */
  getContactMechanismRoles(): Observable<Map<string, ContactMechanismRole>> {
    let cachedContactMechanismRoles: Map<string, ContactMechanismRole> = this.cacheService.get('contactMechanismRoles');

    if (cachedContactMechanismRoles !== undefined) {
      return of(cachedContactMechanismRoles);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<ContactMechanismRole[]>(this.config.apiUrlPrefix + '/party/reference/contact-mechanism-roles',
        {params, reportProgress: true})
      .pipe(map((contactMechanismRoles: ContactMechanismRole[]) => {
        cachedContactMechanismRoles = new Map<string, ContactMechanismRole>();

        for (const contactMechanismRole of contactMechanismRoles) {
          cachedContactMechanismRoles.set(contactMechanismRole.code, contactMechanismRole);
        }

        this.cacheService.set('contactMechanismRoles', cachedContactMechanismRoles);
        return cachedContactMechanismRoles;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the contact mechanism roles.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the contact mechanism types.
   *
   * @return The contact mechanism types.
   */
  getContactMechanismTypes(): Observable<Map<string, ContactMechanismType>> {
    let cachedContactMechanismTypes: Map<string, ContactMechanismType> = this.cacheService.get('contactMechanismTypes');

    if (cachedContactMechanismTypes !== undefined) {
      return of(cachedContactMechanismTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<ContactMechanismType[]>(this.config.apiUrlPrefix + '/party/reference/contact-mechanism-types',
        {params, reportProgress: true})
      .pipe(map((contactMechanismTypes: ContactMechanismType[]) => {
        cachedContactMechanismTypes = new Map<string, ContactMechanismType>();

        for (const contactMechanismType of contactMechanismTypes) {
          cachedContactMechanismTypes.set(contactMechanismType.code, contactMechanismType);
        }

        this.cacheService.set('contactMechanismTypes', cachedContactMechanismTypes);
        return cachedContactMechanismTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the contact mechanism types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the employment statuses.
   *
   * @return The employment statuses.
   */
  getEmploymentStatuses(): Observable<Map<string, EmploymentStatus>> {
    let cachedEmploymentStatuses: Map<string, EmploymentStatus> = this.cacheService.get('employmentStatuses');

    if (cachedEmploymentStatuses !== undefined) {
      return of(cachedEmploymentStatuses);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<EmploymentStatus[]>(this.config.apiUrlPrefix + '/party/reference/employment-statuses',
        {params, reportProgress: true})
      .pipe(map((employmentStatuses: EmploymentStatus[]) => {
        cachedEmploymentStatuses = new Map<string, EmploymentStatus>();

        for (const employmentStatus of employmentStatuses) {
          cachedEmploymentStatuses.set(employmentStatus.code, employmentStatus);
        }

        this.cacheService.set('employmentStatuses', cachedEmploymentStatuses);
        return cachedEmploymentStatuses;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the employment statuses.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the employment types.
   *
   * @return The employment types.
   */
  getEmploymentTypes(): Observable<Map<string, EmploymentType>> {
    let cachedEmploymentTypes: Map<string, EmploymentType> = this.cacheService.get('employmentTypes');

    if (cachedEmploymentTypes !== undefined) {
      return of(cachedEmploymentTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<EmploymentType[]>(this.config.apiUrlPrefix + '/party/reference/employment-types',
        {params, reportProgress: true})
      .pipe(map((employmentTypes: EmploymentType[]) => {
        cachedEmploymentTypes = new Map<string, EmploymentType>();

        for (const employmentType of employmentTypes) {
          cachedEmploymentTypes.set(employmentType.code, employmentType);
        }

        this.cacheService.set('employmentTypes', cachedEmploymentTypes);
        return cachedEmploymentTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the employment types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the external reference types.
   *
   * @return The external reference types.
   */
  getExternalReferenceTypes(): Observable<Map<string, ExternalReferenceType>> {
    let cachedExternalReferenceTypes: Map<string, ExternalReferenceType> = this.cacheService.get('externalReferenceTypes');

    if (cachedExternalReferenceTypes !== undefined) {
      return of(cachedExternalReferenceTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<ExternalReferenceType[]>(this.config.apiUrlPrefix + '/party/reference/external-reference-types',
        {params, reportProgress: true})
      .pipe(map((externalReferenceTypes: ExternalReferenceType[]) => {
        cachedExternalReferenceTypes = new Map<string, ExternalReferenceType>();

        for (const externalReferenceType of externalReferenceTypes) {
          cachedExternalReferenceTypes.set(externalReferenceType.code, externalReferenceType);
        }

        this.cacheService.set('externalReferenceTypes', cachedExternalReferenceTypes);
        return cachedExternalReferenceTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the external reference types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the fields of study.
   *
   * @return The fields of study.
   */
  getFieldsOfStudy(): Observable<Map<string, FieldOfStudy>> {
    let cachedFieldsOfStudy: Map<string, FieldOfStudy> = this.cacheService.get('fieldsOfStudy');

    if (cachedFieldsOfStudy !== undefined) {
      return of(cachedFieldsOfStudy);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<FieldOfStudy[]>(this.config.apiUrlPrefix + '/party/reference/fields-of-study',
        {params, reportProgress: true})
      .pipe(map((fieldsOfStudy: FieldOfStudy[]) => {
        cachedFieldsOfStudy = new Map<string, FieldOfStudy>();

        for (const fieldOfStudy of fieldsOfStudy) {
          cachedFieldsOfStudy.set(fieldOfStudy.code, fieldOfStudy);
        }

        this.cacheService.set('fieldsOfStudy', cachedFieldsOfStudy);
        return cachedFieldsOfStudy;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the fields of study.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the genders.
   *
   * @return The genders.
   */
  getGenders(): Observable<Map<string, Gender>> {
    let cachedGenders: Map<string, Gender> = this.cacheService.get('genders');

    if (cachedGenders !== undefined) {
      return of(cachedGenders);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<Gender[]>(this.config.apiUrlPrefix + '/party/reference/genders',
        {params, reportProgress: true})
      .pipe(map((genders: Gender[]) => {
        cachedGenders = new Map<string, Gender>();

        for (const gender of genders) {
          cachedGenders.set(gender.code, gender);
        }

        this.cacheService.set('genders', cachedGenders);
        return cachedGenders;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the genders.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the identity document types.
   *
   * @return The identity document types.
   */
  getIdentityDocumentTypes(): Observable<Map<string, IdentityDocumentType>> {
    let cachedIdentityDocumentTypes: Map<string, IdentityDocumentType> = this.cacheService.get('identityDocumentTypes');

    if (cachedIdentityDocumentTypes !== undefined) {
      return of(cachedIdentityDocumentTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<IdentityDocumentType[]>(this.config.apiUrlPrefix + '/party/reference/identity-document-types',
        {params, reportProgress: true})
      .pipe(map((identityDocumentTypes: IdentityDocumentType[]) => {
        cachedIdentityDocumentTypes = new Map<string, IdentityDocumentType>();

        for (const identityDocumentType of identityDocumentTypes) {
          cachedIdentityDocumentTypes.set(identityDocumentType.code, identityDocumentType);
        }

        this.cacheService.set('identityDocumentTypes', cachedIdentityDocumentTypes);
        return cachedIdentityDocumentTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the identity document types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the industry classification categories.
   *
   * @return The industry classification categories.
   */
  getIndustryClassificationCategories(): Observable<Map<string, IndustryClassificationCategory>> {
    let cachedIndustryClassificationCategories: Map<string, IndustryClassificationCategory> = this.cacheService.get('industryClassificationCategories');

    if (cachedIndustryClassificationCategories !== undefined) {
      return of(cachedIndustryClassificationCategories);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<IndustryClassificationCategory[]>(this.config.apiUrlPrefix + '/party/reference/industry-classification-categories',
        {params, reportProgress: true})
      .pipe(map((industryClassificationCategories: IndustryClassificationCategory[]) => {
        cachedIndustryClassificationCategories = new Map<string, IndustryClassificationCategory>();

        for (const industryClassificationCategory of industryClassificationCategories) {
          cachedIndustryClassificationCategories.set(industryClassificationCategory.code, industryClassificationCategory);
        }

        this.cacheService.set('industryClassificationCategories', cachedIndustryClassificationCategories);
        return cachedIndustryClassificationCategories;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the industry classification categories.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the industry classification systems.
   *
   * @return The industry classification systems.
   */
  getIndustryClassificationSystems(): Observable<Map<string, IndustryClassificationSystem>> {
    let cachedIndustryClassificationSystems: Map<string, IndustryClassificationSystem> = this.cacheService.get('industryClassificationSystems');

    if (cachedIndustryClassificationSystems !== undefined) {
      return of(cachedIndustryClassificationSystems);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<IndustryClassificationSystem[]>(this.config.apiUrlPrefix + '/party/reference/industry-classification-systems',
        {params, reportProgress: true})
      .pipe(map((industryClassificationSystems: IndustryClassificationSystem[]) => {
        cachedIndustryClassificationSystems = new Map<string, IndustryClassificationSystem>();

        for (const industryClassificationSystem of industryClassificationSystems) {
          cachedIndustryClassificationSystems.set(industryClassificationSystem.code, industryClassificationSystem);
        }

        this.cacheService.set('industryClassificationSystems', cachedIndustryClassificationSystems);
        return cachedIndustryClassificationSystems;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the industry classification systems.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the industry classifications.
   *
   * @return The industry classifications.
   */
  getIndustryClassifications(): Observable<Map<string, IndustryClassification>> {
    let cachedIndustryClassifications: Map<string, IndustryClassification> = this.cacheService.get('industryClassifications');

    if (cachedIndustryClassifications !== undefined) {
      return of(cachedIndustryClassifications);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<IndustryClassification[]>(this.config.apiUrlPrefix + '/party/reference/industry-classifications',
        {params, reportProgress: true})
      .pipe(map((industryClassifications: IndustryClassification[]) => {
        cachedIndustryClassifications = new Map<string, IndustryClassification>();

        for (const industryClassification of industryClassifications) {
          cachedIndustryClassifications.set(industryClassification.code, industryClassification);
        }

        this.cacheService.set('industryClassifications', cachedIndustryClassifications);
        return cachedIndustryClassifications;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the industry classifications.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the lock type categories.
   *
   * @return The lock type categories.
   */
  getLockTypeCategories(): Observable<Map<string, LockTypeCategory>> {
    let cachedLockTypeCategories: Map<string, LockTypeCategory> = this.cacheService.get('lockTypeCategories');

    if (cachedLockTypeCategories !== undefined) {
      return of(cachedLockTypeCategories);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<LockTypeCategory[]>(this.config.apiUrlPrefix + '/party/reference/lock-type-categories',
        {params, reportProgress: true})
      .pipe(map((lockTypeCategories: LockTypeCategory[]) => {
        cachedLockTypeCategories = new Map<string, LockTypeCategory>();

        for (const lockTypeCategory of lockTypeCategories) {
          cachedLockTypeCategories.set(lockTypeCategory.code, lockTypeCategory);
        }

        this.cacheService.set('lockTypeCategories', cachedLockTypeCategories);
        return cachedLockTypeCategories;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the lock type categories.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the lock types.
   *
   * @return The lock types.
   */
  getLockTypes(): Observable<Map<string, LockType>> {
    let cachedLockTypes: Map<string, LockType> = this.cacheService.get('lockTypes');

    if (cachedLockTypes !== undefined) {
      return of(cachedLockTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<LockType[]>(this.config.apiUrlPrefix + '/party/reference/lock-types',
        {params, reportProgress: true})
      .pipe(map((lockTypes: LockType[]) => {
        cachedLockTypes = new Map<string, LockType>();

        for (const lockType of lockTypes) {
          cachedLockTypes.set(lockType.code, lockType);
        }

        this.cacheService.set('lockTypes', cachedLockTypes);
        return cachedLockTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the lock types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the mandatary roles.
   *
   * @return The mandatary roles.
   */
  getMandataryRoles(): Observable<Map<string, MandataryRole>> {
    let cachedMandataryRoles: Map<string, MandataryRole> = this.cacheService.get('mandataryRoles');

    if (cachedMandataryRoles !== undefined) {
      return of(cachedMandataryRoles);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<MandataryRole[]>(this.config.apiUrlPrefix + '/party/reference/mandatary-types',
        {params, reportProgress: true})
      .pipe(map((mandataryRoles: MandataryRole[]) => {
        cachedMandataryRoles = new Map<string, MandataryRole>();

        for (const mandataryRole of mandataryRoles) {
          cachedMandataryRoles.set(mandataryRole.code, mandataryRole);
        }

        this.cacheService.set('mandataryRoles', cachedMandataryRoles);
        return cachedMandataryRoles;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the mandatary roles.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the mandate property types.
   *
   * @return The mandate property types.
   */
  getMandatePropertyTypes(): Observable<Map<string, MandatePropertyType>> {
    let cachedMandatePropertyTypes: Map<string, MandatePropertyType> = this.cacheService.get('mandatePropertyTypes');

    if (cachedMandatePropertyTypes !== undefined) {
      return of(cachedMandatePropertyTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<MandatePropertyType[]>(this.config.apiUrlPrefix + '/party/reference/mandate-property-types',
        {params, reportProgress: true})
      .pipe(map((mandatePropertyTypes: MandatePropertyType[]) => {
        cachedMandatePropertyTypes = new Map<string, MandatePropertyType>();

        for (const mandatePropertyType of mandatePropertyTypes) {
          cachedMandatePropertyTypes.set(mandatePropertyType.code, mandatePropertyType);
        }

        this.cacheService.set('mandatePropertyTypes', cachedMandatePropertyTypes);
        return cachedMandatePropertyTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the mandate property types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the mandate types.
   *
   * @return The mandate types.
   */
  getMandateTypes(): Observable<Map<string, MandateType>> {
    let cachedMandateTypes: Map<string, MandateType> = this.cacheService.get('mandateTypes');

    if (cachedMandateTypes !== undefined) {
      return of(cachedMandateTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<MandateType[]>(this.config.apiUrlPrefix + '/party/reference/mandate-types',
        {params, reportProgress: true})
      .pipe(map((mandateTypes: MandateType[]) => {
        cachedMandateTypes = new Map<string, MandateType>();

        for (const mandateType of mandateTypes) {
          cachedMandateTypes.set(mandateType.code, mandateType);
        }

        this.cacheService.set('mandateTypes', cachedMandateTypes);
        return cachedMandateTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the mandate types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the marital statuses.
   *
   * @return The marital statuses.
   */
  getMaritalStatuses(): Observable<Map<string, MaritalStatus>> {
    let cachedMaritalStatuses: Map<string, MaritalStatus> = this.cacheService.get('maritalStatuses');

    if (cachedMaritalStatuses !== undefined) {
      return of(cachedMaritalStatuses);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<MaritalStatus[]>(this.config.apiUrlPrefix + '/party/reference/marital-statuses',
        {params, reportProgress: true})
      .pipe(map((maritalStatuses: MaritalStatus[]) => {
        cachedMaritalStatuses = new Map<string, MaritalStatus>();

        for (const maritalStatus of maritalStatuses) {
          cachedMaritalStatuses.set(maritalStatus.code, maritalStatus);
        }

        this.cacheService.set('maritalStatuses', cachedMaritalStatuses);
        return cachedMaritalStatuses;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the marital statuses.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the marriage types.
   *
   * @return The marriage types.
   */
  getMarriageTypes(): Observable<Map<string, MarriageType>> {
    let cachedMarriageTypes: Map<string, MarriageType> = this.cacheService.get('marriageTypes');

    if (cachedMarriageTypes !== undefined) {
      return of(cachedMarriageTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<MarriageType[]>(this.config.apiUrlPrefix + '/party/reference/marriage-types',
        {params, reportProgress: true})
      .pipe(map((marriageTypes: MarriageType[]) => {
        cachedMarriageTypes = new Map<string, MarriageType>();

        for (const marriageType of marriageTypes) {
          cachedMarriageTypes.set(marriageType.code, marriageType);
        }

        this.cacheService.set('marriageTypes', cachedMarriageTypes);
        return cachedMarriageTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the marriage types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the next of kin types.
   *
   * @return The next of kin types.
   */
  getNextOfKinTypes(): Observable<Map<string, NextOfKinType>> {
    let cachedNextOfKinTypes: Map<string, NextOfKinType> = this.cacheService.get('nextOfKinTypes');

    if (cachedNextOfKinTypes !== undefined) {
      return of(cachedNextOfKinTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<NextOfKinType[]>(this.config.apiUrlPrefix + '/party/reference/next-of-kin-types',
        {params, reportProgress: true})
      .pipe(map((nextOfKinTypes: NextOfKinType[]) => {
        cachedNextOfKinTypes = new Map<string, NextOfKinType>();

        for (const nextOfKinType of nextOfKinTypes) {
          cachedNextOfKinTypes.set(nextOfKinType.code, nextOfKinType);
        }

        this.cacheService.set('nextOfKinTypes', cachedNextOfKinTypes);
        return cachedNextOfKinTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the next of kin types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the occupations.
   *
   * @return The occupations.
   */
  getOccupations(): Observable<Map<string, Occupation>> {
    let cachedOccupations: Map<string, Occupation> = this.cacheService.get('occupations');

    if (cachedOccupations !== undefined) {
      return of(cachedOccupations);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<Occupation[]>(this.config.apiUrlPrefix + '/party/reference/occupations',
        {params, reportProgress: true})
      .pipe(map((occupations: Occupation[]) => {
        cachedOccupations = new Map<string, Occupation>();

        for (const occupation of occupations) {
          cachedOccupations.set(occupation.code, occupation);
        }

        this.cacheService.set('occupations', cachedOccupations);
        return cachedOccupations;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the occupations.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the physical address purposes.
   *
   * @return The physical address purposes.
   */
  getPhysicalAddressPurposes(): Observable<Map<string, PhysicalAddressPurpose>> {
    let cachedPhysicalAddressPurposes: Map<string, PhysicalAddressPurpose> = this.cacheService.get('physicalAddressPurposes');

    if (cachedPhysicalAddressPurposes !== undefined) {
      return of(cachedPhysicalAddressPurposes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<PhysicalAddressPurpose[]>(this.config.apiUrlPrefix + '/party/reference/physical-address-purposes',
        {params, reportProgress: true})
      .pipe(map((physicalAddressPurposes: PhysicalAddressPurpose[]) => {
        cachedPhysicalAddressPurposes = new Map<string, PhysicalAddressPurpose>();

        for (const physicalAddressPurpose of physicalAddressPurposes) {
          cachedPhysicalAddressPurposes.set(physicalAddressPurpose.code, physicalAddressPurpose);
        }

        this.cacheService.set('physicalAddressPurposes', cachedPhysicalAddressPurposes);
        return cachedPhysicalAddressPurposes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the physical address purposes.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the physical address roles.
   *
   * @return The physical address roles.
   */
  getPhysicalAddressRoles(): Observable<Map<string, PhysicalAddressRole>> {
    let cachedPhysicalAddressRoles: Map<string, PhysicalAddressRole> = this.cacheService.get('physicalAddressRoles');

    if (cachedPhysicalAddressRoles !== undefined) {
      return of(cachedPhysicalAddressRoles);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<PhysicalAddressRole[]>(this.config.apiUrlPrefix + '/party/reference/physical-address-roles',
        {params, reportProgress: true})
      .pipe(map((physicalAddressRoles: PhysicalAddressRole[]) => {
        cachedPhysicalAddressRoles = new Map<string, PhysicalAddressRole>();

        for (const physicalAddressRole of physicalAddressRoles) {
          cachedPhysicalAddressRoles.set(physicalAddressRole.code, physicalAddressRole);
        }

        this.cacheService.set('physicalAddressRoles', cachedPhysicalAddressRoles);
        return cachedPhysicalAddressRoles;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the physical address roles.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the physical address types.
   *
   * @return The physical address types.
   */
  getPhysicalAddressTypes(): Observable<Map<string, PhysicalAddressType>> {
    let cachedPhysicalAddressTypes: Map<string, PhysicalAddressType> = this.cacheService.get('physicalAddressTypes');

    if (cachedPhysicalAddressTypes !== undefined) {
      return of(cachedPhysicalAddressTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<PhysicalAddressRole[]>(this.config.apiUrlPrefix + '/party/reference/physical-address-types',
        {params, reportProgress: true})
      .pipe(map((physicalAddressTypes: PhysicalAddressType[]) => {
        cachedPhysicalAddressTypes = new Map<string, PhysicalAddressType>();

        for (const physicalAddressType of physicalAddressTypes) {
          cachedPhysicalAddressTypes.set(physicalAddressType.code, physicalAddressType);
        }

        this.cacheService.set('physicalAddressTypes', cachedPhysicalAddressTypes);
        return cachedPhysicalAddressTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the physical address types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the preference type categories.
   *
   * @return The preference type categories.
   */
  getPreferenceTypeCategories(): Observable<Map<string, PreferenceTypeCategory>> {
    let cachedPreferenceTypeCategories: Map<string, PreferenceTypeCategory> = this.cacheService.get('preferenceTypeCategories');

    if (cachedPreferenceTypeCategories !== undefined) {
      return of(cachedPreferenceTypeCategories);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<PhysicalAddressRole[]>(this.config.apiUrlPrefix + '/party/reference/preference-type-categories',
        {params, reportProgress: true})
      .pipe(map((preferenceTypeCategories: PreferenceTypeCategory[]) => {
        cachedPreferenceTypeCategories = new Map<string, PreferenceTypeCategory>();

        for (const preferenceTypeCategory of preferenceTypeCategories) {
          cachedPreferenceTypeCategories.set(preferenceTypeCategory.code, preferenceTypeCategory);
        }

        this.cacheService.set('preferenceTypeCategories', cachedPreferenceTypeCategories);
        return cachedPreferenceTypeCategories;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the preference type categories.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the preference types.
   *
   * @return The preference types.
   */
  getPreferenceTypes(): Observable<Map<string, PreferenceType>> {
    let cachedPreferenceTypes: Map<string, PreferenceType> = this.cacheService.get('preferenceTypes');

    if (cachedPreferenceTypes !== undefined) {
      return of(cachedPreferenceTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<PreferenceType[]>(this.config.apiUrlPrefix + '/party/reference/preference-types',
        {params, reportProgress: true})
      .pipe(map((preferenceTypes: PreferenceType[]) => {
        cachedPreferenceTypes = new Map<string, PreferenceType>();

        for (const preferenceType of preferenceTypes) {
          cachedPreferenceTypes.set(preferenceType.code, preferenceType);
        }

        this.cacheService.set('preferenceTypes', cachedPreferenceTypes);
        return cachedPreferenceTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the preference types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the qualification types.
   *
   * @return The qualification types.
   */
  getQualificationTypes(): Observable<Map<string, QualificationType>> {
    let cachedQualificationTypes: Map<string, QualificationType> = this.cacheService.get('qualificationTypes');

    if (cachedQualificationTypes !== undefined) {
      return of(cachedQualificationTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<QualificationType[]>(this.config.apiUrlPrefix + '/party/reference/qualification-types',
        {params, reportProgress: true})
      .pipe(map((qualificationTypes: QualificationType[]) => {
        cachedQualificationTypes = new Map<string, QualificationType>();

        for (const qualificationType of qualificationTypes) {
          cachedQualificationTypes.set(qualificationType.code, qualificationType);
        }

        this.cacheService.set('qualificationTypes', cachedQualificationTypes);
        return cachedQualificationTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the qualification types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the races.
   *
   * @return The races.
   */
  getRaces(): Observable<Map<string, Race>> {
    let cachedRaces: Map<string, Race> = this.cacheService.get('races');

    if (cachedRaces !== undefined) {
      return of(cachedRaces);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<Race[]>(this.config.apiUrlPrefix + '/party/reference/races',
        {params, reportProgress: true})
      .pipe(map((races: Race[]) => {
        cachedRaces = new Map<string, Race>();

        for (const race of races) {
          cachedRaces.set(race.code, race);
        }

        this.cacheService.set('races', cachedRaces);
        return cachedRaces;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the races.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the residence permit types.
   *
   * @return The residence permit types.
   */
  getResidencePermitTypes(): Observable<Map<string, ResidencePermitType>> {
    let cachedResidencePermitTypes: Map<string, ResidencePermitType> = this.cacheService.get('residencePermitTypes');

    if (cachedResidencePermitTypes !== undefined) {
      return of(cachedResidencePermitTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<ResidencePermitType[]>(this.config.apiUrlPrefix + '/party/reference/residence-permit-types',
        {params, reportProgress: true})
      .pipe(map((residencePermitTypes: ResidencePermitType[]) => {
        cachedResidencePermitTypes = new Map<string, ResidencePermitType>();

        for (const residencePermitType of residencePermitTypes) {
          cachedResidencePermitTypes.set(residencePermitType.code, residencePermitType);
        }

        this.cacheService.set('residencePermitTypes', cachedResidencePermitTypes);
        return cachedResidencePermitTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the residence permit types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the residency statuses.
   *
   * @return The residency statuses.
   */
  getResidencyStatuses(): Observable<Map<string, ResidencyStatus>> {
    let cachedResidencyStatuses: Map<string, ResidencyStatus> = this.cacheService.get('residencyStatuses');

    if (cachedResidencyStatuses !== undefined) {
      return of(cachedResidencyStatuses);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<ResidencyStatus[]>(this.config.apiUrlPrefix + '/party/reference/residency-statuses',
        {params, reportProgress: true})
      .pipe(map((residencyStatuses: ResidencyStatus[]) => {
        cachedResidencyStatuses = new Map<string, ResidencePermitType>();

        for (const residencyStatus of residencyStatuses) {
          cachedResidencyStatuses.set(residencyStatus.code, residencyStatus);
        }

        this.cacheService.set('residencyStatuses', cachedResidencyStatuses);
        return cachedResidencyStatuses;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the residency statuses.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the residential types.
   *
   * @return The residential types.
   */
  getResidentialTypes(): Observable<Map<string, ResidentialType>> {
    let cachedResidentialTypes: Map<string, ResidentialType> = this.cacheService.get('residentialTypes');

    if (cachedResidentialTypes !== undefined) {
      return of(cachedResidentialTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<ResidentialType[]>(this.config.apiUrlPrefix + '/party/reference/residential-types',
        {params, reportProgress: true})
      .pipe(map((residentialTypes: ResidentialType[]) => {
        cachedResidentialTypes = new Map<string, ResidentialType>();

        for (const residentialType of residentialTypes) {
          cachedResidentialTypes.set(residentialType.code, residentialType);
        }

        this.cacheService.set('residentialTypes', cachedResidentialTypes);
        return cachedResidentialTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the residential types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the role purposes.
   *
   * @return The role purposes.
   */
  getRolePurposes(): Observable<Map<string, RolePurpose>> {
    let cachedRolePurposes: Map<string, RolePurpose> = this.cacheService.get('rolePurposes');

    if (cachedRolePurposes !== undefined) {
      return of(cachedRolePurposes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<ResidentialType[]>(this.config.apiUrlPrefix + '/party/reference/role-purposes',
        {params, reportProgress: true})
      .pipe(map((rolePurposes: RolePurpose[]) => {
        cachedRolePurposes = new Map<string, RolePurpose>();

        for (const rolePurpose of rolePurposes) {
          cachedRolePurposes.set(rolePurpose.code, rolePurpose);
        }

        this.cacheService.set('rolePurposes', cachedRolePurposes);
        return cachedRolePurposes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the role purposes.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the role type attribute type constraints.
   *
   * @param roleType The code for the role type to retrieve the preference constraints for.
   *
   * @return The role type attribute type constraints.
   */
  getRoleTypeAttributeTypeConstraints(roleType?: string): Observable<RoleTypeAttributeTypeConstraint[]> {
    let cachedRoleTypeAttributeTypeConstraints: RoleTypeAttributeTypeConstraint[] = this.cacheService.get('roleTypeAttributeTypeConstraints');

    if (cachedRoleTypeAttributeTypeConstraints !== undefined) {
      if (!!roleType) {
        let roleTypeAttributeTypeConstraintsForRole: RoleTypeAttributeTypeConstraint[] = [];

        for (const cachedRoleTypeAttributeTypeConstraint of cachedRoleTypeAttributeTypeConstraints) {
          if (roleType! === cachedRoleTypeAttributeTypeConstraint.roleType) {
            roleTypeAttributeTypeConstraintsForRole.push(cachedRoleTypeAttributeTypeConstraint)
          }
        }

        return of(roleTypeAttributeTypeConstraintsForRole);
      } else {
        return of(cachedRoleTypeAttributeTypeConstraints);
      }
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<RoleTypeAttributeTypeConstraint[]>(this.config.apiUrlPrefix + '/party/reference/role-type-attribute-type-constraints',
        {params, reportProgress: true})
      .pipe(map((roleTypeAttributeTypeConstraints: RoleTypeAttributeTypeConstraint[]) => {
        this.cacheService.set('roleTypeAttributeTypeConstraints', roleTypeAttributeTypeConstraints);

        if (!!roleType) {
          let roleTypeAttributeTypeConstraintsForRole: RoleTypeAttributeTypeConstraint[] = [];

          for (const roleTypeAttributeTypeConstraint of roleTypeAttributeTypeConstraints) {
            if (roleType! === roleTypeAttributeTypeConstraint.roleType) {
              roleTypeAttributeTypeConstraintsForRole.push(roleTypeAttributeTypeConstraint)
            }
          }

          return roleTypeAttributeTypeConstraintsForRole;
        } else {
          return roleTypeAttributeTypeConstraints;
        }
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the role type attribute type constraints.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the role type preference type constraints.
   *
   * @param roleType The code for the role type to retrieve the preference constraints for.
   *
   * @return The role type preference type constraints.
   */
  getRoleTypePreferenceTypeConstraints(roleType?: string): Observable<RoleTypePreferenceTypeConstraint[]> {
    let cachedRoleTypePreferenceTypeConstraints: RoleTypePreferenceTypeConstraint[] = this.cacheService.get('roleTypePreferenceTypeConstraints');

    if (cachedRoleTypePreferenceTypeConstraints !== undefined) {
      if (!!roleType) {
        let roleTypePreferenceTypeConstraintsForRole: RoleTypePreferenceTypeConstraint[] = [];

        for (const cachedRoleTypePreferenceTypeConstraint of cachedRoleTypePreferenceTypeConstraints) {
          if (roleType! === cachedRoleTypePreferenceTypeConstraint.roleType) {
            roleTypePreferenceTypeConstraintsForRole.push(cachedRoleTypePreferenceTypeConstraint)
          }
        }

        return of(roleTypePreferenceTypeConstraintsForRole);
      } else {
        return of(cachedRoleTypePreferenceTypeConstraints);
      }
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      if (!!roleType) {
        params = params.append('roleType', roleType);
      }

      return this.httpClient.get<RoleTypePreferenceTypeConstraint[]>(this.config.apiUrlPrefix + '/party/reference/role-type-preference-type-constraints',
        {params, reportProgress: true})
      .pipe(map((roleTypePreferenceTypeConstraints: RoleTypePreferenceTypeConstraint[]) => {
        this.cacheService.set('roleTypePreferenceTypeConstraints', roleTypePreferenceTypeConstraints);

        if (!!roleType) {
          let roleTypePreferenceTypeConstraintsForRole: RoleTypePreferenceTypeConstraint[] = [];

          for (const roleTypePreferenceTypeConstraint of roleTypePreferenceTypeConstraints) {
            if (roleType! === roleTypePreferenceTypeConstraint.roleType) {
              roleTypePreferenceTypeConstraintsForRole.push(roleTypePreferenceTypeConstraint)
            }
          }

          return roleTypePreferenceTypeConstraintsForRole;
        } else {
          return roleTypePreferenceTypeConstraints;
        }
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the role type preference type constraints.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the role types.
   *
   * @return The role types.
   */
  getRoleTypes(): Observable<Map<string, RoleType>> {
    let cachedRoleTypes: Map<string, RoleType> = this.cacheService.get('roleTypes');

    if (cachedRoleTypes !== undefined) {
      return of(cachedRoleTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<RoleType[]>(this.config.apiUrlPrefix + '/party/reference/role-types',
        {params, reportProgress: true})
      .pipe(map((roleTypes: RoleType[]) => {
        cachedRoleTypes = new Map<string, RoleType>();

        for (const roleType of roleTypes) {
          cachedRoleTypes.set(roleType.code, roleType);
        }

        this.cacheService.set('roleTypes', cachedRoleTypes);
        return cachedRoleTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the role types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the segmentation types.
   *
   * @return The segmentation types.
   */
  getSegmentationTypes(): Observable<Map<string, SegmentationType>> {
    let cachedSegmentationTypes: Map<string, SegmentationType> = this.cacheService.get('segmentationTypes');

    if (cachedSegmentationTypes !== undefined) {
      return of(cachedSegmentationTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<SegmentationType[]>(this.config.apiUrlPrefix + '/party/reference/segmentation-types',
        {params, reportProgress: true})
      .pipe(map((segmentationTypes: SegmentationType[]) => {
        cachedSegmentationTypes = new Map<string, SegmentationType>();

        for (const segmentationType of segmentationTypes) {
          cachedSegmentationTypes.set(segmentationType.code, segmentationType);
        }

        this.cacheService.set('segmentationTypes', cachedSegmentationTypes);
        return cachedSegmentationTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the segmentation types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the segments.
   *
   * @return The segments.
   */
  getSegments(): Observable<Map<string, Segment>> {
    let cachedSegments: Map<string, Segment> = this.cacheService.get('segments');

    if (cachedSegments !== undefined) {
      return of(cachedSegments);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<Segment[]>(this.config.apiUrlPrefix + '/party/reference/segments',
        {params, reportProgress: true})
      .pipe(map((segments: Segment[]) => {
        cachedSegments = new Map<string, Segment>();

        for (const segment of segments) {
          cachedSegments.set(segment.code, segment);
        }

        this.cacheService.set('segments', cachedSegments);
        return cachedSegments;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the segments.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the skill types.
   *
   * @return The skill types.
   */
  getSkillTypes(): Observable<Map<string, SkillType>> {
    let cachedSkillTypes: Map<string, SkillType> = this.cacheService.get('skillTypes');

    if (cachedSkillTypes !== undefined) {
      return of(cachedSkillTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<SkillType[]>(this.config.apiUrlPrefix + '/party/reference/skill-types',
        {params, reportProgress: true})
      .pipe(map((skillTypes: SkillType[]) => {
        cachedSkillTypes = new Map<string, SkillType>();

        for (const skillType of skillTypes) {
          cachedSkillTypes.set(skillType.code, skillType);
        }

        this.cacheService.set('skillTypes', cachedSkillTypes);
        return cachedSkillTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the skill types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the source of funds types.
   *
   * @return The source of funds types.
   */
  getSourceOfFundsTypes(): Observable<Map<string, SourceOfFundsType>> {
    let cachedSourceOfFundsTypes: Map<string, SourceOfFundsType> = this.cacheService.get('sourceOfFundsTypes');

    if (cachedSourceOfFundsTypes !== undefined) {
      return of(cachedSourceOfFundsTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<SourceOfFundsType[]>(this.config.apiUrlPrefix + '/party/reference/source-of-funds-types',
        {params, reportProgress: true})
      .pipe(map((sourceOfFundsTypes: SourceOfFundsType[]) => {
        cachedSourceOfFundsTypes = new Map<string, SkillType>();

        for (const sourceOfFundsType of sourceOfFundsTypes) {
          cachedSourceOfFundsTypes.set(sourceOfFundsType.code, sourceOfFundsType);
        }

        this.cacheService.set('sourceOfFundsTypes', cachedSourceOfFundsTypes);
        return cachedSourceOfFundsTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the source of funds types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the source of wealth types.
   *
   * @return The source of wealth types.
   */
  getSourceOfWealthTypes(): Observable<Map<string, SourceOfWealthType>> {
    let cachedSourceOfWealthTypes: Map<string, SourceOfWealthType> = this.cacheService.get('sourceOfWealthTypes');

    if (cachedSourceOfWealthTypes !== undefined) {
      return of(cachedSourceOfWealthTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<SourceOfWealthType[]>(this.config.apiUrlPrefix + '/party/reference/source-of-wealth-types',
        {params, reportProgress: true})
      .pipe(map((sourceOfWealthTypes: SourceOfWealthType[]) => {
        cachedSourceOfWealthTypes = new Map<string, SourceOfWealthType>();

        for (const sourceOfWealthType of sourceOfWealthTypes) {
          cachedSourceOfWealthTypes.set(sourceOfWealthType.code, sourceOfWealthType);
        }

        this.cacheService.set('sourceOfWealthTypes', cachedSourceOfWealthTypes);
        return cachedSourceOfWealthTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the source of wealth types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the status type categories.
   *
   * @return The status type categories.
   */
  getStatusTypeCategories(): Observable<Map<string, StatusTypeCategory>> {
    let cachedStatusTypeCategories: Map<string, StatusTypeCategory> = this.cacheService.get('statusTypeCategories');

    if (cachedStatusTypeCategories !== undefined) {
      return of(cachedStatusTypeCategories);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<StatusTypeCategory[]>(this.config.apiUrlPrefix + '/party/reference/status-type-categories',
        {params, reportProgress: true})
      .pipe(map((statusTypeCategories: StatusTypeCategory[]) => {
        cachedStatusTypeCategories = new Map<string, StatusTypeCategory>();

        for (const statusTypeCategory of statusTypeCategories) {
          cachedStatusTypeCategories.set(statusTypeCategory.code, statusTypeCategory);
        }

        this.cacheService.set('statusTypeCategories', cachedStatusTypeCategories);
        return cachedStatusTypeCategories;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the status type categories.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the status types.
   *
   * @return The status types.
   */
  getStatusTypes(): Observable<Map<string, StatusType>> {
    let cachedStatusTypes: Map<string, StatusType> = this.cacheService.get('statusTypes');

    if (cachedStatusTypes !== undefined) {
      return of(cachedStatusTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<StatusType[]>(this.config.apiUrlPrefix + '/party/reference/status-types',
        {params, reportProgress: true})
      .pipe(map((statusTypes: StatusType[]) => {
        cachedStatusTypes = new Map<string, StatusType>();

        for (const statusType of statusTypes) {
          cachedStatusTypes.set(statusType.code, statusType);
        }

        this.cacheService.set('statusTypes', cachedStatusTypes);
        return cachedStatusTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the status types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the tax number types.
   *
   * @return The tax number types.
   */
  getTaxNumberTypes(): Observable<Map<string, TaxNumberType>> {
    let cachedTaxNumberTypes: Map<string, TaxNumberType> = this.cacheService.get('taxNumberTypes');

    if (cachedTaxNumberTypes !== undefined) {
      return of(cachedTaxNumberTypes);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<TaxNumberType[]>(this.config.apiUrlPrefix + '/party/reference/tax-number-types',
        {params, reportProgress: true})
      .pipe(map((taxNumberTypes: TaxNumberType[]) => {
        cachedTaxNumberTypes = new Map<string, TaxNumberType>();

        for (const taxNumberType of taxNumberTypes) {
          cachedTaxNumberTypes.set(taxNumberType.code, taxNumberType);
        }

        this.cacheService.set('taxNumberTypes', cachedTaxNumberTypes);
        return cachedTaxNumberTypes;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the tax number types.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the times to contact.
   *
   * @return The times to contact.
   */
  getTimesToContact(): Observable<Map<string, TimeToContact>> {
    let cachedTimesToContact: Map<string, TimeToContact> = this.cacheService.get('timesToContact');

    if (cachedTimesToContact !== undefined) {
      return of(cachedTimesToContact);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<TimeToContact[]>(this.config.apiUrlPrefix + '/party/reference/times-to-contact',
        {params, reportProgress: true})
      .pipe(map((timesToContact: TimeToContact[]) => {
        cachedTimesToContact = new Map<string, TimeToContact>();

        for (const timeToContact of timesToContact) {
          cachedTimesToContact.set(timeToContact.code, timeToContact);
        }

        this.cacheService.set('timesToContact', cachedTimesToContact);
        return cachedTimesToContact;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the times to contact.', httpErrorResponse));
      }));
    }
  }

  /**
   * Retrieve the titles.
   *
   * @return The titles.
   */
  getTitles(): Observable<Map<string, Title>> {
    let cachedTitles: Map<string, Title> = this.cacheService.get('titles');

    if (cachedTitles !== undefined) {
      return of(cachedTitles);
    } else {
      let params = new HttpParams();

      params = params.append('localeId', this.localeId);

      return this.httpClient.get<Title[]>(this.config.apiUrlPrefix + '/party/reference/titles',
        {params, reportProgress: true})
      .pipe(map((titles: Title[]) => {
        cachedTitles = new Map<string, Title>();

        for (const title of titles) {
          cachedTitles.set(title.code, title);
        }

        this.cacheService.set('titles', cachedTitles);
        return cachedTitles;
      }), catchError((httpErrorResponse: HttpErrorResponse) => {
        if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
          return throwError(() => new AccessDeniedError(httpErrorResponse));
        } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
          return throwError(() => new CommunicationError(httpErrorResponse));
        }

        return throwError(() => new ServiceUnavailableError('Failed to retrieve the titles.', httpErrorResponse));
      }));
    }
  }
}
