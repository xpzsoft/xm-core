package org.xm.api.core;

import org.xm.api.springcontext.ConstSpringContext;

public class HandlerFactory {
	public static <T> T getHandler(Class<T> type){
		return ConstSpringContext.getBean(type);
	}
}
