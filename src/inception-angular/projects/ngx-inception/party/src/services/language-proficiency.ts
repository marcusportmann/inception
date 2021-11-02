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

import {LanguageProficiencyLevel} from "./language-proficiency-level";

/**
 * The LanguageProficiency class holds the information for a language proficiency for a person.
 *
 * @author Marcus Portmann
 */
export class LanguageProficiency {

  /**
   * The ISO 639-1 alpha-2 code for the language.
   */
  language: string;

  /**
   * The listen proficiency level for the language.
   */
  listenLevel: LanguageProficiencyLevel;

  /**
   * The read proficiency level for the language.
   */
  readLevel: LanguageProficiencyLevel;

  /**
   * The speak proficiency level for the language.
   */
  speakLevel: LanguageProficiencyLevel;

  /**
   * The write proficiency level for the language.
   */
  writeLevel: LanguageProficiencyLevel;

  /**
   * Constructs a new LanguageProficiency.
   *
   * @param language    The ISO 639-1 alpha-2 code for the language.
   * @param listenLevel The listen proficiency level for the language.
   * @param readLevel   The read proficiency level for the language.
   * @param speakLevel  The speak proficiency level for the language.
   * @param writeLevel  The write proficiency level for the language.
   */
  constructor(language: string, listenLevel: LanguageProficiencyLevel,
              readLevel: LanguageProficiencyLevel, speakLevel: LanguageProficiencyLevel,
              writeLevel: LanguageProficiencyLevel) {
    this.language = language;
    this.listenLevel = listenLevel;
    this.readLevel = readLevel;
    this.speakLevel = speakLevel;
    this.writeLevel = writeLevel;
  }
}
