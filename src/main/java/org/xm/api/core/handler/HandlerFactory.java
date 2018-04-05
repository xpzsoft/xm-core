package org.xm.api.core.handler;

import org.xm.api.springcontext.SpringContext;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */
public class HandlerFactory {
	
	/**
	 * 根据类型获取Handler处理器
     * @author xpzsoft
     * @param type Handler类型
     * @param <T> 模板
     * @return T 返回指定Handler处理器
     */
	public static <T> T getHandler(Class<T> type){
		return SpringContext.getContext().getBean(type);
	}
	
	/**
	 * 根据类型获取Handler处理器
     * @author xpzsoft
     * @param name Handler在Spring中配置的名称
     * @return T 返回指定Handler处理器
     */
	public static Object getHandler(String name){
		return SpringContext.getContext().getBean(name);
	}
	
	/**
	 * 根据类型获取Handler处理器
     * @author xpzsoft
     * @param name Handler在Spring中配置的名称
     * @param type Handler类型
     * @param <T> 模板
     * @return T 返回指定Handler处理器
     */
	public static <T> T getHandler(String name, Class<T> type){
		return SpringContext.getContext().getBean(name, type);
	}
}
