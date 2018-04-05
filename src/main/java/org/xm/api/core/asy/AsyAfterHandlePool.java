package org.xm.api.core.asy;

import java.util.ArrayList;
import java.util.List;

import org.xm.api.core.handler.Handler;
import org.xm.api.rt.ReturnItem;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */
public class AsyAfterHandlePool {
	//后置处理线程池
	private List<AsyAfterHandle> pool = new ArrayList<AsyAfterHandle>();
	//一个线程的任务队列长度
	private int size = 20;
	//线程数量
	private int count = 0;
	
	/**
     * @author xpzsoft
     * @Description 异步线程池构造器
     * @param 
     * @return 异步线程池实例
     * @throws
     */
	public AsyAfterHandlePool(){
		//默认添加并开启一个常驻后置处理线程
		pool.add(new AsyAfterHandle(++count, size, true));
		pool.get(0).start();
	}
	
	/**
     * @author xpzsoft
     * @Description 添加异步后置任务
     * @param {handler:[处理器], method:[后置处理函数名称], arg:[参数]}
     * @return 任务添加是否成功
     * @throws
     */
	public synchronized boolean addTask(Handler handler, String method, ReturnItem arg){
		for(int i = 0; i < pool.size(); i++){
			if(pool.get(i).isAlive() &&
					pool.get(i).addAsyAfterHandleItem(handler, method, arg)){
				return true;
			}
			if(!pool.get(i).isAlive()){
				pool.remove(i);
			}
		}
		AsyAfterHandle nt = new AsyAfterHandle(++count, this.size, true);
		pool.add(nt);
		nt.start();
		return nt.addAsyAfterHandleItem(handler, method, arg);
	}
	
	/**
     * @author xpzsoft
     * @Description 获取任务列类长度
     * @param 
     * @return 任务列类长度
     * @throws
     */
	public int getSize() {
		return size;
	}
	
	/**
     * @author xpzsoft
     * @Description 设置任务队列长度
     * @param {size:[队列长度]}
     * @return
     * @throws
     */
	public void setSize(int size) {
		if(size < 1)
			return;
		this.size = size;
	}
}
