package com.expensetracker.expensetracker.controllers;

import com.expensetracker.expensetracker.models.Location;
import com.expensetracker.expensetracker.services.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LocationController {
    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    // Endpoint to get categories with pagination and optional filtering by name
    @GetMapping("/locations")
    public String getCategories(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(required = false) String name,
                                Model model, HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Location> locations = locationService.getLocations(name, pageable);
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("locations", locations);
        return "locations";
    }
//  Creating a new category
    @GetMapping("/locations/add")
    public String showCreateCategoryForm(Model model,HttpServletRequest request) {
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("location", new Location());
        model.addAttribute("pageTitle", "Add New Location - Expense Tracker");
        return "create-new-location";
    }

    @PostMapping("/locations/add")
    public String saveTransaction(@Valid @ModelAttribute("location") Location location,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes, HttpServletRequest request,Model model) {
        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            model.addAttribute("requestURI", request.getRequestURI());
            return "create-new-location";
        }
        locationService.save(location);
        redirectAttributes.addFlashAttribute("successMessage", "Location added successfully!");
        return "redirect:/locations"; // Redirect to  transaction list
    }

    @GetMapping("/locations/edit/{id}")
    public String showLocationEditForm(@PathVariable Long id, Model model, HttpServletRequest request) {
        Location location = locationService.getLocationById(id);
        System.out.println("Searching for location with ID: " + id);
        if (location != null) {
            model.addAttribute("requestURI", request.getRequestURI());
            model.addAttribute("location", location);
            model.addAttribute("pageTitle", "Edit Location - Expense Tracker");
            return "edit-location"; // The edit page template
        } else {
            return "redirect:/locations"; // Redirect to list if transaction not found
        }
    }

    @PostMapping("/locations/edit/{id}")
    public String updateLocation(@PathVariable Long id,
                                 @Valid @ModelAttribute("location") Location location,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("requestURI", request.getRequestURI());
            return "edit-location"; // Return to the form if there are validation errors
        }

        // Set the transaction ID to ensure we are updating the correct record
        location.setId(id);
        locationService.save(location); // Save the updated transaction

        redirectAttributes.addFlashAttribute("successMessage", "Location updated successfully!");
        return "redirect:/locations"; // Redirect back to transaction list
    }
    

}