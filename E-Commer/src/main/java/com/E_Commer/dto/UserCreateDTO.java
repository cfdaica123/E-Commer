package com.E_Commer.dto;

import com.E_Commer.enums.RoleEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {
    private String username;
    private String fullName;
    private String avatarUrl;
    private String email;
    private String password;
    private RoleEnum role;
    private String phoneNumber;
}
