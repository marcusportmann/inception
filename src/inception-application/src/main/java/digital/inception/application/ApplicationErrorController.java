///*
// * Copyright 2019 Marcus Portmann
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
//package digital.inception.application;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.web.ErrorProperties;
//import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
//import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
//import org.springframework.boot.web.servlet.error.ErrorAttributes;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.context.request.ServletWebRequest;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Collections;
//import java.util.Map;
//
///**
// * The <code>ApplicationErrorController</code> class implements the Spring error controller for the
// * application.
// *
// * @author Marcus Portmann
// */
//@Controller
//@RequestMapping("${server.error.path:${error.path:/error}}")
//public class ApplicationErrorController extends BasicErrorController
//{
//  /**
//   * Create a new {@link BasicErrorController} instance.
//   * @param errorAttributes the error attributes
//   * @param errorProperties configuration properties
//   */
//  public BasicErrorController(ErrorAttributes errorAttributes,
//    ErrorProperties errorProperties) {
//    this(errorAttributes, errorProperties, Collections.emptyList());
//  }
//
//  /**
//   * Create a new {@link BasicErrorController} instance.
//   * @param errorAttributes the error attributes
//   * @param errorProperties configuration properties
//   * @param errorViewResolvers error view resolvers
//   */
//  public BasicErrorController(ErrorAttributes errorAttributes,
//    ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
//    super(errorAttributes, errorViewResolvers);
//    Assert.notNull(errorProperties, "ErrorProperties must not be null");
//    this.errorProperties = errorProperties;
//  }
//
//
//
//
//
//
//
//  @Autowired
//  private ErrorAttributes errorAttributes;
//
//  @RequestMapping("/error")
//  @ResponseBody
//  public String handleError(HttpServletRequest request) {
//    ServletWebRequest servletWebRequest = new ServletWebRequest(request);
//    Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(servletWebRequest, true);
//    final StringBuilder errorDetails = new StringBuilder();
//    errorAttributes.forEach((attribute, value) -> {
//      errorDetails.append("<tr><td>")
//        .append(attribute)
//        .append("</td><td><pre>")
//        .append(value)
//        .append("</pre></td></tr>");
//    });
//
//    return String.format("<html><head><style>td{vertical-align:top;border:solid 1px #666;}</style>"
//      + "</head><body><h2>Error Page</h2><table>%s</table></body></html>", errorDetails.toString());
//  }
//
//  @Override
//  public String getErrorPath() {
//    return "/error";
//  }
//}

