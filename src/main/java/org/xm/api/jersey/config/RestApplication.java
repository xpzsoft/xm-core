package org.xm.api.jersey.config;

import java.util.ArrayList;
import java.util.List;

import org.glassfish.jersey.server.ResourceConfig;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class RestApplication extends ResourceConfig{
	private static List<String> service_package = new ArrayList<String>();
	private static List<Class<?>> class_types = new ArrayList<Class<?>>();
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
	
	public static void addRestServicePakages(String args[]){
		for(String str : args)service_package.add(str);
	}
	
	public static void addRegisterClass(Class<?>[] tyeps){
		for(Class<?> type : tyeps)class_types.add(type);
	}
}
