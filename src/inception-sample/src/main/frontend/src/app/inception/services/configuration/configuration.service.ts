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

import {Injectable} from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {HttpClient, HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {Configuration} from "./configuration";
import {
  ConfigurationNotFoundError,
  ConfigurationServiceError
} from "./configuration.service.errors";
import {CommunicationError} from "../../errors/communication-error";
import {ApiError} from "../../errors/api-error";
import {I18n} from "@ngx-translate/i18n-polyfill";
import {SystemUnavailableError} from "../../errors/system-unavailable-error";
import {environment} from "../../../../environments/environment";
import {AccessDeniedError} from "../../errors/access-denied-error";

/**
 * The CodesService class provides the Configuration Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class ConfigurationService {






}
