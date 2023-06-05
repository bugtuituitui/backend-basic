package com.example.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

/**
 * TOKEN工具类
 */
public class JwtUtils {

    /**
     * 密钥
     */
    public static final String SECRET = "LEMON";

    /**
     * 有效时间
     */
    public static final int MINUTES = 60 * 4;

    private static final String UID = "UID";

    /**
     * token生成
     *
     * @param uid
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String generate(String uid) throws UnsupportedEncodingException {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, MINUTES);
        Date expireDate = c.getTime();
        String token = JWT.create().withAudience(uid)
                .withIssuedAt(new Date())
                .withExpiresAt(expireDate)
                .withClaim(UID, uid)
                .sign(Algorithm.HMAC256(SECRET));

        return token;
    }

    /**
     * 密钥验证
     *
     * @param token
     * @return
     */
    public static boolean verify(String token) {
        DecodedJWT decodedJWT = null;

        try {
            Verification jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET));
            decodedJWT = jwtVerifier.build().verify(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 获取payload claim字段
     *
     * @param token
     * @param name
     * @return
     */
    public static String getClaim(String token, String name) {
        return JWT.decode(token).getClaim(name).asString();
    }

    /**
     * 获取UID
     *
     * @param token
     * @return
     */
    public static String getCredential(String token) {
        return getClaim(token, UID);
    }

    /**
     * 是否过期
     *
     * @param token
     * @return
     */
    public static boolean expired(String token) {

        if (JWT.decode(token).getExpiresAt().getTime() < System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    /**
     * 是否有效
     *
     * @param token
     * @return
     */
    public static boolean isValid(String token) {

        // token 不存在
        if (StringUtils.isEmpty(token)) {
            return false;
        }

        // 密钥验证失败
        if (!verify(token)) {
            return false;
        }

        // token 过期
        if (expired(token)) {
            return false;
        }

        return true;
    }
}
