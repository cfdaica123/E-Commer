package com.E_Commer.dto;

import com.E_Commer.enums.RoleEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private String avatarUrl;
    private String email;
    private RoleEnum role;
    private String phoneNumber;
}