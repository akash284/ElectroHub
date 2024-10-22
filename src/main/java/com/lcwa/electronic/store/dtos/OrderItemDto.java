package com.lcwa.electronic.store.dtos;

import com.lcwa.electronic.store.entities.Order;
import com.lcwa.electronic.store.entities.Product;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemDto {

    private int orderItemId;
    private int totalprice;
    private int quantity;



    // ye item kis product ka he
    private ProductDto product;
//    // ye orderitem  kis order k he
    //private OrderDto orderDto;
}
