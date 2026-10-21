package com.expensetracker.expensetracker.services;

import com.expensetracker.expensetracker.models.Customer;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Query method for filtering categories by name with pagination
   

    // Retrieve all categories without any filter, using pagination
    Page<Customer> findAll(Pageable pageable);

	Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);

	
}
