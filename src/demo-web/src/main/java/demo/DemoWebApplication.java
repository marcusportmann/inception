/*
 * Copyright 2021 Marcus Portmann
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

import digital.inception.application.Application;
import digital.inception.core.util.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;

/**
 * The <b>DemoApplication</b> class provides the implementation of the Inception Framework
 * application class for the demo-web application.
 *
 * @author Marcus Portmann
 */
@SpringBootApplication
@EnableCaching
public class DemoWebApplication extends Application implements InitializingBean {

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(DemoWebApplication.class);

  /**
   * Constructs a new <b>DemoApplication</b>.
   *
   * @param applicationContext the Spring application context
   */
  public DemoWebApplication(
      ApplicationContext applicationContext) {
    super(applicationContext);
  }

  /**
   * The main method.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(DemoWebApplication.class, args);
  }

  /** Initialize the demo application. */
  @Override
  public void afterPropertiesSet() {
  }
}
