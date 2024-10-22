package com.lcwa.electronic.store.repositories;

import com.lcwa.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// database operation perform krne ki capability lane k lie hume inherit karna hoga jpa repository
// jpa repository has all the features that are required to perform database operations like findAll() ,findByName() etc..

// Hierarchy
// Repository --> CrudRepository --> ListPagingAndSortingRepository --> JpaRepository --> UserRepository

@Repository
public interface UserRepository  extends JpaRepository<User,String> {

    // iski implementation dynamically runtime p create hojygi jab baki methods ki create hogi tabhi

    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email,String password);

    List<User> findByNameContaining(String keywords);  // ye ek like ki query bnayga
}
