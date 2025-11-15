package com.example.filedemo.service;

import com.example.filedemo.repository.ProductRepository;
import com.example.filedemo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Adatbázis nullázása és feltöltése random adatokkal
    public void resetAndLoadDatabase(int number) {
        // Összes adat törlése
        productRepository.deleteAll();

        // Random adatok generálása és betöltése
        // Generáljunk 100 véletlenszerű terméket
        List<Product> products = generateRandomProducts(number);
        productRepository.saveAll(products);
    }

    // Véletlenszerű termékek generálása
    private List<Product> generateRandomProducts(int count) {
        List<Product> products = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            Product product = new Product();
            product.setName("Product " + (i + 1));
            // Véletlenszerű ár 10 és 1000 között
            product.setPrice(BigDecimal.valueOf(10 + (1000 - 10) * random.nextDouble()));
            products.add(product);
        }

        return products;
    }

    // Termékek lekérdezése adott árfekvésben (SELECT)
    public List<Product> getExpensiveProducts(BigDecimal priceThreshold) {
        // return productRepository.findByPriceGreaterThan(priceThreshold);
        return productRepository.findByPriceLessThan(priceThreshold);
    }

    // Termékek száma adott ártartományban (COUNT)
    public long countExpensiveProducts(BigDecimal priceThreshold) {
        // return productRepository.countByPriceGreaterThan(priceThreshold);
        return productRepository.countByPriceLessThan(priceThreshold);
    }

    // Átlagos ár adott ártartományban (AVG)
    public BigDecimal averageExpensiveProductPrice(BigDecimal priceThreshold) {
        // return productRepository.findAveragePriceByPriceGreaterThan(priceThreshold);
        return productRepository.findAveragePriceByPriceLessThan(priceThreshold);
    }

    // Darabszám ami tartalmazza a kifejezést
    public long countNameContaining(String part) {
        return productRepository.countNameContaining(part);
    }

    // Adatbázisba új termékek hozzáadása (INSERT)
    // public void addProducts(List<Product> products) {
    //     productRepository.saveAll(products);
    // }

    // Termékek frissítése adott feltételek alapján (UPDATE)
    // public void updatePrices(BigDecimal priceThreshold, BigDecimal percentage) {
    //     List<Product> products = productRepository.findByPriceGreaterThan(priceThreshold);
    //     for (Product product : products) {
    //         product.setPrice(product.getPrice().multiply(percentage));
    //         productRepository.save(product);
    //     }
    // }

    // Régi termékek törlése (DELETE)
    public void deleteOldProducts() {
        // Implementáld a régi termékek törlését egyéni JPQL lekérdezéssel
    }
}

