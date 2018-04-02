package org.xm.api.core.asy;

import org.xm.api.core.Handler;
import org.xm.api.rt.ReturnItem;

public class AsyAfterHandleItem {
	private Handler handler =null;
	private String method = null;
	private ReturnItem arg = null;
	
	public AsyAfterHandleItem(Handler handler, String method, ReturnItem arg){
		this.handler = handler;
		this.method = method;
		this.arg = arg;
	}
	
	public Handler getHandler() {
		return handler;
	}
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public ReturnItem getArg() {
		return arg;
	}
	public void setArg(ReturnItem arg) {
		this.arg = arg;
	}
}
