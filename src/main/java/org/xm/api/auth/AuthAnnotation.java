package org.xm.api.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthAnnotation{
	public enum AuthCode{
		AC1(1), AC2(2), AC3(3), AC4(4), AC5(5), AC6(6), AC7(7), AC8(8), AC9(9), AC10(10);
		
		private final int value;

	    // 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		AuthCode(int value) {
	        this.value = value;
	    }

	    public int getValue() {
	        return value;
	    }
	};
	public AuthCode value() default AuthCode.AC1;
}  