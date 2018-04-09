package org.xm.api.jersey.filter;

import java.util.HashMap;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import org.springframework.http.MediaType;
import org.xm.api.core.auth.AuthAnnotation;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */
@Provider
public class VerificationFeature implements DynamicFeature{
	//{key:RESTful api, value:权限}哈希表
	private static HashMap<String, Integer> auth = new HashMap<String, Integer>();
	//{key:RESTful api, value:请求参数是否为APPLICATION_JSON_VALUE格式}哈希表
	private static HashMap<String, Boolean> req_file = new HashMap<String, Boolean>();
	
	/**
	 * 读取注解并完成配置初始化
     * @author xpzsoft
     * @param resourceInfo 资源信息
     * @param context 上下文信息
     */
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
         
		AuthAnnotation methodAuthzSpec = resourceInfo.getResourceMethod().getAnnotation(AuthAnnotation.class);

        if (methodAuthzSpec != null){
        	String f = resourceInfo.getResourceClass().getAnnotation(Path.class).value();
            String g = resourceInfo.getResourceMethod().getAnnotation(Path.class).value();
            if(!g.startsWith("/")){
            	g = "/" + g;
            }
            int authcode = methodAuthzSpec.value().getValue();
            auth.put(f + g, authcode);
            
            Produces prc = resourceInfo.getResourceMethod().getAnnotation(Produces.class);
            req_file.put(f + g, false);
            if(prc != null){
            	for(String str : prc.value()){
            		if(!str.equals(MediaType.APPLICATION_JSON_VALUE)){
            			req_file.put(f + g, true);
            		}
            	}
            }
        	context.register(VerificationFilter.class);
        }
    }
	
	/**
	 * 验证api权限
     * @author xpzsoft
     * @param path api路径
     * @param authcode 权限值
     * @return boolean
     */
	public static boolean doVerificationPermissions(String path, int authcode){
		if(auth.get(path) !=null && Integer.valueOf(auth.get(path).toString()) <= authcode){
			return true;
		}
		return false;
	}
	
	/**
	 * 验证请求参数是否为json格式
     * @author xpzsoft
     * @param path api路径
     * @return boolean
     */
	public static boolean doVerificationNoApplicationJson(String path){
		return req_file.get(path);
	}
}
