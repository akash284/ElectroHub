package com.lcwa.electronic.store.repositories;

import com.lcwa.electronic.store.entities.RefreshToken;
import com.lcwa.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RefreshTokenRepository  extends JpaRepository<RefreshToken,Integer> {

    // custom finder
    // find refresh token by using actual token

    Optional<RefreshToken> findByToken(String token);

    // particular user k token niklna hein to
    Optional<RefreshToken> findByUser(User user);
}
