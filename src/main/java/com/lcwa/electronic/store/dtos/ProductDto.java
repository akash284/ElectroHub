package com.lcwa.electronic.store.dtos;

import com.lcwa.electronic.store.entities.Category;
import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDto {

    private String productId;
    private String title;
    private String describtion;
    private int price;
    private int discountedPrice;
    private int quantity;
    private Date pAddedDate;
    private boolean live;   // product live he y nai
    private boolean stock;
    private String productImageName;

    // PRODUCT FETCH KARE TO CATEGORY AAYE ISLIE CATEGORY LIA
    private categoryDto category;


}
