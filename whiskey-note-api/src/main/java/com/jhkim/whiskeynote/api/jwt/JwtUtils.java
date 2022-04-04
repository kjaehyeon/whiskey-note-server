package com.jhkim.whiskeynote.api.jwt;

import com.jhkim.whiskeynote.core.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JWT 토큰을 생성하고 파싱하는 메서드 제공
 */
@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final JwtKey jwtKey;
    private final SigningKeyResolver signingKeyResolver;
    /**
     * 토큰에서 username 찾기
     *
     * @param token 토큰
     * @return username
     */
    public String getUsername(String token){
        return Jwts.parserBuilder()
                .setSigningKeyResolver(signingKeyResolver)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * user로 토큰 생성
     * HEADER : alg, kid
     * PAYLOAD : sub(subject), iat(토큰발행시간), exp(토큰만료시간) - 이외에도 원하는 Claim 추가가능
     * SIGNATURE : JwtKey.getRandomKey로 구한 Secret Key로 HS512 해시
     *
     * @param user 유저
     * @return jwt token
     */
    public String createToken(User user){
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        Date now = new Date();
        Pair<String, Key> key = jwtKey.getRandomKey();

        //토큰 생성
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + JwtProperties.EXPIRATION_TIME))
                .setHeaderParam(JwsHeader.KEY_ID, key.getFirst())
                .signWith(key.getSecond())
                .compact();
    }
}
