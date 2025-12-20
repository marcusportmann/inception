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

import { map } from 'rxjs/operators';
import { ISO8601Util } from './iso-8601-util';

const ISO8601_REGEX = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(?:\.\d+)?(?:Z|[+-]\d{2}:\d{2})?$/;

function tryParseIso8601Date(value: string): Date | null {
  const trimmed = value.trim();

  if (!ISO8601_REGEX.test(trimmed)) {
    return null;
  }

  const parsedDate: Date = ISO8601Util.toDate(trimmed);
  return isNaN(parsedDate.getTime()) ? null : parsedDate;
}

export function convertStringValuesToTypes<T>(value: T): T {
  // Leave null/undefined as-is
  if (value == null) {
    return value;
  }

  // String → possibly Date
  if (typeof value === 'string') {
    const asDate = tryParseIso8601Date(value);
    return (asDate ?? value) as unknown as T;
  }

  // Arrays → recursively map, return the new array (no mutation)
  if (Array.isArray(value)) {
    return value.map((item) => convertStringValuesToTypes(item)) as unknown as T;
  }

  // Plain objects → recursively convert properties, no mutation of the original
  if (typeof value === 'object') {
    const obj = value as Record<string, unknown>;
    const result: Record<string, unknown> = {};

    for (const [key, val] of Object.entries(obj)) {
      result[key] = convertStringValuesToTypes(val);
    }

    return result as unknown as T;
  }

  // Other primitive types (number, boolean, etc.) stay as-is
  return value;
}

interface Pipeable {
  pipe: (...args: unknown[]) => unknown;
}

function hasPipe(value: unknown): value is Pipeable {
  return !!value && typeof (value as Pipeable).pipe === 'function';
}

export function ResponseConverter(
  _target: unknown,
  _propertyKey: string,
  descriptor: PropertyDescriptor
): PropertyDescriptor {
  const originalMethod = descriptor.value;

  if (typeof originalMethod !== 'function') {
    throw new globalThis.Error('ResponseConverter can only be applied to methods.');
  }

  descriptor.value = function (...args: unknown[]) {
    const result = originalMethod.apply(this, args);

    if (hasPipe(result)) {
      return result.pipe(map((data: unknown) => convertStringValuesToTypes(data)));
    }

    return convertStringValuesToTypes(result);
  };

  return descriptor;
}
