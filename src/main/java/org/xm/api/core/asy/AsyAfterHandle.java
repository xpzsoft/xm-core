package org.xm.api.core.asy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.api.core.Handler;
import org.xm.api.rt.ReturnItem;

public class AsyAfterHandle extends Thread{
	private static final Logger log = LoggerFactory.getLogger(AsyAfterHandle.class);
	private AsyAfterHandleItem[] task_list = null;
	private int id = 0;
	private int count = 0;
	private int life_count = 0;
	private int max_life_count = 60;
	private boolean isconst = false;
	public AsyAfterHandle(int id, int size, boolean isconst){
		this.id = id;
		if(size < 10)
			size = 10;
		task_list = new AsyAfterHandleItem[size + 1];
		this.isconst = isconst;
	}
	
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
	
	public boolean addAsyAfterHandleItem(Handler handler, String method, ReturnItem arg){
		if(isFull()){
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
	
	private synchronized void updateCount(boolean add){
		if(add)
			count++;
		else
			count--;
	}
	
	private boolean isFull(){
		if(count >= task_list.length - 1){
			return true;
		}
		return false;
	}
}
