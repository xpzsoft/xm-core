package org.xm.api.rt;

public class ReturnCode {
	public static final int XM_SUCCESS = 200;//成功
	public static final int XM_FAILED_EXCEPTION = 601;//异常错误
	public static final int XM_FAILED_TOKEN_OVERDUE = 602;//token过期
	public static final int XM_FAILED_AUTHORITY = 603;//没有权限
	public static final int XM_FAILED_UN_PW = 604;//用户名、密码验证失败
	public static final int XM_FAILED_SELF_DEFINE = 605;//自定义
}
