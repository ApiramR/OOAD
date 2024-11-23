package com.example.demo.controller;

import com.example.demo.model.Inventory;
import com.example.demo.model.Medicine;
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
    @GetMapping("/pharmacy/{username}/inventory")
    public String showInventoryPage(Model model) {
        model.addAttribute("inventoryList", inventoryList); // Pass inventory list to the view
        model.addAttribute("responseMessage", ""); // Placeholder for messages
        return "pharmacy-inventory"; // Thymeleaf template
    }

    // Add Inventory Item
    @PostMapping("/pharmacy/{username}/inventory/add")
    public String addInventory(
            @RequestParam int inventoryID,
            @RequestParam Medicine medID,
            @RequestParam long price,
            @RequestParam LocalDate expiryDate,
            @RequestParam int quantityInStock,
            Model model
    ) {
        // Create a new Inventory item with auto-incremented InventoryID
        Inventory inventory = new Inventory(inventoryID, medID.getMedID(),price,expiryDate,quantityInStock);
        inventory.setInventoryID(inventoryIDCounter++);
        inventory.setMedicine(medID);
        inventory.setPrice(price);
        inventory.setExpiryDate(expiryDate);
        inventory.setQuantityInStock(quantityInStock);

        inventoryList.add(inventory);   // Add item to list

        // Update model for rendering
        model.addAttribute("inventoryList", inventoryList);
        model.addAttribute("responseMessage", "Inventory item added successfully!");
        return "pharmacy/inventory";
    }

    @PostMapping("/pharmacy/{username}/inventory/delete/{id}")
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
