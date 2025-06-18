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

package digital.inception.application;

import digital.inception.core.support.MergedMessageSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 * The {@code Application} class provides the class that all application-specific application
 * classes should be derived from.
 *
 * @author Marcus Portmann
 */
@Component
@ComponentScan(
    basePackages = {"digital.inception"},
    lazyInit = true)
@EnableAsync
@EnableScheduling
@SuppressWarnings({"unused"})
public abstract class Application {

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** The distributed in-memory caches. */
  Map<String, Map<?, ?>> caches = new ConcurrentHashMap<>();

  /**
   * Constructs a new {@code Application}.
   *
   * @param applicationContext the Spring application context
   */
  protected Application(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Returns the Spring application context.
   *
   * @return the Spring application context
   */
  protected ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * Returns the application message source.
   *
   * @return the application message source
   */
  @Bean
  protected MessageSource messageSource() {
    MergedMessageSource messageSource = new MergedMessageSource();
    messageSource.setBasename("classpath*:messages");

    return messageSource;
  }

  /**
   * Returns the {@link org.springframework.core.task.TaskExecutor TaskExecutor} that should be used
   * to execute methods annotated with {@link
   * org.springframework.scheduling.annotation.Async @Async}.
   *
   * <p>A {@link org.springframework.core.task.SimpleAsyncTaskExecutor SimpleAsyncTaskExecutor} is
   * <em>not</em> suitable because:
   *
   * <ul>
   *   <li>it spawns a new thread for every task, which is inefficient and can exhaust system
   *       resources;
   *   <li>it has no backing thread pool, so threads are never reused;
   *   <li>it offers no mechanism to bound the number of concurrent tasks.
   * </ul>
   *
   * @return the configured {@code TaskExecutor}
   */
  @Bean
  protected Executor taskExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(5);
    threadPoolTaskExecutor.setMaxPoolSize(10);
    threadPoolTaskExecutor.setQueueCapacity(100);
    threadPoolTaskExecutor.setThreadNamePrefix("async-task-");
    threadPoolTaskExecutor.initialize();
    return threadPoolTaskExecutor;
  }

  /**
   * Returns the Spring task scheduler.
   *
   * @return the Spring task scheduler
   */
  @Bean
  protected TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    // TODO: Make this configurable -- MARCUS
    scheduler.setPoolSize(10);
    scheduler.setThreadNamePrefix("scheduled-task-");
    scheduler.initialize();
    return scheduler;
  }
}
