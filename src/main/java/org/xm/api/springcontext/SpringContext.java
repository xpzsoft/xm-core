package org.xm.api.springcontext;

import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * 
 * @author xpzsoft
 * @Description Web App Server类
 * @version 1.2.0
 */
public class SpringContext {
	private static XmlWebApplicationContext springcontext = null;

	public static XmlWebApplicationContext getContext(){
		return springcontext;
	}

	public static void setSpringcontext(XmlWebApplicationContext springcontext) {
		SpringContext.springcontext = springcontext;
	}
}
