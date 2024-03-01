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

package digital.inception.mail.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * The <b>MailServiceTestConfiguration/code> class.
 *
 * @author Marcus Portmann
 */
@Configuration
public class MailServiceTestConfiguration {

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

    javaMailSender.setProtocol("smtp");
    javaMailSender.setHost("127.0.0.1");
    javaMailSender.setPort(2500);
    javaMailSender.setUsername("inception");
    javaMailSender.setPassword("inception");

    return javaMailSender;
  }
}
