package com.example.filedemo.controller;

import com.example.filedemo.model.Expense;
import com.example.filedemo.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/regex2/{name}")
    public ResponseEntity<List<Expense>> getExpenseByRegex2(@PathVariable String name) {
        String q = "a?b";
        // vagy a vagy b szerepeljen benne
        return ResponseEntity.ok(expenseService.getExpenseByQuery(q));
    }

    // http://localhost:8080/mongodb/expense/regex3/ua$
    // http://localhost:8080/mongodb/expense/regex/%5Eab
    // http://localhost:8080/mongodb/expense/regex/^ab
    // http://localhost:8080/mongodb/expense/regex/%5Eab.*ed$
    //http://localhost:8080/mongodb/expense/regex/%5Eaa
    // http://localhost:8080/mongodb/expense/regex/%5Eaa.b
    // http://localhost:8080/mongodb/expense/regex/%5Eaa?b
    @GetMapping("/regex3/{name}")
    public Integer getExpenseByRegex3(@PathVariable String name) {
        List<Expense> expenseByQuery = expenseService.getExpenseByQuery(name);
        int size = expenseByQuery.size();
        return size;
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
