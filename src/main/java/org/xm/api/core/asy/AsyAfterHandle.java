package org.xm.api.core.asy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.api.core.handler.Handler;
import org.xm.api.rt.ReturnItem;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */
public class AsyAfterHandle extends Thread{
	private static final Logger log = LoggerFactory.getLogger(AsyAfterHandle.class);
	//异步后置处理任务列表
	private AsyAfterHandleItem[] task_list = null;
	//异步线程id
	private int id = 0;
	//任务计数
	private int count = 0;
	//消耗的生命值
	private int life_count = 0;
	//最大生命值
	private int max_life_count = 60;
	//是否长开启线程
	private boolean isconst = false;
	
	/**
     * @author xpzsoft
     * @Description 异步后置处理线程构造器
     * @param {id:[线程id], size:[任务最大容量], isconst:[是否长开]}
     * @return 返回AsyAfterHandle实例
     * @throws
     */
	public AsyAfterHandle(int id, int size, boolean isconst){
		this.id = id;
		if(size < 10)
			size = 10;
		task_list = new AsyAfterHandleItem[size + 1];
		this.isconst = isconst;
	}
	
	/**
     * @author xpzsoft
     * @Description 线程执行函数
     * @param 
     * @return
     * @throws
     */
	public void run(){
		log.info("xm: after handle thread [id=" + id + "] is started! {isconst=" + isconst + ", size=" + (task_list.length - 1) + "}");
		if(!isconst){
			while(life_count < max_life_count){
				for(int i = 0; i < task_list.length; i++){
					if(task_list[i] != null){
						task_list[i].getHandler().handleAfterAsy(task_list[i].getMethod(), task_list[i].getArg());
						task_list[i] = null;
						updateCount(false);
						life_count = 0;
					}
				}
				life_count += 1;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.error(e.getMessage());
				}
			}
		}
		else{
			while(true){
				for(int i = 0; i < task_list.length; i++){
					if(task_list[i] != null){
						task_list[i].getHandler().handleAfterAsy(task_list[i].getMethod(), task_list[i].getArg());
						task_list[i] = null;
						updateCount(false);
					}
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		this.interrupt();
		log.info("xm: after handle thread [id=" + id + "] is stopped! {isconst=" + isconst + ", size=" + (task_list.length - 1) + "}");
	}
	
	/**
     * @author xpzsoft
     * @Description 添加异步后置任务
     * @param {handler:[处理器], method:[后置执行方法], arg:[参数]}
     * @return 执行结果是否成功
     * @throws
     */
	public synchronized boolean addAsyAfterHandleItem(Handler handler, String method, ReturnItem arg){
		if(count >= task_list.length){
			return false;
		}
		for(int i = 0; i < task_list.length; i++){
			if(task_list[i] == null){
				task_list[i] = new AsyAfterHandleItem(handler, method, arg);
				updateCount(true);
				return true;
			}
		}
		return false;
	}
	
	
	/**
     * @author xpzsoft
     * @Description 更新后置任务数量
     * @param {add:[加或减]}
     * @return 
     * @throws
     */
	private synchronized void updateCount(boolean add){
		if(add)
			count++;
		else
			count--;
	}
}
