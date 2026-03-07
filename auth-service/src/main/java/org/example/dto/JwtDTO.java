package org.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.Roles;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtDTO {
    private String id;
    private String username;
    private Roles role;

    public JwtDTO(String id, String email, Roles role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public JwtDTO(String id) {
        this.id = id;
    }

    public JwtDTO(String id, String username) {
        this.id = id;
        this.username = username;
    }
}
