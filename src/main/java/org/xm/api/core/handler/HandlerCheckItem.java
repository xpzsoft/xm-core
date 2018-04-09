package org.xm.api.core.handler;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */
public class HandlerCheckItem {
	//0不执行afterhandle，1同步执行afterhandle，2异步执行afterhandle
	private byte isaffter = 0;
	//记录处理器前置处理函数是否存在
	private boolean isbefore_exist = false;
	//记录处理器业务处理函数是否存在
	private boolean isin_exist = false;
	//记录处理器后置处理函数是否存在
	private boolean isafter_exist = false;
	
	public byte getIsaffter() {
		return isaffter;
	}
	public void setIsaffter(byte isaffter) {
		this.isaffter = isaffter;
	}
	public boolean isIsbefore_exist() {
		return isbefore_exist;
	}
	public void setIsbefore_exist(boolean isbefore_exist) {
		this.isbefore_exist = isbefore_exist;
	}
	public boolean isIsin_exist() {
		return isin_exist;
	}
	public void setIsin_exist(boolean isin_exist) {
		this.isin_exist = isin_exist;
	}
	public boolean isIsafter_exist() {
		return isafter_exist;
	}
	public void setIsafter_exist(boolean isafter_exist) {
		this.isafter_exist = isafter_exist;
	}
}
