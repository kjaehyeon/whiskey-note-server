package com.jhkim.whiskeynote.api.jwt;

import com.jhkim.whiskeynote.api.config.JwtKeyConfig;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Random;

/**
 * JWT key를 제공하고 조회한다.
 * Key Rolling을 지원한다.
 */

@Component
@Slf4j
public class JwtKey {

    private final HashMap<String, String> SECRET_KEY_SET;
    private final String[] KID_SET;
    private Random randomIndex = new Random();

    public JwtKey(JwtKeyConfig jwtKeyConfig){
        SECRET_KEY_SET = new HashMap<>();
        SECRET_KEY_SET.put("key1", jwtKeyConfig.getKey1());
        SECRET_KEY_SET.put("key2", jwtKeyConfig.getKey2());
        SECRET_KEY_SET.put("key3", jwtKeyConfig.getKey3());
        KID_SET = SECRET_KEY_SET.keySet().toArray(new String[0]);
    }

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
