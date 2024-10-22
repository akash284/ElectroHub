package com.lcwa.electronic.store.services;

import com.lcwa.electronic.store.dtos.CreateOrderRequest;
import com.lcwa.electronic.store.dtos.OrderDto;
import com.lcwa.electronic.store.dtos.PageableResponse;

import java.util.List;

public interface OrderService {

    // create order
    OrderDto createOrder(CreateOrderRequest orderDto);  //, String userId, String cartId these things already are he createOrderRequest mein
    //remove order
    void removeOrder(String orderId);
    // get orders of users
    List<OrderDto> getOrdersOfUser(String userId);
    //get orders
   PageableResponse<OrderDto> getAllOrders(int pageNumber,int pageSize,String sortBy,String sortDir);

}
