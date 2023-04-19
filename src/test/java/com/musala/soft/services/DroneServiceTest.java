package com.musala.soft.services;

import com.musala.soft.exceptions.RecordNotFoundException;
import com.musala.soft.exceptions.WeightExceedException;
import com.musala.soft.models.Drone;
import com.musala.soft.models.Product;
import com.musala.soft.models.enums.Model;
import com.musala.soft.services.interfaces.ProductService;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DroneServiceTest {

    @MockBean
    ProductService productService ;

    @Autowired
    DroneServiceImpl droneService;

    @Test
    @Transactional
    @DisplayName("Unit Test: Weight Exceed drone limits" )
    public void whenProductsExceedTheWeight_thenThrowException() throws Exception {
        createDrones(3, 100);

        int numberOfThreads = 10;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger integer = new AtomicInteger();
        for (int i = 0; i < numberOfThreads; i++) {
            service.execute(() -> {
                try {
                    List<Product> products = createProducts(3, (500*(integer.get()+1)));
                    droneService.load(products);
                    System.out.println(integer.get()+"");
                    integer.getAndIncrement();
                }catch (WeightExceedException | RecordNotFoundException ex){
                    System.out.println(ex.getMessage());
                }

                latch.countDown();
            });
        }
        latch.await();
        Assertions.assertNotEquals(numberOfThreads, integer.get());
    }

    private void createDrones(int n, double weight){
        for (int i=0; i<n; i++){
            droneService.register(createDrone(i+"", Model.Lightweight, weight * (i+1)));
        }
    }

    private List<Product> createProducts(int n, double weight){
        List<Product> products = new ArrayList<>();
        for (int i=0; i<n; i++){
            products.add(createProduct(i+"", i+"", weight * (i+1)));
        }
        return products;
    }


    private Drone createDrone(String serialNumber, Model model, double weight){
        Drone drone = new Drone();
        drone.setSerialNumber(serialNumber);
        drone.setModel(Model.Lightweight);
        drone.setWeight(weight);
        return drone;
    }


    private Product createProduct(String name, String code, Double weight){
        Product product = new Product();
        product.setName(name);
        product.setName(code);
        product.setWeight(weight);
        return product;
    }



}
