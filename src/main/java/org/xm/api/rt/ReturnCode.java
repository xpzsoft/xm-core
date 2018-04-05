package org.xm.api.rt;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */
public class ReturnCode {
	//处理结果标识：成功
	public static final int XM_SUCCESS = 200;
	//处理结果标识：异常错误
	public static final int XM_FAILED_EXCEPTION = 601;
	//处理结果标识：token过期
	public static final int XM_FAILED_TOKEN_OVERDUE = 602;
	//处理结果标识：没有权限
	public static final int XM_FAILED_AUTHORITY = 603;
	//处理结果标识：用户名、密码验证失败
	public static final int XM_FAILED_UN_PW = 604;
	//处理结果标识：自定义
	public static final int XM_FAILED_SELF_DEFINE = 605;
}
