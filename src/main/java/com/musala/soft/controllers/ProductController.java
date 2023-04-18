package com.musala.soft.controllers;

import com.musala.soft.models.Product;
import com.musala.soft.models.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/products")
@Slf4j
public class ProductController {

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ProductDto> register(@RequestBody @Valid ProductDto productDto){
        log.info("Register a new Product {}", productDto);
        Product product = modelMapper.map(productDto, Product.class);
        log.info("Register Product Successfully {}", product);
        return new ResponseEntity<ProductDto>(modelMapper.map(product, ProductDto.class), HttpStatus.CREATED);
    }
}
