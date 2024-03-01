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

package digital.inception.indexing.test;

import jakarta.mail.Address;
import jakarta.mail.BodyPart;
import jakarta.mail.Flags.Flag;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMultipart;
import java.util.Properties;
import org.springframework.util.StringUtils;

/**
 * The <b>IMAPTest</b> class.
 */
public class IMAPTest {
  public static void main(String[] args) {
    try {
      String host = "localhost";
      String username = "test";
      String password = "Password1";

      Properties props = new Properties();
      //props.setProperty("mail.imap.ssl.enable", "true");

      Session session = Session.getInstance(props);
      Store store = session.getStore("imap");
      store.connect(host, username, password);

      Folder inbox = store.getFolder("INBOX");
      inbox.open(Folder.READ_ONLY);
      Message[] messages = inbox.getMessages();

      for (Message message : messages) {

        if (message.isSet(Flag.SEEN)) {
          System.out.println("[DEBUG] Seen Message Subject: " + message.getSubject());


          // NOTE: IF a message cannot be processed it can be marked a not being seen, so it can be
          //       processed later.
          message.setFlag(Flag.SEEN, false);
        } else {
          System.out.println("[DEBUG] Subject: " + message.getSubject());
          System.out.println("[DEBUG] Content Type: " + message.getContentType());
          System.out.println("[DEBUG] Content Class: " + message.getContent().getClass().getName());

          System.out.println("[DEBUG] Content Class: " + message.getFrom());

          for (Address fromAddress : message.getFrom()) {
            if (fromAddress instanceof InternetAddress) {
              InternetAddress fromInternetAddress = (InternetAddress) fromAddress;

              // NOTE: We should explicitly check for and refuse to process group addresses
              if  (fromInternetAddress.isGroup()) {
                throw new RuntimeException("Group addresses are not supported (" + fromInternetAddress.toString() + ")");
              }

              if (!StringUtils.hasText(fromInternetAddress.getPersonal())) {
                int zzz = 0;
                zzz++;
              }


              System.out.println("[DEBUG] From Address: " + fromInternetAddress.getAddress() + " (" + (StringUtils.hasText(fromInternetAddress.getPersonal()) ? fromInternetAddress.getPersonal().replaceAll("^\"|\"$", "") : "") + ")");
            } else {
              // NOTE: We should reject non-Internet addresses
              throw new RuntimeException("Non-Internet addresses are not supported (" + fromAddress.getClass().getName() + ")");
            }
          }

          for (Address recipientAddress : message.getAllRecipients()) {
            if (recipientAddress instanceof InternetAddress) {
              InternetAddress recipientInternetAddress = (InternetAddress) recipientAddress;

              // NOTE: We should explicitly check for and refuse to process group addresses
              if  (recipientInternetAddress.isGroup()) {
                throw new RuntimeException("Group addresses are not supported (" + recipientInternetAddress.toString() + ")");
              }

              if (!StringUtils.hasText(recipientInternetAddress.getPersonal())) {
                int zzz = 0;
                zzz++;
              }



              System.out.println("[DEBUG] Recipient Address: " + recipientInternetAddress.getAddress() + " (" + (StringUtils.hasText(recipientInternetAddress.getPersonal()) ? recipientInternetAddress.getPersonal().replaceAll("^\"|\"$", "") : "") + ")");
            } else {
              // NOTE: We should reject non-Internet addresses
              throw new RuntimeException("Non-Internet addresses are not supported (" + recipientAddress.getClass().getName() + ")");
            }
          }

          if (message.getContent() instanceof String) {
            System.out.println("[DEBUG] Start of Content");
            System.out.print(message.getContent());
            System.out.println("[DEBUG] End of Content");

          } else if (message.getContent() instanceof MimeMultipart) {
            MimeMultipart mimeMultipart = (MimeMultipart)message.getContent();

            for (int bodyPartIndex = 0; bodyPartIndex < mimeMultipart.getCount(); bodyPartIndex++) {
              BodyPart bodyPart = mimeMultipart.getBodyPart(bodyPartIndex);

              System.out.println("[DEBUG] Body Part " + (bodyPartIndex + 1) + " Content Type: " + bodyPart.getContentType());
              System.out.println("[DEBUG] Body Part " + (bodyPartIndex + 1) + " Content Class: " + bodyPart.getContent().getClass().getName());
              System.out.println("[DEBUG] Body Part " + (bodyPartIndex + 1) + " Disposition: " + bodyPart.getDisposition());
              System.out.println("[DEBUG] Body Part " + (bodyPartIndex + 1) + " Size: " + bodyPart.getSize());
              System.out.println("[DEBUG] Body Part " + (bodyPartIndex + 1) + " Is PDF: " + bodyPart.isMimeType("application/pdf"));
              System.out.println("[DEBUG] Body Part " + (bodyPartIndex + 1) + " File Name: " + bodyPart.getFileName());
              System.out.println("[DEBUG] Body Part " + (bodyPartIndex + 1) + " Line Count: " + bodyPart.getLineCount());
              System.out.println("[DEBUG] Body Part " + (bodyPartIndex + 1) + " Description: " + bodyPart.getDescription());

              if (bodyPart.getContent() instanceof String) {
                System.out.println("[DEBUG] Body Part " + (bodyPartIndex + 1) + " Start of Content");
                System.out.print(bodyPart.getContent());
                System.out.println("[DEBUG] Body Part " + (bodyPartIndex + 1) + " End of Content");
              }
            }
          }
        }
      }


      inbox.close(false);
      store.close();






//      ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
//          .clientId(sharepointConfig.getClientId())
//          .clientSecret(sharepointConfig.getClientSecret())
//          .tenantId(sharepointConfig.getTenantId())
//          .build();




      //https://login.microsoftonline.com/common/oauth2/authorize?client_id=00000002-0000-0ff1-ce00-000000000000&redirect_uri=https%3a%2f%2foutlook.office365.com%2fowa%2f&resource=00000002-0000-0ff1-ce00-000000000000&response_mode=form_post&response_type=code+id_token&scope=openid&msafed=1&msaredir=1&client-request-id=025e62b5-316e-8c79-3b42-4dfef81fa306&protectedtoken=true&claims=%7b%22id_token%22%3a%7b%22xms_cc%22%3a%7b%22values%22%3a%5b%22CP1%22%5d%7d%7d%7d&login_hint=marcusp%40discovery.co.za&nonce=638293157398521379.568f7181-7962-4c12-a70c-3e2c8c739cb2&state=DYsxEoAwCMCoPZ-DLWALPKdyujr6fRmSKSkAUJMtKT0FOsXYhYaK22AS9WNMe5SMUH0ynkGMS3ug3BwWGcbFJd-9vd9qPw


      // https://login.microsoftonline.com/common/oauth2/authorize?client_id=00000002-0000-0ff1-ce00-000000000000&redirect_uri=https%3a%2f%2foutlook.office365.com%2fowa%2f&resource=00000002-0000-0ff1-ce00-000000000000&response_mode=form_post&response_type=code+id_token&scope=openid&msafed=1&msaredir=1&client-request-id=158d520e-e9b8-c055-53ee-33290143734a&protectedtoken=true&claims=%7b%22id_token%22%3a%7b%22xms_cc%22%3a%7b%22values%22%3a%5b%22CP1%22%5d%7d%7d%7d&login_hint=marcusp%40discovery.co.za&nonce=638293161647437136.b992213c-fe12-4238-9263-168d114bee38&state=DcsxEoAwCAXRRMfjYPzAEDiO0dhaen0p3nZbSylrWlI9MqWbOIfAYNpVOsT2EcEMueiZYFIWp2ATgvkN6JhTvOa7tfc72w8



      //System.out.println(Base64.getDecoder().decode("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"));


      int xxx = 0;
      xxx++;


     //ClientId=D10ADE4449C349EBA58F47F04DC43D7A; OIDC=1; HostSwitch=1; OptInH=SDsZ5k-6kN_d7g3ZUYrNxpJA9fipXs1c6AwHJZrO460; DefaultAnchorMailbox=1003BFFD9CBA10D0@710b1675-b870-4652-b453-d417a82a0b6f; O365Consumer=0; as=Y2RgYGAEYmYgZjEyNDcBAA; OpenIdConnect.token.v1=; domainName=discovery.co.za; SuiteServiceProxyKey=vBFLEgkDoFRiurRSU+vbfwZbVpjPesmmzIajgW6nO4M=&oRJB70gd5E7Xh3zm91BDtg==; UC=e6483626ebf24c788db3348a6fe2d318; X-OWA-CANARY=yHC8R7D4bEK9UkPvGfUtCWDJ07JArNsY19QLrSjf5PsJ7qpdN281FJEKAcNfGcymDqcYUknligM.; RoutingKeyCookie=v2:%2bcDQOKIElQ2Ak9nZ%2bbFhy9dPq9O2KpnqEffpbfdjrsw%3d:f658b437-12cd-4a24-a32a-f9bdfead7df3@discovery.co.za; HostSwitchPrg=1; X-OWA-RedirectHistory=AtKNjSIBey3WskCs2wg|ApRdfW4ByN_HskCs2wg|ArLym14BmW6TskCs2wg|ApRdfW4BvVWLskCs2wg




      //String authToken = getAuthToken("", "", "");


    } catch (Throwable e) {
      System.err.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }







//  public static String getAuthToken(String tenantId,String clientId,String client_secret) throws Exception {
//    RestTemplate restTemplate = new RestTemplate();
//
//    // This allows us to read the response more than once - Necessary for debugging.
//    restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(restTemplate.getRequestFactory()));
//
//    // Disable default URL encoding
//    DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();
//    uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
//    restTemplate.setUriTemplateHandler(uriBuilderFactory);
//
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//    headers.add("cache-control", "no-cache");
//
//    String scopes = "https://outlook.office365.com/.default";
//    String encodedBody = "client_id=" + clientId + "&scope=" + scopes + "&client_secret=" + client_secret
//        + "&grant_type=client_credentials";
//
//    HttpEntity request = new HttpEntity<>(encodedBody, headers);
//
//    ResponseEntity<String> response = restTemplate.exchange("https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token", HttpMethod.POST, request, String.class);
//    if (Objects.isNull(response) || !response.getStatusCode().is2xxSuccessful()) {
//      throw new RuntimeException("Invalid OAUth2 response.");
//    }
//
//    ObjectMapper objectMapper = new ObjectMapper();
//    JavaType type = objectMapper.constructType(
//        objectMapper.getTypeFactory().constructParametricType(Map.class, String.class, String.class));
//    Map<String, String> parsed = new ObjectMapper().readValue(response.getBody(), type);
//
//    String accessToken = parsed.get("access_token");
//
//    return accessToken;
//
//
//
//
//
//
//
//
//
//
////    CloseableHttpClient client = HttpClients.createDefault();
////    HttpPost loginPost = new HttpPost("https://login.microsoftonline.com/" + tanantId + "/oauth2/v2.0/token");
////    String scopes = "https://outlook.office365.com/.default";
////    String encodedBody = "client_id=" + clientId + "&scope=" + scopes + "&client_secret=" + client_secret
////        + "&grant_type=client_credentials";
////    loginPost.setEntity(new StringEntity(encodedBody, ContentType.APPLICATION_FORM_URLENCODED));
////    loginPost.addHeader(new BasicHeader("cache-control", "no-cache"));
////    CloseableHttpResponse loginResponse = client.execute(loginPost);
////    InputStream inputStream = loginResponse.getEntity().getContent();
////    byte[] response = readAllBytes(inputStream);
////    ObjectMapper objectMapper = new ObjectMapper();
////    JavaType type = objectMapper.constructType(
////        objectMapper.getTypeFactory().constructParametricType(Map.class, String.class, String.class));
////    Map<String, String> parsed = new ObjectMapper().readValue(response, type);
////    return parsed.get("access_token");
//
//
//
//
//
//
//
//
//  }
}
