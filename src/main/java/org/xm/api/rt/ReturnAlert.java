package org.xm.api.rt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReturnAlert {
	private static final Logger log = LoggerFactory.getLogger(ReturnAlert.class);
	
	public static final int ALERT_DEFAULT = 0;
	public static final int ALERT_SUCCESS = 1;
	public static final int ALERT_INFO = 2;
	public static final int ALERT_WARNING = 3;
	public static final int ALERT_ERROR = 4;
	
	private String [] htmls = new String[5];
	private final String []paths = {"/alert/alert-default.html", "/alert/alert-success.html", "/alert/alert-info.html", "/alert/alert-warning.html", "/alert/alert-error.html"};

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
	
	public String getAlert(int alert_type, String msg){
		if(alert_type >= htmls.length){
			return "";
		}
		String html = htmls[alert_type];
		return html.replaceFirst("xm_text", msg).replaceAll("xm_alertdscdsfsfd", UUID.randomUUID().toString());
	}
}
