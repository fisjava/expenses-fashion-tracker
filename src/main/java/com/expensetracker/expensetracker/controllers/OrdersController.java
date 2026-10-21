package com.expensetracker.expensetracker.controllers;

import java.io.IOException;
import com.expensetracker.expensetracker.models.Customer;
import com.expensetracker.expensetracker.models.Order;
import com.expensetracker.expensetracker.services.CustomerService;
import com.expensetracker.expensetracker.services.OrderreportService;
import com.expensetracker.expensetracker.services.OrderRepository;
import com.expensetracker.expensetracker.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    
    
    @Autowired
    private CustomerService customerService;
    

    @Autowired
    private OrderreportService orderreportService;
    
    

    // Method to list all orders
    @GetMapping
    public String listOrders(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(required = false) String description,
                                   @RequestParam(required = false) BigDecimal amount,
                                   @RequestParam(required = false) String amountFilter, // "=", "<=", ">="
                                   @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                   @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                   Model model, HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderService.findOrders(description, amount, amountFilter, startDate, endDate, pageable);
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("orders", orders);
        model.addAttribute("description", description);
        model.addAttribute("amount", amount);
        model.addAttribute("amountFilter", amountFilter);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "orders"; // View that displays the list of transactions
    }

    // Method to show the form to add a new transaction
    @GetMapping("/add")
    public String showOrdersForm(Model model, HttpServletRequest request) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers); // Pass categories to the form
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("order", new Order());
        model.addAttribute("pageTitle", "Add New Order - Expense Tracker");
        return "add-order";
    }

    // Method to save the new transaction
    @PostMapping("/add")
    public String saveOrder(@Valid @ModelAttribute("order") Order order,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "add-order";
        }
        orderRepository.save(order);
        redirectAttributes.addFlashAttribute("successMessage", "Order added successfully!");
        return "redirect:/orders"; // Redirect to  transaction list
    }

    @GetMapping("/view/{id}")
    @ResponseBody
    public ResponseEntity<Order> viewOrder(@PathVariable Long id) {
        // Fetch the transaction by ID from the repository or service layer
        Order order = orderService.getOrderById(id);

        // Check if transaction exists and return JSON response
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if transaction not found
        }
    }
   
    @GetMapping("/search")
    public String showsearchOrdersForm(Model model, HttpServletRequest request) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers); // Pass categories to the form
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("order", new Order());
        model.addAttribute("pageTitle", "Search Order - Expense Tracker");
        return "search";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpServletRequest request) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("requestURI", request.getRequestURI());
        if (order != null) {
            List<Customer> customers = customerService.getAllCustomers();
            model.addAttribute("customers", customers); // Pass categories to the form
            model.addAttribute("order", order);

            model.addAttribute("pageTitle", "Edit Order - Expense Tracker");
            return "edit-order"; // The edit page template
        } else {
            return "redirect:/orders"; // Redirect to list if transaction not found
        }
    }

    @PostMapping("/edit/{id}")
    public String updateOrder(@PathVariable Long id,
                                    @Valid @ModelAttribute("order") Order order,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "edit-order"; // Return to the form if there are validation errors
        }

        // Set the transaction ID to ensure we are updating the correct record
        order.setId(id);
        orderRepository.save(order); // Save the updated transaction

        redirectAttributes.addFlashAttribute("successMessage", "Order updated successfully!");
        return "redirect:/orders"; // Redirect back to transaction list
    }
    
    @GetMapping("/report/{format}")
    public String generateReport(@PathVariable String format) throws FileNotFoundException, JRException {
        return orderreportService.exportReport(format);
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable long id, Model model, RedirectAttributes redirectAttributes) {
        orderService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Order deleted successfully");
        return "redirect:/orders";
    }


}
