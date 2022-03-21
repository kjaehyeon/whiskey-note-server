package com.jhkim.whiskeynote.core.entity;

import com.jhkim.whiskeynote.core.constant.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
@Entity
public class User extends BaseEntity{
    private String username;
    private String password;
    private String email;
    private UserRole role;
}
