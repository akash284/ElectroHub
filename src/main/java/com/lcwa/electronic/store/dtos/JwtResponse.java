package com.lcwa.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private String token;
    UserDto user;

    private RefreshTokenDto refreshToken; //new token bnega isse when jwt token get expired
}
