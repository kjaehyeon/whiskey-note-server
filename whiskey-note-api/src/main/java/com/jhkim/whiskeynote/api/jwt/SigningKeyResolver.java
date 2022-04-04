package com.jhkim.whiskeynote.api.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * JwsHeader를 통해 Signature 검증에 필요한 Key를 가져오는 코드를 구현합니다.
 */
@Component
@RequiredArgsConstructor
public class SigningKeyResolver extends SigningKeyResolverAdapter {
    private final JwtKey jwtKey;

    @Override
    public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
        String kid = jwsHeader.getKeyId();
        if (kid == null)
            return null;
        return jwtKey.getKey(kid);
    }
}