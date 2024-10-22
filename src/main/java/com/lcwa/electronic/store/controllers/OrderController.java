package com.lcwa.electronic.store.controllers;

import com.lcwa.electronic.store.config.AppConstants;
import com.lcwa.electronic.store.dtos.ApiResponseMessage;
import com.lcwa.electronic.store.dtos.CreateOrderRequest;
import com.lcwa.electronic.store.dtos.OrderDto;
import com.lcwa.electronic.store.dtos.PageableResponse;
import com.lcwa.electronic.store.entities.Order;
import com.lcwa.electronic.store.services.OrderService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    public OrderService orderService;

    @PreAuthorize("hasAnyRole('"+ AppConstants.ROLE_NORMAL+"','"+ AppConstants.ROLE_ADMIN+"')")
    @PostMapping
    public ResponseEntity<OrderDto> creteOrder(@Valid @RequestBody CreateOrderRequest request) {

        OrderDto order = orderService.createOrder(request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('"+AppConstants.ROLE_ADMIN+"'))")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId) {
        orderService.removeOrder(orderId);

        ApiResponseMessage response = ApiResponseMessage.builder()
                .success(true)
                .message("Order with Order Id is removed!")
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // get orders of the user
    @PreAuthorize("hasAnyRole('"+ AppConstants.ROLE_NORMAL+"','"+ AppConstants.ROLE_ADMIN+"')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getOrderOfUsers(@PathVariable String userId) {
        List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);

        return new ResponseEntity<>(ordersOfUser, HttpStatus.OK);
    }

    // get orders
    @PreAuthorize("hasRole('"+AppConstants.ROLE_ADMIN+"')")
    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getOrders(
          
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "orderedDate", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

        PageableResponse<OrderDto> allOrders = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);

        return new ResponseEntity<>(allOrders, HttpStatus.OK);
    }
}
