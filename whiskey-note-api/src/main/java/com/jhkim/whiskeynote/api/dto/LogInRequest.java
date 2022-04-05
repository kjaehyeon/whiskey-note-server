package com.jhkim.whiskeynote.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class LogInRequest {
    private final String username;
    private final String password;
}
