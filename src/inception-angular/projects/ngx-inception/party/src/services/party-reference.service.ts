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

      return this.httpClient.get<AssociationPropertyType[]>(this.config.partyReferenceApiUrlPrefix + '/association-property-types',
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

      return this.httpClient.get<AssociationType[]>(this.config.partyReferenceApiUrlPrefix + '/association-types',
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

      return this.httpClient.get<AttributeTypeCategory[]>(this.config.partyReferenceApiUrlPrefix + '/attribute-type-categories',
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

      return this.httpClient.get<AttributeType[]>(this.config.partyReferenceApiUrlPrefix + '/attribute-types',
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

      return this.httpClient.get<ConsentType[]>(this.config.partyReferenceApiUrlPrefix + '/consent-types',
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

      return this.httpClient.get<ContactMechanismPurpose[]>(this.config.partyReferenceApiUrlPrefix + '/contact-mechanism-purposes',
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

      return this.httpClient.get<ContactMechanismRole[]>(this.config.partyReferenceApiUrlPrefix + '/contact-mechanism-roles',
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

      return this.httpClient.get<ContactMechanismType[]>(this.config.partyReferenceApiUrlPrefix + '/contact-mechanism-types',
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

      return this.httpClient.get<EmploymentStatus[]>(this.config.partyReferenceApiUrlPrefix + '/employment-statuses',
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

      return this.httpClient.get<EmploymentType[]>(this.config.partyReferenceApiUrlPrefix + '/employment-types',
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

      return this.httpClient.get<ExternalReferenceType[]>(this.config.partyReferenceApiUrlPrefix + '/external-reference-types',
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

      return this.httpClient.get<FieldOfStudy[]>(this.config.partyReferenceApiUrlPrefix + '/fields-of-study',
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

      return this.httpClient.get<Gender[]>(this.config.partyReferenceApiUrlPrefix + '/genders',
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
  getIdentityDocumentTypes(): Observable<IdentityDocumentType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<IdentityDocumentType[]>(this.config.partyReferenceApiUrlPrefix + '/identity-document-types',
      {params, reportProgress: true})
    .pipe(map((identityDocumentTypes: IdentityDocumentType[]) => {
      return identityDocumentTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the identity document types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the industry classification categories.
   *
   * @return The industry classification categories.
   */
  getIndustryClassificationCategories(): Observable<IndustryClassificationCategory[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<IndustryClassificationCategory[]>(this.config.partyReferenceApiUrlPrefix + '/industry-classification-categories',
      {params, reportProgress: true})
    .pipe(map((industryClassificationCategories: IndustryClassificationCategory[]) => {
      return industryClassificationCategories;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the industry classification categories.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the industry classification systems.
   *
   * @return The industry classification systems.
   */
  getIndustryClassificationSystems(): Observable<IndustryClassificationSystem[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<IndustryClassificationSystem[]>(this.config.partyReferenceApiUrlPrefix + '/industry-classification-systems',
      {params, reportProgress: true})
    .pipe(map((industryClassificationSystems: IndustryClassificationSystem[]) => {
      return industryClassificationSystems;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the industry classification systems.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the industry classifications.
   *
   * @return The industry classifications.
   */
  getIndustryClassifications(): Observable<IndustryClassification[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<IndustryClassification[]>(this.config.partyReferenceApiUrlPrefix + '/industry-classifications',
      {params, reportProgress: true})
    .pipe(map((industryClassifications: IndustryClassification[]) => {
      return industryClassifications;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the industry classifications.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the lock type categories.
   *
   * @return The lock type categories.
   */
  getLockTypeCategories(): Observable<LockTypeCategory[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<LockTypeCategory[]>(this.config.partyReferenceApiUrlPrefix + '/lock-type-categories',
      {params, reportProgress: true})
    .pipe(map((lockTypeCategories: LockTypeCategory[]) => {
      return lockTypeCategories;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the lock type categories.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the lock types.
   *
   * @return The lock types.
   */
  getLockTypes(): Observable<LockType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<LockType[]>(this.config.partyReferenceApiUrlPrefix + '/lock-types',
      {params, reportProgress: true})
    .pipe(map((lockTypes: LockType[]) => {
      return lockTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the lock types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the mandatary roles.
   *
   * @return The mandatary roles.
   */
  getMandataryRoles(): Observable<MandataryRole[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<MandataryRole[]>(this.config.partyReferenceApiUrlPrefix + '/mandatary-types',
      {params, reportProgress: true})
    .pipe(map((mandataryRoles: MandataryRole[]) => {
      return mandataryRoles;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the mandatary roles.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the mandate property types.
   *
   * @return The mandate property types.
   */
  getMandatePropertyTypes(): Observable<MandatePropertyType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<MandatePropertyType[]>(this.config.partyReferenceApiUrlPrefix + '/mandate-property-types',
      {params, reportProgress: true})
    .pipe(map((mandatePropertyTypes: MandatePropertyType[]) => {
      return mandatePropertyTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the mandate property types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the mandate types.
   *
   * @return The mandate types.
   */
  getMandateTypes(): Observable<MandateType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<MandateType[]>(this.config.partyReferenceApiUrlPrefix + '/mandate-types',
      {params, reportProgress: true})
    .pipe(map((mandateTypes: MandateType[]) => {
      return mandateTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the mandate types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the marital statuses.
   *
   * @return The marital statuses.
   */
  getMaritalStatuses(): Observable<MaritalStatus[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<MaritalStatus[]>(this.config.partyReferenceApiUrlPrefix + '/marital-statuses',
      {params, reportProgress: true})
    .pipe(map((maritalStatuses: MaritalStatus[]) => {
      return maritalStatuses;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the marital statuses.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the marriage types.
   *
   * @return The marriage types.
   */
  getMarriageTypes(): Observable<MarriageType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<MarriageType[]>(this.config.partyReferenceApiUrlPrefix + '/marriage-types',
      {params, reportProgress: true})
    .pipe(map((marriageTypes: MarriageType[]) => {
      return marriageTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the marriage types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the next of kin types.
   *
   * @return The next of kin types.
   */
  getNextOfKinTypes(): Observable<NextOfKinType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<NextOfKinType[]>(this.config.partyReferenceApiUrlPrefix + '/next-of-kin-types',
      {params, reportProgress: true})
    .pipe(map((nextOfKinTypes: NextOfKinType[]) => {
      return nextOfKinTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the next of kin types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the occupations.
   *
   * @return The occupations.
   */
  getOccupations(): Observable<Occupation[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<Occupation[]>(this.config.partyReferenceApiUrlPrefix + '/occupations',
      {params, reportProgress: true})
    .pipe(map((occupations: Occupation[]) => {
      return occupations;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the occupations.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the physical address purposes.
   *
   * @return The physical address purposes.
   */
  getPhysicalAddressPurposes(): Observable<PhysicalAddressPurpose[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<PhysicalAddressPurpose[]>(this.config.partyReferenceApiUrlPrefix + '/physical-address-purposes',
      {params, reportProgress: true})
    .pipe(map((physicalAddressPurposes: PhysicalAddressPurpose[]) => {
      return physicalAddressPurposes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the physical address purposes.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the physical address roles.
   *
   * @return The physical address roles.
   */
  getPhysicalAddressRoles(): Observable<PhysicalAddressRole[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<PhysicalAddressRole[]>(this.config.partyReferenceApiUrlPrefix + '/physical-address-roles',
      {params, reportProgress: true})
    .pipe(map((physicalAddressRoles: PhysicalAddressRole[]) => {
      return physicalAddressRoles;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the physical address roles.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the physical address types.
   *
   * @return The physical address types.
   */
  getPhysicalAddressTypes(): Observable<PhysicalAddressType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<PhysicalAddressRole[]>(this.config.partyReferenceApiUrlPrefix + '/physical-address-types',
      {params, reportProgress: true})
    .pipe(map((physicalAddressTypes: PhysicalAddressType[]) => {
      return physicalAddressTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the physical address types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the preference type categories.
   *
   * @return The preference type categories.
   */
  getPreferenceTypeCategories(): Observable<PreferenceTypeCategory[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<PhysicalAddressRole[]>(this.config.partyReferenceApiUrlPrefix + '/preference-type-categories',
      {params, reportProgress: true})
    .pipe(map((preferenceTypeCategories: PreferenceTypeCategory[]) => {
      return preferenceTypeCategories;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the preference type categories.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the preference types.
   *
   * @return The preference types.
   */
  getPreferenceTypes(): Observable<PreferenceType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<PreferenceType[]>(this.config.partyReferenceApiUrlPrefix + '/preference-types',
      {params, reportProgress: true})
    .pipe(map((preferenceTypes: PreferenceType[]) => {
      return preferenceTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the preference types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the qualification types.
   *
   * @return The qualification types.
   */
  getQualificationTypes(): Observable<QualificationType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<QualificationType[]>(this.config.partyReferenceApiUrlPrefix + '/qualification-types',
      {params, reportProgress: true})
    .pipe(map((qualificationTypes: QualificationType[]) => {
      return qualificationTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the qualification types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the races.
   *
   * @return The races.
   */
  getRaces(): Observable<Race[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<Race[]>(this.config.partyReferenceApiUrlPrefix + '/races',
      {params, reportProgress: true})
    .pipe(map((races: Race[]) => {
      return races;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the races.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the residence permit types.
   *
   * @return The residence permit types.
   */
  getResidencePermitTypes(): Observable<ResidencePermitType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<ResidencePermitType[]>(this.config.partyReferenceApiUrlPrefix + '/residence-permit-types',
      {params, reportProgress: true})
    .pipe(map((residencePermitTypes: ResidencePermitType[]) => {
      return residencePermitTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the residence permit types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the residency statuses.
   *
   * @return The residency statuses.
   */
  getResidencyStatuses(): Observable<ResidencyStatus[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<ResidencyStatus[]>(this.config.partyReferenceApiUrlPrefix + '/residency-statuses',
      {params, reportProgress: true})
    .pipe(map((residencyStatuses: ResidencyStatus[]) => {
      return residencyStatuses;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the residency statuses.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the residential types.
   *
   * @return The residential types.
   */
  getResidentialTypes(): Observable<ResidentialType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<ResidentialType[]>(this.config.partyReferenceApiUrlPrefix + '/residential-types',
      {params, reportProgress: true})
    .pipe(map((residentialTypes: ResidentialType[]) => {
      return residentialTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the residential types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the role purposes.
   *
   * @return The role purposes.
   */
  getRolePurposes(): Observable<RolePurpose[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<ResidentialType[]>(this.config.partyReferenceApiUrlPrefix + '/role-purposes',
      {params, reportProgress: true})
    .pipe(map((rolePurposes: RolePurpose[]) => {
      return rolePurposes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the role purposes.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the role type attribute type constraints.
   *
   * @param roleType The code for the role type to retrieve the preference constraints for.
   *
   * @return The role type attribute type constraints.
   */
  getRoleTypeAttributeTypeConstraints(roleType?: string): Observable<RoleTypeAttributeTypeConstraint[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    if (!!roleType) {
      params = params.append('roleType', roleType);
    }

    return this.httpClient.get<RoleTypeAttributeTypeConstraint[]>(this.config.partyReferenceApiUrlPrefix + '/role-type-attribute-type-constraints',
      {params, reportProgress: true})
    .pipe(map((roleTypeAttributeTypeConstraints: RoleTypeAttributeTypeConstraint[]) => {
      return roleTypeAttributeTypeConstraints;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the role type attribute type constraints.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the role type preference type constraints.
   *
   * @param roleType The code for the role type to retrieve the preference constraints for.
   *
   * @return The role type preference type constraints.
   */
  getRoleTypePreferenceTypeConstraints(roleType?: string): Observable<RoleTypePreferenceTypeConstraint[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    if (!!roleType) {
      params = params.append('roleType', roleType);
    }

    return this.httpClient.get<RoleTypePreferenceTypeConstraint[]>(this.config.partyReferenceApiUrlPrefix + '/role-type-preference-type-constraints',
      {params, reportProgress: true})
    .pipe(map((roleTypePreferenceTypeConstraints: RoleTypePreferenceTypeConstraint[]) => {
      return roleTypePreferenceTypeConstraints;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the role type preference type constraints.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the role types.
   *
   * @return The role types.
   */
  getRoleTypes(): Observable<RoleType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<RoleType[]>(this.config.partyReferenceApiUrlPrefix + '/role-types',
      {params, reportProgress: true})
    .pipe(map((roleTypes: RoleType[]) => {
      return roleTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the role types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the segmentation types.
   *
   * @return The segmentation types.
   */
  getSegmentationTypes(): Observable<SegmentationType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<SegmentationType[]>(this.config.partyReferenceApiUrlPrefix + '/segmentation-types',
      {params, reportProgress: true})
    .pipe(map((segmentationTypes: SegmentationType[]) => {
      return segmentationTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the segmentation types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the segments.
   *
   * @return The segments.
   */
  getSegments(): Observable<Segment[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<Segment[]>(this.config.partyReferenceApiUrlPrefix + '/segments',
      {params, reportProgress: true})
    .pipe(map((segments: Segment[]) => {
      return segments;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the segments.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the skill types.
   *
   * @return The skill types.
   */
  getSkillTypes(): Observable<SkillType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<SkillType[]>(this.config.partyReferenceApiUrlPrefix + '/skill-types',
      {params, reportProgress: true})
    .pipe(map((skillTypes: SkillType[]) => {
      return skillTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the skill types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the source of funds types.
   *
   * @return The source of funds types.
   */
  getSourceOfFundsTypes(): Observable<SourceOfFundsType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<SourceOfFundsType[]>(this.config.partyReferenceApiUrlPrefix + '/source-of-funds-types',
      {params, reportProgress: true})
    .pipe(map((sourceOfFundsTypes: SourceOfFundsType[]) => {
      return sourceOfFundsTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the source of funds types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the source of wealth types.
   *
   * @return The source of wealth types.
   */
  getSourceOfWealthTypes(): Observable<SourceOfWealthType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<SourceOfWealthType[]>(this.config.partyReferenceApiUrlPrefix + '/source-of-wealth-types',
      {params, reportProgress: true})
    .pipe(map((sourceOfWealthTypes: SourceOfWealthType[]) => {
      return sourceOfWealthTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the source of wealth types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the status type categories.
   *
   * @return The status type categories.
   */
  getStatusTypeCategories(): Observable<StatusTypeCategory[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<StatusTypeCategory[]>(this.config.partyReferenceApiUrlPrefix + '/status-type-categories',
      {params, reportProgress: true})
    .pipe(map((statusTypeCategories: StatusTypeCategory[]) => {
      return statusTypeCategories;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the status type categories.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the status types.
   *
   * @return The status types.
   */
  getStatusTypes(): Observable<StatusType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<StatusType[]>(this.config.partyReferenceApiUrlPrefix + '/status-types',
      {params, reportProgress: true})
    .pipe(map((statusTypes: StatusType[]) => {
      return statusTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the status types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the tax number types.
   *
   * @return The tax number types.
   */
  getTaxNumberTypes(): Observable<TaxNumberType[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<TaxNumberType[]>(this.config.partyReferenceApiUrlPrefix + '/tax-number-types',
      {params, reportProgress: true})
    .pipe(map((taxNumberTypes: TaxNumberType[]) => {
      return taxNumberTypes;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the tax number types.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the times to contact.
   *
   * @return The times to contact.
   */
  getTimesToContact(): Observable<TimeToContact[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<TimeToContact[]>(this.config.partyReferenceApiUrlPrefix + '/times-to-contact',
      {params, reportProgress: true})
    .pipe(map((timesToContact: TimeToContact[]) => {
      return timesToContact;
    }), catchError((httpErrorResponse: HttpErrorResponse) => {
      if (AccessDeniedError.isAccessDeniedError(httpErrorResponse)) {
        return throwError(() => new AccessDeniedError(httpErrorResponse));
      } else if (CommunicationError.isCommunicationError(httpErrorResponse)) {
        return throwError(() => new CommunicationError(httpErrorResponse));
      }

      return throwError(() => new ServiceUnavailableError('Failed to retrieve the times to contact.', httpErrorResponse));
    }));
  }

  /**
   * Retrieve the titles.
   *
   * @return The titles.
   */
  getTitles(): Observable<Title[]> {
    let params = new HttpParams();

    params = params.append('localeId', this.localeId);

    return this.httpClient.get<Title[]>(this.config.partyReferenceApiUrlPrefix + '/titles',
      {params, reportProgress: true})
    .pipe(map((titles: Title[]) => {
      return titles;
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
