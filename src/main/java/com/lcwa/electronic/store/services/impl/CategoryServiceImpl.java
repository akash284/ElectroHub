package com.lcwa.electronic.store.services.impl;

import com.lcwa.electronic.store.dtos.PageableResponse;
import com.lcwa.electronic.store.dtos.categoryDto;
import com.lcwa.electronic.store.entities.Category;
import com.lcwa.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwa.electronic.store.helper.Helper;
import com.lcwa.electronic.store.repositories.CategoryRepository;
import com.lcwa.electronic.store.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    // operations perform karne k lie
    @Autowired
   private CategoryRepository categoryRepository;


    // entity ko dto mein
    // dto ko entity mein convert krne k lie
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public categoryDto createCategory(categoryDto categoryDto) {

        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        // dto ko entity mein convert krege tabhi save krpyge
        Category  catentity = modelMapper.map(categoryDto, Category.class);

        Category savedCategory = categoryRepository.save(catentity);

        return modelMapper.map(savedCategory,categoryDto.class);
    }

    @Override
    public categoryDto updateCategory(categoryDto categoryDto, String categoryId) {

        // get category of given id
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category with given id not found !!"));

        // update category details
        category.setTitle(categoryDto.getTitle());
        category.setDescribtion(categoryDto.getDescribtion());
        category.setCoverImage(categoryDto.getCoverImage());

        Category updatedCategory = categoryRepository.save(category);

        return modelMapper.map(updatedCategory,categoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {

        // get category of given id
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category with given id not found !!"));

        categoryRepository.delete(category);
    }


   // isme humein pagination and sorting  implement karna hein
    @Override
    public PageableResponse<categoryDto> getAllCategory(int pageNumber,int pageSize,String sortBy,String sortDir) {

        Sort sort=(sortDir.equalsIgnoreCase("desc")) ?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<Category> page = categoryRepository.findAll(pageable);

        PageableResponse<categoryDto> pageableResponse = Helper.getPageableResponse(page, categoryDto.class);

        return pageableResponse;
    }

    @Override
    public categoryDto getSingleCategory(String categoryId) {

        // get category of given id
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category not found exception !!"));

        return modelMapper.map(category,categoryDto.class);
    }
}
