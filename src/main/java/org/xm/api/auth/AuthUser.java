package org.xm.api.auth;

import java.util.UUID;

public class AuthUser {
	private String tokenid = UUID.randomUUID().toString();
	private String user_id = null;
	private boolean login_state = false;//true登录成功，false未登录
	private int authority = AuthAnnotation.AuthCode.AC10.getValue();
	
	public String getTokenid() {
		return tokenid;
	}

	public int getAuthority() {
		return authority;
	}
	
	public void setAuthority(int authority) {
		this.authority = authority;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public boolean isLogin_state() {
		return login_state;
	}

	public void setLogin_state(boolean login_state) {
		this.login_state = login_state;
	}
}
