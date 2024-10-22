package com.lcwa.electronic.store.services.impl;

import com.lcwa.electronic.store.dtos.AddItemToCardRequest;
import com.lcwa.electronic.store.dtos.CartDto;
import com.lcwa.electronic.store.entities.Cart;
import com.lcwa.electronic.store.entities.CartItem;
import com.lcwa.electronic.store.entities.Product;
import com.lcwa.electronic.store.entities.User;
import com.lcwa.electronic.store.exceptions.BadApiRequest;
import com.lcwa.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwa.electronic.store.repositories.CartItemRepository;
import com.lcwa.electronic.store.repositories.CartRepository;
import com.lcwa.electronic.store.repositories.ProductRepository;
import com.lcwa.electronic.store.repositories.UserRepository;
import com.lcwa.electronic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
public class CartServiceImpl implements CartService {

    // dependencies
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private CartItemRepository cartItemRepository;


    @Override
    public CartDto addItemToCart(String userId, AddItemToCardRequest request) {

        String productId=request.getProductId();
        int quantity=request.getQuantity();


        if(quantity<=0){
            throw new BadApiRequest("requested quantity is not valid");
        }
        // fetch the product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product with given id not found in the database !!"));
        // user ko fetch krege taki cart fetch karpaye
        // fetch the user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user with given id not found in the database!!"));

        // fetch the cart

        Cart cart=null;
        try{
            cart=cartRepository.findByUser(user).get();
        }catch (NoSuchElementException ex){
            cart=new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }

        // perform cart operations
        // if cart item already present : Then Update

//        boolean updated=false;
//        // lambda k andar variable ko update ni krskte he kyuki final hota hein islie AtomicReference use krege
        AtomicReference<Boolean>updated=new AtomicReference<>(false);

        List<CartItem> items = cart.getItems();// all items in the cart

        items = items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                //item already present in cart
                item.setQuantity(quantity);
                item.setTotalprice(quantity * product.getDiscountedPrice());
             //   updated = true;  // mtlb ki ye pehle se cart m present tha

                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

       // cart.setItems(UpdatedItems);

        // cart availabe ni h to create karo
        // create cartitems only if not created earlier
        //updated.get==false
        if(!updated.get()){
            // create items
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalprice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            // adding item to card
            cart.getItems().add(cartItem);
        }

        //user naya hota to user add kardete
        cart.setUser(user);

        // cart update hoga to cartItem bhi update hojyga cascade all kar rkha hein
        Cart updatedCart = cartRepository.save(cart);

return modelMapper.map(updatedCart,CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {

        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFoundException("cart item not found in database"));

        cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {

        // fetch the user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user with given id not found in the database!!"));
       // fetch the cart from the user
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart with given user not found"));

        // cart se items ko hataya toh turant database se bhi remove hojana chahiye islie orphanRemoval=true
        cart.getItems().clear();
        cartRepository.save(cart);
        cartRepository.delete(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {

        // fetch the user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user with given id not found in the database!!"));
        // fetch the cart from the user
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart with given user not found"));

        return modelMapper.map(cart,CartDto.class);
    }
}
