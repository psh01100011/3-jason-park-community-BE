package com.ktb.community_BE.jwt;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {
    private final Key key = Keys.hmacShaKeyFor(
            Base64.getDecoder().decode("amFzb24ucGFya2phc29uLnBhcmtqYXNvbi5wYXJramFzb24ucGFya2phc29uLnBhcmtqYXNvbi5wYXJr")
    );

    //access token 생성
    public String createAccessToken(Long userId){
        long accessTtlSec = 2 * 5;
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(accessTtlSec)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    //refresh token 생성
    public String createRefreshToken(Long userId){
        long accessTtlSec = 7 * 24 * 3600;
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(accessTtlSec)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    // 검증
    public Jws<Claims> parse(String jwt){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
    }

}
