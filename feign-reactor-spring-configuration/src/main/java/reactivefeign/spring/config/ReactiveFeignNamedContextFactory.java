/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package reactivefeign.spring.config;

import org.springframework.cloud.context.named.NamedContextFactory;

/**
 * A factory that creates instances of feign classes. It creates a Spring
 * ApplicationContext per client name, and extracts the beans that it needs from there.
 *
 * patterned after org.springframework.cloud.netflix.feign.FeignContext
 */
public class ReactiveFeignNamedContextFactory extends NamedContextFactory<ReactiveFeignClientSpecification> {

	public ReactiveFeignNamedContextFactory() {
		super(ReactiveFeignClientsConfiguration.class, "reactivefeign", "reactivefeign.client.name");
	}

}
