package com.lcwa.electronic.store.services;

import com.lcwa.electronic.store.dtos.PageableResponse;
import com.lcwa.electronic.store.dtos.categoryDto;

import java.util.List;

public interface CategoryService {

    // create category
    categoryDto createCategory(categoryDto categoryDto);

    //update category
    categoryDto updateCategory(categoryDto categoryDto,String categoryId);

    //delete category
    void deleteCategory(String categoryId);

    // get all
    // Pagination apply karna to yeh bhi use karlege
    PageableResponse<categoryDto> getAllCategory(int pageNumber,int pageSize,String sortBy,String sortDir);
    // get single category
    categoryDto getSingleCategory(String categoryId);
    // search category


}
