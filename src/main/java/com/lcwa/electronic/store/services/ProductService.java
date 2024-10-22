package com.lcwa.electronic.store.services;

import com.lcwa.electronic.store.dtos.PageableResponse;
import com.lcwa.electronic.store.dtos.ProductDto;
import com.lcwa.electronic.store.entities.Product;
import org.hibernate.query.Page;

import java.util.List;

public interface ProductService {

    //create product
    ProductDto createProduct(ProductDto productDto);

    //update product
    ProductDto updateProduct(ProductDto productDto, String productId);

    //delete product
    void deleteProduct(String productId);

    // get single product
    ProductDto getProduct(String productId);

    // get all product
    // pagination and sorting ko implement karne k lie
    PageableResponse<ProductDto> getAllProduct(int pageNumber,int pageSize,String sortBy,String sortDir);

    //  get all : live
    // pagination and sorting ko implement karne k lie
    PageableResponse<ProductDto> getAllLiveProduct(int pageNumber,int pageSize,String sortBy,String sortDir);

    // search product
    // pagination and sorting ko implement karne k lie
    PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber,int pageSize,String sortBy,String sortDir);


    // create product with category
    ProductDto createProductWithCategory(ProductDto productDto,String categoryId);


    // update category in existing product
    // updating category
    ProductDto updateCategory(String productId,String categoryId);

    // get all products of given category
    PageableResponse<ProductDto> getAllProductsOfGivenCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir);



}
