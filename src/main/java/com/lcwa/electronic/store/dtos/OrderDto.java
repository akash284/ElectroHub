package com.lcwa.electronic.store.dtos;

import com.lcwa.electronic.store.entities.OrderItem;
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
@ToString
public class OrderDto {

    private String orderId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOTPAID";
    private int orderAmount;
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderedDate=new Date();
    private Date deliveredDate;

    // dependecny on table
  //  private UserDto userDto;
     private List<OrderItemDto> orderItems=new ArrayList<>();
}
