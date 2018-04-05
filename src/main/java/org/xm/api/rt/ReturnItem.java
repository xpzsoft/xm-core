package org.xm.api.rt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.xm.api.springcontext.SpringContext;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */
public class ReturnItem {
	//处理结果标识码
	private int code = ReturnCode.XM_SUCCESS;
	//处理结果返回的值
	private Object value = null;
	//处理结果返回的弹出框脚本
	private String alert = null;
	
	//本次请求的token id
	@JsonIgnore
	private String tokeId = null;
	//本次请求的用户id
	@JsonIgnore
	private String userId = null;
	//本次请求处理过程中传递的参数
	@JsonIgnore
	private List<Object> params = null;
	//取参数时中间变量
	@JsonIgnore
	private Object nitem = null;
	//本次请求是否过滤code不等于ReturnCode.XM_SUCCESS的情况
	@JsonIgnore
	private Boolean ignore = false;
	
	/**
	 * 处理结果返回对象构造器
     * @author xpzsoft
     */
	public ReturnItem(){}
	
	/**
	 * 处理结果返回对象带参构造器
     * @author xpzsoft
     * @param tokenid 本次请求的tokenId
     * @param userid 本次请求的userId
     */
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
	
	/**
	 * 设置弹出框
     * @author xpzsoft
     * @param alert_type 弹出框类型
     * @param msg 弹出框显示的文本
     */
	public void setAlert(int alert_type, String msg) {
		this.alert = SpringContext.getContext().getBean("getReturnAlert", ReturnAlert.class).getAlert(alert_type, msg);
	}
	
	/**
	 * 设置处理结果和返回对象
     * @author xpzsoft
     * @param code 处理结果标识码
     * @param value 返回对象
     */
	public void setInfo(int code, Object value){
		this.code = code;
		this.value = value;
	}
	
	/**
	 * 设置处理结果、返回对象和弹出框
     * @author xpzsoft
     * @param code 处理结果标识码
     * @param value 返回对象
     * @param alert_type 弹出框类型
     * @param msg 弹出框显示的文本
     */
	public void setInfo(int code, Object value, int alert_type, String msg){
		this.code = code;
		this.value = value;
		this.alert = SpringContext.getContext().getBean("getReturnAlert", ReturnAlert.class).getAlert(alert_type, msg);
	}
	
	public String getTokeId() {
		return tokeId;
	}

	public String getUserId() {
		return userId;
	}
	
	/**
	 * 获取参数
     * @author xpzsoft 
     * @param index 参数索引
     * @return ReturnItem
     */
	public ReturnItem get(int index){
		nitem = this.params.get(index);
		return this;
	}
	
	/**
	 * 添加参数
     * @author xpzsoft
     * @param obj 参数实例
     * @return ReturnItem
     */
	public ReturnItem addParam(Object obj){
		this.getParams().add(obj);
		return this;
	}
	
	private List<Object> getParams() {
		if(params == null){
			params = new ArrayList<Object>();
		}
		return params;
	}
	
	/**
	 * 将获取的参数转换为Byte类型
     * @author xpzsoft
     * @return Byte
     */
	public Byte toByte(){
		return (Byte)nitem;
	}
	
	/**
	 * 将获取的参数转换为Short类型
     * @author xpzsoft
     * @return Short
     */
	public Short toShort(){
		return (Short)nitem;
	}
	
	/**
	 * 将获取的参数转换为Integer类型
     * @author xpzsoft
     * @return Integer
     */
	public Integer toInteger(){
		return (Integer)nitem;
	}
	
	/**
	 * 将获取的参数转换为Float类型
     * @author xpzsoft
     * @return Float
     */
	public Float toFloat(){
		return (Float)nitem;
	}
	
	/**
	 * 将获取的参数转换为Double类型
     * @author xpzsoft
     * @return Double
     */
	public Double toDouble(){
		return (Double)nitem;
	}
	
	/**
	 * 将获取的参数转换为Long类型
     * @author xpzsoft
     * @return Long
     */
	public Long toLong(){
		return (Long)nitem;
	}
	
	/**
	 * 将获取的参数转换为BigInteger类型
     * @author xpzsoft
     * @return BigInteger
     */
	public BigInteger toBigInteger(){
		return new BigInteger(nitem.toString());
	}
	
	/**
	 * 将获取的参数转换为BigDecimal类型
     * @author xpzsoft
     * @return BigDecimal
     */
	public BigDecimal toBigDecimal(){
		return new BigDecimal(nitem.toString());
	}
	
	/**
	 * 将获取的参数转换为String类型
     * @author xpzsoft
     * @return String
     */
	public String toString(){
		return (String)nitem;
	}
	
	/**
	 * 将获取的参数转换为Boolean类型
     * @author xpzsoft
     * @return Boolean
     */
	public Boolean toBoolean(){
		return (Boolean)nitem;
	}
	
	/**
	 * 将获取的参数转换为Object类型
     * @author xpzsoft
     * @return Object
     */
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
