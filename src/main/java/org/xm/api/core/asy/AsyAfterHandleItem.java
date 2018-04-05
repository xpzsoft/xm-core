package org.xm.api.core.asy;

import org.xm.api.core.handler.Handler;
import org.xm.api.rt.ReturnItem;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */
public class AsyAfterHandleItem {
	//处理器
	private Handler handler =null;
	//处理器的后置方法名称
	private String method = null;
	//参数
	private ReturnItem arg = null;
	
	/**
     * @author xpzsoft
     * @Description 异步后置处理任务对象构造器
     * @param {handler:[处理器], method:[处理器的后置方法名称], arg:[参数]}
     * @return 任务对象实例
     * @throws
     */
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
