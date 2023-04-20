package com.musala.soft.services;

import com.musala.soft.models.Product;
import com.musala.soft.repositories.ProductRepository;
import com.musala.soft.services.interfaces.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> saveAll(List<Product> products) {
        String uniqueID = UUID.randomUUID().toString();
        products.stream().forEach(c -> c.setTransactionId(uniqueID));
        productRepository.saveAll(products);
        return products;
    }

    @Override
    public List<Product> findByTransactionId(String transactionId) {
        if (transactionId.isEmpty()) return new ArrayList<>();
        List<Product> productList =  productRepository.findByTransactionId(transactionId);
        return productList;
    }
}
