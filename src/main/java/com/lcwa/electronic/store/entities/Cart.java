package com.lcwa.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="cart")
public class Cart {

    @Id
    private String cartId;

    private Date createdAt;


    // ye cart kis user ka he
    @OneToOne
    private User user;

    // is cart k andar kitne items hein iske lie
    // mapping karni cartItems se
    // ye bidirectional mapping hogi kyuki agr mere pass cart heto ye pta kar skta hu ki kitne items he and similarly agr cartitems hoge to ye pata laga sakte hu ki kis Cart k he


    // hum agr cart save krre to uske items bhi save hojaye
    // cart nikale to uske items turant nikal jaye
    // cart se jese hi htaye dbasae se bhi hat jaye islie orphanRemoval=true kardia
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    private List<CartItem> items=new ArrayList<>();
}
