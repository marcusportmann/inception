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
//package digital.inception.validation;
//
////~--- non-JDK imports --------------------------------------------------------
//
//import digital.inception.core.support.MergedMessageSource;
//
//import org.springframework.context.MessageSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
//
///**
// * The <code>ValidationConfiguration</code> class provides access to the Spring configuration for
// * the <b>Validation</b> module that forms part of the <b>Inception</b> framework.
// *
// * @author Marcus Portmann
// */
//@Configuration
//public class ValidationConfiguration
//{
//  /**
//   * Returns the local validator factory bean that provides support for JSR 303 Bean Validation.
//   *
//   * @return the local validator factory bean that provides support for JSR 303 Bean Validation
//   */
//  @Bean
//  @DependsOn({ "validationMessageSource" })
//  public javax.validation.Validator localValidatorFactoryBean()
//  {
//    final LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
//    localValidatorFactoryBean.setValidationMessageSource(validationMessageSource());
//
//    return localValidatorFactoryBean;
//  }
//
//  /**
//   * Returns the validation message source.
//   *
//   * @return the validation message source
//   */
//  @Bean
//  public MessageSource validationMessageSource()
//  {
//    MergedMessageSource validationMessageSource = new MergedMessageSource();
//    validationMessageSource.setBasename("classpath*:validation-messages");
//
//    return validationMessageSource;
//  }
//}
