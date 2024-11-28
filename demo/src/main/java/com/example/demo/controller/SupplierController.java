package com.example.demo.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Inventory;
import com.example.demo.model.Medicine;
import com.example.demo.model.Patient;
import com.example.demo.model.PatientComments;
import com.example.demo.model.Prescription;
import com.example.demo.model.Report;
import com.example.demo.model.Supplier;
import com.example.demo.service.ModelMapperUtil;
import com.example.demo.service.SupplierService;

@Controller
public class SupplierController {


    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ModelMapperUtil modelMapperUtil;

    // Counter for auto-incrementing InventoryID
    private long inventoryIDCounter = 1;

    private final List<Inventory> inventoryList = new ArrayList<>();

    @RequestMapping(value="/supplier",method = RequestMethod.GET)
    String PharmacyDashboard(Model model){
        if (Auth(model)) return "redirect:/login";
        return "SupplierProfile";
    }
    @GetMapping(value="/supplier/{username}/profile")
    public String patientProfile(Model model,@PathVariable String username){
        if (Auth(model)) return "redirect:/login";
        return "SupplierProfile";
    }
    @GetMapping(value="/supplier/{username}/reports")
    public String reportDashboard(Model model,@PathVariable String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getName().equals(username)){
            return "redirect:/login?loginagain";
        }
        Supplier supplier = supplierService.getSupplierByUsername(username);
        //List<Report>reports = supplier.getReports();
        String reportpath = "/reports/";
        initialize(username, model, supplier);
        //model.addAttribute("orders",orders);
        model.addAttribute("reportpath",reportpath);
        return "Supplieriinventory.html";
    }
    @PostMapping(value="/supplier/{username}/reports")
    public String supplierAddReports(Model model,@PathVariable String username, @RequestParam String title, @RequestParam MultipartFile file){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getName().equals(username)){
            return "redirect:/login?loginagain";
        }
        Supplier supplier = supplierService.getSupplierByUsername(username);
        //String added = reportService.addReport(file,title,supplier);
        model.addAttribute("success","success");  
        //List<Report>order = supplier.orders();
        String reportpath = "/reports/";
        initialize(username, model, supplier);
        //model.addAttribute("reports",orders);
        model.addAttribute("reportpath",reportpath);
        return "PatientReports.html";
    }
    @RequestMapping(value="/supplier/{username}/prescriptions")
    public String prescriptionDashboard(Model model,@PathVariable String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getName().equals(username)){
            return "redirect:/login?loginagain";
        }
        Supplier supplier = supplierService.getSupplierByUsername(username);
        //List<Prescription>prescriptions = supplier.getPrescriptions();
        //List<PatientComments>comments = supplier.getComments();
        initialize(username, model, supplier);
        //model.addAttribute("comments",comments);
        //model.addAttribute("prescriptions",prescriptions);//change it to prescriptions
        return "SupplierOrders.html";
    }
     @PostMapping("/supplier/settings")
    @ResponseBody
    public ResponseEntity<?> PatientChangeSettings(@RequestParam Map<String, String> formData,@RequestParam(value = "profilepicture", required = false) MultipartFile file){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
        }
        Map<String,String> response = new HashMap<String,String>();
        String username = authentication.getName();
        Supplier supplier = supplierService.getSupplierByUsername(username);
        if (file != null && !file.isEmpty()){
            System.out.println("Yes iam changing profile picture");
            System.out.println(supplierService.Saveprofilepicture(file,supplier));
        }
        if (supplierService.updatePatient(formData,supplier)){
            response.put("Fname",supplier.getFname());
            response.put("Mname",supplier.getMname());
            response.put("Lname",supplier.getLname());
            return ResponseEntity.ok(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
        }
    }
        @GetMapping(value="/supplier/settings")
    public String patientSettings(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            return "redirect:/login?loginagain";
        }
        String username = authentication.getName();
        Supplier supplier = supplierService.getSupplierByUsername(username);
        initialize(username, model, supplier);
        return "SupplierSettings.html";
    }

    @PostMapping("/add")
    public String addInventory(
            @RequestParam int inventoryID,
            @RequestParam Medicine medID,
            @RequestParam long price,
            @RequestParam LocalDate expiryDate,
            @RequestParam int quantityInStock,
            Model model,
            @PathVariable String username) {

        if (Authentication(username)) return "redirect:/login?loginagain";

        // Create a new Inventory item with auto-incremented InventoryID
        Inventory inventory = new Inventory();
        inventory.setInventoryID(inventoryIDCounter++);
        inventory.setMedicine(medID);
        inventory.setPrice(price);
        inventory.setExpiryDate(expiryDate);
        inventory.setQuantityInStock(quantityInStock);

        inventoryList.add(inventory);   // Add item to list

        // Update model for rendering
        model.addAttribute("inventoryList", inventoryList);
        model.addAttribute("responseMessage", "Inventory item added successfully!");
        return "Pharmacy/pharmacy-inventory";
    }

    // @PutMapping("/update/{id}")
    // public ResponseEntity<String> updateInventory(@PathVariable Long id, @RequestBody Map<String, Object> request) {
    //     try {
    //         // Fetch Inventory by ID
    //         Inventory existingInventory = suppInventoryService.findById(id);
    //         if (existingInventory == null) {
    //             return ResponseEntity.badRequest().body("Inventory not found");
    //         }

    //         // Parse request data and update inventory details
    //         Long medID = Long.valueOf(request.get("medID").toString());
    //         Long price = Long.valueOf(request.get("price").toString());
    //         String expiryDate = request.get("expiryDate").toString();
    //         Integer quantityInStock = Integer.valueOf(request.get("quantityInStock").toString());

    //         Medicine medicine = medicineService.getMedicineByMedID(medID);
    //         if (medicine == null) {
    //             return ResponseEntity.badRequest().body("Invalid Medicine ID");
    //         }

    //         existingInventory.setMedicine(medicine);
    //         existingInventory.setPrice(price);
    //         existingInventory.setExpiryDate(LocalDate.parse(expiryDate));
    //         existingInventory.setQuantityInStock(quantityInStock);

    //         suppInventoryService.addSuppInventory(existingInventory); // Save updated inventory

    //         return ResponseEntity.ok("Inventory updated successfully.");
    //     } catch (Exception e) {
    //         return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    //     }
    // }

    // @DeleteMapping("/delete/{id}")
    // public ResponseEntity<String> deleteSuppInventory(@PathVariable Long id) {
    //     suppInventoryService.deleteSuppInventory(id);
    //     return ResponseEntity.ok("Inventory with ID " + id + " has been deleted.");
    // }


    // @GetMapping("/all")
    // public ResponseEntity<List<Map<String, Object>>> getAllInventory() {
    //     List<Inventory> inventoryList = suppInventoryService.getAllInventory();
    //     List<Map<String, Object>> response = inventoryList.stream().map(item -> {
    //         Map<String, Object> inventoryMap = new HashMap<>();
    //         inventoryMap.put("inventoryID", item.getInventoryID());
    //         inventoryMap.put("medID", item.getMedicine() != null ? item.getMedicine().getMedID() : null);
    //         inventoryMap.put("medicineName", item.getMedicine() != null ? item.getMedicine().getMedName() : "Unknown");
    //         inventoryMap.put("strength", item.getMedicine() != null ? item.getMedicine().getStrength() : "Unknown");
    //         inventoryMap.put("price", item.getPrice());
    //         inventoryMap.put("expiryDate", item.getExpiryDate() != null ? item.getExpiryDate().toString() : null);
    //         inventoryMap.put("quantityInStock", item.getQuantityInStock());
    //         return inventoryMap;
    //     }).toList();

    //     return ResponseEntity.ok(response);
    // }

    static boolean Authentication(@PathVariable String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getName().equals(username)){
            if (authentication == null){
                System.out.println("Authentication is null");
            }
            else{
                System.out.println(username);
                System.out.println(authentication.getName());
            }
            return true;
        }
        return false;
    }

    private boolean Auth(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            System.out.println("Authenticated User: " + authentication.getName());
            System.out.println("Authenticated User roles: " + authentication.getAuthorities());
        }
        if (authentication != null){
            String username = authentication.getName();
            Supplier supplier = supplierService.getSupplierByUsername(username);
            String profilepicture = "/images/" + supplier.getProfilepicture();

            String[] fields = supplierService.getFields();
            Map<String, Object> supplierDict = modelMapperUtil.mapFieldsToGetters(supplier, fields);
            supplierDict.put("profilepic",profilepicture);
            model.addAttribute("supplier",supplierDict);
        }
        else{
            return true;
        }
        return false;
    }
    public void initialize(String username,Model model,Supplier supplier){
        String profilepicpath = "/images/";
        String profilepicture = profilepicpath + supplier.getProfilepicture();
        String[] fields = supplierService.getFields();
        Map<String, Object> patientDict = modelMapperUtil.mapFieldsToGetters(supplier, fields);
        patientDict.put("profilepic",profilepicture);
        model.addAttribute("supplier",patientDict);    
    }

}
