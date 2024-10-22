package com.lcwa.electronic.store.services.impl;

import com.lcwa.electronic.store.dtos.CreateOrderRequest;
import com.lcwa.electronic.store.dtos.OrderDto;
import com.lcwa.electronic.store.dtos.PageableResponse;
import com.lcwa.electronic.store.entities.*;
import com.lcwa.electronic.store.exceptions.BadApiRequest;
import com.lcwa.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwa.electronic.store.helper.Helper;
import com.lcwa.electronic.store.repositories.CartRepository;
import com.lcwa.electronic.store.repositories.OrderRepository;
import com.lcwa.electronic.store.repositories.UserRepository;
import com.lcwa.electronic.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {


    // dependency

    // user ko fetch karne k lie
    @Autowired
    private UserRepository userRepository;

    // order
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartRepository cartRepository;

    // ab hum json mein hi cartid,userid bhejdege url se lene ki jarurat ni hein
    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {

        String userId=orderDto.getUserId();
        String cartId=orderDto.getCartId();
        //step-1  fetch User
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user with given userId not found in the database"));
        // ab is user k corresponding order ko generate karna hein
        // humein orderItems milege cart se islie cartId,isse pata chlega ki is cart k sare   items ko order mein add karna hein

        // step-2 fetch cart
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("cart with given cartId not found"));
        // IS CART K ANDAR JITNE BHI ITEMS HE UNHE order m convert mein karna hein  ,order generate karna hein and jitne bhi cartitems he unhe orderItem mein convert karna hein

        // step-3 get items in the cart
        List<CartItem> cartItems = cart.getItems();

        if(cartItems.size()<=0){
            throw new BadApiRequest("Invalid number of items in cart !! ");
        }
        // other check we can implement

        // step-4 generate orders
        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .billingAddress(orderDto.getBillingAddress())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();


        // orderAmount,orderItem set ni kia abhi tak

        // step-5 convert all cartItems to orderItems

//        int total=0  pr ye change ni karpayege kyuki lambda m variables final hote hein
        AtomicReference<Integer> orderAmount=new AtomicReference<>(0);

        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {

            // cartItem --> orderItem mein
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalprice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();
            orderAmount.set(orderAmount.get()+orderItem.getTotalprice());

            return orderItem;
        }).collect(Collectors.toList());

        // sab set hogya order mein
        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        // ab hum cart ko clear krdege
        cart.getItems().clear();
        // ab order and cart dono ko save karna hein
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);

        return  modelMapper.map(savedOrder,OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("order with given orderId not found !!"));
        orderRepository.delete(order);
        // order remove hoga to orderitems bhi remove hojayge bcz cascade use kar rakha hein order.java mein
    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user with given userId not found!!"));
        List<Order> orders = orderRepository.findByUser(user);

        List<OrderDto> orderDtos = orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {


        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> page = orderRepository.findAll(pageable);

        return Helper.getPageableResponse(page,OrderDto.class);
    }
}
