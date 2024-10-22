package com.lcwa.electronic.store.dtos;

import com.lcwa.electronic.store.entities.CartItem;
import com.lcwa.electronic.store.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {


    private String cartId;
    private Date createdAt;
    private UserDto user;
    private List<CartItemDto> items=new ArrayList<>();
}
