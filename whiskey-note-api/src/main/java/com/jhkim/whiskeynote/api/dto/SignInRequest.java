package com.jhkim.whiskeynote.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class SignInRequest {
    private final String email;
    private final String password;
}
