/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.netflix.hystrix;

import java.util.ArrayList;
import java.util.List;

import com.netflix.hystrix.HystrixCircuitBreaker;
import com.netflix.hystrix.HystrixCommandMetrics;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;

/**
 * Hystrix断路器的{@link HealthIndicator}实现。
 * <p>
 * 此默认实现不会更改系统状态（例如<code>OK</code>），但包括所有打开的电路名称。
 *
 * @author Christian Dupuis
 */
public class HystrixHealthIndicator extends AbstractHealthIndicator {

	private static final Status CIRCUIT_OPEN = new Status("CIRCUIT_OPEN");

	@Override
	protected void doHealthCheck(Builder builder) throws Exception {
		List<String> openCircuitBreakers = new ArrayList<>();

		// 收集Hystrix的所有开路断路器
		for (HystrixCommandMetrics metrics : HystrixCommandMetrics.getInstances()) {
			HystrixCircuitBreaker circuitBreaker = HystrixCircuitBreaker.Factory
					.getInstance(metrics.getCommandKey());
			if (circuitBreaker != null && circuitBreaker.isOpen()) {
				openCircuitBreakers.add(metrics.getCommandGroup().name() + "::"
						+ metrics.getCommandKey().name());
			}
		}

		// 如果至少有一个开路报告OUT_OF_SERVICE添加命令组和密钥名称
		if (!openCircuitBreakers.isEmpty()) {
			builder.status(CIRCUIT_OPEN).withDetail("openCircuitBreakers",
					openCircuitBreakers);
		}
		else {
			builder.up();
		}
	}

}
