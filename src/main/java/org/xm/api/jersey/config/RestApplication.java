package org.xm.api.jersey.config;

import java.util.ArrayList;
import java.util.List;

import org.glassfish.jersey.server.ResourceConfig;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 * 
 */
public class RestApplication extends ResourceConfig{
	//Jersey发布成为RESTful web service的类所在的包
	private static List<String> service_package = new ArrayList<String>();
	//Jersey要注册的类型
	private static List<Class<?>> class_types = new ArrayList<Class<?>>();
	
	/**
	 * Jersey配置构造器
     * @author xpzsoft
     */
	public RestApplication() {
        //给出要扫描的包, 扫描多个包使用分号隔开
		StringBuilder builder = new StringBuilder();
		for(String str : service_package){
			builder.append(str);
			builder.append(";");
		}
        packages(builder.toString());
        register(JacksonJsonProvider.class);
        for(Class<?> type : class_types){
        	register(type);
        }
    }
	
	/**
	 * 添加扫描的包
     * @author xpzsoft
     * @param args 保命数组
     */
	public static void addRestServicePakages(String args[]){
		for(String str : args)service_package.add(str);
	}
	
	/**
	 * 添加要注册的类型
     * @author xpzsoft
     */
	public static void addRegisterClass(Class<?>[] tyeps){
		for(Class<?> type : tyeps)class_types.add(type);
	}
}
