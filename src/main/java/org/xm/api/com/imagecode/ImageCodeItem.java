package org.xm.api.com.imagecode;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */
public class ImageCodeItem {
	//验证码的值
	private String code = null;
	//base64格式的验证码图片
	private String imgBase64 = null;
	
	public ImageCodeItem(String code, String imgBase64){
		this.code = code;
		this.imgBase64 = imgBase64;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getImgBase64() {
		return imgBase64;
	}
	
	public void setImgBase64(String imgBase64) {
		this.imgBase64 = imgBase64;
	}
}
