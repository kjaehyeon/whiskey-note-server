package com.jhkim.whiskeynote.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor(staticName = "of")
public class LogInRequest {
    @NotBlank
    private final String username;
    @NotBlank
    private final String password;
}
