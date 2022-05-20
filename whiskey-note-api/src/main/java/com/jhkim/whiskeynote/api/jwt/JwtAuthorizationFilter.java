package com.jhkim.whiskeynote.api.jwt;

import com.jhkim.whiskeynote.core.entity.User;
import com.jhkim.whiskeynote.core.exception.ErrorCode;
import com.jhkim.whiskeynote.core.exception.GeneralException;
import com.jhkim.whiskeynote.core.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public JwtAuthorizationFilter(
            UserRepository userRepository,
            JwtUtils jwtUtils
    ){
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String token = request.getHeader(JwtProperties.KEY_NAME);
        if(token != null){
            //authentication을 만들어서 SecurityContext에 넣어준다.
            Authentication authentication = this.getUsernamePasswordAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(String token) {
        String username = jwtUtils.getUsername(token);
        if(username != null){
            User user = userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

            return new UsernamePasswordAuthenticationToken(
                    user, //principal,
                    null,
                    user.getAuthorities()
            );
        }
        return null;
    }
}
