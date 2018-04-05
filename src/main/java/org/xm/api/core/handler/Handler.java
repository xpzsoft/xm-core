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
     * @author xpzsoft
     * @Description 默认处理器处理不带参数的请求
     * @param 
     * @return 返回处理结果
     * @throws
     */
	public ReturnItem handle();
	
	/**
     * @author xpzsoft
     * @Description 默认处理器处理带参数的请求
     * @param {arg:[参数]}
     * @return 返回处理结果
     * @throws
     */
	public <T> ReturnItem handle(T arg);
	
	/**
     * @author xpzsoft
     * @Description 自定义处理器处理带参数的请求
     * @param {arg:[参数], handler_name:[自定义处理器名称]}
     * @return 返回处理结果
     * @throws
     */
	public ReturnItem handle(Object arg, String handler_name);
	
	/**
     * @author xpzsoft
     * @Description 默认处理器处理带参数的请求
     * @param {requet:[客户端请求对象]}
     * @return 返回处理结果
     * @throws
     */
	public ReturnItem handle2(HttpServletRequest requet);
	
	/**
     * @author xpzsoft
     * @Description 默认处理器处理带参数的请求
     * @param {requet:[客户端请求对象], arg:[参数]}
     * @return 返回处理结果
     * @throws
     */
	public <T> ReturnItem handle2(HttpServletRequest requet, T arg);
	
	/**
     * @author xpzsoft
     * @Description 自定义处理器处理带参数的请求
     * @param {requet:[客户端请求对象], arg:[参数], handler_name:[自定义处理器名称]}
     * @return 返回处理结果
     * @throws
     */
	public ReturnItem handle2(HttpServletRequest requet, Object arg, String handler_name);
	
	/**
     * @author xpzsoft
     * @Description 注册自定义处理器
     * @param {names:[处理器名称数组]}
     * @return 
     * @throws
     */
	public void registerHandler(String [] names);
	
	/**
     * @author xpzsoft
     * @Description 调用异步处理
     * @param {methodname:[方法名称], arg:[参数]}
     * @return
     * @throws
     */
	public void handleAfterAsy(String methodname, ReturnItem arg);
}
