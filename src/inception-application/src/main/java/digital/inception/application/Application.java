/*
 * Copyright 2018 Marcus Portmann
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

package digital.inception.application;

//~--- non-JDK imports --------------------------------------------------------

import digital.inception.core.support.MergedMessageSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * The <code>Application</code> class provides the class that all application-specific application
 * classes should be derived from.
 *
 * @author Marcus Portmann
 */
@Component
@ComponentScan(basePackages = { "digital.inception" }, lazyInit = true)
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@SuppressWarnings({ "unused" })
public abstract class Application extends ApplicationBase
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  /**
   * The distributed in-memory caches.
   */
  Map<String, Map> caches = new ConcurrentHashMap<>();

  /**
   * Constructs a new <code>Application</code>.
   *
   * @param applicationContext the Spring application context
   */
  protected Application(ApplicationContext applicationContext)
  {
    super(applicationContext);
  }

  /**
   * Returns the application message source.
   *
   * @return the application message source
   */
  @Bean
  protected MessageSource messageSource()
  {
    MergedMessageSource messageSource = new MergedMessageSource();
    messageSource.setBasename("classpath*:messages");

    return messageSource;
  }

  /**
   * Returns the Spring task executor to use for @Async method invocations.
   *
   * @return the Spring task executor to use for @Async method invocations
   */
  @Bean
  protected Executor taskExecutor()
  {
    return new SimpleAsyncTaskExecutor();
  }

  /**
   * Returns the Spring task scheduler.
   *
   * @return the Spring task scheduler
   */
  @Bean
  protected TaskScheduler taskScheduler()
  {
    return new ConcurrentTaskScheduler();
  }
}
