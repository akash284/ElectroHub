package com.lcwa.electronic.store.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="products")
public class Product {


    @Id
    @Column(name = "product_id")
    private String productId;

    private String title;

    @Column(length = 100000)
    private String describtion;

    private int price;
    private int discountedPrice;
    private int quantity;
    private Date pAddedDate;

    private boolean live;   // product live he y nai

    private boolean stock; // true mtlb stock m he product warna ni
    private String productImageName;

    // jese hi hum product fetch kare to turant cateogry uski ajaye
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="category")  // ye column create hojyga
    private Category category;
}
