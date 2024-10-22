package com.lcwa.electronic.store.controllers;

import com.lcwa.electronic.store.dtos.*;
import com.lcwa.electronic.store.services.FileService;
import com.lcwa.electronic.store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
public class ProductController {


    Logger logger= LoggerFactory.getLogger(ProductController.class);
    // dependency 
    @Autowired
    private ProductService productService;

    // for uploading the image

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String imagePath;

    // create product basically client se data ayga
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {

        ProductDto createdProduct = productService.createProduct(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }
    // update product client sent request to update the product info

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto,
                                                    @PathVariable String productId) {

        ProductDto updatedProduct = productService.updateProduct(productDto, productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    // delete product client ask to delete the product
    // voh product id ni mila to response mein or bhi chize dege client ko response mein
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId) {

        productService.deleteProduct(productId);

        ApiResponseMessage productIsDeletedSuccesfully = ApiResponseMessage
                .builder()
                .message("Product is deleted succesfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(productIsDeletedSuccesfully, HttpStatus.OK);
    }

    // get single product : client ask for product
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getSingleProduct(@PathVariable String productId) {

        ProductDto productdto = productService.getProduct(productId);
        return new ResponseEntity<>(productdto, HttpStatus.OK);
    }

    // get all product : cleint ask for all the product
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProduct(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> allProducts = productService.getAllProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    // get all live
//    /products/live
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProducts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortByr", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> allProducts = productService.getAllLiveProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    // search all
    @GetMapping("/search/{query_keyword}")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProductsContainingQuery(
            @PathVariable String query_keyword,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortByr", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> allProducts = productService.searchByTitle(query_keyword,pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    // upload image
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @PathVariable String productId,
            @RequestParam("productImage")MultipartFile image
            ) throws IOException {

        // UPLOADED FILE
        String fileName = fileService.UploadFile(image, imagePath);
        // get the product
        ProductDto productDto = productService.getProduct(productId);
        // UPDATE THE PRODUCT
        // set the image name
        productDto.setProductImageName(fileName);
        ProductDto updatedProduct = productService.updateProduct(productDto, productId);
        ImageResponse productImageIsSuccesfullySaved = ImageResponse.builder().imageName(updatedProduct.getProductImageName())
                .message("product image is succesfully saved")
                .status(HttpStatus.CREATED)
                .success(true)
                .build();
        return new ResponseEntity<>(productImageIsSuccesfullySaved,HttpStatus.CREATED);

    }

    // Serve product image

    @GetMapping("/image/{productId}")
    public void serveProductImage(@PathVariable String productId, HttpServletResponse response) throws IOException {

        // GET USER
        ProductDto productdto = productService.getProduct(productId);
        logger.info("user image name {} :",productdto.getProductImageName());
        // GET THE IMAGE
        InputStream resources = fileService.getResources(imagePath, productdto.getProductImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        // COPY THE DATA
        StreamUtils.copy(resources,response.getOutputStream());

    }



}
