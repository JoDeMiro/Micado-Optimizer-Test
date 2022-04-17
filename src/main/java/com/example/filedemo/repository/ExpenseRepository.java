package com.example.filedemo.repository;

import com.example.filedemo.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends MongoRepository<Expense, String> {

    @Query("{'name': ?0}")
    Optional<Expense> findByName(String name);

    @Query("{'name': {$regex: ?0 }})")
    List<Expense> findByQuery(String name);

    @Query("{'name': ?0}")
    List<Expense> findByNameStartingWith(String name);

}
