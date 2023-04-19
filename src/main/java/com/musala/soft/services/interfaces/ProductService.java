package com.musala.soft.services.interfaces;

import com.musala.soft.models.Product;

import java.util.List;

public interface ProductService {
    List<Product> saveAll(List<Product> products);
    List<Product> findByTransactionId(String transactionId);
}
