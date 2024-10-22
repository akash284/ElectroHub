package com.lcwa.electronic.store.controllers;

import com.lcwa.electronic.store.dtos.ApiResponseMessage;
import com.lcwa.electronic.store.dtos.PageableResponse;
import com.lcwa.electronic.store.dtos.ProductDto;
import com.lcwa.electronic.store.dtos.categoryDto;
import com.lcwa.electronic.store.services.CategoryService;
import com.lcwa.electronic.store.services.ProductService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {


    // dependency
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;


    //create category
    // it will accept the data and load to categorydto
    @PostMapping
    public ResponseEntity<categoryDto> createCategory(@Valid @RequestBody categoryDto categoryDto){

        // call service to save object
        // data ko save krege service ki help se
        // delegate the task to service
        categoryDto categorydto1 = categoryService.createCategory(categoryDto);

        return new ResponseEntity<>(categorydto1, HttpStatus.CREATED);

    }

    //update
    // iska bhi data client se ayga to @RequestBody and @PathVariable use krege
    @PutMapping("/{categoryId}")
    public ResponseEntity<categoryDto> updateCategory(@RequestBody  categoryDto categoryDto,
                                                      @PathVariable  String categoryId){

      categoryDto categoryDto1 = categoryService.updateCategory(categoryDto, categoryId);

      return new ResponseEntity<>(categoryDto1,HttpStatus.OK);

    }
    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){
        categoryService.deleteCategory(categoryId);
        ApiResponseMessage categoryIsDeleted = ApiResponseMessage.builder().message("category is deleted ").status(HttpStatus.OK).success(true).build();

        return new ResponseEntity<>(categoryIsDeleted,HttpStatus.OK);
    }
    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<categoryDto>> getAllCategory(
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int  pageSize,
            @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir
    ){

        PageableResponse<categoryDto> pageableResponse = categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortDir);

        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }
    //get single category
    @GetMapping("/{categoryId}")
    public ResponseEntity<categoryDto> getSingleCategory(@PathVariable String categoryId){

        categoryDto singleCategory = categoryService.getSingleCategory(categoryId);

        return ResponseEntity.ok(singleCategory);
    }

    // create product with category
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestBody ProductDto dto
    ){
        ProductDto productWithCategory = productService.createProductWithCategory(dto, categoryId);

        return new ResponseEntity<>(productWithCategory,HttpStatus.CREATED);
    }

    // update category in existng product
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategoryInExistingProduct(
            @PathVariable String productId,
            @PathVariable String categoryId
    )
    {
        ProductDto productDto = productService.updateCategory(productId, categoryId);

        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }

    // get products of given category
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getALlProductsOfGivenCategory(
            @PathVariable String categoryId,
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int  pageSize,
            @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> allProductsOfGivenCategory = productService.getAllProductsOfGivenCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);

        return new ResponseEntity<>(allProductsOfGivenCategory,HttpStatus.OK);
    }

}
