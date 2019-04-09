/*
 * Copyright 2013-2017 the original author or authors.
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

package org.springframework.cloud.netflix.zuul.filters.pre;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ZuulServlet;

import org.springframework.web.servlet.DispatcherServlet;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.IS_DISPATCHER_SERVLET_REQUEST_KEY;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;

/**
 * 检测请求是通过{@link DispatcherServlet}还是{@link ZuulServlet}运行。
 * 目的是在Zuul过滤器处理的最开始时检测这个预先，并依赖于所有过滤器中的这些信息。
 * 使用RequestContext使得没有请求引用的类可以访问该信息。
 *
 * @author Adrian Ivan
 */
public class ServletDetectionFilter extends ZuulFilter {

	public ServletDetectionFilter() {
	}

	@Override
	public String filterType() {
		return PRE_TYPE;
	}

	/**
	 * Must run before other filters that rely on the difference between DispatcherServlet
	 * and ZuulServlet.
	 */
	@Override
	public int filterOrder() {
		return SERVLET_DETECTION_FILTER_ORDER;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		if (!(request instanceof HttpServletRequestWrapper)
				&& isDispatcherServletRequest(request)) {
			ctx.set(IS_DISPATCHER_SERVLET_REQUEST_KEY, true);
		}
		else {
			ctx.set(IS_DISPATCHER_SERVLET_REQUEST_KEY, false);
		}

		return null;
	}

	private boolean isDispatcherServletRequest(HttpServletRequest request) {
		return request.getAttribute(
				DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null;
	}

}
