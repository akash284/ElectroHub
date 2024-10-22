package com.lcwa.electronic.store.services.impl;

import com.lcwa.electronic.store.dtos.RefreshTokenDto;
import com.lcwa.electronic.store.dtos.UserDto;
import com.lcwa.electronic.store.entities.RefreshToken;
import com.lcwa.electronic.store.entities.User;
import com.lcwa.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwa.electronic.store.repositories.RefreshTokenRepository;
import com.lcwa.electronic.store.repositories.UserRepository;
import com.lcwa.electronic.store.services.RefreshTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RefreshTokenDto createRefreshToken(String username) {

        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("user with id not found"));


        RefreshToken refreshToken = refreshTokenRepository.findByUser(user).orElse(null);

        // is user k lie pehle se hi refreshToken present he y nahi
        // if not create it
       if(refreshToken==null){

           refreshToken = RefreshToken.builder()
                   .user(user)
                   .token(UUID.randomUUID().toString())
                   .expiryDate(Instant.now().plusSeconds(5 * 24 * 60 * 60))
                   .build();

       }
       else{
           refreshToken.setToken(UUID.randomUUID().toString());
           refreshToken.setExpiryDate(Instant.now().plusSeconds(5 * 24 * 60 * 60));
       }

        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);

        return modelMapper.map(savedToken,RefreshTokenDto.class);
    }

    @Override
    public RefreshTokenDto findByToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("token not found!"));
        return modelMapper.map(refreshToken,RefreshTokenDto.class);
    }

    @Override
    public RefreshTokenDto verifyRefreshToken(RefreshTokenDto token) {
        var refreshToken=modelMapper.map(token,RefreshToken.class);

        if(token.getExpiryDate().compareTo(Instant.now())<0){

            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token Expired!");
        }
        return token;

    }

    @Override
    public UserDto getUser(RefreshTokenDto dto) {
        // ye token se refresh token nikala
        RefreshToken refreshToken = refreshTokenRepository.findByToken(dto.getToken()).orElseThrow(() -> new ResourceNotFoundException("token not found"));
        User user = refreshToken.getUser();

        return modelMapper.map(user, UserDto.class);

    }
}
