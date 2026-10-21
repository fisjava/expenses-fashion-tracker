package com.expensetracker.expensetracker.services;

import com.expensetracker.expensetracker.models.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    // Method to retrieve categories with optional name filtering and pagination
    public Page<Location> getLocations(String name, Pageable pageable) {
        // If name filter is provided, use it, otherwise fetch all categories
        if (name != null && !name.isEmpty()) {
            return locationRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            return locationRepository.findAll(pageable);
        }
    }

    // Method to save a new category
    public void save(Location location) {
        locationRepository.save(location);
    }


    public Location getLocationById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    // Method to retrieve all categories without pagination
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
  
}
