package com.lcwa.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtRequest {

    private String email;   //as a username lie h whole prject mein
    private String password;
}
