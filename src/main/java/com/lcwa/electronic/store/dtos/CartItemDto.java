package com.lcwa.electronic.store.dtos;

import com.lcwa.electronic.store.entities.Cart;
import com.lcwa.electronic.store.entities.Product;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private int cartItemId;
    private ProductDto product;

    private int quantity;
    private int totalprice;


}
