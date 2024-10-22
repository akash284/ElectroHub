package com.lcwa.electronic.store.services;

import com.lcwa.electronic.store.dtos.AddItemToCardRequest;
import com.lcwa.electronic.store.dtos.CartDto;

public interface CartService {

    // add items to the cart: cart hi ni heto item kese rkhege?
    // jab koi user cart mein data ko data ko add kar raha hoga

    //CASE1 :cart for that user,cart is not available in dbase then hum cart ko create krdege: CREATE THE CART AND add ITEM
    // CASE2: cart available heto add the items to the cart

    // Cart mein data ko add karege to hum pura cart hi return kardege and uske andr jo bhi items he voh sab

    // every user has cart,user ka cart nikalege

     // userId==cart bcz every user has one cart
    // add item to cart
    CartDto addItemToCart(String userId, AddItemToCardRequest request);

    // remove item form cart:
    void removeItemFromCart(String userId,int cartItem);

    // remove all items from cart
    void clearCart(String userId);

    //get cart by user
    CartDto getCartByUser(String userId);
}
