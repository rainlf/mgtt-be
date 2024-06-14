package com.rainlf.weixin.infra.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author rain
 * @date 6/14/2024 9:13 AM
 */
@Slf4j
@Component
public class JwtTokenService {
    // 生成 JWT token
    public String generateToken(String openId) {
        return Jwts.builder()
                .subject(openId)
                .issuedAt(new Date())
                .compact();
    }

    // 从 Jwt token 获取openId
    public String getOpenId(String token) {
        Claims claims = Jwts.parser()
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    // 验证 Jwt token
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .build()
                    .parse(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }
}
