package com.jhkim.whiskeynote.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor(staticName = "of")
public class LogInRequest {
    @NotBlank
    private final String username;
    @NotBlank
    private final String password;
}
