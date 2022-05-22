package com.jhkim.whiskeynote.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor(staticName = "of")
public class LoginResponse {
    private String token;
}
