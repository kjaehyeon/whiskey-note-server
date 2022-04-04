package com.jhkim.whiskeynote.api.jwt;

import com.jhkim.whiskeynote.api.config.JwtKeyConfig;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;
import java.util.Random;

/**
 * JWT key를 제공하고 조회한다.
 * Key Rolling을 지원한다.
 */

@RequiredArgsConstructor
@Component
public class JwtKey {
    private final JwtKeyConfig jwtKeyConfig;

    private final Map<String, String> SECRET_KEY_SET = Map.of(
            "key1" , jwtKeyConfig.getKey1(),
        "key2" , jwtKeyConfig.getKey2(),
        "key3" , jwtKeyConfig.getKey3()
    );
    private final String[] KID_SET = SECRET_KEY_SET.keySet().toArray(new String[0]);
    private Random randomIndex = new Random();

    /**
     * SECRET_KEY_SET에서 랜덤키 가져옴
     */
    public Pair<String, Key> getRandomKey(){
        String kid = KID_SET[randomIndex.nextInt(KID_SET.length)];
        String secretKey = SECRET_KEY_SET.get(kid);
        return Pair.of(
                kid,
                Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))
        );
    }

    /**
     * kid로 key찾기
     * @param kid
     * @return
     */
    public Key getKey(String kid){
        String key = SECRET_KEY_SET.getOrDefault(kid, null);
        if(key == null)
            return null;
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)); //key의 길이에 따라서 적절한 암호화방식 선택해준다.
    }
}
