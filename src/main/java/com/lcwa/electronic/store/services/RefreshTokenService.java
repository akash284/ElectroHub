package com.lcwa.electronic.store.services;

import com.lcwa.electronic.store.dtos.RefreshTokenDto;
import com.lcwa.electronic.store.dtos.UserDto;

public interface RefreshTokenService {

    // create Refresh Token
    RefreshTokenDto createRefreshToken(String username);
    // find by token
    RefreshTokenDto findByToken(String token);
    //verify
    RefreshTokenDto verifyRefreshToken(RefreshTokenDto token);

    UserDto getUser(RefreshTokenDto dto);
}
