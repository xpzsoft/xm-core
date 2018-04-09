/**
 * 
 */
/**
 * @author xpzsoft
 *
 */
package org.xm.api.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */
public class ServerConfig{
	private static final Logger log = LoggerFactory.getLogger(ServerConfig.class);
	
	//config.xml配置文件路径
	private static final String path = "config/config.xml";
	
	//Web App名称
	private String server_name = "/";
	//Web App host地址
	private String server_host = "127.0.0.1";
	//Web App web.xml配置文件路径
	private String server_config = "config/web/web.xml";
	//Web App 根目录
	private String server_root = "webapp";
	//Web App 虚拟主机地址
	private List<String> server_virtualhost  = new ArrayList<String>();
	//Web App http协议访问端口
	private int http_port = 8888;
	//Web App http协议访问最长延时
	private int http_timeout = 30000;
	//Web App https协议访问端口
	private int https_port = 9999;
	//Web App https协议访问延时
	private int https_timeout= 30000;
	//Web App https协议密码
	private String https_pw1 = "123456";
	//Web App https协议密码
	private String https_pw2 = "123456";
	//Web App http协议是否启用
	private boolean http_enable = true;
	//Web App https协议是否启用
	private boolean https_enable = true;
	
	/**
	 * 加载配置文件
     * @author xpzsoft
     * @return boolean
     * @throws Exception 读取文件失败异常
     */
	public boolean loadConfig() throws Exception{
		File file = new File(path);
		if(!file.exists())
			return false;
		
		SAXReader reader = new SAXReader();   
		try {
			Document document = reader.read(file);
			Element root = document.getRootElement();
			parseData(root);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 解析配置文件
     * @author xpzsoft
     * @Description 
     * @param root XML文档根节点
     */
	@SuppressWarnings("unchecked")
	private void parseData(Element root) throws Exception{
		for(Element item : (List<Element>)root.elements()){
			String value = item.getTextTrim();
			if(value != null && value.length() > 0){
				if(item.getName().equals("web-name"))
					this.server_name = "/" + value;
				else if(item.getName().equals("web-config"))
					this.server_config = value;
				else if(item.getName().equals("web-root"))
					this.server_root = value;
				else if(item.getName().equals("web-host"))
					this.server_host = value;
			}
			
			if(item.getName().equals("web-http")){
				String port = item.attributeValue("port");
				String timeout = item.attributeValue("timeout");
				String enable = item.attributeValue("enable");
				if(port != null && port.trim() != null && port.trim().length() > 0){
					this.http_port = Integer.valueOf(port.trim());
				}
				if(timeout != null && timeout.trim() != null && timeout.trim().length() > 0){
					this.http_timeout = Integer.valueOf(timeout.trim());
				}
				if(enable != null && enable.trim() != null && enable.trim().equals("false")){
					this.http_enable = false;
				}
			}
			
			if(item.getName().equals("web-https")){
				String port = item.attributeValue("port");
				String timeout = item.attributeValue("timeout");
				String enable = item.attributeValue("enable");
				String pw1 = item.attributeValue("password1");
				String pw2 = item.attributeValue("password2");
				if(port != null && port.trim() != null && port.trim().length() > 0){
					this.https_port = Integer.valueOf(port.trim());
				}
				if(timeout != null && timeout.trim() != null && timeout.trim().length() > 0){
					this.https_timeout = Integer.valueOf(timeout.trim());
				}
				if(enable != null && enable.trim() != null && enable.trim().equals("false")){
					this.https_enable = false;
				}
				if(pw1 != null && pw1.trim() != null){
					this.https_pw1 = pw1.trim();
				}
				if(pw2 != null && pw2.trim() != null){
					this.https_pw2 = pw2.trim();
				}
			}
			
			if(item.getName().equals("web-virtualhost")){
				for(Element vh : (List<Element>)item.elements()){
					if(vh.getName().equals("vh")){
						value = vh.getTextTrim();
						if(value != null && value.length() > 0){
							this.server_virtualhost.add(value);
						}
					}
				}
			}
		}
		
		if(this.http_enable && this.https_enable && this.http_port == this.https_port){
			log.error("https port and https port is conflict!");
			throw new Exception("http port and https port is conflict, they should not be same!");
		}
		
		if(!this.http_enable && !this.https_enable){
			this.http_enable = true;
		}
	}

	public String getServer_name() {
		return server_name;
	}

	public String getServer_host() {
		return server_host;
	}

	public String getServer_config() {
		return server_config;
	}

	public String getServer_root() {
		return server_root;
	}

	public List<String> getServer_virtualhost() {
		return server_virtualhost;
	}

	public int getHttp_port() {
		return http_port;
	}

	public int getHttp_timeout() {
		return http_timeout;
	}

	public int getHttps_port() {
		return https_port;
	}

	public int getHttps_timeout() {
		return https_timeout;
	}

	public String getHttps_pw1() {
		return https_pw1;
	}

	public String getHttps_pw2() {
		return https_pw2;
	}

	public boolean isHttp_enable() {
		return http_enable;
	}

	public boolean isHttps_enable() {
		return https_enable;
	}
}