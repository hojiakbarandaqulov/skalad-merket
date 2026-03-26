package org.example.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSummaryResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String photoUrl;
}
