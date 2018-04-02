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
import org.xm.api.springcontext.ConstSpringContext;

public class ApplicationStart implements ApplicationContextAware{
	private static final Logger log = LoggerFactory.getLogger(ApplicationStart.class);
	private Server server;
	private ServerConfig serverConfig = null;
    private ApplicationContext applicationContext;
    private boolean isOK = true; 
    
    //获取spring conetxt
    public void setApplicationContext(ApplicationContext applicationContext)  
            throws BeansException {
        this.applicationContext = applicationContext;  
          
    }
    
    public void setServerConfig(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}
    
    public boolean getIsOK(){
    	return isOK;
    }

	public void start(){
//    	String path = App.class.getClassLoader().getResource("").getPath();
//        path = path.substring(1, path.length()- 16);

		OldServerKiller.init(serverConfig.getHttp_port(), serverConfig.getHttps_port());
		
        server = new Server();
        
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
        
        
        if(serverConfig.isHttp_enable()){
        	ServerConnector connector = new ServerConnector(server);
            //SelectChannelConnector connector = new SelectChannelConnector(); 
            connector.setPort(serverConfig.getHttp_port());
            connector.setHost(serverConfig.getServer_host());
            connector.setIdleTimeout(serverConfig.getHttp_timeout());
            server.addConnector(connector);  
        }
        
          
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

        // 以下代码是关键  
        webAppContext.setClassLoader(applicationContext.getClassLoader());
          
        XmlWebApplicationContext xmlWebAppContext = new XmlWebApplicationContext();
        xmlWebAppContext.setParent(applicationContext);  
        xmlWebAppContext.setConfigLocation("");  
        xmlWebAppContext.setServletContext(webAppContext.getServletContext());  
        xmlWebAppContext.refresh();  
        ConstSpringContext.setSpringcontext(xmlWebAppContext);
          
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
