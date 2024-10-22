package com.lcwa.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int cartItemId;

    // cartItem k andr konsa product he
    // ye manage krne k lie unidirectional mapping krege
    // x product kis cartItem m he isse matlb ni he

    @OneToOne
    @JoinColumn(name="product_id")
    private Product product;

    private int quantity;
    private int totalprice;

    // mapping   cart
    // se ye cartitems kis cart ko belong karte iske lie
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

}
