package org.xm.api.core.asy;

import java.util.ArrayList;
import java.util.List;

import org.xm.api.core.Handler;
import org.xm.api.rt.ReturnItem;

public class AsyAfterHanlePool {
	private List<AsyAfterHandle> pool = new ArrayList<AsyAfterHandle>();
	private int size = 10;
	private int count = 0;
	public AsyAfterHanlePool(int size){
		//默认添加并开启一个常驻后置处理线程
		pool.add(new AsyAfterHandle(++count, size, true));
		pool.get(0).start();
		this.size = size;
	}
	
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
}
