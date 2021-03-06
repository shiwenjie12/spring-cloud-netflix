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

package org.springframework.cloud.netflix.zuul.filters;

import java.util.Collection;
import java.util.List;

/**
 * 路线定位器
 * @author Dave Syer
 */
public interface RouteLocator {

	/**
	 * Ignored route paths (or patterns), if any.
	 * @return {@link Collection} of ignored paths
	 */
	Collection<String> getIgnoredPaths();

	/**
	 * A map of route path (pattern) to location (e.g. service id or URL).
	 * @return {@link List} of routes
	 */
	List<Route> getRoutes();

	/**
	 * Maps a path to an actual route with full metadata.
	 * @param path used to match the {@link Route}
	 * @return matching {@link Route} based on the provided path
	 */
	Route getMatchingRoute(String path);

}
