/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.ws;

// ~--- non-JDK imports --------------------------------------------------------

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The <code>InceptionWSConfiguration</code> class provides the configuration for the
 * <b>inception-ws</b> library.
 *
 * @author Marcus Portmann
 */
@Configuration
public class InceptionWSConfiguration {

  /**
   * Returns the web services bean factory post processor.
   *
   * @return web services bean factory post processor
   */
  @Bean
  protected static BeanFactoryPostProcessor webServicesBeanFactoryPostProcessor() {
    return beanFactory -> beanFactory.registerSingleton("cxf", new SpringBus());
  }

  /**
   * Returns the Apache CXF servlet registration bean.
   *
   * @return the Apache CXF servlet registration bean
   */
  @Bean
  public ServletRegistrationBean<CXFServlet> cxfServletRegistrationBean() {
    CXFServlet cxfServlet = new CXFServlet();

    return new ServletRegistrationBean<>(cxfServlet, "/service/*");
  }
}
