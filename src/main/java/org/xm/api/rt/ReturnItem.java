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
     * @author xpzsoft
     * @Description 处理结果返回对象构造器
     * @param 
     * @return 处理结果返回对象实例
     * @throws
     */
	public ReturnItem(){}
	
	/**
     * @author xpzsoft
     * @Description 处理结果返回对象带参构造器
     * @param {tokenid:[本次请求的tokenId], userid:[本次请求的userId]}
     * @return 处理结果返回对象实例
     * @throws
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
     * @author xpzsoft
     * @Description 设置弹出框
     * @param {alert_type:[弹出框类型], msg:[弹出框显示的文本]}
     * @return 
     * @throws
     */
	public void setAlert(int alert_type, String msg) {
		this.alert = SpringContext.getContext().getBean("getReturnAlert", ReturnAlert.class).getAlert(alert_type, msg);
	}
	
	/**
     * @author xpzsoft
     * @Description 设置处理结果和返回对象
     * @param {code:[处理结果标识码], value:[返回对象]}
     * @return
     * @throws
     */
	public void setInfo(int code, Object value){
		this.code = code;
		this.value = value;
	}
	
	/**
     * @author xpzsoft
     * @Description 设置处理结果、返回对象和弹出框
     * @param {code:[处理结果标识码], value:[返回对象], alert_type:[弹出框类型], msg:[弹出框显示的文本]}
     * @return 
     * @throws
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
     * @author xpzsoft
     * @Description 获取参数
     * @param {index:[参数索引]}
     * @return 参数实例
     * @throws
     */
	public ReturnItem get(int index){
		nitem = this.params.get(index);
		return this;
	}
	
	/**
     * @author xpzsoft
     * @Description 添加参数
     * @param {obj:[参数实例]}
     * @return 
     * @throws
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
     * @author xpzsoft
     * @Description 将获取的参数转换为Byte类型
     */
	public Byte toByte(){
		return (Byte)nitem;
	}
	
	/**
     * @author xpzsoft
     * @Description 将获取的参数转换为Short类型
     */
	public Short toShort(){
		return (Short)nitem;
	}
	
	/**
     * @author xpzsoft
     * @Description 将获取的参数转换为Integer类型
     */
	public Integer toInteger(){
		return (Integer)nitem;
	}
	
	/**
     * @author xpzsoft
     * @Description 将获取的参数转换为Float类型
     */
	public Float toFloat(){
		return (Float)nitem;
	}
	
	/**
     * @author xpzsoft
     * @Description 将获取的参数转换为Double类型
     */
	public Double toDouble(){
		return (Double)nitem;
	}
	
	/**
     * @author xpzsoft
     * @Description 将获取的参数转换为Long类型
     */
	public Long toLong(){
		return (Long)nitem;
	}
	
	/**
     * @author xpzsoft
     * @Description 将获取的参数转换为BigInteger类型
     */
	public BigInteger toBigInteger(){
		return new BigInteger(nitem.toString());
	}
	
	/**
     * @author xpzsoft
     * @Description 将获取的参数转换为BigDecimal类型
     */
	public BigDecimal toBigDecimal(){
		return new BigDecimal(nitem.toString());
	}
	
	/**
     * @author xpzsoft
     * @Description 将获取的参数转换为String类型
     */
	public String toString(){
		return (String)nitem;
	}
	
	/**
     * @author xpzsoft
     * @Description 将获取的参数转换为Boolean类型
     */
	public Boolean toBoolean(){
		return (Boolean)nitem;
	}
	
	/**
     * @author xpzsoft
     * @Description 将获取的参数转换为Object类型
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
