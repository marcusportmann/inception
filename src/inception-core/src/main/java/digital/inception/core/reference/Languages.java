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

package digital.inception.core.reference;

import java.util.List;
import java.util.Optional;

/**
 * The <b>Languages</b> class provides static reference data for languages.
 *
 * @author Marcus Portmann
 */
public final class Languages {

  /** The list of languages. */
  private static final List<Language> languages =
      List.of(
          new Language("ZZ", "ZZZ", 0, "Unknown", "Unknown", "Unknown"),
          new Language("AB", "ABK", 1, "Abkhazian", "Abkhazian", "Abkhazian"),
          new Language("AF", "AFR", 1, "Afrikaans", "Afrikaans", "Afrikaans"),
          new Language("AM", "AMH", 1, "Amharic", "Amharic", "Amharic"),
          new Language("AR", "ARA", 1, "Arabic", "Arabic", "Arabic"),
          new Language("HY", "HYE", 1, "Armenian", "Armenian", "Armenian"),
          new Language("AY", "AYM", 1, "Aymara", "Aymara", "Aymara"),
          new Language("AZ", "AZE", 1, "Azerbaijani", "Azerbaijani", "Azerbaijani"),
          new Language("BE", "BEL", 1, "Belarusian", "Belarusian", "Belarusian"),
          new Language("BN", "BEN", 1, "Bengali", "Bengali", "Bengali"),
          new Language("BI", "BIS", 1, "Bislama", "Bislama", "Bislama"),
          new Language("BS", "BOS", 1, "Bosnian", "Bosnian", "Bosnian"),
          new Language("BG", "BUL", 1, "Bulgarian", "Bulgarian", "Bulgarian"),
          new Language("MY", "MYA", 1, "Burmese", "Burmese", "Burmese"),
          new Language("NY", "NYA", 1, "Chewa", "Chewa", "Chichewa, Chewa, Nyanja"),
          new Language("ZH", "ZHO", 1, "Chinese", "Chinese", "Chinese"),
          new Language("HR", "HRV", 1, "Croatian", "Croatian", "Croatian"),
          new Language("CS", "CZE", 1, "Czech", "Czech", "Czech"),
          new Language("DA", "DAN", 1, "Danish", "Danish", "Danish"),
          new Language("NL", "NLD", 1, "Dutch", "Dutch", "Dutch, Flemish"),
          new Language("DZ", "DZO", 1, "Dzongkha", "Dzongkha", "Dzongkha"),
          new Language("EN", "ENG", 1, "English", "English", "English"),
          new Language("ET", "EST", 1, "Estonian", "Estonian", "Estonian"),
          new Language("FJ", "FIJ", 1, "Fijian", "Fijian", "Fijian"),
          new Language("FI", "FIN", 1, "Finnish", "Finnish", "Finnish"),
          new Language("FR", "FRA", 1, "French", "French", "French"),
          new Language("KA", "KAT", 1, "Georgian", "Georgian", "Georgian"),
          new Language("DE", "DEU", 1, "German", "German", "German"),
          new Language("EL", "ELL", 1, "Greek", "Greek", "Greek"),
          new Language("GN", "GRN", 1, "Guarani", "Guarani", "Guarani"),
          new Language(
              "HT", "HAT", 1, "Haitian Creole", "Haitian Creole", "Haitian, Haitian Creole"),
          new Language("HE", "HEB", 1, "Hebrew", "Hebrew", "Hebrew"),
          new Language("HI", "HIN", 1, "Hindi", "Hindi", "Hindi"),
          new Language("HO", "HMO", 1, "Hiri Motu", "Hiri Motu", "Hiri Motu"),
          new Language("HU", "HUN", 1, "Hungarian", "Hungarian", "Hungarian"),
          new Language("IS", "ISL", 1, "Icelandic", "Icelandic", "Icelandic"),
          new Language("ID", "IND", 1, "Indonesian", "Indonesian", "Indonesian"),
          new Language("IT", "ITA", 1, "Italian", "Italian", "Italian"),
          new Language("JA", "JPN", 1, "Japanese", "Japanese", "Japanese"),
          new Language("KK", "KAZ", 1, "Kazakh", "Kazakh", "Kazakh"),
          new Language("KM", "KHM", 1, "Khmer", "Khmer", "Khmer"),
          new Language("RW", "KIN", 1, "Kinyarwanda", "Kinyarwanda", "Kinyarwanda"),
          new Language("KO", "KOR", 1, "Korean", "Korean", "Korean"),
          new Language("KY", "KIR", 1, "Kyrgyz", "Kyrgyz", "Kirghiz, Kyrgyz"),
          new Language("LO", "LAO", 1, "Lao", "Lao", "Lao"),
          new Language("LV", "LAV", 1, "Latvian", "Latvian", "Latvian"),
          new Language("LT", "LIT", 1, "Lithuanian", "Lithuanian", "Lithuanian"),
          new Language(
              "LB", "LTZ", 1, "Luxembourgish", "Luxembourgish", "Luxembourgish, Letzeburgesch"),
          new Language("MK", "MKD", 1, "Macedonian", "Macedonian", "Macedonian"),
          new Language("MG", "MLG", 1, "Malagasy", "Malagasy", "Malagasy"),
          new Language("MS", "MSA", 1, "Malay", "Malay", "Malay"),
          new Language("MT", "MLT", 1, "Maltese", "Maltese", "Maltese"),
          new Language("MI", "MRI", 1, "Maori", "Maori", "Maori"),
          new Language("MR", "MAR", 1, "Marathi", "Marathi", "Marathi"),
          new Language("MN", "MON", 1, "Mongolian", "Mongolian", "Mongolian"),
          new Language(
              "ND", "NDE", 1, "Ndebele", "Ndebele", "Ndebele, Northern Ndebele, North Ndebele"),
          new Language("NE", "NEP", 1, "Nepali", "Nepali", "Nepali"),
          new Language("SE", "SME", 1, "Northern Sami", "Northern Sami", "Northern Sami"),
          new Language("NO", "NOR", 1, "Norwegian", "Norwegian", "Norwegian"),
          new Language("OS", "OSS", 1, "Ossetian", "Ossetian", "Ossetian, Ossetic"),
          new Language("PS", "PUS", 1, "Pashto", "Pashto", "Pushto, Pashto"),
          new Language("PL", "POL", 1, "Polish", "Polish", "Polish"),
          new Language("PT", "POR", 1, "Portuguese", "Portuguese", "Portuguese"),
          new Language("QU", "QUE", 1, "Quechua", "Quechua", "Quechua"),
          new Language("RO", "RON", 1, "Romanian", "Romanian", "Romanian, Moldavian, Moldovan"),
          new Language("RM", "ROH", 1, "Romansh", "Romansh", "Romansh"),
          new Language("RU", "RUS", 1, "Russian", "Russian", "Russian"),
          new Language("SR", "SRP", 1, "Serbian", "Serbian", "Serbian"),
          new Language("SN", "SNA", 1, "Shona", "Shona", "Shona"),
          new Language("SI", "SIN", 1, "Sinhala", "Sinhala", "Sinhala, Sinhalese"),
          new Language("SK", "SLK", 1, "Slovak", "Slovak", "Slovak"),
          new Language("SL", "SLV", 1, "Slovenian", "Slovenian", "Slovenian"),
          new Language("SO", "SQI", 1, "Somali", "Somali", "Somali"),
          new Language("ST", "SOT", 1, "Sotho", "Sotho", "Sotho, Southern Sotho"),
          new Language(
              "NR",
              "NBL",
              1,
              "Southern Ndebele",
              "Southern Ndebele",
              "Southern Ndebele, South Ndebele"),
          new Language("ES", "SPA", 1, "Spanish", "Spanish", "Spanish"),
          new Language("SW", "SWA", 1, "Swahili", "Swahili", "Swahili"),
          new Language("SV", "SWE", 1, "Swedish", "Swedish", "Swedish"),
          new Language("TG", "TGK", 1, "Tajik", "Tajik", "Tajik"),
          new Language("TA", "TAM", 1, "Tamil", "Tamil", "Tamil"),
          new Language("TH", "THA", 1, "Thai", "Thai", "Thai"),
          new Language("TI", "TIR", 1, "Tigrinya", "Tigrinya", "Tigrinya"),
          new Language("TS", "TSO", 1, "Tsonga", "Tsonga", "Tsonga"),
          new Language("TN", "TSN", 1, "Tswana", "Tswana", "Tswana"),
          new Language("TR", "TUR", 1, "Turkish", "Turkish", "Turkish"),
          new Language("TK", "TUK", 1, "Turkmen", "Turkmen", "Turkmen"),
          new Language("UK", "UKR", 1, "Ukrainian", "Ukrainian", "Ukrainian"),
          new Language("UR", "URD", 1, "Urdu", "Urdu", "Urdu"),
          new Language("UZ", "UZB", 1, "Uzbek", "Uzbek", "Uzbek"),
          new Language("VE", "VEN", 1, "Venda", "Venda", "Venda"),
          new Language("VI", "VIE", 1, "Vietnamese", "Vietnamese", "Vietnamese"),
          new Language("XH", "XHO", 1, "Xhosa", "Xhosa", "Xhosa"),
          new Language("ZU", "ZUL", 1, "Zulu", "Zulu", "Zulu"));

  /** Private default constructor to enforce singleton pattern. */
  private Languages() {}

  /**
   * Retrieve the language with the specified ISO 639-1 alpha-2 code.
   *
   * @param code the ISO 639-1 alpha-2 code for the language
   * @return an Optional containing the language with the specified ISO 639-1 alpha-2 code or an
   *     empty Optional if the language could not be found
   */
  public static Optional<Language> getByCode(String code) {
    for (Language language : languages) {
      if (language.getCode().equals(code)) {
        return Optional.of(language);
      }
    }

    return Optional.empty();
  }

  /**
   * Retrieve the language with the specified ISO 639-2 alpha-3 code.
   *
   * @param iso3Code the ISO 639-2 alpha-3 code for the language
   * @return an Optional containing the language with the specified ISO 639-2 alpha-3 code or an
   *     empty Optional if the language could not be found
   */
  public static Optional<Language> getByIso3Code(String iso3Code) {
    for (Language language : languages) {
      if (language.getIso3Code().equals(iso3Code)) {
        return Optional.of(language);
      }
    }

    return Optional.empty();
  }

  /**
   * Retrieve the ISO 639-1 alpha-2 code for the ISO 639-1 alpha-3 code for the country.
   *
   * @param code the ISO 639-1 alpha-3 code for the country
   * @return an Optional containing the ISO 639-1 alpha-2 code for the ISO 639-1 alpha-3 code for
   *     the country or an empty Optional if the country with the specified ISO 639-1 alpha-3 code
   *     could not be found
   */
  public static Optional<String> getCodeForIso3Code(String code) {
    return getByIso3Code(code).map(Language::getCode);
  }

  /**
   * Retrieve the ISO 639-1 alpha-3 code for the ISO 639-1 alpha-2 code for the country.
   *
   * @param code the ISO 639-1 alpha-2 code for the country
   * @return an Optional containing the ISO 639-1 alpha-3 code for the ISO 639-1 alpha-2 code for
   *     the country or an empty Optional if the country with the specified ISO 639-1 alpha-2 code
   *     could not be found
   */
  public static Optional<String> getIso3CodeForCode(String code) {
    return getByCode(code).map(Language::getIso3Code);
  }

  /**
   * Returns the list of languages.
   *
   * @return the list of languages
   */
  public static List<Language> getLanguages() {
    return languages;
  }
}
