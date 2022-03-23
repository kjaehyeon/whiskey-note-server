package com.jhkim.whiskeynote.core.service;

import com.jhkim.whiskeynote.core.dto.UserCreateRequest;
import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User create(UserCreateRequest userCreateRequest){
        userRepository.findUserByEmail(userCreateRequest.getEmail())
                .ifPresent( user -> {
                    throw new RuntimeException("유저가 이미 존재합니다");
                });
        return userRepository.save(userCreateRequest.toEntity(passwordEncoder));
    }
}
