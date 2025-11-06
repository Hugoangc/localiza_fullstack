// src/main/java/com/practice/localiza/dto/UsuarioResponseDTO.java
package com.practice.localiza.dto;

import com.practice.localiza.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String phone;

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.phone = user.getPhone();
    }
}


