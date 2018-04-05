package org.xm.api.core.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandlerAnnotion {
	public enum AfterType{
		
		ASY("asy"),//异步执行 
		SYN("syn");//同步执行
		
		private final String type;

	    // 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		AfterType(String type) {
			this.type = type;
	    }

	    public String getType() {
	        return type;
	    }
	};
	public AfterType value() default AfterType.SYN;
}
