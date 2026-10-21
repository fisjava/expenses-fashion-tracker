package com.expensetracker.expensetracker.services;

import com.expensetracker.expensetracker.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Method to retrieve categories with optional name filtering and pagination
    public Page<Customer> getCustomers(String name, Pageable pageable) {
        // If name filter is provided, use it, otherwise fetch all categories
        if (name != null && !name.isEmpty()) {
            return customerRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            return customerRepository.findAll(pageable);
        }
    }

    // Method to save a new category
    public void save(Customer customer) {
        customerRepository.save(customer);
    }


    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    // Method to retrieve all categories without pagination
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }
   
}

