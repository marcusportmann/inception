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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * The {@code CsrfController} class implements the CSRF endpoints.
 *
 * @author Marcus Portmann
 */
@Controller
@RequestMapping("csrf")
@CrossOrigin
@Tag(name = "CSRF")
public class CsrfController {

  /* The bucket used to limit the issuing of tokens */
  private final Bucket tokensIssuedRateLimitBucket;

  /**
   * Constructs a new {@code CsrfController}.
   *
   * @param tokensIssuedPerSecond the number of tokens that can be issued per second
   */
  public CsrfController(
      @Value("${inception.authorization-server.limits.tokens-issued-per-second:10}")
          int tokensIssuedPerSecond) {

    // Keep per-second semantics to match property name; allow small bursts only.
    this.tokensIssuedRateLimitBucket =
        Bucket.builder()
            .addLimit(
                limit ->
                    limit
                        .capacity(tokensIssuedPerSecond)
                        .refillGreedy(tokensIssuedPerSecond, Duration.ofSeconds(1)))
            .build();
  }

  /**
   * Returns CSRF token details for client use (including during a WebSocket handshake).
   *
   * <p>The token is resolved from the {@link HttpServletRequest}, where it is stored as a request
   * attribute (e.g., by Spring Security). On success, the returned map contains:
   *
   * <ul>
   *   <li><b>token</b> — the opaque CSRF token value
   *   <li><b>headerName</b> — the HTTP header clients should use when sending the token
   *   <li><b>parameterName</b> — the form/query parameter name clients should use when sending the
   *       token
   * </ul>
   *
   * @param request the current HTTP servlet request from which to resolve the CSRF token
   * @return a map containing {@code token}, {@code headerName}, and {@code parameterName}
   */
  @Operation(summary = "Retrieve the CSRF token", description = "Retrieve the CSRF token")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "The CSRF token was retrieved"),
        @ApiResponse(responseCode = "429", description = "Rate limit exceeded"),
        @ApiResponse(responseCode = "503", description = "Service unavailable")
      })
  @GetMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public Map<String, String> token(HttpServletRequest request) {
    if (!tokensIssuedRateLimitBucket.tryConsume(1)) {
      // Let your global exception handler render application/problem+json
      throw new ResponseStatusException(
          HttpStatus.TOO_MANY_REQUESTS, "Cannot issue CSRF token. Rate limit exceeded.");
    }

    CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    if (csrf == null) {
      // Typically indicates Spring Security did not attach a token for this request
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No CSRF token available");
    }

    Map<String, String> body = new HashMap<>();
    body.put("token", csrf.getToken());
    body.put("headerName", csrf.getHeaderName());
    body.put("parameterName", csrf.getParameterName());
    return body;
  }
}
