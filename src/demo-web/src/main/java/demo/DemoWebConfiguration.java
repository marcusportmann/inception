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

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The <b>DemoWebConfiguration</b> class provides the configuration class for the demo-web
 * application.
 *
 * @author Marcus Portmann
 */
@Configuration
@ComponentScan(basePackages = {"digital.inception", "demo"})
@EnableJpaRepositories(
    entityManagerFactoryRef = "applicationEntityManagerFactory",
    basePackages = {"demo"})
public class DemoWebConfiguration {}
