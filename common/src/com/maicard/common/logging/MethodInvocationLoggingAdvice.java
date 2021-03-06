/*
 * Copyright 2009 the original author or authors.
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

package com.maicard.common.logging;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


/**
 * Method invocation logging advice.
 * 
 * @author Byeongkil Woo
 */
public class MethodInvocationLoggingAdvice implements MethodInterceptor {
	
	private MethodInvocationLogger methodInvocationLogger;

	public void setMethodInvocationLogger(MethodInvocationLogger methodInvocationLogger) {
		this.methodInvocationLogger = methodInvocationLogger;
	}
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		MethodInvocationInfoInterceptor.addMessage(methodInvocationLogger.invocationMessage(invocation));
		MethodInvocationInfoInterceptor.increaseDepth();
		
		long startTime = System.currentTimeMillis();
		
		Object returnValue = null;
		
		try {
			returnValue = invocation.proceed();
		} catch (Throwable t) {
			MethodInvocationInfoInterceptor.addMessage(methodInvocationLogger.onThrowingMessage(invocation, t));
			throw t;
		}

		long endTime = System.currentTimeMillis();
		
		MethodInvocationInfoInterceptor.decreaseDepth();
		MethodInvocationInfoInterceptor.addMessage(methodInvocationLogger.processMessage(invocation, returnValue, endTime - startTime));

		return returnValue;
	}

}
