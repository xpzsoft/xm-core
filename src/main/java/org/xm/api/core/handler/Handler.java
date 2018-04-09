package org.xm.api.core.handler;

import javax.servlet.http.HttpServletRequest;

import org.xm.api.rt.ReturnItem;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */
public interface Handler {
	/**
	 * 默认处理器处理不带参数的请求
     * @author xpzsoft
     * @return ReturnItem
     */
	public ReturnItem handle();
	
	/**
	 * 默认处理器处理带参数的请求
     * @author xpzsoft
     * @param arg 参数
     * @param <T> 模板
     * @return ReturnItem
     */
	public <T> ReturnItem handle(T arg);
	
	/**
	 * 自定义处理器处理带参数的请求
     * @author xpzsoft
     * @param arg 参数
     * @param handler_name 自定义处理器名称
     * @return ReturnItem
     */
	public ReturnItem handle(Object arg, String handler_name);
	
	/**
	 * 默认处理器处理带参数的请求
     * @author xpzsoft
     * @param requet 客户端请求对象
     * @return ReturnItem
     */
	public ReturnItem handle2(HttpServletRequest requet);
	
	/**
	 * 默认处理器处理带参数的请求
     * @author xpzsoft
     * @param requet 客户端请求对象
     * @param arg 参数
     * @param <T> 模板
     * @return ReturnItem
     */
	public <T> ReturnItem handle2(HttpServletRequest requet, T arg);
	
	/**
	 * 默认处理器处理带参数的请求
     * @author xpzsoft
     * @param requet 客户端请求对象
     * @param arg 参数
     * @param handler_name 自定义处理器名称
     * @return ReturnItem
     */
	public ReturnItem handle2(HttpServletRequest requet, Object arg, String handler_name);
	
	/**
	 * 注册自定义处理器
     * @author xpzsoft 
     * @param names 处理器名称数组
     */
	public void registerHandler(String [] names);
	
	/**
	 * 调用异步处理
     * @author xpzsoft
     * @param methodname 方法名称
     * @param arg 参数
     */
	public void handleAfterAsy(String methodname, ReturnItem arg);
}
