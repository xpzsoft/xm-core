package org.xm.api.com.imagecode;

public class ImageCodeItem {
	private String code = null;
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
