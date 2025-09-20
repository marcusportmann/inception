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

package digital.inception.server.authorization.csrf;

import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** The {@code CsrfController} class implements the CSRF token endpoint. */
@Controller
@RequestMapping("csrf")
public class CsrfController {

  /* The bucket used to limit the issuing of tokens */
  private final Bucket tokensIssuedRateLimitBucket;

  /**
   * Constructs a new {@code CsrfController}.
   *
   * @param tokensIssuedPerSecond the number of tokens that can be issued per second
   */
  public CsrfController(
      @Value("${nova.authorization-server.limits.tokens-issued-per-second:10}")
          int tokensIssuedPerSecond) {
    this.tokensIssuedRateLimitBucket =
        Bucket.builder()
            .addLimit(
                limit ->
                    limit
                        .capacity(tokensIssuedPerSecond * 60L)
                        .refillGreedy(tokensIssuedPerSecond * 60L, Duration.ofMinutes(1)))
            .build();
  }

  /**
   * The "token" endpoint that returns the CSRF token for WebSocket connections
   *
   * @param request the HTTP servlet request
   * @return a map containing the CSRF token
   */
  @GetMapping("/token")
  public Map<String, String> getCsrfToken(HttpServletRequest request) {
    Map<String, String> response = new HashMap<>();

    if (!tokensIssuedRateLimitBucket.tryConsume(1)) {
      response.put(
          "error", "Cannot issue CSRF token. Rate limit exceeded. Please try again later.");

      return response;
    }

    CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

    if (csrf != null) {
      response.put("token", csrf.getToken());
      response.put("headerName", csrf.getHeaderName());
      response.put("parameterName", csrf.getParameterName());
    } else {
      response.put("error", "No CSRF token available");
    }

    return response;
  }
}
