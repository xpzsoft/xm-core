package org.xm.api.jersey.filter;

import java.util.HashMap;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import org.springframework.http.MediaType;
import org.xm.api.auth.AuthAnnotation;

@Provider
public class VerificationFeature implements DynamicFeature{
	private static HashMap<String, Integer> auth = new HashMap<String, Integer>();
	private static HashMap<String, Boolean> req_file = new HashMap<String, Boolean>();
	
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
	
	public static boolean doVerificationPermissions(String path, int authcode){
		if(auth.get(path) !=null && Integer.valueOf(auth.get(path).toString()) <= authcode){
			return true;
		}
		return false;
	}
	public static boolean doVerificationNoApplicationJson(String path){
		return req_file.get(path);
	}
}
