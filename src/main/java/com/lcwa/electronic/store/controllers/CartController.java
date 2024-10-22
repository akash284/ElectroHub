package com.lcwa.electronic.store.controllers;


import com.lcwa.electronic.store.config.AppConstants;
import com.lcwa.electronic.store.dtos.AddItemToCardRequest;
import com.lcwa.electronic.store.dtos.ApiResponseMessage;
import com.lcwa.electronic.store.dtos.CartDto;
import com.lcwa.electronic.store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    // add items to cart
 //   @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @PreAuthorize("hasAnyRole('"+ AppConstants.ROLE_NORMAL+"','"+ AppConstants.ROLE_ADMIN+"')")
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId,
                                                 @RequestBody AddItemToCardRequest request){
        CartDto cartDto = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);

    }

    // remove item from cart
    @PreAuthorize("hasAnyRole('"+ AppConstants.ROLE_NORMAL+"','"+ AppConstants.ROLE_ADMIN+"')")
    @DeleteMapping("{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId,
                                                                 @PathVariable int itemId){
        cartService.removeItemFromCart(userId,itemId);

        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("Item is removed!!")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    // CLEAR CART
    @PreAuthorize("hasAnyRole('"+ AppConstants.ROLE_NORMAL+"','"+ AppConstants.ROLE_ADMIN+"')")
    @DeleteMapping("{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId){
        cartService.clearCart(userId);

        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("cart is cleared !!")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    // get cart
    @PreAuthorize("hasAnyRole('"+ AppConstants.ROLE_NORMAL+"','"+ AppConstants.ROLE_ADMIN+"')")
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId){
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);

    }
}
