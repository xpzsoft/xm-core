package org.xm.api.jersey.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;

import org.xm.api.core.auth.AuthToken;
import org.xm.api.core.auth.AuthTokenUser;
import org.xm.api.rt.ReturnAlert;
import org.xm.api.rt.ReturnCode;
import org.xm.api.rt.ReturnItem;
import org.xm.api.springcontext.SpringContext;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */
@Priority(Priorities.AUTHENTICATION)
public class VerificationFilter implements ContainerRequestFilter{
	//HttpServletRequest 客户端请求对象
	@Context  
    HttpServletRequest webRequest;
	
	/**
     * @author xpzsoft
     * @Description 请求过滤
     * @param {request:[客户端请求对象]}
     * @return
     * @throws
     */
	public void filter(ContainerRequestContext request) throws IOException {
		// TODO Auto-generated method stub
		
		/*
		 * 获取请求的RESTful api路径
		 */
		String path = request.getUriInfo().getPath();
		if(path == null || path.length() == 0){
			try {
				throw new Exception("Request path is invalid !");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/*
		 * 查找token
		 */
		
		//从header中查找token
		String token = request.getHeaderString(AuthToken.getTOKEN_NAME());
		
		//从GET、POST请求的Parameter中查找token
		if(token == null){
			token = webRequest.getParameter(AuthToken.getTOKEN_NAME());
		}
		
		//从form表单中查找token
		if(token == null){
			String contenttype = request.getHeaderString("Content-Type");
			//只解析application/x-www-form-urlencoded类型的表单
			if(contenttype !=null && contenttype.equals("application/x-www-form-urlencoded")){
				InputStream is = request.getEntityStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
		        StringBuilder sb = new StringBuilder();
		        String line = null;   
		        
		        try {   
		            while ((line = reader.readLine()) != null) {   
		                sb.append(line);   
		            }   
		            is.close();
		            line = URLDecoder.decode(sb.toString(),"utf-8");
		            String arr [] =line.split("&");
		            String ht = AuthToken.getTOKEN_NAME() + "=";
		            for(String str : arr){
		            	if(str.startsWith(ht)){
		            		token = str.substring(ht.length());
		            		break;
		            	}
		            }
		        } catch (IOException e) {
		            is.close();
		            e.printStackTrace();   
		        }
		        
		        InputStream in_withcode = new ByteArrayInputStream(sb.toString().getBytes("utf-8"));
		        request.setEntityStream(in_withcode);
			}
		}
		
		//从cookie中查找token
		if(token == null){
			Cookie mt = request.getCookies().get(AuthToken.getTOKEN_NAME());
			if(mt != null)
				token = request.getCookies().get(AuthToken.getTOKEN_NAME()).getValue();
		}
		
		if(token == null){
			if(VerificationFeature.doVerificationNoApplicationJson(path)){
				request.abortWith(Response.status(401).build());
			}
			else{
				ReturnItem ri = SpringContext.getContext().getBean("getReturnItem", ReturnItem.class);
				ri.setInfo(ReturnCode.XM_FAILED_TOKEN_OVERDUE, "[token] 认证失败，请重新登录！", ReturnAlert.ALERT_ERROR, "[token] 认证失败，请重新登录！");
				request.abortWith(Response.ok().entity(ri).build());
			}
			return;
		}
		else{
			AuthTokenUser auth_user = AuthToken.unsign(AuthToken.decodeXOR(token), AuthTokenUser.class);
			if(auth_user == null){
				if(VerificationFeature.doVerificationNoApplicationJson(path)){
					request.abortWith(Response.status(401).build());
				}
				else{
					ReturnItem ri = SpringContext.getContext().getBean("getReturnItem", ReturnItem.class);
					ri.setInfo(ReturnCode.XM_FAILED_TOKEN_OVERDUE, "[token] 认证失败！", ReturnAlert.ALERT_ERROR, "[token] 认证失败！");
					request.abortWith(Response.ok().entity(ri).build());
				}
				return;
			}
			else{
				webRequest.setAttribute("xm-token-id", auth_user.getTokenId());
				webRequest.setAttribute("xm-user-id", auth_user.getUserId());
			}
			
			//验证用户权限
			if(!VerificationFeature.doVerificationPermissions(path, auth_user.getAuthority())){
				if(VerificationFeature.doVerificationNoApplicationJson(path)){
					request.abortWith(Response.status(401).build());
				}
				else{
					ReturnItem ri = SpringContext.getContext().getBean("getReturnItem", ReturnItem.class);
					ri.setInfo(ReturnCode.XM_FAILED_AUTHORITY, "[" + path + "]没有访问权限！", ReturnAlert.ALERT_WARNING, "[" + path + "]没有访问权限！");
					request.abortWith(Response.ok().entity(ri).build());
				}
				return;
			}
			
			//检查是否单点登录
			if(!AuthToken.checkUser(auth_user)){
				if(VerificationFeature.doVerificationNoApplicationJson(path)){
					request.abortWith(Response.status(401).build());
				}
				else{
					ReturnItem ri = SpringContext.getContext().getBean("getReturnItem", ReturnItem.class);
					ri.setInfo(ReturnCode.XM_FAILED_AUTHORITY, "账号已经登录！", ReturnAlert.ALERT_WARNING, "账号已经登录！");
					request.abortWith(Response.ok().entity(ri).build());
				}
				return;
			}
		}
	}
}
