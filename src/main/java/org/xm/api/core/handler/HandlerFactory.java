package org.xm.api.core.handler;

import org.xm.api.springcontext.SpringContext;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */
public class HandlerFactory {
	
	/**
     * @author xpzsoft
     * @Description 根据类型获取Handler处理器
     * @param {type:[Handler类型]}
     * @return 返回指定Handler处理器
     * @throws
     */
	public static <T> T getHandler(Class<T> type){
		return SpringContext.getContext().getBean(type);
	}

	public static Object getHandler(String name){
		return SpringContext.getContext().getBean(name);
	}

	public static <T> T getHandler(String name, Class<T> type){
		return SpringContext.getContext().getBean(name, type);
	}
}
