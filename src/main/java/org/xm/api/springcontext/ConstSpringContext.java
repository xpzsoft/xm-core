package org.xm.api.springcontext;

import org.springframework.web.context.support.XmlWebApplicationContext;

public class ConstSpringContext {
	private static XmlWebApplicationContext springcontext = null;
	
	public static <T> T getBean(Class<T> type){
		return springcontext.getBean(type);
	}
	
	public static Object getBean(String beanid){
		return springcontext.getBean(beanid);
	}
	
	public static <T> T getBean(String beanid, Class<T> type){
		return springcontext.getBean(beanid, type);
	}
	
	public static XmlWebApplicationContext getContext(){
		return springcontext;
	}
	
	public static void setSpringcontext(XmlWebApplicationContext springcontext) {
		ConstSpringContext.springcontext = springcontext;
	}
}
