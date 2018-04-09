package org.xm.api.core.auth;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.api.core.auth.AuthAnnotation.AuthCode;
import org.xm.api.springcontext.SpringContext;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */
public class AuthToken {
	//单点登录
	public static final int AUTHN_SINGLE = 1;
	//多点登录、临时用户
    public static final int AUTHN_MULTIPLY = 2;
	
	private static final Logger log = LoggerFactory.getLogger(AuthToken.class);
	
	//秘钥
	private static final String SECRET;
	//token 有效时间
    private static final String EXP = "token_exp";
    //token body内容
    private static final String PAYLOAD = "token_payload";
  //token有效时长
    private static final Long TOKEN_TIME_DEFAULT = 1000L * 60L * 30L;
    
    //token 名称
    private static String TOKEN_NAME = "token";
    
    //当前用户列表
    private static ConcurrentHashMap<String, String> usermaps = new ConcurrentHashMap<String, String>();
    
    //程序启动时随机产生秘钥
    static{
    	SECRET = UUID.randomUUID().toString() + UUID.randomUUID().toString();
    }
    
    /**
     * 为用户创建一个token签名对象
     * @author xpzsoft
     * @param auth_type 用户登录类型，为AUTHN_SINGLE或者AUTHN_MULTIPLY
     * @param user_id 用户唯一标识
     * @return AuthTokenItem
     * @throws RuntimeException 如果auth_type为AUTHN_SINGLE， 则user_id不能为空，否则抛出异常
     */
    public static AuthTokenItem sign(int auth_type, String user_id){
    	if(auth_type == AUTHN_SINGLE && user_id == null)
    		throw new RuntimeException("'user_id' in sign(int auth_type, String user_id) should not be null.");
    	
    	AuthTokenUser user = SpringContext.getContext().getBean("getAuthTokenUser", AuthTokenUser.class);
    	
    	if(auth_type == AUTHN_SINGLE){
    		user.setUserId(user_id);
    		user.setSingleLogin(true);
    		usermaps.put(user_id, user.getTokenId());
    	}
    
    	String token = encodeXOR(sign(user, TOKEN_TIME_DEFAULT));
    	AuthTokenItem token_item = SpringContext.getContext().getBean("getAuthTokenItem", AuthTokenItem.class);
    	token_item.setAuthUser(user);
    	token_item.setToken_str(token);
    	return token_item;
    }
    
    /**
     * 为用户创建一个token签名对象
     * @author xpzsoft
     * @param auth_type 用户登录类型，为AUTHN_SINGLE或者AUTHN_MULTIPLY
     * @param user_id 用户唯一标识
     * @param code 权限
     * @return AuthTokenItem
     * @throws RuntimeException 如果auth_type为AUTHN_SINGLE， 则user_id不能为空，否则抛出异常
     */
    public static AuthTokenItem sign(int auth_type, String user_id, AuthAnnotation.AuthCode code){
    	if(code == null)
    		throw new RuntimeException("'code' in sign(int auth_type, String user_id, AuthAnnotation.AuthCode code) should not be null.");
    	if(auth_type == AUTHN_SINGLE && user_id == null)
    		throw new RuntimeException("'user_id' in sign(int auth_type, String user_id, AuthAnnotation.AuthCode code) should not be null.");
    	
    	AuthTokenUser user = SpringContext.getContext().getBean("getAuthTokenUser", AuthTokenUser.class);
    	user.setAuthority(code.getValue());
    	if(auth_type == AUTHN_SINGLE){
    		user.setUserId(user_id);
    		user.setSingleLogin(true);
    		usermaps.put(user_id, user.getTokenId());
    	}
    	
    	String token = encodeXOR(sign(user, TOKEN_TIME_DEFAULT));
    	AuthTokenItem token_item = SpringContext.getContext().getBean("getAuthTokenItem", AuthTokenItem.class);
    	token_item.setAuthUser(user);
    	token_item.setToken_str(token);
    	return token_item;
    }
    
    /**
     * 为用户创建一个token签名对象
     * @author xpzsoft
     * @param auth_type 用户登录类型，为AUTHN_SINGLE或者AUTHN_MULTIPLY
     * @param user_id 用户唯一标识
     * @param code 权限
     * @param time token有效时长（单位毫秒）
     * @return AuthTokenItem
     * @throws RuntimeException 如果auth_type为AUTHN_SINGLE， 则user_id不能为空，否则抛出异常
     */
    public static AuthTokenItem sign(int auth_type, String user_id, AuthAnnotation.AuthCode code, Long time){
    	if(code == null)
    		throw new RuntimeException("'code' in sign(int auth_type, String user_id, AuthAnnotation.AuthCode code, Long time) should not be null.");
    	if(time < 10000)
    		throw new RuntimeException("'time' in sign(int auth_type, String user_id, AuthAnnotation.AuthCode code, Long time) should be bigger than 10000ms.");
    	if(auth_type == AUTHN_SINGLE && user_id == null)
    		throw new RuntimeException("'user_id' in sign(int auth_type, String user_id, AuthAnnotation.AuthCode code, Long time) should not be null.");

		AuthTokenUser user = SpringContext.getContext().getBean("getAuthTokenUser", AuthTokenUser.class);
    	user.setAuthority(code.getValue());
    	if(auth_type == AUTHN_SINGLE){
    		user.setUserId(user_id);
    		user.setSingleLogin(true);
    		usermaps.put(user_id, user.getTokenId());
    	}
    	
    	String token = encodeXOR(sign(user, time));
		AuthTokenItem token_item = SpringContext.getContext().getBean("getAuthTokenItem", AuthTokenItem.class);
    	token_item.setAuthUser(user);
    	token_item.setToken_str(token);
    	return token_item;
    }

    /**
     * 创建token签名字符串
     * @author xpzsoft
     * @param object token中携带的用户信息，存放于PAYLOAD中
     * @param maxAge token有效时长（单位毫秒）
     * @return String
     */
    private static <T> String sign(T object, long maxAge) {
        try {
            final Builder builder = JWT.create();
            String jsonString = SpringContext.getContext().getBean("getObjectMapper", ObjectMapper.class).writeValueAsString(object);
            builder.withClaim(EXP, System.currentTimeMillis() + maxAge);
            builder.withClaim(PAYLOAD, jsonString);
            return builder.sign(Algorithm.HMAC256(SECRET));
        } catch(Exception e) {
        	log.error(e.getMessage());
        	e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密传入的token字符串，并反序列化PAYLOAD的用户对象
     * @author xpzsoft
     * @param jwt token字符串 
     * @param classT 反序列化的类型
     * @param <T> 模板
     * @return T
     */
    public static<T> T unsign(String jwt, Class<T> classT) {
        try {
        	final JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            final DecodedJWT claims= verifier.verify(jwt);
            if(claims.getClaims().containsKey(EXP) && claims.getClaims().containsKey(PAYLOAD)){
            	long exp = claims.getClaim(EXP).asLong();
            	long currentTimeMillis = System.currentTimeMillis();
                if (exp > currentTimeMillis) {
                	String json = claims.getClaim(PAYLOAD).asString();
                    return SpringContext.getContext().getBean("getObjectMapper", ObjectMapper.class).readValue(json, classT);
                }
                else
                	log.error("token is overdue!");
            }
            else
            	log.error("virify token failed with EXP and PAYLOAD is null!");
            return null;
        } catch (Exception e) {
        	log.error(e.getMessage());
            return null;
        }
    }
    
    /**
     * 对字符串进行异或加密
     * @author xpzsoft
     * @param str 需要加密的字符串
     * @return String
     */
    public static String encodeXOR(String str){
    	if(str == null || str.trim().length() < 1){
    		throw new RuntimeException("param 'str' in encodeXOR(String str) should not be null or ''");
    	}
    	StringBuilder buffer = new StringBuilder();
    	for(int i = 0; i < str.length(); i++){
    		int j = i % SECRET.length();
    		buffer.append((short)(str.charAt(i)^SECRET.charAt(j)));
    		if(i != str.length() - 1)
    			buffer.append('.');
    	}
    	return buffer.toString();
    }
    
    /**
     * 对字符串进行异或解密
     * @author xpzsoft
     * @param str 需要解密的字符串
     * @return String
     */
    public static String decodeXOR(String str){
    	if(str == null || str.trim().length() < 1){
    		throw new RuntimeException("param 'str' in decodeXOR(String str) should not be null or ''");
    	}
    	StringBuilder buffer = new StringBuilder();
    	String datas[] = str.split("\\.");
    	for(int i = 0; i < datas.length; i++){
    		int j = i % SECRET.length();
    		buffer.append((char)(Short.valueOf(datas[i])^SECRET.charAt(j)));
    	}
    	return buffer.toString();
    }
	
    /**
     * 检查AuthTokenUser对象是否合格
     * @author xpzsoft
     * @param user AuthTokenUser对象
     * @return boolean
     */
	public static boolean checkUser(AuthTokenUser user){
		if(!user.isSingleLogin())
			return true;
		if(user.getUserId() == null || user.getTokenId() == null || usermaps.get(user.getUserId()) == null || !usermaps.get(user.getUserId()).equals(user.getTokenId()))
			return false;
		return true;
	}
	
	/**
	 * 根据权限值生成AuthCode对象
     * @author xpzsoft
     * @param num 权限值
     * @return AuthCode
     */
	public static AuthCode getAuthCode(int num){
		if(num == AuthCode.AC2.getValue())
			return AuthCode.AC2;
		else if(num == AuthCode.AC3.getValue())
			return AuthCode.AC3;
		else if(num == AuthCode.AC4.getValue())
			return AuthCode.AC4;
		else if(num == AuthCode.AC5.getValue())
			return AuthCode.AC5;
		else if(num == AuthCode.AC6.getValue())
			return AuthCode.AC6;
		else if(num == AuthCode.AC7.getValue())
			return AuthCode.AC7;
		else if(num == AuthCode.AC8.getValue())
			return AuthCode.AC8;
		else if(num == AuthCode.AC9.getValue())
			return AuthCode.AC9;
		else if(num == AuthCode.AC10.getValue())
			return AuthCode.AC10;
		return AuthCode.AC1;
	}
	
	/**
	 * 返回token名称
     * @author xpzsoft
     * @return String
     */
	public static String getTOKEN_NAME() {
		return TOKEN_NAME;
	}
	
	/**
	 * 设置token的名称
     * @author xpzsoft
     * @param tOKEN_NAME token新名称
     */
	public static void setTOKEN_NAME(String tOKEN_NAME) {
		if(tOKEN_NAME == null || tOKEN_NAME.trim().length() < 1)
			return;
		TOKEN_NAME = tOKEN_NAME;
	}
	
//  public static void main(String args[]){
//	String str = "dsadsa34-fds.fdfeDDDD!@#$%^&*()谢鹏志";
//	String str1 = AuthToken.encodeXOR(str);
//	String str2 = AuthToken.decodeXOR(str1);
//	System.out.println(str1);
//	System.out.println(str2);
//}
}
