package com.example.filedemo.repository;

import com.example.filedemo.model.Expense;
import com.example.filedemo.responses.ExpensesStats;
import com.example.filedemo.responses.ExtendedExpenseStats;
import org.springframework.data.mongodb.repository.Aggregation;
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

    // Regex alapú COUNT – itt a fontos rész, hogy ne próbáljon metódusnévből query-t gyártani
    @Query(value = "{ 'name': { $regex: ?0 } }", count = true)
    long countByNameRegex(String name);

    long count();

    long countBy();

    @Aggregation(pipeline = {
            "{ '$match': { 'name': { $regex: ?0 } } }",
            "{ '$group': { _id: null, count: { $sum: 1 }, avgAmount: { $avg: { $toDouble: '$amount' } } } }"
    })
    List<ExpensesStats> getStatsByNameRegex(String regex);

    @Aggregation(pipeline = {
            "{ '$match': { 'name': { $regex: ?0 } } }",
            "{ '$sort': { 'amount': 1 } }", // növekvő sorrend amount szerint (stringként tárolt mezőn)
            "{ '$group': { " +
                    "_id: null, " +
                    "count: { $sum: 1 }, " +
                    "avgAmount: { $avg: { $toDouble: '$amount' } }, " +
                    "minAmount: { $min: { $toDouble: '$amount' } }, " +
                    "maxAmount: { $max: { $toDouble: '$amount' } }, " +
                    "sumAmount: { $sum: { $toDouble: '$amount' } } " +
                    "} }"
    })
    List<ExtendedExpenseStats> getExtendedStatsByNameRegex(String regex);

}
