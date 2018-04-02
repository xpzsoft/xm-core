package org.xm.api.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xm.api.core.Handler;
import org.xm.api.core.HandlerFactory;
import org.xm.api.jersey.config.RestApplication;
import org.xm.api.springcontext.ConstSpringContext;

public class Server {
	private static final Logger log = LoggerFactory.getLogger(Server.class);
	
	private String path_spring = "file:config/spring/applicationContext.xml";
	private Map<Class<?>, String[]> handlers = new HashMap<Class<?>, String[]>();
	
	public Server(String packages[]){
		RestApplication.addRestServicePakages(new String []{"org.xm.api.jersey.filter"});
		if(packages != null)
			RestApplication.addRestServicePakages(packages);
	}
	
	public Server(String packages[], Class<?>[] types){
		RestApplication.addRestServicePakages(new String []{"org.xm.api.jersey.filter"});
		if(packages != null)
			RestApplication.addRestServicePakages(packages);
		if(types != null)
			RestApplication.addRegisterClass(types);
	}
	
	public Server(String packages[], Class<?>[] types, String path_spring){
		this.path_spring = path_spring;
		RestApplication.addRestServicePakages(new String []{"org.xm.api.jersey.filter"});
		if(packages != null)
			RestApplication.addRestServicePakages(packages);
		if(types != null)
			RestApplication.addRegisterClass(types);
	}
	
	public Server registHandler(Class<?> hander, String names[]){
		handlers.put(hander, names);
		return this;
	}
	
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
	
	@SuppressWarnings("resource")
	public void run(){
	    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(  
	            new String[] {this.path_spring});  
	    applicationContext.registerShutdownHook();
	    
	    ApplicationStart as = ConstSpringContext.getBean(ApplicationStart.class);
	    if(as.getIsOK()){
	    	registHandler();
	    	ServerConfig serverConfig = ConstSpringContext.getBean(ServerConfig.class);
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
