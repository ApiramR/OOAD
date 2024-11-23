package com.example.demo.controller;

import com.example.demo.model.Inventory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class inventoryController {

    // Simulated in-memory list to store inventory items
    private final List<Inventory> inventoryList = new ArrayList<>();

    // Counter for auto-incrementing InventoryID
    private long inventoryIDCounter = 1;

    // Show Inventory Page
    @GetMapping
    public String showInventoryPage(Model model) {
        model.addAttribute("inventoryList", inventoryList); // Pass inventory list to the view
        model.addAttribute("responseMessage", ""); // Placeholder for messages
        return "pharmacy-inventory"; // Thymeleaf template
    }

    // Add Inventory Item
    @PostMapping("/add")
    public String addInventory(
            @RequestParam int medID,
            @RequestParam String medName,
            @RequestParam String medType,
            @RequestParam String strength,
            @RequestParam String manufacturer,
            @RequestParam long price,
            @RequestParam LocalDate expiryDate,
            @RequestParam int quantityInStock,
            Model model
    ) {/*
        // Create a new Inventory item with auto-incremented InventoryID
        Inventory inventory = new Inventory(medID, medName, medType, strength, manufacturer, price);
        inventory.setInventoryID(inventoryIDCounter++);
        inventory.setExpiryDate(expiryDate);
        inventory.setQuantityInStock(quantityInStock);

        // Add item to list
        inventoryList.add(inventory); */

        // Update model for rendering
        model.addAttribute("inventoryList", inventoryList);
        model.addAttribute("responseMessage", "Inventory item added successfully!");
        return "pharmacy/inventory";
    }

    @PostMapping("/delete/{id}")
    public String deleteInventory(@PathVariable long id, Model model) {
        // Remove the inventory item with the given ID
        boolean removed = inventoryList.removeIf(inventory -> inventory.getInventoryID() == id);

        // Set response message
        String message = removed ? "Inventory item deleted successfully!" : "Inventory item not found!";
        model.addAttribute("responseMessage", message);
        model.addAttribute("inventoryList", inventoryList); // Update list in the model
        return "pharmacy/inventory"; // Redirect back to the inventory page
    }

}
