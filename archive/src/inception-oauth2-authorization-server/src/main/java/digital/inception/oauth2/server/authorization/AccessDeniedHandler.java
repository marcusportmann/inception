/// *
// * Copyright 2022 Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
// package digital.inception.rs.oauth;
//
//
//
// import digital.inception.core.util.ISO8601Util;
// import java.io.BufferedOutputStream;
// import java.io.IOException;
// import java.math.BigDecimal;
// import java.nio.charset.StandardCharsets;
// import java.time.LocalDateTime;
// import javax.json.Json;
// import javax.json.JsonObject;
// import javax.json.JsonObjectBuilder;
// import javax.json.JsonWriter;
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import org.springframework.http.HttpStatus;
// import org.springframework.security.access.AccessDeniedException;
//
//
//
/// **
// * The <b>AccessDeniedHandler</b> implements the custom access denied handler.
// *
// * @author Marcus Portmann
// */
// public class AccessDeniedHandler
//    implements org.springframework.security.web.access.AccessDeniedHandler {
//
//  @Override
//  public void handle(
//      HttpServletRequest request,
//      HttpServletResponse response,
//      AccessDeniedException accessDeniedException)
//      throws IOException, ServletException {
//    try {
//      JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
//
//      jsonObjectBuilder.add("uri", request.getRequestURI());
//      jsonObjectBuilder.add("timestamp", ISO8601Util.fromLocalDateTime(LocalDateTime.now()));
//      jsonObjectBuilder.add("status", BigDecimal.valueOf(HttpStatus.UNAUTHORIZED.value()));
//      jsonObjectBuilder.add("reasonPhrase", HttpStatus.UNAUTHORIZED.getReasonPhrase());
//      jsonObjectBuilder.add("message", "Access is denied");
//
//      JsonObject jsonObject = jsonObjectBuilder.build();
//
//      response.setStatus(HttpStatus.UNAUTHORIZED.value());
//      response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//      response.setContentType("application/json");
//
//      try (BufferedOutputStream outputStream =
//          new BufferedOutputStream(response.getOutputStream())) {
//        JsonWriter jsonWriter = Json.createWriter(outputStream);
//        jsonWriter.writeObject(jsonObject);
//        jsonWriter.close();
//      }
//    } catch (Throwable e) {
//      throw new ServletException("Failed to handle the access denied error", e);
//    }
//  }
// }
