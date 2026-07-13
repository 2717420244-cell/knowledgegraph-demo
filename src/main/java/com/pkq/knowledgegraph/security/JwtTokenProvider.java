package com.pkq.knowledgegraph.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long expiration;

    // 构造函数：从配置文件读取密钥和过期时间
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expiration = expiration;
    }

    /**
     * 生成 Token
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(username)       // "sub" 字段存用户名
                .issuedAt(now)           // "iat" 签发时间
                .expiration(expiry)      // "exp" 过期时间
                .signWith(key)           // 用密钥签名
                .compact();              // 拼成 header.payload.signature 格式
    }

    /**
     * 从 Token 中取出用户名
     */
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * 验证 Token 是否有效（签名匹配 + 未过期）
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public long getExpiration() {
        return expiration;
    }

    /**
     * 解析 Token（验证签名 + 检查过期）
     */
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)          // 用密钥验证签名
                .build()
                .parseSignedClaims(token) // 如果签名不匹配或已过期，抛异常
                .getPayload();            // 取出 Payload 部分
    }
}