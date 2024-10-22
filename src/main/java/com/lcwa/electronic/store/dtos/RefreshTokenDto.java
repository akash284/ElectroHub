package com.lcwa.electronic.store.dtos;

import com.lcwa.electronic.store.entities.User;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.time.Instant;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenDto {

    private int id;
    private String token;
    private Instant expiryDate;

}

