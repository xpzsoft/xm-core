package org.xm.api.core;

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
import org.xm.api.core.asy.AsyAfterHanlePool;
import org.xm.api.rt.ReturnCode;
import org.xm.api.rt.ReturnItem;
import org.xm.api.springcontext.ConstSpringContext;

public abstract class HandlerDefault implements Handler{
	private static final Logger log = LoggerFactory.getLogger(HandlerDefault.class);
	private Map<String, CheckItem> checks = new HashMap<String, CheckItem>();
	private Set<String> handler_names = null;
	private AsyAfterHanlePool pool = null;
	
	public HandlerDefault(){
		checks.put("Handle", new CheckItem());
		checkAnno("afterHandle", checks.get("Handle"));
	}
	
	public HandlerDefault(String [] names){
		checks.put("Handle", new CheckItem());
		checkAnno("afterHandle", checks.get("Handle"));
		registerHandler(names);
	}
	
	@Override
	public ReturnItem handle() {
		return handle(null);
	}
	
	@Override
	public <T> ReturnItem handle(T arg) {
		// TODO Auto-generated method stub
		ReturnItem ri = ConstSpringContext.getBean(ReturnItem.class);
		
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
					pool = ConstSpringContext.getBean(AsyAfterHanlePool.class);
				if(!pool.addTask(this, "afterHandle", ri)){
					log.error("xm : ASY after handle can't be deal.");
				}
				break;
			default:
				break;
		}	
		return ri;
	}
	
	@Override
	public ReturnItem handle(Object arg, String handler_name) {
		// TODO Auto-generated method stub
		
		if(handler_names == null || !handler_names.contains(handler_name)){
			throw new RuntimeException("'" + handler_name + "' is not registed!");
		}
		
		String beforeHandle = "before" + handler_name;
		String inHandle = "in" + handler_name;
		String afterHandle = "after" + handler_name;
		
		ReturnItem ri = ConstSpringContext.getBean(ReturnItem.class);
		
		CheckItem check = checks.get(handler_name);
		
		if(check.isIsbefore_exist() && !invorkMethod(beforeHandle, new Class<?>[]{Object.class, ReturnItem.class}, arg, ri)){
			ri.setInfo(ReturnCode.XM_FAILED_EXCEPTION, "invoke '" + beforeHandle +"' failed!");
		}
		if(!ri.isIgnore() && ri.getCode() != ReturnCode.XM_SUCCESS)
			return ri;
		
		if(!invorkMethod(inHandle, new Class<?>[]{ReturnItem.class}, ri)){
			ri.setInfo(ReturnCode.XM_FAILED_EXCEPTION, "invoke '" + inHandle +"' failed!");
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
						pool = ConstSpringContext.getBean(AsyAfterHanlePool.class);
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
	
	public ReturnItem handle2(HttpServletRequest requet){
		return handle2(requet, null);
	}
	
	public <T> ReturnItem handle2(HttpServletRequest requet, T arg){
		// TODO Auto-generated method stub
		ReturnItem ri = ConstSpringContext.getContext().getBean(ReturnItem.class, requet.getAttribute("xm-token-id"), requet.getAttribute("xm-user-id"));
		
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
					pool = ConstSpringContext.getBean(AsyAfterHanlePool.class);
				if(!pool.addTask(this, "afterHandle", ri)){
					log.error("xm : ASY after handle can't be deal.");
				}
				break;
			default:
				break;
		}
		
		return ri;
	}
	
	public ReturnItem handle2(HttpServletRequest requet, Object arg, String handler_name){
		if(handler_names == null || !handler_names.contains(handler_name)){
			throw new RuntimeException("'" + handler_name + "' is not registed!");
		}
		
		String beforeHandle = "before" + handler_name;
		String inHandle = "in" + handler_name;
		String afterHandle = "after" + handler_name;
		
		ReturnItem ri = ConstSpringContext.getContext().getBean(ReturnItem.class, requet.getAttribute("xm-token-id"), requet.getAttribute("xm-user-id"));
		
		CheckItem check = checks.get(handler_name);
		
		if(check.isIsbefore_exist() && !invorkMethod(beforeHandle, new Class<?>[]{Object.class, ReturnItem.class}, arg, ri)){
			ri.setInfo(ReturnCode.XM_FAILED_EXCEPTION, "invoke '" + beforeHandle +"' failed!");
		}
		if(!ri.isIgnore() && ri.getCode() != ReturnCode.XM_SUCCESS)
			return ri;
		
		if(!invorkMethod(inHandle, new Class<?>[]{ReturnItem.class}, ri)){
			ri.setInfo(ReturnCode.XM_FAILED_EXCEPTION, "invoke '" + inHandle +"' failed!");
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
						pool = ConstSpringContext.getBean(AsyAfterHanlePool.class);
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
			CheckItem check = new CheckItem();
			checks.put(name, check);
			checkFunofBeforAndAfter(beforeHandle, inHandle, afterHandle, check);
			if(check.isIsafter_exist())
				checkAnno(afterHandle, check);
		}
	}

	@Override
	public void handleAfterAsy(String methodname, ReturnItem arg) {
		// TODO Auto-generated method stub
		if(!invorkMethod(methodname, new Class<?>[]{ReturnItem.class}, arg)){
			log.error("xm: invoke '" + methodname + "' failed!");
		}
	}
	
	protected abstract <T> void beforeHandle(T arg, ReturnItem ri);
	
	protected abstract void inHandle(ReturnItem ri);
	
	protected abstract void afterHandle(ReturnItem ri);
	
	private void checkFunofBeforAndAfter(String beforename, String inname, String aftername, CheckItem check){
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
	
	private void checkAnno(String afterhandl, CheckItem check){
		try {
			Method m = this.getClass().getDeclaredMethod(afterhandl, new Class<?>[]{ReturnItem.class});
			if(m != null){
				HandleAnnotion ha = m.getAnnotation(HandleAnnotion.class);
				if(ha == null)
					check.setIsaffter((byte) 0);
				else if(ha.value() == HandleAnnotion.AfterType.SYN){
					check.setIsaffter((byte) 1);
				}
				else if(ha.value() == HandleAnnotion.AfterType.ASY){
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
	
	private boolean invorkMethod(String methodname, Class<?>[] classes,  Object... objects){
		try {
			Method m = null;
			if(objects.length == 1)
				m = this.getClass().getDeclaredMethod(methodname, classes);
			else if(objects.length == 2)
				m = this.getClass().getDeclaredMethod(methodname, classes);
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
