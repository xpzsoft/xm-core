package org.xm.api.core.auth;

import java.util.UUID;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */
public class AuthTokenUser {
	//唯一标识token
	private String tokenId = UUID.randomUUID().toString();
	//用户id
	private String userId = null;
	//是否单点登录，默认为否
	private boolean isSingleLogin = false;
	//用户权限级别，默认为最高级别10级
	private int authority = AuthAnnotation.AuthCode.AC10.getValue();

	public String getTokenId() {
		return tokenId;
	}
	
	public int getAuthority() {
		return authority;
	}
	
	public void setAuthority(int authority) {
		this.authority = authority;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isSingleLogin() {
		return isSingleLogin;
	}

	public void setSingleLogin(boolean isSingleLogin) {
		this.isSingleLogin = isSingleLogin;
	}	
}
