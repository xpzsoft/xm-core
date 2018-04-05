package org.xm.api.core.auth;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */
public class AuthTokenItem {
	//签名后的token字符串
	private String token_str = null;
	//token中的用户信息
	private AuthTokenUser authUser = null;
	
	public String getToken_str() {
		return token_str;
	}
	
	public void setToken_str(String token_str) {
		this.token_str = token_str;
	}
	
	public AuthTokenUser getAuthUser() {
		return authUser;
	}
	
	public void setAuthUser(AuthTokenUser authUser) {
		this.authUser = authUser;
	}
}
