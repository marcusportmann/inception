package digital.inception.rs.oauth;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.HashMap;
import java.util.Map;

public class TokenEnhancer implements org.springframework.security.oauth2.provider.token.TokenEnhancer
{
  @Override
  public OAuth2AccessToken enhance(
    OAuth2AccessToken accessToken,
    OAuth2Authentication authentication) {
    Map<String, Object> additionalInfo = new HashMap<>();

    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
    return accessToken;
  }
}
