package com.saiyun.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class TokenUtil {
	
	private static final long EXPIRE_TIME =  1000 * 10;  //12 * 60 * 60 * 1000
	private static final String TOKEN_SECRET = "licanxinYeager123";

	/**
	 * 生成签名，15分钟过期
	 * @param **username**
	* @param **password**
	* @return
	 */
	public static String sign(String userId) {
	    try {
	        // 设置过期时间
	        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
	        // 私钥和加密算法
	        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
	        // 设置头部信息
	        Map<String, Object> header = new HashMap<>(2);
	        header.put("Type", "Jwt");
	        header.put("alg", "HS256");
	        // 返回token字符串
	        return JWT.create()
	                .withHeader(header)
	                .withClaim("userId", userId)
	                .withExpiresAt(date)
	                .sign(algorithm);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	/**
	 * 检验token是否正确
	 * @param **token**
	* @return
	 */
	public static String verify(String token) throws JWTDecodeException{
	    
	        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
	        JWTVerifier verifier = JWT.require(algorithm).build();
			DecodedJWT jwt = verifier.verify(token);
			Map<String, Claim> map = jwt.getClaims();
			String userId = map.get("userId").asString();
	        return userId;
	    
	}

}
