package com.lcwa.electronic.store.repositories;

import com.lcwa.electronic.store.entities.Category;
import com.lcwa.electronic.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<Product,String> {

    //search
    // custom finder methods
    // query methods


    Page<Product> findByTitleContaining(String subtitle,Pageable pageable);
    Page<Product> findByLiveTrue(Pageable pageable);


    // find all products with given category
    Page<Product> findByCategory(Category category,Pageable pageable);
}
