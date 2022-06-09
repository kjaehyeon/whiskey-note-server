package com.jhkim.whiskeynote.api.service;

import com.jhkim.whiskeynote.api.dto.auth.LogInRequest;
import com.jhkim.whiskeynote.api.dto.auth.LoginResponse;
import com.jhkim.whiskeynote.api.dto.auth.SignUpRequest;
import com.jhkim.whiskeynote.api.jwt.JwtUtils;
import com.jhkim.whiskeynote.core.dto.UserCreateRequest;
import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import com.jhkim.whiskeynote.core.repository.UserRepository;
import com.jhkim.whiskeynote.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public void signUp(
            SignUpRequest signUpRequest
    ){
        userService.create(signUpRequest.toUserCreateRequest("ROLE_USER"));
    }

    @Transactional
    public void signUpAdmin(
            SignUpRequest signUpRequest
    ){
        userService.create(signUpRequest.toUserCreateRequest("ROLE_ADMIN"));
    }

    @Transactional
    public LoginResponse login(
            LogInRequest logInRequest
    ) {
        final User user = getUserFromLoginRequest(logInRequest);

        confirmPassword(user, logInRequest);

        return LoginResponse.of(jwtUtils.createToken(user));
    }

    private User getUserFromLoginRequest(
            LogInRequest logInRequest
    ){
        return userRepository.findUserByUsername(logInRequest.getUsername())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));
    }

    private void confirmPassword(
            User user,
            LogInRequest logInRequest
    ){
        if(!passwordEncoder.matches(logInRequest.getPassword(), user.getPassword())){
            throw new GeneralException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }
}
