package com.lcwa.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.repository.cdi.Eager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="orders")
@Builder
public class Order {

    @Id
    private String orderId;

    // pending dispatched,delivered
    private String orderStatus;

    // paid,not-paid
    private String paymentStatus;

    private int orderAmount;

    @Column(length =1000)
    private String billingAddress;

    private String billingPhone;

    private String billingName;
    private Date orderedDate;
    private Date deliveredDate;


    // user
    // order fetch kare to turant User ajaye ki kisne place kia he order
    // fk yha par chahiye to ise mapped by mat karo
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;



    @OneToMany(mappedBy = "order",fetch=FetchType.EAGER,cascade = CascadeType.ALL)
    private List<OrderItem> orderItems=new ArrayList<>();

}
