/**
 * 
 */
/**
 * @author xpzsoft
 *
 */
package org.xm.api.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.xm.api.com.imagecode.ImageCode;
import org.xm.api.core.asy.AsyAfterHandlePool;
import org.xm.api.core.auth.AuthTokenItem;
import org.xm.api.core.auth.AuthTokenUser;
import org.xm.api.core.handler.HandlerCheckItem;
import org.xm.api.rt.ReturnAlert;
import org.xm.api.rt.ReturnItem;
import org.xm.api.server.ApplicationStart;
import org.xm.api.server.ServerConfig;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class BeanConfig{
	@Bean
	@Scope("prototype")
	public AuthTokenUser getAuthTokenUser(){
		return new AuthTokenUser();
	}
	
	@Bean
	@Scope("prototype")
	public AuthTokenItem getAuthTokenItem(){
		return new AuthTokenItem();
	}
	
	@Bean
    @Scope("prototype")
    public ReturnItem getReturnItem(){
        return new ReturnItem();
    }

    @Bean
    @Scope("prototype")
    public ReturnItem getReturnItem(String arg1, String arg2){
        return new ReturnItem(arg1, arg2);
    }
	
	@Bean(initMethod = "readHtmls")
	@Scope("singleton")
	public ReturnAlert getReturnAlert(){
		return new ReturnAlert();
	}
	
	@Bean
	@Scope("prototype")
	public HandlerCheckItem getHandlerCheckItem(){
		return new HandlerCheckItem();
	}
	
	@Bean
	@Scope("singleton")
	public ObjectMapper getObjectMapper(){
		return new ObjectMapper();
	}
	
	@Bean
	@Scope("singleton")
	public ImageCode getImageCode(){
		return new ImageCode();
	}
	
	@Bean
	@Scope("singleton")
	public AsyAfterHandlePool getAsyAfterHandlePool(){
		return new AsyAfterHandlePool();
	}
	
	@Bean(initMethod = "loadConfig")
	@Scope("singleton")
	public ServerConfig getServerConfig(){
		return new ServerConfig();
	}
	
	@Bean(initMethod = "start")
	@Scope("singleton")
	public ApplicationStart getApplicationStart(ServerConfig serverConfig){
		return new ApplicationStart(serverConfig);
	}
}