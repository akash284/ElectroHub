package com.lcwa.electronic.store.repositories;

import com.lcwa.electronic.store.entities.Order;
import com.lcwa.electronic.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository  extends JpaRepository<Order,String> {

    // find all orders for given user
    List<Order> findByUser(User user);
}
