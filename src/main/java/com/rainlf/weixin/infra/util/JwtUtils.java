package com.rainlf.weixin.infra.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

/**
 * @author rain
 * @date 6/14/2024 9:13 AM
 */
@Slf4j
public class JwtUtils {

    private static String KEY = UUID.randomUUID().toString();

    // 生成 JWT token
    public static String generateToken(String openId) {
        return Jwts.builder()
                .subject(openId)
                .issuedAt(new Date())
                .signWith(key())
                .compact();
    }

    // 从 Jwt token 获取openId
    public static String getOpenId(String token) {
        Claims claims = Jwts.parser()
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    // 验证 Jwt token
    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }

    private static Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(KEY)
        );
    }
}
