package org.xm.api.rt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */
public class ReturnAlert {
	private static final Logger log = LoggerFactory.getLogger(ReturnAlert.class);
	
	//弹出框类型：默认
	public static final int ALERT_DEFAULT = 0;
	//弹出框类型：成功
	public static final int ALERT_SUCCESS = 1;
	//弹出框类型：信息
	public static final int ALERT_INFO = 2;
	//弹出框类型：警告
	public static final int ALERT_WARNING = 3;
	//弹出框类型：错误
	public static final int ALERT_ERROR = 4;
	
	//五种类型的弹出框对应的html脚本代码
	private String [] htmls = new String[5];
	//五中类型的弹出框脚本文件地址
	private final String []paths = {"/alert/alert-default.html", "/alert/alert-success.html", "/alert/alert-info.html", "/alert/alert-warning.html", "/alert/alert-error.html"};
	
	/**
	 * 读取弹出框脚本
     * @author xpzsoft
     */
	public void readHtmls(){
		for(int i = 0; i < paths.length; i++){
			
            InputStream is = this.getClass().getResourceAsStream(paths[i]);
            BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				log.error(e1.getMessage());
				continue;
			}
            
            StringBuffer sb= new StringBuffer("");
            String str = null;
            try {
				 while((str = br.readLine()) != null) {
				      sb.append(str);;
				 }
				 htmls[i] = sb.toString();
				 br.close();
				 is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
	}
	
	/**
	 * 获取弹出框脚本
     * @author xpzsoft
     * @param alert_type 弹出框类型
     * @param msg 弹出框显示的信息
     * @return String
     */
	public String getAlert(int alert_type, String msg){
		if(alert_type < 0 || alert_type > 4)
			alert_type = 0;

		String html = htmls[alert_type];
		return html.replaceFirst("xm_text", msg).replaceAll("xm_alertdscdsfsfd", UUID.randomUUID().toString());
	}
}
