package com.jhkim.whiskeynote.core.dto;

import com.jhkim.whiskeynote.core.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(staticName = "of")
public class UserDto {
    private Long id;
    private String username;
    private String authority;

    public static UserDto fromEntity(User user){
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .authority(user.getAuthority())
                .build();
    }
}
