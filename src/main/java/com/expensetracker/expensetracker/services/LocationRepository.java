package com.expensetracker.expensetracker.services;

import com.expensetracker.expensetracker.models.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

    // Query method for filtering categories by name with pagination
    Page<Location> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Retrieve all categories without any filter, using pagination
    Page<Location> findAll(Pageable pageable);
}
