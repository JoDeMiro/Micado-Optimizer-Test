package com.example.filedemo.controller;

import com.example.filedemo.model.Expense;
import com.example.filedemo.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/mongodb/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity addExpense(@RequestBody Expense expense) {
        expenseService.addExpense(expense);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Object> updateExpense(@RequestBody Expense expense) {
        expenseService.updateExpense(expense);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Expense> getExpenseByName(@PathVariable String name) {
        return ResponseEntity.ok(expenseService.getExpenseByName(name));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteExpense(@PathVariable String id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    // Extra
    @GetMapping("/create")
    public ResponseEntity<Object> createExpenseSample() {
        expenseService.createExpenseSample();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/generate/{number}")
    public ResponseEntity<Object> generateExpenseSample(@PathVariable int number) {
        expenseService.generateExpenseSample(number);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/regex/{name}")
    public ResponseEntity<List<Expense>> getExpenseByRegex(@PathVariable String name) {
        return ResponseEntity.ok(expenseService.getExpenseByQuery(name));
    }

    @GetMapping("/startswith/{name}")
    public ResponseEntity<List<Expense>> findByNameStartingWith(@PathVariable String name) {
        return ResponseEntity.ok(expenseService.findByNameStartingWith(name));
    }

    @GetMapping("/clear")
    public ResponseEntity<Object> clear() {
        expenseService.clearExpense();
        expenseService.setInitialized(false);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/init")
    public ResponseEntity<Object> init() {
        // If initialized then skip
        // Clear first if you want to initialize again
        if (expenseService.isInitialized() == false) {
            expenseService.setInitialized(true);
            expenseService.generateExpenseSample(1000);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/init/{sample}")
    public ResponseEntity<Object> init(@PathVariable int sample) {
        // If initialized then skip
        // Clear first if you want to initialize again
        if (expenseService.isInitialized() == false) {
            expenseService.setInitialized(true);
            expenseService.generateExpenseSample(sample);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
