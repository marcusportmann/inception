/*
 * Copyright 2021 Marcus Portmann
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

/**
 * The MeasurementUnit enumeration defines the possible measurement units.
 *
 * @author Marcus Portmann
 */
export enum MeasurementUnit {

  /**
   * Metric Centimeter.
   */
  MetricCentimeter = 'metric_centimeter',

  /**
   * Metric Meter.
   */
  MetricMeter = 'metric_meter',

  /**
   * Metric Kilogram.
   */
  MetricKilogram = 'metric_kilogram',

  /**
   * Imperial Inch.
   */
  ImperialInch = 'imperial_inch',

  /**
   * Imperial Foot.
   */
  ImperialFoot = 'imperial_foot',

  /**
   * Imperial Pound.
   */
  ImperialPound = 'imperial_pound',

  /**
   * Customary Inch.
   */
  CustomaryInch = 'customary_inch',

  /**
   * Customary Foot.
   */
  CustomaryFoot = 'customary_foot',

  /**
   * CustomaryPound.
   */
  CustomaryPound = 'customary_pound'
}

