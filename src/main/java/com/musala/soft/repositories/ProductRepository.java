package com.musala.soft.repositories;

import com.musala.soft.models.Product;

import java.util.List;

public interface ProductRepository extends GenericRepository<Product, Long> {
    List<Product> findByTransactionId(String transactionId);
}
