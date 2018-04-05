package org.xm.api.core.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.api.core.asy.AsyAfterHandlePool;
import org.xm.api.rt.ReturnCode;
import org.xm.api.rt.ReturnItem;
import org.xm.api.springcontext.SpringContext;



/**
 * 
 * @author xpzsoft
 * @inheritDoc Handler
 * @version 1.2.0
 */
public abstract class HandlerDefault implements Handler{
	private static final Logger log = LoggerFactory.getLogger(HandlerDefault.class);
	private Map<String, HandlerCheckItem> checks = new HashMap<String, HandlerCheckItem>();
	private Set<String> handler_names = null;
	private AsyAfterHandlePool pool = null;
	
	/**
     * @author xpzsoft
     * @Description 默认无参处理器构造器
     * @param 
     * @return 返回处理器实体
     * @throws
     */
	public HandlerDefault(){
		checks.put("Handle", new HandlerCheckItem());
		checkAnno("afterHandle", checks.get("Handle"));
	}
	
	/**
     * @author xpzsoft
     * @Description 带参处理器构造器
     * @param {handler_names:[自定义处理器名称数组]} 
     * @return 返回处理器实体
     * @throws 
     */
	public HandlerDefault(String [] handler_names){
		checks.put("Handle", new HandlerCheckItem());
		checkAnno("afterHandle", checks.get("Handle"));
		registerHandler(handler_names);
	}
	
	/**
	 * {@link Handler#handle()} can be checked for the result.
	 */
	@Override
	public ReturnItem handle() {
		return handle(null);
	}
	
	/**
	 * {@link Handler#handle(T arg)} can be checked for the result.
	 */
	@Override
	public <T> ReturnItem handle(T arg) {
		// TODO Auto-generated method stub
		ReturnItem ri = SpringContext.getContext().getBean("getReturnItem", ReturnItem.class);
		
		beforeHandle(arg, ri);
		if(!ri.isIgnore() && ri.getCode() != ReturnCode.XM_SUCCESS)
			return ri;
		
		inHandle(ri);
		if(!ri.isIgnore() && ri.getCode() != ReturnCode.XM_SUCCESS)
			return ri;
		
		switch(checks.get("Handle").getIsaffter()){
			case 1:
				afterHandle(ri);
				break;
			case 2:
				if(pool == null)
					pool = SpringContext.getContext().getBean("getAsyAfterHandlePool", AsyAfterHandlePool.class);
				if(!pool.addTask(this, "afterHandle", ri)){
					log.error("xm : ASY after handle can't be deal.");
				}
				break;
			default:
				break;
		}	
		return ri;
	}
	
	/**
	 * {@link Handler#handle(Object arg, String handler_name)} can be checked for the result.
	 * @exception 如果hader_names为空，或者handler_name没有注册，则抛出运行时异常
	 */
	@Override
	public ReturnItem handle(Object arg, String handler_name) {
		// TODO Auto-generated method stub
		
		if(handler_names == null || !handler_names.contains(handler_name)){
			throw new RuntimeException("'" + handler_name + "' is not registed!");
		}
		
		String beforeHandle = "before" + handler_name;
		String inHandle = "in" + handler_name;
		String afterHandle = "after" + handler_name;
		
		ReturnItem ri = SpringContext.getContext().getBean("getReturnItem", ReturnItem.class);
		
		HandlerCheckItem check = checks.get(handler_name);
		
		if(check.isIsbefore_exist() && !invorkMethod(beforeHandle, new Class<?>[]{Object.class, ReturnItem.class}, arg, ri)){
			ri.setInfo(ReturnCode.XM_FAILED_EXCEPTION, "invoke '" + beforeHandle +"' failed!");
			log.error("invoke '" + beforeHandle +"' failed!");
		}
		if(!ri.isIgnore() && ri.getCode() != ReturnCode.XM_SUCCESS)
			return ri;
		
		if(!invorkMethod(inHandle, new Class<?>[]{ReturnItem.class}, ri)){
			ri.setInfo(ReturnCode.XM_FAILED_EXCEPTION, "invoke '" + inHandle +"' failed!");
			log.error("invoke '" + inHandle +"' failed!");
		}
		if(!ri.isIgnore() && ri.getCode() != ReturnCode.XM_SUCCESS)
			return ri;
		
		if(check.isIsafter_exist()){
			switch(check.getIsaffter()){
				case 1:
					handleAfterAsy(afterHandle, ri);
					break;
				case 2:
					if(pool == null)
						pool = SpringContext.getContext().getBean("getAsyAfterHandlePool", AsyAfterHandlePool.class);
					if(!pool.addTask(this, afterHandle, ri)){
						log.error("xm : ASY after handle can't be deal!");
					}
					break;
				default:
					break;
			}
		}
					
		return ri;
	}
	
	/**
	 * {@link Handler#handle2(HttpServletRequest requet)} can be checked for the result.
	 */
	public ReturnItem handle2(HttpServletRequest requet){
		return handle2(requet, null);
	}
	
	/**
	 * {@link Handler#handle2(HttpServletRequest requet, T arg)} can be checked for the result.
	 */
	public <T> ReturnItem handle2(HttpServletRequest requet, T arg){
		// TODO Auto-generated method stub
		ReturnItem ri = (ReturnItem)SpringContext.getContext().getBean("getReturnItem", requet.getAttribute("xm-token-id"), requet.getAttribute("xm-user-id"));
		
		beforeHandle(arg, ri);
		if(!ri.isIgnore() && ri.getCode() != ReturnCode.XM_SUCCESS)
			return ri;
		
		inHandle(ri);
		if(!ri.isIgnore() && ri.getCode() != ReturnCode.XM_SUCCESS)
			return ri;
		
		switch(checks.get("Handle").getIsaffter()){
			case 1:
				afterHandle(ri);
				break;
			case 2:
				if(pool == null)
					pool = SpringContext.getContext().getBean("getAsyAfterHandlePool", AsyAfterHandlePool.class);
				if(!pool.addTask(this, "afterHandle", ri)){
					log.error("xm : ASY after handle can't be deal.");
				}
				break;
			default:
				break;
		}
		
		return ri;
	}
	
	/**
	 * {@link Handler#handle2(HttpServletRequest requet, Object arg, String handler_name)} can be checked for the result.
	 * @exception 如果hader_names为空，或者handler_name没有注册，则抛出运行时异常
	 */
	public ReturnItem handle2(HttpServletRequest requet, Object arg, String handler_name){
		if(handler_names == null || !handler_names.contains(handler_name)){
			throw new RuntimeException("'" + handler_name + "' is not registed!");
		}
		
		String beforeHandle = "before" + handler_name;
		String inHandle = "in" + handler_name;
		String afterHandle = "after" + handler_name;
		
		ReturnItem ri = (ReturnItem)SpringContext.getContext().getBean("getReturnItem", requet.getAttribute("xm-token-id"), requet.getAttribute("xm-user-id"));
		
		HandlerCheckItem check = checks.get(handler_name);
		
		if(check.isIsbefore_exist() && !invorkMethod(beforeHandle, new Class<?>[]{Object.class, ReturnItem.class}, arg, ri)){
			ri.setInfo(ReturnCode.XM_FAILED_EXCEPTION, "invoke '" + beforeHandle +"' failed!");
			log.error("invoke '" + beforeHandle +"' failed!");
		}
		if(!ri.isIgnore() && ri.getCode() != ReturnCode.XM_SUCCESS)
			return ri;
		
		if(!invorkMethod(inHandle, new Class<?>[]{ReturnItem.class}, ri)){
			ri.setInfo(ReturnCode.XM_FAILED_EXCEPTION, "invoke '" + inHandle +"' failed!");
			log.error("invoke '" + inHandle +"' failed!");
		}
		if(!ri.isIgnore() && ri.getCode() != ReturnCode.XM_SUCCESS)
			return ri;
		
		if(check.isIsafter_exist()){
			switch(check.getIsaffter()){
				case 1:
					handleAfterAsy(afterHandle, ri);
					break;
				case 2:
					if(pool == null)
						pool = SpringContext.getContext().getBean("getAsyAfterHandlePool", AsyAfterHandlePool.class);
					if(!pool.addTask(this, afterHandle, ri)){
						log.error("xm : ASY after handle can't be deal!");
					}
					break;
				default:
					break;
			}
		}
		
		return ri;
	}
	
	/**
	 * {@link Handler#registerHandler(String [] names)} can be checked for the result.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void registerHandler(String [] names){
		handler_names = new HashSet(Arrays.asList(names));
		for(String name : names){
			
			if(name == null){
				log.error("Param 'handler_name' should not be 'null' in registerHandler(String [] names), it can not be registed!");
				continue;
			}
			
			name = name.trim();
			
			if(!checkMethodName(name)){
				log.error("Param '" + name + "' contain illegal characters in registerHandler(String [] names), it can not be registed!");
				continue;
			}
			
			String beforeHandle = "before" + name;
			String inHandle = "in" + name;
			String afterHandle = "after" + name;
			HandlerCheckItem check = new HandlerCheckItem();
			checks.put(name, check);
			checkFunofBeforAndAfter(beforeHandle, inHandle, afterHandle, check);
			if(check.isIsafter_exist())
				checkAnno(afterHandle, check);
		}
	}
	
	/**
	 * {@link Handler#handleAfterAsy(String methodname, ReturnItem arg)} can be checked for the result.
	 */
	@Override
	public void handleAfterAsy(String methodname, ReturnItem arg) {
		// TODO Auto-generated method stub
		if(!invorkMethod(methodname, new Class<?>[]{ReturnItem.class}, arg)){
			log.error("xm: invoke '" + methodname + "' failed!");
		}
	}
	
	/**
     * @author xpzsoft
     * @Description 处理器前置处理
     * @param {arg:[参数], ri:[处理结果]}
     * @return 
     * @throws
     */
	protected abstract <T> void beforeHandle(T arg, ReturnItem ri);
	
	/**
     * @author xpzsoft
     * @Description 处理器业务处理
     * @param {ri:[处理结果]}
     * @return 
     * @throws
     */
	protected abstract void inHandle(ReturnItem ri);
	
	/**
     * @author xpzsoft
     * @Description 处理器后置处理
     * @param {ri:[处理结果]}
     * @return 
     * @throws
     */
	protected abstract void afterHandle(ReturnItem ri);
	
	/**
     * @author xpzsoft
     * @Description 检查处理器是否包含了before和After两个步骤
     * @param {beforename:[处理器前置处理名称], inname:[处理器业务处理名称], aftername:[处理器后置处理名称], check:[处理器监察对象]}
     * @return 
     * @throws
     */
	private void checkFunofBeforAndAfter(String beforename, String inname, String aftername, HandlerCheckItem check){
		Method [] m = this.getClass().getDeclaredMethods();
		for(int i = 0; i < m.length; i++){
			if(m[i].getName().equals(beforename)){
				check.setIsbefore_exist(true);
			}
			else if(m[i].getName().equals(aftername)){
				check.setIsafter_exist(true);
			}
			else if(m[i].getName().equals(inname)){
				check.setIsin_exist(true);
			}
		}
		
		if(!check.isIsafter_exist()){
			log.info("xm: '" + aftername + "' is not defiend " + this.getClass().getName());
		}
		
		if(!check.isIsbefore_exist()){
			log.info("xm: '" + beforename + "' is not defiend " + this.getClass().getName());
		}
		
		if(!check.isIsin_exist()){
			log.error("'" + inname + "' is not defiend in " + this.getClass().getName());
			throw new RuntimeException("'" + inname + "' is not defiend in " + this.getClass().getName());
		}
	}
	
	/**
     * @author xpzsoft
     * @Description 检查处理器的后置处理注解
     * @param {afterhandle:[处理器后置处理名称], check:[处理器监察对象]}
     * @return 
     * @throws
     */
	private void checkAnno(String afterhandle, HandlerCheckItem check){
		try {
			Method m = this.getClass().getDeclaredMethod(afterhandle, new Class<?>[]{ReturnItem.class});
			if(m != null){
				HandlerAnnotion ha = m.getAnnotation(HandlerAnnotion.class);
				if(ha == null)
					check.setIsaffter((byte) 0);
				else if(ha.value() == HandlerAnnotion.AfterType.SYN){
					check.setIsaffter((byte) 1);
				}
				else if(ha.value() == HandlerAnnotion.AfterType.ASY){
					check.setIsaffter((byte) 2);
				}
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	/**
     * @author xpzsoft
     * @Description 检查是否包含该方法
     * @param {methodname:[要检查的方法的名称]}
     * @return 是否包含该方法
     * @throws
     */
	private boolean checkMethodName(String methodname){
		if(methodname == null || methodname.length() == 0 || methodname.equals("Handle"))
			return false;
		for(int i = 0; i < methodname.length(); i++){
			char c = methodname.charAt(i);
			if(!((c >='0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_')){
				return false;
			}
		}
		return true;
	}
	
	/**
     * @author xpzsoft
     * @Description 调用方法
     * @param {methodname:[要检查的方法的名称], classes:[类型数组], objects:[参数列表]}
     * @return 是否包含该方法
     * @throws
     */
	private boolean invorkMethod(String methodname, Class<?>[] classes,  Object... objects){
		try {
			if(classes.length != objects.length)
				return false;
			
			Method m = this.getClass().getDeclaredMethod(methodname, classes);
			if(m == null){
				try{
					throw new RuntimeException("not find method '" + methodname + "' in class " + this.getClass().getName());
				}
				catch(RuntimeException e){
					e.printStackTrace();
					log.error(e.getMessage());
				}
				return false;
			}
			
			try {
				m.setAccessible(true);
				if(objects.length == 1)
					m.invoke(this, objects[0]);
				else if(objects.length == 2)
					m.invoke(this, objects[0], objects[1]);
					
				return true;
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e.getMessage());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e.getMessage());
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e.getMessage());
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return false;
	}
}
