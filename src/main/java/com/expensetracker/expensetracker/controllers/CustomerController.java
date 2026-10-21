package com.expensetracker.expensetracker.controllers;

import java.io.FileNotFoundException;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.expensetracker.expensetracker.models.Customer;
import com.expensetracker.expensetracker.models.Location;
import com.expensetracker.expensetracker.services.CustomerRepository;
import com.expensetracker.expensetracker.services.CustomerService;

import com.expensetracker.expensetracker.services.LocationService;
import com.expensetracker.expensetracker.services.CustomerreportService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;


@Controller

public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LocationService locationService;
    
    @Autowired
    private CustomerreportService customerreportService;
    
   
	

    // Method to list all transactions
    @GetMapping("/customers")
    public String getCustomers(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(required = false) String location_id,
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false) String phone,
                                @RequestParam(required = false) String date,
                                Model model, HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerService.getCustomers(name, pageable);
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("customers", customers);
        return "customers";
    }

    // Method to show the form to add a new transaction
    @GetMapping("/customers/add")
    public String showCustomerForm(Model model, HttpServletRequest request) {
        List<Location> locations = locationService.getAllLocations();
        model.addAttribute("locations", locations); // Pass categories to the form
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("customer", new Customer());
        model.addAttribute("pageTitle", "Add New Customer - Expense Tracker");
        return "add-customer";
    }
    
   
    // Method to save the new transaction
    @PostMapping("/customers/add")
    public String saveCustomer(@Valid @ModelAttribute("customer") Customer customer,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "add-customer";
        }
        customerRepository.save(customer);
        redirectAttributes.addFlashAttribute("successMessage", "Customer added successfully!");
        return "redirect:/customers"; // Redirect to  transaction list
    }

    
    @GetMapping("customers/view/{id}")
    @ResponseBody
    public ResponseEntity<Customer> viewCustomer(@PathVariable Long id) {
        // Fetch the transaction by ID from the repository or service layer
        Customer customer = customerService.getCustomerById(id);

        // Check if transaction exists and return JSON response
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if transaction not found
        }
    }

    @GetMapping("customers/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpServletRequest request) {
        Customer customer = customerService.getCustomerById(id);
        model.addAttribute("requestURI", request.getRequestURI());
        if (customer != null) {
            List<Location> locations = locationService.getAllLocations();
            model.addAttribute("locations", locations); // Pass categories to the form
            model.addAttribute("customer", customer);

            model.addAttribute("pageTitle", "Edit Customer - Expense Tracker");
            return "edit-customer"; // The edit page template
        } else {
            return "redirect:/customers"; // Redirect to list if transaction not found
        }
    }

    @PostMapping("customers/edit/{id}")
    public String updateCustomer(@PathVariable Long id,
                                    @Valid @ModelAttribute("customer") Customer customer,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "edit-customer"; // Return to the form if there are validation errors
        }

        // Set the transaction ID to ensure we are updating the correct record
        customer.setId(id);
        customerRepository.save(customer); // Save the updated transaction

        redirectAttributes.addFlashAttribute("successMessage", "Customer updated successfully!");
        return "redirect:/customers"; // Redirect back to transaction list
    }




    @GetMapping("customer/delete/{id}")
    public String delete(@PathVariable long id, Model model, RedirectAttributes redirectAttributes) {
        customerService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Customer deleted successfully");
        return "redirect:/customers";
    }
    
 
  
}
