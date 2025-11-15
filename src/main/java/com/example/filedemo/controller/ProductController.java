package com.example.filedemo.controller;

import com.example.filedemo.responses.GenericResponse;
import com.example.filedemo.service.ProductService;
import com.example.filedemo.model.Product;
import io.micrometer.core.instrument.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@RestController
@RequestMapping("/postgre")
public class ProductController {

    private static String ipAddress;

    static {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            ipAddress = ip.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private ProductService productService;

    // Endpoint: Clear database and fill with data
    @GetMapping("/reset-and-load/{number}")
    public String resetAndLoadDatabase(@PathVariable int number) {
        productService.resetAndLoadDatabase(number);
        return "Database cleared and filled with random data.";
    }

    // Endpoint: Count products where price is less than priceThreshold
    @GetMapping("/count-expensive/{priceThreshold}")
    public GenericResponse countExpensiveProducts(@PathVariable BigDecimal priceThreshold) {

        long start = System.currentTimeMillis();

        long count = productService.countExpensiveProducts(priceThreshold);

        System.out.println(count);

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        GenericResponse<String, BigDecimal, Long, Long> response = new GenericResponse<>("Product Mean Price Response", priceThreshold, count, elapsedTime, ipAddress);

        return response;
    }

    // Endpoint: Count products where price is less than priceThreshold
    @GetMapping("/count-expensive-part/{part}")
    public GenericResponse countNameContaining(@PathVariable String part) {

        long start = System.currentTimeMillis();

        long count = productService.countNameContaining(part);

        System.out.println(count);

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;

        GenericResponse<String, String, Long, Long> response = new GenericResponse<>("Product Mean Price Response", part, count, elapsedTime, ipAddress);

        return response;
    }

    // Endpoint: Mean price where price is less than priceThreshold
    @GetMapping("/avg-expensive-price/{priceThreshold}")
    public GenericResponse averageExpensiveProductPrice(@PathVariable BigDecimal priceThreshold) {

        long start = System.currentTimeMillis();

        BigDecimal avgPrice = productService.averageExpensiveProductPrice(priceThreshold);

        long stop = System.currentTimeMillis();
        long elapsedTime = stop - start;
        GenericResponse<String, BigDecimal, BigDecimal, Long> response = new GenericResponse<>("Product Mean Price Response", priceThreshold, avgPrice, elapsedTime, ipAddress);

        return response;
    }

    // Endpoint
    @GetMapping("/expensive/{priceThreshold}")
    public List<Product> getExpensiveProducts(@PathVariable BigDecimal priceThreshold) {

        return productService.getExpensiveProducts(priceThreshold);
    }

    // Add new product
    // @PostMapping("/add")
    // public String addProducts(@RequestBody List<Product> products) {
    //     productService.addProducts(products);
    //     return "Products are added";
    // }

    // Update price
    // @PutMapping("/update-prices")
    // public String updatePrices(@RequestParam BigDecimal priceThreshold, @RequestParam BigDecimal percentage) {
    //     productService.updatePrices(priceThreshold, percentage);
    //     return "Prices are updated";
    // }

    // Delete old products
    @DeleteMapping("/delete-old")
    public String deleteOldProducts() {
        productService.deleteOldProducts();
        return "Old products are deleted";
    }
}

