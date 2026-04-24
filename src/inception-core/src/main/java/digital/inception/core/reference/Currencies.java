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
 * The {@code Currencies} class provides static reference data for currencies.
 *
 * @author Marcus Portmann
 */
public final class Currencies {

  /** The list of currencies. */
  private static final List<Currency> currencies =
      List.of(
          new Currency("XXX", "999", 0, "Unknown", "Unknown", "No currency"),
          new Currency("AED", "784", 1, "UAE Dirham", "Dirham", "United Arab Emirates dirham"),
          new Currency("AFN", "971", 1, "Afghani", "Afghani", "Afghan afghani"),
          new Currency("ALL", "008", 1, "Lek", "Lek", "Albanian lek"),
          new Currency("AMD", "051", 1, "Armenian Dram", "Dram", "Armenian dram"),
          new Currency("AOA", "973", 1, "Kwanza", "Kwanza", "Angolan kwanza"),
          new Currency("ARS", "032", 1, "Argentine Peso", "Peso", "Argentine peso"),
          new Currency(
              "AUD", "036", 1, "Australian Dollar", "Australian Dollar", "Australian dollar"),
          new Currency("AWG", "533", 1, "Aruban Florin", "Florin", "Aruban florin"),
          new Currency("AZN", "944", 1, "Azerbaijan Manat", "Manat", "Azerbaijani manat"),
          new Currency(
              "BAM",
              "977",
              1,
              "Convertible Mark",
              "Mark",
              "Bosnia and Herzegovina convertible mark"),
          new Currency("BBD", "052", 1, "Barbados Dollar", "Barbados Dollar", "Barbadian dollar"),
          new Currency("BDT", "050", 1, "Taka", "Taka", "Bangladeshi taka"),
          new Currency("BGN", "975", 1, "Bulgarian Lev", "Lev", "Bulgarian lev"),
          new Currency("BHD", "048", 1, "Bahraini Dinar", "Dinar", "Bahraini dinar"),
          new Currency("BIF", "108", 1, "Burundi Franc", "Franc", "Burundian franc"),
          new Currency("BMD", "060", 1, "Bermudian Dollar", "Bermuda Dollar", "Bermudian dollar"),
          new Currency("BND", "096", 1, "Brunei Dollar", "Brunei Dollar", "Brunei dollar"),
          new Currency("BOB", "068", 1, "Boliviano", "Boliviano", "Bolivian boliviano"),
          new Currency("BOV", "984", 1, "Mvdol", "Mvdol", "Bolivian Mvdol"),
          new Currency("BRL", "986", 1, "Brazilian Real", "Real", "Brazilian real"),
          new Currency("BSD", "044", 1, "Bahamian Dollar", "Bahamian Dollar", "Bahamian dollar"),
          new Currency("BTN", "064", 1, "Ngultrum", "Ngultrum", "Bhutanese ngultrum"),
          new Currency("BWP", "072", 1, "Pula", "Pula", "Botswana pula"),
          new Currency("BYN", "933", 1, "Belarusian Ruble", "Ruble", "Belarusian ruble"),
          new Currency("BZD", "084", 1, "Belize Dollar", "Belize Dollar", "Belize dollar"),
          new Currency("CAD", "124", 1, "Canadian Dollar", "Canadian Dollar", "Canadian dollar"),
          new Currency("CDF", "976", 1, "Congolese Franc", "Franc", "Congolese franc"),
          new Currency("CHE", "947", 1, "WIR Euro", "WIR Euro", "WIR euro"),
          new Currency("CHF", "756", 1, "Swiss Franc", "Swiss Franc", "Swiss franc"),
          new Currency("CHW", "948", 1, "WIR Franc", "WIR Franc", "WIR franc"),
          new Currency("CLF", "990", 1, "Unidad de Fomento", "UF", "Chilean unidad de fomento"),
          new Currency("CLP", "152", 1, "Chilean Peso", "Peso", "Chilean peso"),
          new Currency("CNY", "156", 1, "Yuan Renminbi", "Yuan", "Chinese yuan renminbi"),
          new Currency("COP", "170", 1, "Colombian Peso", "Peso", "Colombian peso"),
          new Currency(
              "COU", "970", 1, "Unidad de Valor Real", "UVR", "Colombian unidad de valor real"),
          new Currency("CRC", "188", 1, "Costa Rican Colon", "Colon", "Costa Rican colon"),
          new Currency("CUP", "192", 1, "Cuban Peso", "Peso", "Cuban peso"),
          new Currency("CVE", "132", 1, "Cabo Verde Escudo", "Escudo", "Cape Verde escudo"),
          new Currency("CZK", "203", 1, "Czech Koruna", "Koruna", "Czech koruna"),
          new Currency("DJF", "262", 1, "Djibouti Franc", "Franc", "Djiboutian franc"),
          new Currency("DKK", "208", 1, "Danish Krone", "Krone", "Danish krone"),
          new Currency("DOP", "214", 1, "Dominican Peso", "Peso", "Dominican peso"),
          new Currency("DZD", "012", 1, "Algerian Dinar", "Dinar", "Algerian dinar"),
          new Currency("EGP", "818", 1, "Egyptian Pound", "Pound", "Egyptian pound"),
          new Currency("ERN", "232", 1, "Nakfa", "Nakfa", "Eritrean nakfa"),
          new Currency("ETB", "230", 1, "Ethiopian Birr", "Birr", "Ethiopian birr"),
          new Currency("EUR", "978", 1, "Euro", "Euro", "Euro"),
          new Currency("FJD", "242", 1, "Fiji Dollar", "Fiji Dollar", "Fijian dollar"),
          new Currency(
              "FKP", "238", 1, "Falkland Islands Pound", "Pound", "Falkland Islands pound"),
          new Currency("GBP", "826", 1, "Pound Sterling", "Pound", "Pound sterling"),
          new Currency("GEL", "981", 1, "Lari", "Lari", "Georgian lari"),
          new Currency("GHS", "936", 1, "Ghana Cedi", "Cedi", "Ghanaian cedi"),
          new Currency("GIP", "292", 1, "Gibraltar Pound", "Pound", "Gibraltar pound"),
          new Currency("GMD", "270", 1, "Dalasi", "Dalasi", "Gambian dalasi"),
          new Currency("GNF", "324", 1, "Guinean Franc", "Franc", "Guinean franc"),
          new Currency("GTQ", "320", 1, "Quetzal", "Quetzal", "Guatemalan quetzal"),
          new Currency("GYD", "328", 1, "Guyana Dollar", "Guyana Dollar", "Guyanese dollar"),
          new Currency("HKD", "344", 1, "Hong Kong Dollar", "HK Dollar", "Hong Kong dollar"),
          new Currency("HNL", "340", 1, "Lempira", "Lempira", "Honduran lempira"),
          new Currency("HTG", "332", 1, "Gourde", "Gourde", "Haitian gourde"),
          new Currency("HUF", "348", 1, "Forint", "Forint", "Hungarian forint"),
          new Currency("IDR", "360", 1, "Rupiah", "Rupiah", "Indonesian rupiah"),
          new Currency("ILS", "376", 1, "New Israeli Shekel", "Shekel", "Israeli new shekel"),
          new Currency("INR", "356", 1, "Indian Rupee", "Rupee", "Indian rupee"),
          new Currency("IQD", "368", 1, "Iraqi Dinar", "Dinar", "Iraqi dinar"),
          new Currency("IRR", "364", 1, "Iranian Rial", "Rial", "Iranian rial"),
          new Currency("ISK", "352", 1, "Iceland Krona", "Krona", "Icelandic króna"),
          new Currency("JMD", "388", 1, "Jamaican Dollar", "Jamaica Dollar", "Jamaican dollar"),
          new Currency("JOD", "400", 1, "Jordanian Dinar", "Dinar", "Jordanian dinar"),
          new Currency("JPY", "392", 1, "Yen", "Yen", "Japanese yen"),
          new Currency("KES", "404", 1, "Kenyan Shilling", "Shilling", "Kenyan shilling"),
          new Currency("KGS", "417", 1, "Som", "Som", "Kyrgyzstani som"),
          new Currency("KHR", "116", 1, "Riel", "Riel", "Cambodian riel"),
          new Currency("KMF", "174", 1, "Comorian Franc", "Franc", "Comorian franc"),
          new Currency("KPW", "408", 1, "North Korean Won", "Won", "North Korean won"),
          new Currency("KRW", "410", 1, "Won", "Won", "South Korean won"),
          new Currency("KWD", "414", 1, "Kuwaiti Dinar", "Dinar", "Kuwaiti dinar"),
          new Currency(
              "KYD", "136", 1, "Cayman Islands Dollar", "Cayman Dollar", "Cayman Islands dollar"),
          new Currency("KZT", "398", 1, "Tenge", "Tenge", "Kazakhstani tenge"),
          new Currency("LAK", "418", 1, "Lao Kip", "Kip", "Lao kip"),
          new Currency("LBP", "422", 1, "Lebanese Pound", "Pound", "Lebanese pound"),
          new Currency("LKR", "144", 1, "Sri Lanka Rupee", "Rupee", "Sri Lankan rupee"),
          new Currency("LRD", "430", 1, "Liberian Dollar", "Liberia Dollar", "Liberian dollar"),
          new Currency("LSL", "426", 1, "Loti", "Loti", "Lesotho loti"),
          new Currency("LYD", "434", 1, "Libyan Dinar", "Dinar", "Libyan dinar"),
          new Currency("MAD", "504", 1, "Moroccan Dirham", "Dirham", "Moroccan dirham"),
          new Currency("MDL", "498", 1, "Moldovan Leu", "Leu", "Moldovan leu"),
          new Currency("MGA", "969", 1, "Malagasy Ariary", "Ariary", "Malagasy ariary"),
          new Currency("MKD", "807", 1, "Denar", "Denar", "Macedonian denar"),
          new Currency("MMK", "104", 1, "Kyat", "Kyat", "Myanmar kyat"),
          new Currency("MNT", "496", 1, "Tugrik", "Tugrik", "Mongolian tögrög"),
          new Currency("MOP", "446", 1, "Pataca", "Pataca", "Macanese pataca"),
          new Currency("MRU", "929", 1, "Ouguiya", "Ouguiya", "Mauritanian ouguiya"),
          new Currency("MUR", "480", 1, "Mauritius Rupee", "Rupee", "Mauritian rupee"),
          new Currency("MVR", "462", 1, "Rufiyaa", "Rufiyaa", "Maldivian rufiyaa"),
          new Currency("MWK", "454", 1, "Malawi Kwacha", "Kwacha", "Malawian kwacha"),
          new Currency("MXN", "484", 1, "Mexican Peso", "Peso", "Mexican peso"),
          new Currency(
              "MXV", "979", 1, "Mexican Unidad de Inversion", "UDI", "Mexican unidad de inversion"),
          new Currency("MYR", "458", 1, "Malaysian Ringgit", "Ringgit", "Malaysian ringgit"),
          new Currency("MZN", "943", 1, "Mozambique Metical", "Metical", "Mozambican metical"),
          new Currency("NAD", "516", 1, "Namibia Dollar", "Namibia Dollar", "Namibian dollar"),
          new Currency("NGN", "566", 1, "Naira", "Naira", "Nigerian naira"),
          new Currency("NIO", "558", 1, "Cordoba Oro", "Cordoba", "Nicaraguan córdoba"),
          new Currency("NOK", "578", 1, "Norwegian Krone", "Krone", "Norwegian krone"),
          new Currency("NPR", "524", 1, "Nepalese Rupee", "Rupee", "Nepalese rupee"),
          new Currency("NZD", "554", 1, "New Zealand Dollar", "NZ Dollar", "New Zealand dollar"),
          new Currency("OMR", "512", 1, "Rial Omani", "Rial", "Omani rial"),
          new Currency("PAB", "590", 1, "Balboa", "Balboa", "Panamanian balboa"),
          new Currency("PEN", "604", 1, "Sol", "Sol", "Peruvian sol"),
          new Currency("PGK", "598", 1, "Kina", "Kina", "Papua New Guinean kina"),
          new Currency("PHP", "608", 1, "Philippine Peso", "Peso", "Philippine peso"),
          new Currency("PKR", "586", 1, "Pakistan Rupee", "Rupee", "Pakistani rupee"),
          new Currency("PLN", "985", 1, "Zloty", "Zloty", "Polish złoty"),
          new Currency("PYG", "600", 1, "Guarani", "Guarani", "Paraguayan guaraní"),
          new Currency("QAR", "634", 1, "Qatari Rial", "Rial", "Qatari rial"),
          new Currency("RON", "946", 1, "Romanian Leu", "Leu", "Romanian leu"),
          new Currency("RSD", "941", 1, "Serbian Dinar", "Dinar", "Serbian dinar"),
          new Currency("RUB", "643", 1, "Russian Ruble", "Ruble", "Russian ruble"),
          new Currency("RWF", "646", 1, "Rwanda Franc", "Franc", "Rwandan franc"),
          new Currency("SAR", "682", 1, "Saudi Riyal", "Riyal", "Saudi riyal"),
          new Currency(
              "SBD",
              "090",
              1,
              "Solomon Islands Dollar",
              "Solomon Dollar",
              "Solomon Islands dollar"),
          new Currency("SCR", "690", 1, "Seychelles Rupee", "Rupee", "Seychellois rupee"),
          new Currency("SDG", "938", 1, "Sudanese Pound", "Pound", "Sudanese pound"),
          new Currency("SEK", "752", 1, "Swedish Krona", "Krona", "Swedish krona"),
          new Currency("SGD", "702", 1, "Singapore Dollar", "Singapore Dollar", "Singapore dollar"),
          new Currency("SHP", "654", 1, "Saint Helena Pound", "Pound", "Saint Helena pound"),
          new Currency("SLE", "925", 1, "Leone", "Leone", "Sierra Leonean leone"),
          new Currency("SOS", "706", 1, "Somali Shilling", "Shilling", "Somali shilling"),
          new Currency("SRD", "968", 1, "Surinam Dollar", "Surinam Dollar", "Surinamese dollar"),
          new Currency("SSP", "728", 1, "South Sudanese Pound", "Pound", "South Sudanese pound"),
          new Currency("STN", "930", 1, "Dobra", "Dobra", "São Tomé and Príncipe dobra"),
          new Currency("SVC", "222", 1, "El Salvador Colon", "Colon", "Salvadoran colón"),
          new Currency("SYP", "760", 1, "Syrian Pound", "Pound", "Syrian pound"),
          new Currency("SZL", "748", 1, "Lilangeni", "Lilangeni", "Swazi lilangeni"),
          new Currency("THB", "764", 1, "Baht", "Baht", "Thai baht"),
          new Currency("TJS", "972", 1, "Somoni", "Somoni", "Tajikistani somoni"),
          new Currency("TMT", "934", 1, "Turkmenistan New Manat", "Manat", "Turkmen manat"),
          new Currency("TND", "788", 1, "Tunisian Dinar", "Dinar", "Tunisian dinar"),
          new Currency("TOP", "776", 1, "Pa'anga", "Pa'anga", "Tongan paʻanga"),
          new Currency("TRY", "949", 1, "Turkish Lira", "Lira", "Turkish lira"),
          new Currency(
              "TTD",
              "780",
              1,
              "Trinidad and Tobago Dollar",
              "TT Dollar",
              "Trinidad and Tobago dollar"),
          new Currency("TWD", "901", 1, "New Taiwan Dollar", "NT Dollar", "New Taiwan dollar"),
          new Currency("TZS", "834", 1, "Tanzanian Shilling", "Shilling", "Tanzanian shilling"),
          new Currency("UAH", "980", 1, "Hryvnia", "Hryvnia", "Ukrainian hryvnia"),
          new Currency("UGX", "800", 1, "Uganda Shilling", "Shilling", "Ugandan shilling"),
          new Currency("USD", "840", 1, "US Dollar", "Dollar", "United States dollar"),
          new Currency(
              "USN", "997", 1, "US Dollar (Next day)", "USN", "United States dollar (next day)"),
          new Currency(
              "UYI", "940", 1, "Uruguay Peso en Unidades Indexadas", "UYI", "Uruguay indexed unit"),
          new Currency("UYU", "858", 1, "Peso Uruguayo", "Peso", "Uruguayan peso"),
          new Currency(
              "UYW", "927", 1, "Unidad Previsional", "UYW", "Uruguayan nominal wage index unit"),
          new Currency("UZS", "860", 1, "Uzbekistan Sum", "Sum", "Uzbekistani soʻm"),
          new Currency(
              "VED", "926", 1, "Bolívar Soberano", "Bolívar", "Venezuelan bolívar soberano"),
          new Currency(
              "VES", "928", 1, "Bolívar Soberano", "Bolívar", "Venezuelan bolívar soberano"),
          new Currency("VND", "704", 1, "Dong", "Dong", "Vietnamese đồng"),
          new Currency("VUV", "548", 1, "Vatu", "Vatu", "Vanuatu vatu"),
          new Currency("WST", "882", 1, "Tala", "Tala", "Samoan tala"),
          new Currency("XAF", "950", 1, "CFA Franc BEAC", "CFA Franc", "CFA franc BEAC"),
          new Currency(
              "XCD", "951", 1, "East Caribbean Dollar", "EC Dollar", "East Caribbean dollar"),
          new Currency(
              "XCG", "532", 1, "Caribbean Guilder", "Caribbean Guilder", "Caribbean guilder"),
          new Currency("XOF", "952", 1, "CFA Franc BCEAO", "CFA Franc", "CFA franc BCEAO"),
          new Currency("XPF", "953", 1, "CFP Franc", "CFP Franc", "CFP franc"),
          new Currency("YER", "886", 1, "Yemeni Rial", "Rial", "Yemeni rial"),
          new Currency("ZAR", "710", 1, "Rand", "Rand", "South African rand"),
          new Currency("ZMW", "967", 1, "Zambian Kwacha", "Kwacha", "Zambian kwacha"),
          new Currency("ZWG", "924", 1, "Zimbabwe Gold", "Zimbabwe Gold", "Zimbabwe Gold"));

  /** Private default constructor to enforce the singleton pattern. */
  private Currencies() {}

  /**
   * Retrieve the currency with the specified ISO 4217 alphabetic code.
   *
   * @param code the ISO 4217 alphabetic code for the currency
   * @return an {@link Optional} containing the currency with the specified ISO 4217 alphabetic code
   *     or an empty {@link Optional} if the currency could not be found
   */
  public static Optional<Currency> getByCode(String code) {
    for (Currency currency : currencies) {
      if (currency.getCode().equals(code)) {
        return Optional.of(currency);
      }
    }

    return Optional.empty();
  }

  /**
   * Retrieve the currency with the specified ISO 4217 numeric code.
   *
   * @param numericCode the ISO 4217 numeric code for the currency
   * @return an {@link Optional} containing the currency with the specified ISO 4217 numeric code or
   *     an empty {@link Optional} if the currency could not be found
   */
  public static Optional<Currency> getByNumericCode(String numericCode) {
    for (Currency currency : currencies) {
      if (currency.getNumericCode().equals(numericCode)) {
        return Optional.of(currency);
      }
    }

    return Optional.empty();
  }

  /**
   * Retrieve the ISO 4217 alphabetic code for the ISO 4217 numeric code for the currency.
   *
   * @param code the ISO 4217 numeric code for the currency
   * @return an {@link Optional} containing the ISO 4217 alphabetic code for the ISO 4217 numeric
   *     code for the currency or an empty {@link Optional} if the currency with the specified ISO
   *     4217 numeric code could not be found
   */
  public static Optional<String> getCodeForNumericCode(String code) {
    return getByNumericCode(code).map(Currency::getCode);
  }

  /**
   * Returns the list of currencies.
   *
   * @return the list of currencies
   */
  public static List<Currency> getCurrencies() {
    return currencies;
  }

  /**
   * Retrieve the ISO 4217 numeric code for the ISO 4217 alphabetic code for the currency.
   *
   * @param code the ISO 4217 alphabetic code for the currency
   * @return an {@link Optional} containing the ISO 4217 numeric code for the ISO 4217 alphabetic
   *     code for the currency or an empty {@link Optional} if the currency with the specified ISO
   *     4217 alphabetic code could not be found
   */
  public static Optional<String> getNumericCodeForCode(String code) {
    return getByCode(code).map(Currency::getNumericCode);
  }
}
