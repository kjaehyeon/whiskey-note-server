package com.jhkim.whiskeynote.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor(staticName = "of")
public class SignUpRequest {
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 4, message = "4자리 이상 입력해주세요.")
    private String password;
    @NotBlank
    @Email
    private String email;
}
