package com.expensetracker.expensetracker.services;

import com.expensetracker.expensetracker.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Page<Order> findOrders(String description, BigDecimal amount, String amountFilter,
                                              LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return orderRepository.findFilteredOrders(description, amount, amountFilter, startDate, endDate, pageable);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    // Fetch total expenses for month, week, year
    public Map<String, Double> getTotalExpenses() {
        Map<String, Double> totalExpenses = new HashMap<>();

        // Current date
        LocalDate currentDate = LocalDate.now();

        // Calculate total expenses for the current month
        LocalDate firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());
        double monthlyExpense = orderRepository.sumExpensesBetweenDates(firstDayOfMonth, currentDate);
        totalExpenses.put("monthly", monthlyExpense);

        // Calculate total expenses for the current week (assuming week starts on Monday)
        LocalDate startOfWeek = currentDate.with(java.time.DayOfWeek.MONDAY);
        double weeklyExpense = orderRepository.sumExpensesBetweenDates(startOfWeek, currentDate);
        totalExpenses.put("weekly", weeklyExpense);

        // Calculate total expenses for the current year
        LocalDate firstDayOfYear = currentDate.with(TemporalAdjusters.firstDayOfYear());
        double yearlyExpense = orderRepository.sumExpensesBetweenDates(firstDayOfYear, currentDate);
        totalExpenses.put("yearly", yearlyExpense);

        return totalExpenses;
    }


    // Method to retrieve total incomes for the current month, week, and year
    public Map<String, Double> getTotalIncomes() {
        Map<String, Double> totalIncomes = new HashMap<>();

        // Get current date
        LocalDate currentDate = LocalDate.now();

        // Calculate total incomes for the current month
        LocalDate firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());
        double monthlyIncome = orderRepository.sumIncomesBetweenDates(firstDayOfMonth, currentDate);
        totalIncomes.put("monthly", monthlyIncome);

        // Calculate total incomes for the current week (assuming week starts on Monday)
        LocalDate startOfWeek = currentDate.with(DayOfWeek.MONDAY);
        double weeklyIncome = orderRepository.sumIncomesBetweenDates(startOfWeek, currentDate);
        totalIncomes.put("weekly", weeklyIncome);

        // Calculate total incomes for the current year
        LocalDate firstDayOfYear = currentDate.with(TemporalAdjusters.firstDayOfYear());
        double yearlyIncome = orderRepository.sumIncomesBetweenDates(firstDayOfYear, currentDate);
        totalIncomes.put("yearly", yearlyIncome);

        return totalIncomes;
    }


    // Method to retrieve total incomes for the current month, week, and year
    public Map<String, Double> getTotalOrders() {
        Map<String, Double> totalIncomes = new HashMap<>();

        // Get current date
        LocalDate currentDate = LocalDate.now();

        // Calculate total incomes for the current month
        LocalDate firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());
        double monthlyIncome = orderRepository.countTransactionsBetweenDates(firstDayOfMonth, currentDate);
        totalIncomes.put("monthly", monthlyIncome);

        // Calculate total incomes for the current week (assuming week starts on Monday)
        LocalDate startOfWeek = currentDate.with(DayOfWeek.MONDAY);
        double weeklyIncome = orderRepository.countTransactionsBetweenDates(startOfWeek, currentDate);
        totalIncomes.put("weekly", weeklyIncome);

        // Calculate total incomes for the current year
        LocalDate firstDayOfYear = currentDate.with(TemporalAdjusters.firstDayOfYear());
        double yearlyIncome = orderRepository.countTransactionsBetweenDates(firstDayOfYear, currentDate);
        totalIncomes.put("yearly", yearlyIncome);

        return totalIncomes;
    }


    public Map<String, Double> getTotalIncomesByMonth() {
        Map<String, Double> totalIncomesByMonth = new HashMap<>();

        // Get the current year
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        // Loop through each month of the current year
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(currentYear, month);
            LocalDate firstDayOfMonth = yearMonth.atDay(1); // First day of the month
            LocalDate lastDayOfMonth = yearMonth.atEndOfMonth(); // Last day of the month

            // Calculate total incomes for the month
            Double totalIncome = orderRepository.countIncomesBetweenDates(firstDayOfMonth, lastDayOfMonth);

            // Check for null value to avoid NullPointerException
            totalIncomesByMonth.put(yearMonth.getMonth().name(), totalIncome != null ? totalIncome : 0.0);
        }

        return totalIncomesByMonth;
    }

    public Map<String, Double> getTotalExpensesByMonth() {
        Map<String, Double> totalExpensesByMonth = new HashMap<>();

        // Get the current year
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        // Loop through each month of the current year
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(currentYear, month);
            LocalDate firstDayOfMonth = yearMonth.atDay(1); // First day of the month
            LocalDate lastDayOfMonth = yearMonth.atEndOfMonth(); // Last day of the month

            // Calculate total expenses for the month
            Double totalExpense = orderRepository.countExpensesBetweenDates(firstDayOfMonth, lastDayOfMonth);

            // If no expenses found (null), set totalExpense to 0.0
            totalExpensesByMonth.put(yearMonth.getMonth().name(), totalExpense != null ? totalExpense : 0.0);
        }

        return totalExpensesByMonth;
    }
    //
    // Method to retrieve total transactions count by month for the current year
    public Map<String, Integer> getTotalOrdersByMonth() {
        Map<String, Integer> totalOrdersByMonth = new HashMap<>();

        // Get the current year
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        // Loop through each month of the current year
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(currentYear, month);
            LocalDate firstDayOfMonth = yearMonth.atDay(1); // First day of the month
            LocalDate lastDayOfMonth = yearMonth.atEndOfMonth(); // Last day of the month

            // Calculate total transactions for the month
            int totalOrders = orderRepository.countTransactionsMonth(firstDayOfMonth, lastDayOfMonth);
            totalOrdersByMonth.put(yearMonth.getMonth().name(), totalOrders);
        }

        return totalOrdersByMonth;
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

}
