package com.lcwa.electronic.store.services.impl;

import com.lcwa.electronic.store.dtos.PageableResponse;
import com.lcwa.electronic.store.dtos.ProductDto;
import com.lcwa.electronic.store.entities.Category;
import com.lcwa.electronic.store.entities.Product;
import com.lcwa.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwa.electronic.store.helper.Helper;
import com.lcwa.electronic.store.repositories.CategoryRepository;
import com.lcwa.electronic.store.repositories.ProductRepository;
import com.lcwa.electronic.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {


    Logger logger= LoggerFactory.getLogger(ProductServiceImpl.class);
    // dependency
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Value("${product.image.path}")
    private String fpath;

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public ProductDto createProduct(ProductDto productDto) {


        Product product = modelMapper.map(productDto, Product.class);
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        product.setPAddedDate(new Date());
        Product savedproduct = productRepository.save(product);
        return modelMapper.map(savedproduct,ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {

        // fetch the result of given product id
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with given id not found !! "));

        // updating the product
        product.setDescribtion(productDto.getDescribtion());
        product.setTitle(productDto.getTitle());
        product.setQuantity(productDto.getQuantity());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setProductImageName(productDto.getProductImageName());


        // date jo pehle thi wahi rahegi islie update ni kara hu

        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());

        // save the entity
        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void deleteProduct(String productId) {

        // fetch the result of given product id
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with given id not found !! "));

        String fullpath=fpath+product.getProductImageName();

        try{
            Path path= Paths.get(fullpath);
            Files.delete(path);
        }catch (NoSuchFileException ex){
                logger.info("Product image not found in folder");
                ex.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        productRepository.delete(product);

    }

    @Override
    public ProductDto getProduct(String productId) {

        // fetch the result of given product id
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with given id not found !! "));
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProduct(int pageNumber,int pageSize,String sortBy,String sortDir) {

        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findAll(pageable);


      return   Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLiveProduct(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);

        return   Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByTitleContaining(subTitle,pageable);

        return   Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public ProductDto createProductWithCategory(ProductDto productDto, String categoryId) {

        // fetch the category from the db: one case is ki ye category hohi na db mein
        // to fetch the category humein Category Repository chahiye hogi

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category with given id not found!!"));
        Product product = modelMapper.map(productDto, Product.class);

        // product id
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        //added
        product.setPAddedDate(new Date());

        // produt mein  category set karni hogi
        product.setCategory(category);
        Product savedproduct = productRepository.save(product);
        return modelMapper.map(savedproduct,ProductDto.class);

        // ab category controlller mein jayege bcz url us trike se bana rakhi hein

    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId) {

        // fetch product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with given id : " + productId + " not  found!!"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category with given id: " + categoryId + " not found!!"));

        // update the category
        product.setCategory(category);
        Product saveproduct = productRepository.save(product);


        return modelMapper.map(saveproduct,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProductsOfGivenCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir) {


        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category with given category id not found!!"));

        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);


        // all products with given category
        Page<Product> page = productRepository.findByCategory(category,pageable);

        return Helper.getPageableResponse(page,ProductDto.class);

    }


}
