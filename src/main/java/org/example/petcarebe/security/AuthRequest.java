package org.example.petcarebe.security;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthRequest {
    private String username;
    private String password;

}