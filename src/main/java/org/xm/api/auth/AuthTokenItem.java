package org.xm.api.auth;

public class AuthTokenItem {
	private String token_str = null;
	private AuthUser authUser = null;
	
	public String getToken_str() {
		return token_str;
	}
	
	public void setToken_str(String token_str) {
		this.token_str = token_str;
	}
	
	public AuthUser getAuthUser() {
		return authUser;
	}
	
	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}
}
