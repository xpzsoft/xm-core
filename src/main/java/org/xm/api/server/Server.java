package org.xm.api.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xm.api.core.handler.Handler;
import org.xm.api.core.handler.HandlerFactory;
import org.xm.api.jersey.config.RestApplication;
import org.xm.api.springcontext.SpringContext;

/**
 * 
 * @author xpzsoft
 * @Description Web App Server类
 * @version 1.2.0
 */
public class Server {
	private static final Logger log = LoggerFactory.getLogger(Server.class);
	
	//spring配置文件地址
	private String path_spring = "file:config/spring/applicationContext.xml";
	//处理器列表
	private Map<Class<?>, String[]> handlers = new HashMap<Class<?>, String[]>();
	
	/**
     * @author xpzsoft
     * @Description Server构造器
     * @param {packages:[Jersey扫描包的数组]}
     * @return Server实例
     * @throws
     */
	public Server(String packages[]){
		RestApplication.addRestServicePakages(new String []{"org.xm.api.jersey.filter"});
		if(packages != null)
			RestApplication.addRestServicePakages(packages);
	}
	
	/**
     * @author xpzsoft
     * @Description Server构造器
     * @param {packages:[Jersey扫描包的数组], types:[Jersey要注册的类型]}
     * @return Server实例
     * @throws
     */
	public Server(String packages[], Class<?>[] types){
		RestApplication.addRestServicePakages(new String []{"org.xm.api.jersey.filter"});
		if(packages != null)
			RestApplication.addRestServicePakages(packages);
		if(types != null)
			RestApplication.addRegisterClass(types);
	}
	
	/**
     * @author xpzsoft
     * @Description Server构造器
     * @param {packages:[Jersey扫描包的数组], types:[Jersey要注册的类型], path_spring:[Spring配置文件路径]}
     * @return Server实例
     * @throws
     */
	public Server(String packages[], Class<?>[] types, String path_spring){
		this.path_spring = path_spring;
		RestApplication.addRestServicePakages(new String []{"org.xm.api.jersey.filter"});
		if(packages != null)
			RestApplication.addRestServicePakages(packages);
		if(types != null)
			RestApplication.addRegisterClass(types);
	}
	
	/**
     * @author xpzsoft
     * @Description 处理器注册
     * @param {hander:[处理器类], names:[处理器名称数组]}
     * @return Server实例
     * @throws
     */
	public Server registHandler(Class<?> hander, String names[]){
		handlers.put(hander, names);
		return this;
	}
	
	/**
     * @author xpzsoft
     * @Description 执行处理器注册
     */
	@SuppressWarnings("rawtypes")
	private void registHandler(){
		Iterator iter = handlers.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Class<?> key = (Class<?>)entry.getKey();
			String[] val = (String[])entry.getValue();
			((Handler)HandlerFactory.getHandler(key)).registerHandler(val);
		}
	}
	
	/**
     * @author xpzsoft
     * @Description 启动Server
     */
	@SuppressWarnings("resource")
	public void run(){
		//启动Spring
	    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(  
	            new String[] {this.path_spring});  
	    applicationContext.registerShutdownHook();

	    ApplicationStart as = SpringContext.getContext().getBean("getApplicationStart", ApplicationStart.class);
	    if(as.getIsOK()){
	    	registHandler();
	    	ServerConfig serverConfig = SpringContext.getContext().getBean("getServerConfig", ServerConfig.class);
		    log.info("Web Start Successfully! You can Access your WebSite by: ");
	        if(serverConfig.getServer_virtualhost().size() > 0){
	        	for(String vh : serverConfig.getServer_virtualhost()){
	        		System.out.println("http://" + vh);
	        	}
	        }
	        
	        if(serverConfig.isHttp_enable()){
	        	log.info("http://" + serverConfig.getServer_host() + ":" + serverConfig.getHttp_port() + serverConfig.getServer_name());
	        }
	        if(serverConfig.isHttps_enable()){
	        	log.info("https://" + serverConfig.getServer_host() + ":" + serverConfig.getHttps_port() + serverConfig.getServer_name());
	        }
	    }
	}
}
