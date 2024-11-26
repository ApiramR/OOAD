// package com.example.demo.controller;


// import java.util.Map;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestParam;

// import com.example.demo.model.Prescription;
// import com.example.demo.service.PrescriptionService;

// import lombok.AllArgsConstructor;



// @Controller
// @AllArgsConstructor
// public class PrescriptionController {
    

//     @GetMapping(value="/doctor/prescription")
//     public String getPatientPrecriptionPage() {
//         return "prescription.html";
//     }

//     @GetMapping(value="/doctor/patient")
//     public String getPatientPage() {
//         return "patient.html";
//     }

    

//     private final PrescriptionService prescriptionService ;

//     @PostMapping ("/doctor/prescription")
//     public ResponseEntity<Prescription> createPrescription(@RequestParam  Map<String,String> prescrip){
//         Prescription prescriptionModel=new Prescription();
        
//         //Prescription savedPrescription= prescriptionService.addPrescription(prescriptionModel);

//         System.out.println("ihergbber");

//         return new ResponseEntity<>(savedPrescription, HttpStatus.CREATED);
//     }

//     @GetMapping("/doctor/prescription/{id}")
//     public ResponseEntity<Prescription> getPrescriptionById(@PathVariable("id") Long presid){
//         Prescription prescriptionModel =prescriptionService.getPrescriptionByID(presid);
//             return ResponseEntity.ok(prescriptionModel);

//     }

//     // @GetMapping("/doctor/prescription/{id}")
//     // public ResponseEntity<Prescription> getPrescription(@PathVariable("id")Long presid ) {
//     //     try {
//     //         Prescription prescription = prescriptionService.getPrescriptionByID(presid);
//     //         return ResponseEntity.ok(prescription);
//     //     } catch (RuntimeException e) {
//     //         return ResponseEntity.notFound().build();
//     //     }
//     // }


//     @PutMapping("/doctor/prescription/{id}")
//     public ResponseEntity<Prescription> updatePrescription(@PathVariable("id") Long presid,@RequestBody Prescription updatedPrescription){
//         System.out.println("update in progress");
//         Prescription prescriptionModel= prescriptionService.updatePrescription(updatedPrescription);
//         return ResponseEntity.ok(prescriptionModel);
//     }

//     @DeleteMapping("/doctor/prescription/{id}")
//     public ResponseEntity<String> deletePrescription(@PathVariable("id") Long presid){
//         prescriptionService.deletePrescription(presid);
//         return ResponseEntity.ok("deletion successful");
//     }
// }


package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Prescription;
import com.example.demo.service.PrescriptionService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping(value="/doctor")
public class PrescriptionController {

    @Autowired
    PrescriptionService prescriptionService;

    // Load Prescription Page
    @GetMapping(value="/prescription")
    public String getPatientPrescriptionPage() {
        return "prescription.html";
    }

    // Load Patient Page
    @GetMapping(value="/patient")
    public String getPatientPage() {
        return "patient.html";
    }

    // Create a new prescription
    @PostMapping(value="/prescription")
    public ResponseEntity<Prescription> createPrescription(@RequestParam Map<String, String> prescrip) {
        try {
            Prescription prescription = new Prescription();
            prescription.setMeds(prescrip.get("meds"));
            prescription.setDosage(prescrip.get("dosage"));
            prescription.setDateIssued(prescrip.get("dateIssued"));
            prescription.setDescription(prescrip.get("description"));
            // Assuming a valid Patient object needs to be set
            // prescription.setPatient(patient); 

            Prescription savedPrescription = prescriptionService.addPrescription(prescription);
            return new ResponseEntity<>(savedPrescription, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Retrieve a prescription by ID
    @GetMapping("/prescription/{id}")
    
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable("id") Long presid){
    try {
        // Fetch prescription details from the service
        Prescription prescription = prescriptionService.getPrescriptionByID(presid);
        
        // Ensure prescription object is properly returned
        if (prescription != null) {
            return ResponseEntity.ok(prescription);
        } else {
            // If prescription is not found, return a 404 error
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    } catch (Exception e) {
        // Catch any exceptions and return an internal server error
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    }



//     @PutMapping(value = "/prescription/{id}")
//     public ResponseEntity<String> updatePrescription(
//         @PathVariable("id") Long presId,
//         @RequestBody Prescription updatedPrescription) {

//     System.out.println("Updating prescription with ID: " + presId);

//     try {
//         // Call the service to handle the update logic
//         Prescription result = prescriptionService.updatePrescription(presId, updatedPrescription);

//         if (result != null) {
//             return ResponseEntity.ok("Prescription updated successfully!");
//         } else {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prescription not found for ID: " + presId);
//         }
//     } catch (Exception e) {
//         // Log the error for debugging purposes
//         System.err.println("Error updating prescription: " + e.getMessage());
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                 .body("Error updating prescription: " + e.getMessage());
//     }
// }





    // Delete a prescription
    @DeleteMapping("/prescription/{id}")
    public ResponseEntity<String> deletePrescription(@PathVariable("id") Long presid) {
        try {
            prescriptionService.deletePrescription(presid);
            return ResponseEntity.ok("Deletion successful");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prescription not found");
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countPrescription() {
        try {
            long itemCount = prescriptionService.countAllPrescriptions();
            return ResponseEntity.ok(itemCount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(0L);
        }
    }
}


