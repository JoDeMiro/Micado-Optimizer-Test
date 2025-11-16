package com.example.filedemo.service;

import com.example.filedemo.model.Expense;
import com.example.filedemo.model.ExpenseCategory;
import com.example.filedemo.repository.ExpenseRepository;
import com.example.filedemo.responses.AdvancedExpenseStats;
import com.example.filedemo.responses.ExpensesStats;
import com.example.filedemo.responses.ExtendedExpenseStats;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    private boolean initialized = false;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
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

    public void generateExpenseSample(int number) {
        Expense expense;
        for (int i = 0; i < number; i++) {
            expense = new Expense();
            // Random random
            // Random r = new Random( System.currentTimeMillis() );
            // Random Fixed
            Random r = new Random( i );

            int value =  ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
            BigDecimal amount = new BigDecimal(value);
            expense.setExpenseAmount(amount);
            expense.setExpenseCategory(ExpenseCategory.RESEARCH);

            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 6;

            String generatedString = r.ints(leftLimit, rightLimit + 1)
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            expense.setExpenseName(generatedString);
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

    public Long countByNameRegex(String name) {
        // ugyanaz a logika, mint a listázásnál: bárhol tartalmazza
        String regex = ".*" + name + ".*";
        return expenseRepository.countByNameRegex(regex);
    }

    public Long countAllExpenses() {
        return expenseRepository.count();
    }

    public Long countAllExpensesBy() {
        return expenseRepository.countBy();
    }

    public List<Expense> getFirst10() {
        Pageable firstTen = PageRequest.of(0, 10);  // oldal index = 0, méret = 10
        return expenseRepository.findAll(firstTen).getContent();
    }

    public ExpensesStats getStatsByNamePattern(String pattern) {
        String regex = pattern; // pl. "^a" vagy ".*a.*"
        List<ExpensesStats> stats = expenseRepository.getStatsByNameRegex(regex);
        if (stats.isEmpty()) {
            return new ExpensesStats(0L, 0.0);
        }
        return stats.get(0);
    }

    public ExtendedExpenseStats getExtendedStatsByPattern(String pattern) {
        // itt eldöntheted: a pattern már regex-e, vagy köré teszel ".*" dolgokat
        String regex = pattern; // pl. "^ab", ".*bc.*", stb.

        List<ExtendedExpenseStats> statsList = expenseRepository.getExtendedStatsByNameRegex(regex);
        if (statsList.isEmpty()) {
            return new ExtendedExpenseStats(0L, null, null, null, null);
        }
        return statsList.get(0);
    }

    public AdvancedExpenseStats getAdvancedStatsByPattern(String pattern) {
        String regex = pattern; // vagy ".*" + pattern + ".*", ahogy te használod
        List<AdvancedExpenseStats> statsList = expenseRepository.getAdvancedStatsByNameRegex(regex);
        if (statsList.isEmpty()) {
            return new AdvancedExpenseStats(); // vagy null-safe alapértékek
        }
        return statsList.get(0);
    }


    public void deleteExpense(String id) {
        expenseRepository.deleteById(id);
    }

    public void clearExpense() {
        expenseRepository.deleteAll();
    }

}
