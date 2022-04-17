package com.example.filedemo.service;

import com.example.filedemo.model.Expense;
import com.example.filedemo.model.ExpenseCategory;
import com.example.filedemo.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public void addExpense(Expense expense) {
        expenseRepository.insert(expense);
    }

    public void createExpenseSample() {
        Expense expense;
        for (int i = 0; i < 3; i++) {
            expense = new Expense();
            Random r = new Random( System.currentTimeMillis() );
            int value =  ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
            BigDecimal amount = new BigDecimal(value);
            expense.setExpenseAmount(amount);
            expense.setExpenseCategory(ExpenseCategory.RESEARCH);
            if (i == 0) {
                expense.setExpenseName("ABCDEF");
            } else {
                int leftLimit = 97; // letter 'a'
                int rightLimit = 122; // letter 'z'
                int targetStringLength = 6;
                Random random = new Random();

                String generatedString = random.ints(leftLimit, rightLimit + 1)
                        .limit(targetStringLength)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();

                expense.setExpenseName(generatedString);
            }
            expenseRepository.insert(expense);
        }
    }

    public void updateExpense(Expense expense) {
        Expense savedExpense = expenseRepository.findById(expense.getId()).orElseThrow(() -> new RuntimeException(
                String.format("Cannot Find Expense by ID %s", expense.getId())
        ));

        savedExpense.setExpenseName(expense.getExpenseName());
        savedExpense.setExpenseCategory(expense.getExpenseCategory());
        savedExpense.setExpenseAmount(expense.getExpenseAmount());

        expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense getExpenseByName(String name) {
        return expenseRepository.findByName(name).orElseThrow(() -> new RuntimeException(
                String.format("Cannot Find Expense by Name %s", name)
        ));
    }

    public List<Expense> getExpenseByQuery(String name) {
        String query = ".*" + name + ".*";
        return expenseRepository.findByQuery(query);
    }

    public List<Expense> findByNameStartingWith(String name) {
        return expenseRepository.findByNameStartingWith(name);
    }

    public void deleteExpense(String id) {
        expenseRepository.deleteById(id);
    }

}
