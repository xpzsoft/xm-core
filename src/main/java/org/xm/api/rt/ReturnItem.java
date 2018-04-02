package org.xm.api.rt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.xm.api.springcontext.ConstSpringContext;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ReturnItem {
	private int code = ReturnCode.XM_SUCCESS;
	private Object value = null;
	private String alert = null;
	
	@JsonIgnore
	private String tokeId = null;
	@JsonIgnore
	private String userId = null;
	@JsonIgnore
	private List<Object> params = null;
	@JsonIgnore
	private Object nitem = null;
	@JsonIgnore
	private Boolean ignore = false;
	
	public ReturnItem(){
		
	}
	
	public ReturnItem(String tokenid, String userid){
		this.tokeId = tokenid;
		this.userId = userid;
	}
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public String getAlert() {
		return alert;
	}
	
	public void setAlert(int alert_type, String msg) {
		this.alert = ConstSpringContext.getBean(ReturnAlert.class).getAlert(alert_type, msg);
	}
	
	public void setInfo(int code, Object value){
		this.code = code;
		this.value = value;
	}
	
	public void setInfo(int code, Object value, int alert_type, String msg){
		this.code = code;
		this.value = value;
		this.alert = ConstSpringContext.getBean(ReturnAlert.class).getAlert(alert_type, msg);
	}
	
	public String getTokeId() {
		return tokeId;
	}

	public String getUserId() {
		return userId;
	}

	public List<Object> getParams() {
		if(params == null){
			params = new ArrayList<Object>();
		}
		return params;
	}
	
	public ReturnItem get(int index){
		nitem = this.params.get(index);
		return this;
	}
	
	public ReturnItem addParam(Object obj){
		this.getParams().add(obj);
		return this;
	}
	
	public Byte toByte(){
		return (Byte)nitem;
	}
	public Short toShort(){
		return (Short)nitem;
	}
	public Integer toInteger(){
		return (Integer)nitem;
	}
	public Float toFloat(){
		return (Float)nitem;
	}
	public Double toDouble(){
		return (Double)nitem;
	}
	public Long toLong(){
		return (Long)nitem;
	}
	public BigInteger toBigInteger(){
		return new BigInteger(nitem.toString());
	}
	public BigDecimal toBigDecimal(){
		return new BigDecimal(nitem.toString());
	}
	public String toString(){
		return (String)nitem;
	}
	public Boolean toBoolean(){
		return (Boolean)nitem;
	}
	public Object toObject(){
		return nitem;
	}

	public Boolean isIgnore() {
		return ignore;
	}

	public void setIgnore(Boolean ignore) {
		this.ignore = ignore;
	}
	
}
