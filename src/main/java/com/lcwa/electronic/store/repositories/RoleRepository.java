package com.lcwa.electronic.store.repositories;

import com.lcwa.electronic.store.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository<Role,String> {

    // Name wise role ko fetch karna hoto
    Optional<Role> findByName(String name);
}
