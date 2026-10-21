package com.expensetracker.expensetracker.controllers;

import com.expensetracker.expensetracker.models.Asset;

import com.expensetracker.expensetracker.services.AssetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AssetController {
    private final AssetService assetService;

    @Autowired
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    // Endpoint to get categories with pagination and optional filtering by name
    @GetMapping("/assets")
    public String getAssets(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(required = false) String name,
                                Model model, HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Asset> assets = assetService.getAssets(name, pageable);
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("assets", assets);
        return "assets";
    }
//  Creating a new category
    @GetMapping("/assets/add")
    public String showCreateAssetForm(Model model,HttpServletRequest request) {
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("asset", new Asset());
        model.addAttribute("pageTitle", "Add New Asset - Expense Tracker");
        return "add-asset";
    }

    @PostMapping("/assets/add")
    public String saveAssets(@Valid @ModelAttribute("asset") Asset asset,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes, HttpServletRequest request,Model model) {
        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            model.addAttribute("requestURI", request.getRequestURI());
            return "add-asset";
        }
        assetService.save(asset);
        redirectAttributes.addFlashAttribute("successMessage", "Asset added successfully!");
        return "redirect:/assets"; // Redirect to  transaction list
    }

    @GetMapping("/assets/edit/{id}")
    public String showAssetEditForm(@PathVariable Long id, Model model, HttpServletRequest request) {
        Asset asset = assetService.getAssetById(id);
        System.out.println("Searching for category with ID: " + id);
        if (asset != null) {
            model.addAttribute("requestURI", request.getRequestURI());
            model.addAttribute("asset", asset);
            model.addAttribute("pageTitle", "Edit Asset - Expense Tracker");
            return "edit_asset"; // The edit page template
        } else {
            return "redirect:/assets"; // Redirect to list if transaction not found
        }
    }

    @PostMapping("/assets/edit/{id}")
    public String updateAsset(@PathVariable Long id,
                                 @Valid @ModelAttribute("asset") Asset asset,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("requestURI", request.getRequestURI());
            return "edit_asset"; // Return to the form if there are validation errors
        }

        // Set the transaction ID to ensure we are updating the correct record
        asset.setId(id);
        assetService.save(asset); // Save the updated transaction

        redirectAttributes.addFlashAttribute("successMessage", "Asset updated successfully!");
        return "redirect:/assets"; // Redirect back to transaction list
    }

    @GetMapping("/view/{id}")
    @ResponseBody
    public ResponseEntity<Asset> viewAsset(@PathVariable Long id) {
        // Fetch the transaction by ID from the repository or service layer
        Asset asset = assetService.getAssetById(id);

        // Check if transaction exists and return JSON response
        if (asset != null) {
            return ResponseEntity.ok(asset);
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if transaction not found
        }
    }
    @GetMapping("asset/delete/{id}")
    public String delete(@PathVariable long id, Model model, RedirectAttributes redirectAttributes) {
        assetService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Asset deleted successfully");
        return "redirect:/assets";
    }
}