package com.expensetracker.expensetracker.services;

import com.expensetracker.expensetracker.models.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    @Autowired
    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    // Method to retrieve categories with optional name filtering and pagination
    public Page<Asset> getAssets(String name, Pageable pageable) {
        // If name filter is provided, use it, otherwise fetch all categories
        if (name != null && !name.isEmpty()) {
            return assetRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            return assetRepository.findAll(pageable);
        }
    }

    // Method to save a new category
    public void save(Asset asset) {
        assetRepository.save(asset);
    }


    public Asset getAssetById(Long id) {
        return assetRepository.findById(id).orElse(null);
    }

    // Method to retrieve all categories without pagination
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }
    public void deleteById(Long id) {
        assetRepository.deleteById(id);
    }
   
}

