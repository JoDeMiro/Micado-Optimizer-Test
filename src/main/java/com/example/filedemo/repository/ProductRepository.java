package com.example.filedemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.filedemo.model.Product;

import javax.persistence.QueryHint;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByPriceGreaterThan(BigDecimal price);

    List<Product> findByPriceLessThan(BigDecimal price);

    // Count
    long countByPriceGreaterThan(BigDecimal priceThreshold);

    long countByPriceLessThan(BigDecimal priceThreshold);

    // JPQL query calculate the mean price
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "false") })
    @Query("SELECT AVG(p.price) FROM Product p WHERE p.price > :priceThreshold")
    BigDecimal findAveragePriceByPriceGreaterThan(@Param("priceThreshold") BigDecimal priceThreshold);

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "false") })
    @Query("SELECT AVG(p.price) FROM Product p WHERE p.price < :priceThreshold")
    BigDecimal findAveragePriceByPriceLessThan(@Param("priceThreshold") BigDecimal priceThreshold);
}

