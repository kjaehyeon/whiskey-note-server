package com.jhkim.whiskeynote.core.service;

import com.jhkim.whiskeynote.core.dto.UserCreateRequest;
import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
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
        userRepository.findUserByUsername(userCreateRequest.getUsername())
                .ifPresent( user -> {
                    throw new GeneralException(ErrorCode.ALREADY_EXISTS_USER);
                });
        return userRepository.save(userCreateRequest.toEntity(passwordEncoder));
    }

}
