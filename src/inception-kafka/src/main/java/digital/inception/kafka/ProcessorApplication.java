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

package digital.inception.kafka;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;

/**
 * The <code>ProcessorApplication</code> class implements the base class for a Spring Boot
 * application that processes records retrieved from one or more Kafka topics.
 *
 * @author Marcus Portmann
 */
public abstract class ProcessorApplication
    implements ApplicationRunner, InitializingBean, DisposableBean {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(ProcessorApplication.class);

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** The processors. */
  private final List<Processor<?, ?>> processors = new ArrayList<>();

  /**
   * Constructs a new <b>ProcessorApplication</b>.
   *
   * @param applicationContext the Spring application context
   */
  public ProcessorApplication(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public void afterPropertiesSet() {
    for (Class<? extends Processor<?, ?>> processorClass : getProcessorClasses()) {
      try {
        processors.add(applicationContext.getBean(processorClass));
      } catch (Throwable e) {
        throw new BeanInitializationException(
            "Failed to initialize the processor (" + processorClass.getSimpleName() + ")", e);
      }
    }
  }

  /** Shutdown the processor application and its associated processors. */
  @Override
  public void destroy() {
    for (Processor<?, ?> processor : processors) {
      try {
        log.info("Shutting down the processor (" + processor.getClass().getSimpleName() + ")");
        processor.shutdown();
      } catch (Throwable e) {
        log.error(
            "Failed to shutdown the processor (" + processor.getClass().getSimpleName() + ")", e);
      }
    }

    log.info("Shutdown " + processors.size() + " processors");
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    for (Processor<?, ?> processor : processors) {
      try {
        log.info("Starting the processor (" + processor.getClass().getSimpleName() + ")");
        processor.start();
      } catch (Throwable e) {
        throw new ProcessorException(
            "Failed to start the processor (" + processor.getClass().getSimpleName() + ")", e);
      }
    }

    for (Processor<?, ?> processor : processors) {
      try {
        processor.join();
      } catch (Throwable ignored) {
      }
    }
  }

  /**
   * Returns the classes for the processors that will be managed by the processor application.
   *
   * @return the classes for the processors that will be managed by the processor application
   */
  protected abstract List<Class<? extends Processor<?, ?>>> getProcessorClasses();
}
