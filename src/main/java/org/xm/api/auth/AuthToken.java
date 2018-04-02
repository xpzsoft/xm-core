package org.xm.api.auth;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xm.api.auth.AuthAnnotation.AuthCode;
import org.xm.api.springcontext.ConstSpringContext;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthToken {
	private static final Logger log = LoggerFactory.getLogger(AuthToken.class);
	private static final String SECRET;
	
    private static final String EXP = "token_exp";

    private static final String PAYLOAD = "token_payload";
    
    private static String TOKEN_NAME = "token";
    private static Long token_time = 1000L * 60L * 30L;
    private static ConcurrentHashMap<String, String> usermaps = new ConcurrentHashMap<String, String>();
    
    public static final int AUTHN_SINGLE = 1;//单点
    public static final int AUTHN_MULTIPLY = 2;//多点
    
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static ObjectMapper mapper = new ObjectMapper();
    
    static{
    	SECRET = UUID.randomUUID().toString() + UUID.randomUUID().toString();
    }
    
    public static AuthTokenItem sign(){
    	AuthUser user = ConstSpringContext.getBean(AuthUser.class);
    	String token = encodeXOR(sign(user, token_time));
    	AuthTokenItem token_item = ConstSpringContext.getBean(AuthTokenItem.class);
    	token_item.setAuthUser(user);
    	token_item.setToken_str(token);
    	return token_item;
    }
    
    public static AuthTokenItem sign(int auth_type, String user_id){
    	if(auth_type == AUTHN_SINGLE && user_id == null)
    		throw new RuntimeException("'user_id' in sign(int auth_type, String user_id) should not be null.");
    	
    	AuthUser user = ConstSpringContext.getBean(AuthUser.class);
    	
    	if(auth_type == AUTHN_SINGLE){
    		user.setUser_id(user_id);
    		user.setLogin_state(true);
    		usermaps.put(user_id, user.getTokenid());
    	}
    
    	String token = encodeXOR(sign(user, token_time));
    	AuthTokenItem token_item = ConstSpringContext.getBean(AuthTokenItem.class);
    	token_item.setAuthUser(user);
    	token_item.setToken_str(token);
    	return token_item;
    }
    
    public static AuthTokenItem sign(int auth_type, String user_id, AuthAnnotation.AuthCode code){
    	if(code == null)
    		throw new RuntimeException("'code' in sign(int auth_type, String user_id, AuthAnnotation.AuthCode code) should not be null.");
    	if(auth_type == AUTHN_SINGLE && user_id == null)
    		throw new RuntimeException("'user_id' in sign(int auth_type, String user_id, AuthAnnotation.AuthCode code) should not be null.");
    	
    	AuthUser user = ConstSpringContext.getBean(AuthUser.class);
    	user.setAuthority(code.getValue());
    	if(auth_type == AUTHN_SINGLE){
    		user.setUser_id(user_id);
    		user.setLogin_state(true);
    		usermaps.put(user_id, user.getTokenid());
    	}
    	
    	String token = encodeXOR(sign(user, token_time));
    	AuthTokenItem token_item = ConstSpringContext.getBean(AuthTokenItem.class);
    	token_item.setAuthUser(user);
    	token_item.setToken_str(token);
    	return token_item;
    }
    
    public static AuthTokenItem sign(int auth_type, String user_id, AuthAnnotation.AuthCode code, Long time){
    	if(code == null)
    		throw new RuntimeException("'code' in sign(int auth_type, String user_id, AuthAnnotation.AuthCode code) should not be null.");
    	if(auth_type == AUTHN_SINGLE && user_id == null)
    		throw new RuntimeException("'user_id' in sign(int auth_type, String user_id, AuthAnnotation.AuthCode code) should not be null.");
    	
    	AuthUser user = ConstSpringContext.getBean(AuthUser.class);
    	user.setAuthority(code.getValue());
    	if(auth_type == AUTHN_SINGLE){
    		user.setUser_id(user_id);
    		user.setLogin_state(true);
    		usermaps.put(user_id, user.getTokenid());
    	}
    	
    	String token = encodeXOR(sign(user, time));
    	AuthTokenItem token_item = ConstSpringContext.getBean(AuthTokenItem.class);
    	token_item.setAuthUser(user);
    	token_item.setToken_str(token);
    	return token_item;
    }

    //加密，传入一个对象和有效期
    public static <T> String sign(T object, long maxAge) {
        try {
            final Builder builder = JWT.create();
            String jsonString = mapper.writeValueAsString(object);
            builder.withClaim(EXP, System.currentTimeMillis() + maxAge);
            builder.withClaim(PAYLOAD, jsonString);
            return builder.sign(Algorithm.HMAC256(SECRET));
        } catch(Exception e) {
        	log.error(e.getMessage());
        	e.printStackTrace();
            return null;
        }
    }

    //解密，传入一个加密后的token字符串和解密后的类型
    public static<T> T unsign(String jwt, Class<T> classT) {
        try {
        	final JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            final DecodedJWT claims= verifier.verify(jwt);
            if(claims.getClaims().containsKey(EXP) && claims.getClaims().containsKey(PAYLOAD)){
            	long exp = claims.getClaim(EXP).asLong();
            	long currentTimeMillis = System.currentTimeMillis();
                if (exp > currentTimeMillis) {
                	String json = claims.getClaim(PAYLOAD).asString();
                    return objectMapper.readValue(json, classT);
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
    
    public static String encodeXOR(String str){
    	StringBuilder buffer = new StringBuilder();
    	for(int i = 0; i < str.length(); i++){
    		int j = i % SECRET.length();
    		buffer.append((short)(str.charAt(i)^SECRET.charAt(j)));
    		if(i != str.length() - 1)
    			buffer.append('.');
    	}
    	return buffer.toString();
    }
    
    public static String decodeXOR(String str){
    	StringBuilder buffer = new StringBuilder();
    	String datas[] = str.split("\\.");
    	for(int i = 0; i < datas.length; i++){
    		int j = i % SECRET.length();
    		buffer.append((char)(Short.valueOf(datas[i])^SECRET.charAt(j)));
    	}
    	return buffer.toString();
    }
    
//    public static void main(String args[]){
//    	String str = "dsadsa34-fds.fdfeDDDD!@#$%^&*()谢鹏志";
//    	String str1 = AuthToken.encodeXOR(str);
//    	String str2 = AuthToken.decodeXOR(str1);
//    	System.out.println(str1);
//    	System.out.println(str2);
//    }

	public static String getTOKEN_NAME() {
		return TOKEN_NAME;
	}

	public static void setTOKEN_NAME(String tOKEN_NAME) {
		TOKEN_NAME = tOKEN_NAME;
	}
	
	public static boolean checkUser(AuthUser user){
		if(!user.isLogin_state())
			return true;
		if(user.getUser_id() == null || user.getTokenid() == null || usermaps.get(user.getUser_id()) == null || !usermaps.get(user.getUser_id()).equals(user.getTokenid()))
			return false;
		return true;
	}

	public static AuthCode getAuthCode(int num){
		if(num == AuthCode.AC1.getValue())
			return AuthCode.AC1;
		else if(num == AuthCode.AC2.getValue())
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
		return null;
	}
}
