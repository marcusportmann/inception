

package digital.inception.reference;

//~--- non-JDK imports --------------------------------------------------------


public enum Language
{
  ENGLISH("EN", "English"),
  AFRIKAANS("AF", "Afrikaans"),
  NDEBELE_NORTH("ND", "Ndebele, North"),
  NDEBELE_SOUTH("NR", "Ndebele, South"),
  SOTHO_NORTHERN("N6", "Sotho, Northern"),
  SOTHO_SOUTHERN("ST", "Sotho, Southern"),
  TSONGA("TS", "Tsonga"),
  TSWANA("TN", "Tswana"),
  VENDA("VE", "Venda"),
  XHOSA("XH", "Xhosa"),
  ZULU("ZU", "Zulu"),
  SWAZI("Z2", "Swazi"),
  ARABIC("AR", "Arabic"),
  BULGARIAN("BG", "Bulgarian"),
  CATALAN("CA", "Catalan"),
  CZECH("CS", "Czech"),
  DANISH("DA", "Danish"),
  GERMAN("DE", "German"),
  GREEK("EL", "Greek"),
  SPANISH("ES", "Spanish"),
  ESTONIAN("ET", "Estonian"),
  FINNISH("FI", "Finnish"),
  FRENCH("FR", "French"),
  HEBREW("HE", "Hebrew"),
  HINDI("HI", "Hindi"),
  CROATIAN("HR", "Croatian"),
  HUNGARIAN("HU", "Hungarian"),
  INDONESIAN("ID", "Indonesian"),
  ICELANDIC("IS", "Icelandic"),
  ITALIAN("IT", "Italian"),
  JAPANESE("JA", "Japanese"),
  KAZAKH("KK", "Kazakh"),
  KOREAN("KO", "Korean"),
  LITHUANIAN("LT", "Lithuanian"),
  LATVIAN("LV", "Latvian"),
  MALAY("MS", "Malay"),
  DUTCH("NL", "Dutch"),
  NORWEGIAN("NO", "Norwegian"),
  POLISH("PL", "Polish"),
  PORTUGUESE("PT", "Portuguese"),
  ROMANIAN("RO", "Romanian"),
  RUSSIAN("RU", "Russian"),
  SERBIAN_LATIN("SH", "Serbian (Latin)"),
  SLOVAK("SK", "Slovak"),
  SLOVENIAN("SL", "Slovenian"),
  SERBIAN("SR", "Serbian"),
  SWEDISH("SV", "Swedish"),
  THAI("TH", "Thai"),
  TURKISH("TR", "Turkish"),
  UKRAINIAN("UK", "Ukrainian"),
  VIETNAMESE("VI", "Vietnamese"),
  CHINESE_SIMPLIFIED("ZF", "Chinese Simplified"),
  CHINESE("ZH", "Chinese");

  /**
   * All the languages.
   */
  public static final Language[] ALL_LANGUAGES =
      {
          ENGLISH, AFRIKAANS, NDEBELE_NORTH, NDEBELE_SOUTH, SOTHO_NORTHERN, SOTHO_SOUTHERN, TSONGA,
          TSWANA, VENDA, XHOSA, ZULU, SWAZI, ARABIC, BULGARIAN, CATALAN, CZECH, DANISH, GERMAN, GREEK,
          SPANISH, ESTONIAN, FINNISH, FRENCH, HEBREW, HINDI, CROATIAN, HUNGARIAN, INDONESIAN, ICELANDIC,
          ITALIAN, JAPANESE, KAZAKH, KOREAN, LITHUANIAN, LATVIAN, MALAY, DUTCH, NORWEGIAN, POLISH,
          PORTUGUESE, ROMANIAN, RUSSIAN, SERBIAN_LATIN, SLOVAK, SLOVENIAN, SERBIAN, SWEDISH, THAI,
          TURKISH, UKRAINIAN, VIETNAMESE, CHINESE_SIMPLIFIED, CHINESE
      };

  /**
   * The regular expression used to validate a code identifying a language.
   */
  public static final String PATTERN = "[A-Z0-9][A-Z0-9]";

  /**
   * The description for the language.
   */
  private String description;

  /**
   * The code for the language.
   */
  private String code;

  Language(String code, String description)
  {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the language given by the specified code value.
   *
   * @param code the code value identifying the language
   *
   * @return the language given by the specified code value
   */
  public static Language fromCode(String code)
  {
    switch (code)
    {
      case "EN":
        return Language.ENGLISH;

      case "AF":
        return Language.AFRIKAANS;

      case "ND":
        return Language.NDEBELE_NORTH;

      case "NR":
        return Language.NDEBELE_SOUTH;

      case "N6":
        return Language.SOTHO_NORTHERN;

      case "ST":
        return Language.SOTHO_SOUTHERN;

      case "TS":
        return Language.TSONGA;

      case "TN":
        return Language.TSWANA;

      case "VE":
        return Language.VENDA;

      case "XH":
        return Language.XHOSA;

      case "ZU":
        return Language.ZULU;

      case "Z2":
        return Language.SWAZI;

      case "AR":
        return Language.ARABIC;

      case "BG":
        return Language.BULGARIAN;

      case "CA":
        return Language.CATALAN;

      case "CS":
        return Language.CZECH;

      case "DA":
        return Language.DANISH;

      case "DE":
        return Language.GERMAN;

      case "EL":
        return Language.GREEK;

      case "ES":
        return Language.SPANISH;

      case "ET":
        return Language.ESTONIAN;

      case "FI":
        return Language.FINNISH;

      case "FR":
        return Language.FRENCH;

      case "HE":
        return Language.HEBREW;

      case "HI":
        return Language.HINDI;

      case "HR":
        return Language.CROATIAN;

      case "HU":
        return Language.HUNGARIAN;

      case "ID":
        return Language.INDONESIAN;

      case "IS":
        return Language.ICELANDIC;

      case "IT":
        return Language.ITALIAN;

      case "JA":
        return Language.JAPANESE;

      case "KK":
        return Language.KAZAKH;

      case "KO":
        return Language.KOREAN;

      case "LT":
        return Language.LITHUANIAN;

      case "LV":
        return Language.LATVIAN;

      case "MS":
        return Language.MALAY;

      case "NL":
        return Language.DUTCH;

      case "NO":
        return Language.NORWEGIAN;

      case "PL":
        return Language.POLISH;

      case "PT":
        return Language.PORTUGUESE;

      case "RO":
        return Language.ROMANIAN;

      case "RU":
        return Language.RUSSIAN;

      case "SH":
        return Language.SERBIAN_LATIN;

      case "SK":
        return Language.SLOVAK;

      case "SL":
        return Language.SLOVENIAN;

      case "SR":
        return Language.SERBIAN;

      case "SV":
        return Language.SWEDISH;

      case "TH":
        return Language.THAI;

      case "TR":
        return Language.TURKISH;

      case "UK":
        return Language.UKRAINIAN;

      case "VI":
        return Language.VIETNAMESE;


      case "ZF":
        return Language.CHINESE_SIMPLIFIED;

      case "ZH":
        return Language.CHINESE;

      default:
        throw new RuntimeException("Failed to determine the language with the invalid code ("
            + code + ")");
    }
  }

  /**
   * Returns the code for the language.
   *
   * @return the code for the language
   */
  public String code()
  {
    return code;
  }

  /**
   * Returns the description for the language.
   *
   * @return the description for the language
   */
  public String description()
  {
    return description;
  }
}
