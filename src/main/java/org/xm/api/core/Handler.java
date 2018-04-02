package org.xm.api.core;

import javax.servlet.http.HttpServletRequest;

import org.xm.api.rt.ReturnItem;

public interface Handler {
	public ReturnItem handle();
	public <T> ReturnItem handle(T arg);
	public ReturnItem handle(Object arg, String handler_name);
	public ReturnItem handle2(HttpServletRequest requet);
	public <T> ReturnItem handle2(HttpServletRequest requet, T arg);
	public ReturnItem handle2(HttpServletRequest requet, Object arg, String handler_name);
	public void handleAfterAsy(String methodname, ReturnItem arg);
	public void registerHandler(String [] names);
}
