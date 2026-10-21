package com.expensetracker.expensetracker.controllers;
import com.expensetracker.expensetracker.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class DashboardController {
    @Autowired
    private OrderService orderService;
    // This method maps the root URL ("/") to the dashboard view
    @GetMapping("/")
    public String showDashboard(Model model,HttpServletRequest request) {
        // Returns the name of the HTML template (index.html) inside /templates
        model.addAttribute("pageTitle", "Dashboard - Expense Tracker");
        model.addAttribute("requestURI", request.getRequestURI()); // Add request URI to the model
        return "indexorder";
    }

    @GetMapping("/reports")
    public String showReports(Model model, HttpServletRequest request) {
        // Returns the name of the HTML template (index.html) inside /templates
        model.addAttribute("requestURI", request.getRequestURI()); // Add request URI to the model
        model.addAttribute("pageTitle", "Reports - Expense Tracker");
        return "orderreports";
    }

    // Method to retrieve total expenses for monthly, weekly, and yearly via AJAX
    @GetMapping("/total-expenses")
    @ResponseBody
    public ResponseEntity<Map<String, Double>> getTotalExpenses() {
        Map<String, Double> expenses = orderService.getTotalExpenses();
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/total-incomes")
    @ResponseBody
    public ResponseEntity<Map<String, Double>> getTotalIncomes() {
        Map<String, Double> incomes = orderService.getTotalIncomes();
        return ResponseEntity.ok(incomes);
    }


    @GetMapping("/total-orders")
    @ResponseBody
    public ResponseEntity<Map<String, Double>> getTotalTransactions() {
        Map<String, Double> incomes = orderService.getTotalOrders();
        return ResponseEntity.ok(incomes);
    }


    // Endpoint for total incomes by month
    @GetMapping("/total-incomes-by-month")
    @ResponseBody
    public ResponseEntity<Map<String, Double>> getTotalIncomesByMonth() {
        Map<String, Double> incomesByMonth = orderService.getTotalIncomesByMonth();
        return ResponseEntity.ok(incomesByMonth);
    }

    // Endpoint for total expenses by month
    @GetMapping("/total-expenses-by-month")
    @ResponseBody
    public ResponseEntity<Map<String, Double>> getTotalExpensesByMonth() {
        Map<String, Double> expensesByMonth = orderService.getTotalExpensesByMonth();
        return ResponseEntity.ok(expensesByMonth);
    }
    //
    // Endpoint for total transactions count by month
    @GetMapping("/total-orders-by-month")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> getTotalTransactionsByMonth() {
        Map<String, Integer> ordersByMonth = orderService.getTotalOrdersByMonth();
        return ResponseEntity.ok(ordersByMonth);
    }







}
