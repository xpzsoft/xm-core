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
     * @author xpzsoft
     * @Description 读取注解，完成配置初始化
     * @param {resourceInfo:[资源信息], context:[上下文信息]}
     * @return
     * @throws
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
     * @author xpzsoft
     * @Description 验证api权限
     * @param {path:[api路径], authcode:[权限值]}
     * @return 是否满足权限要求
     * @throws
     */
	public static boolean doVerificationPermissions(String path, int authcode){
		if(auth.get(path) !=null && Integer.valueOf(auth.get(path).toString()) <= authcode){
			return true;
		}
		return false;
	}
	
	/**
     * @author xpzsoft
     * @Description 验证请求参数是否为json格式
     * @param {path:[api路径]}
     * @return 是否json格式
     * @throws
     */
	public static boolean doVerificationNoApplicationJson(String path){
		return req_file.get(path);
	}
}
