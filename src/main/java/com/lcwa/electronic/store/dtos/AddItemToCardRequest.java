package com.lcwa.electronic.store.dtos;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddItemToCardRequest {

    private String productId;
    private int quantity;
}
