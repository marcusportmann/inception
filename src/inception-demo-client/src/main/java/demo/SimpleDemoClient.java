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

package demo;

import demo.api.client.ApiClient;
import demo.api.client.DataApi;
import demo.api.client.TestApi;
import demo.api.client.model.Data;
import digital.inception.api.client.ApiClientUtil;
import digital.inception.codes.ws.client.CodeCategory;
import digital.inception.codes.ws.client.CodeCategoryNotFoundException;
import digital.inception.codes.ws.client.CodesService;
import digital.inception.codes.ws.client.ICodesService;
import digital.inception.core.api.ProblemDetails;
import digital.inception.core.util.CryptoUtil;
import digital.inception.core.util.ISO8601Util;
import digital.inception.reference.api.client.ReferenceApi;
import digital.inception.reference.api.client.model.Language;
import digital.inception.ws.security.WebServiceClientSecurityHelper;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * The <b>SimpleDemoClient</b> class.
 *
 * @author Marcus Portmann
 */
public class SimpleDemoClient {

  /** The Codes Service endpoint. */
  public static final String CODES_SERVICE_ENDPOINT = "http://localhost:8080/service/CodesService";

  /** The path to the classpath resource containing the WSDL for the Codes Service. */
  public static final String CODES_SERVICE_WSDL = "META-INF/wsdl/CodesService.wsdl";

  /** The path to the classpath resource containing the WSDL for the Mail Service. */
  public static final String MAIL_SERVICE_WSDL = "META-INF/wsdl/MailService.wsdl";

  /** Enable the web services security X509 certificate token profile for the demo client. */
  private static final boolean DEMO_CLIENT_ENABLE_X509_CERTIFICATE_TOKEN_PROFILE = false;

  /** The keystore alias for the demo client. */
  private static final String DEMO_CLIENT_KEYSTORE_ALIAS = "demo-client";

  /** The keystore password for the demo client. */
  private static final String DEMO_CLIENT_KEYSTORE_PASSWORD = "Password1";

  /** The keystore path for the demo client. */
  private static final String DEMO_CLIENT_KEYSTORE_PATH = "META-INF/demo-client.p12";

  /** The keystore type for the demo client. */
  private static final String DEMO_CLIENT_KEYSTORE_TYPE = "pkcs12";

  /**
   * The main method.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    invokeCodesService();

    invokeDemoApi();

    // invokeReferenceApi();

    // invokeTestApi();
  }

  private static void invokeCodesService() {
    try {
      KeyStore keyStore =
          CryptoUtil.loadKeyStore(
              DEMO_CLIENT_KEYSTORE_TYPE,
              DEMO_CLIENT_KEYSTORE_PATH,
              DEMO_CLIENT_KEYSTORE_PASSWORD,
              DEMO_CLIENT_KEYSTORE_ALIAS);

      ICodesService codesService;

      if (DEMO_CLIENT_ENABLE_X509_CERTIFICATE_TOKEN_PROFILE) {
        codesService =
            WebServiceClientSecurityHelper.getWSSecurityX509CertificateServiceProxy(
                CodesService.class,
                ICodesService.class,
                CODES_SERVICE_WSDL,
                CODES_SERVICE_ENDPOINT,
                keyStore,
                DEMO_CLIENT_KEYSTORE_PASSWORD,
                DEMO_CLIENT_KEYSTORE_ALIAS,
                keyStore);
      } else {
        codesService =
            WebServiceClientSecurityHelper.getServiceProxy(
                CodesService.class,
                ICodesService.class,
                CODES_SERVICE_WSDL,
                CODES_SERVICE_ENDPOINT);
      }

      OffsetDateTime lastModified = codesService.getCodeCategoryLastModified("TestCodeCategory01");

      System.out.println("lastModified = " + lastModified);

      List<CodeCategory> codeCategories = codesService.getCodeCategories();

      for (CodeCategory codeCategory : codeCategories) {
        System.out.println("Found code category: " + codeCategory.getName());
      }

      try {
        codesService.getCodeCategory("INVALID_CODE_CATEGORY_ID");

      } catch (CodeCategoryNotFoundException e) {
        System.out.println("Correctly received a CodeCategoryNotFoundException");
      }

    } catch (Throwable e) {
      System.err.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }

  private static void invokeDemoApi() {
    try {
      DataApi dataApi = new DataApi(new ApiClient(ApiClientUtil.createWebClient()));

      Data data = dataApi.getData().block();

      if (data == null) {
        throw new RuntimeException("Data is null");
      }

      if ((data.getBooleanValue() == null) || (!data.getBooleanValue())) {
        throw new RuntimeException(
            "Invalid value for booleanValue (" + data.getBooleanValue() + ")");
      }

      if (!"ZA".equals(data.getCountry())) {
        throw new RuntimeException("Invalid value for country (" + data.getCountry() + ")");
      }

      if ((data.getDateValue() == null)
          || (!LocalDate.parse("1976-03-07").equals(data.getDateValue()))) {
        throw new RuntimeException("Invalid value for dateValue (" + data.getDateValue() + ")");
      }

      if ((data.getDecimalValue() == null)
          || (!(new BigDecimal("111.111")).equals(data.getDecimalValue()))) {
        throw new RuntimeException(
            "Invalid value for decimalValue (" + data.getDecimalValue() + ")");
      }

      if ((data.getDoubleValue() == null)
          || (!Double.valueOf("222.222").equals(data.getDoubleValue()))) {
        throw new RuntimeException("Invalid value for doubleValue (" + data.getDoubleValue() + ")");
      }

      if ((data.getFloatValue() == null)
          || (!Float.valueOf("333.333").equals(data.getFloatValue()))) {
        throw new RuntimeException("Invalid value for floatValue (" + data.getFloatValue() + ")");
      }

      if ((data.getIntegerValue() == null)
          || (!Integer.valueOf("444").equals(data.getIntegerValue()))) {
        throw new RuntimeException(
            "Invalid value for integerValue (" + data.getIntegerValue() + ")");
      }

      if (!"EN".equals(data.getLanguage())) {
        throw new RuntimeException("Invalid value for language (" + data.getLanguage() + ")");
      }

      if (!"This is a valid string value".equals(data.getStringValue())) {
        throw new RuntimeException("Invalid value for stringValue (" + data.getStringValue() + ")");
      }

      if ((data.getTimestampValue() == null)
          || (!ISO8601Util.toOffsetDateTime("2016-07-17T23:56:19.123+02:00")
              .equals(data.getTimestampValue()))) {
        throw new RuntimeException(
            "Invalid value for timestampValue (" + data.getTimestampValue() + ")");
      }

      if ((data.getTimestampWithTimeZoneValue() == null)
          || (!ISO8601Util.toOffsetDateTime("2019-02-28T00:14:27.505+02:00")
              .equals(data.getTimestampWithTimeZoneValue()))) {
        throw new RuntimeException(
            "Invalid value for timestampWithTimeZoneValue ("
                + data.getTimestampWithTimeZoneValue()
                + ")");
      }

      if ((data.getTimeValue() == null)
          || (!ISO8601Util.toLocalTime("14:30:00")
              .equals(ISO8601Util.toLocalTime(data.getTimeValue())))) {
        throw new RuntimeException("Invalid value for timeValue (" + data.getTimeValue() + ")");
      }

      if ((data.getTimeWithTimeZoneValue() == null)
          || (!ISO8601Util.toOffsetTime("18:30:00+02:00")
              .equals(ISO8601Util.toOffsetTime(data.getTimeWithTimeZoneValue())))) {
        throw new RuntimeException(
            "Invalid value for timeWithTimeZoneValue (" + data.getTimeValue() + ")");
      }
    } catch (Throwable e) {
      System.err.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }

  private static void invokeReferenceApi() {
    try {
      ReferenceApi referenceApi = new ReferenceApi();

      List<Language> languages = referenceApi.getLanguages("en-US").collectList().block();

      for (Language language : languages) {
        System.out.println("Found language: " + language.getName());
      }

    } catch (Throwable e) {
      System.err.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }

  private static void invokeTestApi() {
    try {
      TestApi testApi = new TestApi();

      testApi.getApiClient().setBasePath("http://localhost:8080");

      OffsetDateTime offsetDateTime = testApi.testOffsetDateTime(OffsetDateTime.now()).block();

      System.out.println("Found time = " + offsetDateTime);
    } catch (Throwable e) {
      System.err.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.err);
    }

    try {
      TestApi testApi = new TestApi();

      testApi.testExceptionHandling().block();

    } catch (WebClientResponseException e) {
      try {
        var problemDetails = e.getResponseBodyAs(ProblemDetails.class);

        System.out.println("Found problem details type: " + problemDetails.getType());
        System.out.println("Found problem details title: " + problemDetails.getTitle());
        System.out.println("Found problem details detail: " + problemDetails.getDetail());

      } catch (Throwable t) {
        System.err.println(
            "[ERROR] Failed to extract the problem details response: " + t.getMessage());
        t.printStackTrace(System.err);
      }
    } catch (Throwable e) {
      System.err.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }
}
