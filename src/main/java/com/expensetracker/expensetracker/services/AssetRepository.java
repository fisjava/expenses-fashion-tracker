package com.expensetracker.expensetracker.services;

import com.expensetracker.expensetracker.models.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    // Query method for filtering categories by name with pagination
    Page<Asset> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Retrieve all categories without any filter, using pagination
    Page<Asset> findAll(Pageable pageable);
}
