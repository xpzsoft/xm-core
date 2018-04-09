package org.xm.api.server;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
//import org.eclipse.jetty.server.nio.SelectChannelConnector;  
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;  
import org.springframework.context.ApplicationContext;  
import org.springframework.context.ApplicationContextAware;  
import org.springframework.web.context.WebApplicationContext;  
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.xm.api.springcontext.SpringContext;

/**
 * WebApp启动类
 * @author xpzsoft
 * @version 1.2.0
 */
public class ApplicationStart implements ApplicationContextAware{
	private static final Logger log = LoggerFactory.getLogger(ApplicationStart.class);
	//web app的服务器对象
	private Server server;
	//web app的服务器配置
	private ServerConfig serverConfig = null;
	//web app的上下文
    private ApplicationContext applicationContext;
    //web app是否成功启动
    private boolean isOK = true; 
    
    /**
     * ApplicationStart构造器
     * @author xpzsoft
     * @param serverConfig 服务器配置项]}
     */
    public ApplicationStart(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}
    
    /**
     * 设置spring conetxt，spring与jetty共用context
     * @author xpzsoft
     * @param applicationContext WebApp上下文
     */
    public void setApplicationContext(ApplicationContext applicationContext)  
            throws BeansException {
        this.applicationContext = applicationContext;  
          
    }
    
    public boolean getIsOK(){
    	return isOK;
    }
    
    /**
     * 启动WebApp
     * @author xpzsoft
     */
	public void start(){
		
		//关闭被占用的端口
		OldServerKiller.init(serverConfig.getHttp_port(), serverConfig.getHttps_port());
		
        server = new Server();
        
        //配置http协议参数
        if(serverConfig.isHttps_enable()){
        	HttpConfiguration https_config = new HttpConfiguration();
            https_config.setSecureScheme("https");

            SslContextFactory sslContextFactory = new SslContextFactory();
            sslContextFactory.setKeyStorePath(serverConfig.getServer_root() + "/keystore");
            // 私钥
            sslContextFactory.setKeyStorePassword(serverConfig.getHttps_pw1());
            // 公钥
            sslContextFactory.setKeyManagerPassword(serverConfig.getHttps_pw2());

            ServerConnector httpsConnector = new ServerConnector(server,
                    new SslConnectionFactory(sslContextFactory,"http/1.1"),
                    new HttpConnectionFactory(https_config));
                    // 设置访问端口
            httpsConnector.setPort(serverConfig.getHttps_port());
            httpsConnector.setIdleTimeout(serverConfig.getHttps_timeout());
            server.addConnector(httpsConnector);
        }
        
      //配置https协议参数
        if(serverConfig.isHttp_enable()){
        	ServerConnector connector = new ServerConnector(server);
            //SelectChannelConnector connector = new SelectChannelConnector(); 
            connector.setPort(serverConfig.getHttp_port());
            connector.setHost(serverConfig.getServer_host());
            connector.setIdleTimeout(serverConfig.getHttp_timeout());
            server.addConnector(connector);  
        }
        
        //配置Web App相关参数
        WebAppContext webAppContext = new WebAppContext();
        
        webAppContext.setContextPath(serverConfig.getServer_name());  
        webAppContext.setDescriptor(serverConfig.getServer_config());  
        webAppContext.setResourceBase(serverConfig.getServer_root());
        if(serverConfig.getServer_virtualhost().size() > 0){
        	String vh [] = new String[serverConfig.getServer_virtualhost().size()];
        	serverConfig.getServer_virtualhost().toArray(vh);
        	webAppContext.setVirtualHosts(vh);
        }
        webAppContext.setConfigurationDiscovered(true);  
        webAppContext.setParentLoaderPriority(true);
        webAppContext.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
        server.setHandler(webAppContext);

        //Jetty容器共享Spring上下文
        webAppContext.setClassLoader(applicationContext.getClassLoader());
          
        XmlWebApplicationContext xmlWebAppContext = new XmlWebApplicationContext();
        xmlWebAppContext.setParent(applicationContext);  
        xmlWebAppContext.setConfigLocation("");  
        xmlWebAppContext.setServletContext(webAppContext.getServletContext());  
        xmlWebAppContext.refresh();  
        SpringContext.setSpringcontext(xmlWebAppContext);
          
        webAppContext.setAttribute(  
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,  
                xmlWebAppContext);  
  
        try {
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			if(serverConfig.isHttps_enable()){
				log.error("please check whether the file 'keystore' is existed in path '" + serverConfig.getServer_root() + "/keystore'!");
			}
			isOK = false;
			e.printStackTrace();
		}
    }

}
