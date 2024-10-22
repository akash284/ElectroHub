package com.lcwa.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;

    private int totalprice;
    private int quantity;


    // yaha pr fk chahiye thi to mapped by ni kia humne
    // ye item kis product ka he
    @OneToOne
    private Product product;

    // ye orderitem  kis order k he
    // bahut sare orderItem ek order ko belong kar sakte hein
    @ManyToOne
    private Order order;

}
