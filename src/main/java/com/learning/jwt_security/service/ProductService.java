package com.learning.jwt_security.service;

import com.learning.jwt_security.model.Product;
import com.learning.jwt_security.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;

    // Get All Products
    public List<Product> allProducts(){
        return productRepo.findAll();
    }

    // Get Product by Id
    public Optional<Product> getProductById(Long id){
        return productRepo.findById(id);
    }

    // Save/Update Product
    public Product saveProduct(Product product){
        return productRepo.save(product);
    }

    // Delete Product
    public String deleteById(Long id){
        productRepo.deleteById(id);
        return "Product Deleted Successfully";
    }
}
