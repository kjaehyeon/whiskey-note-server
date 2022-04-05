package com.jhkim.whiskeynote.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class LoginResponse {
    private String token;
}
