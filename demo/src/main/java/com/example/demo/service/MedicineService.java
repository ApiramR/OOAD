package com.example.demo.service;


import com.example.demo.model.Medicine;
import com.example.demo.repo.MedicineRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepo medicineRepo;

    public Medicine addMedicine(Medicine medicine) {
        return medicineRepo.save(medicine);
    }

    public Medicine findById(int id) {
        Optional<Medicine> medicine = medicineRepo.findById((long) id);
        if (medicine.isEmpty()) {
            throw new RuntimeException("Medicine not found");
        }
        return medicine.get();
    }

    public void deleteMedicine(Long id) {
        medicineRepo.deleteById(id);
    }

    public List<Medicine> getAllMedicines() {
        return medicineRepo.findAll();
    }

    public Medicine findById(Long id) {
        return medicineRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with ID: " + id));
    }


    @Autowired
    public MedicineService(MedicineRepo medicineRepo) {
        this.medicineRepo = medicineRepo;
    }

    public List<Long> getMedIDsByMedName(String medName) {
        return medicineRepo.findMedIDByMedName(medName);
    }
}


/*package com.example.demo.service;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repo.InventoryRepo;
import com.example.demo.model.Inventory;

import com.example.demo.repo.MedicineRepo;
import com.example.demo.model.Medicine;



@Service
public class MedicineService {

    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private MedicineRepo medicineRepo;


    public Medicine addMedicine(Medicine medicine) {
        return medicineRepo.save(medicine);
    }

    public Medicine findById(int id) {
        Optional<Medicine> user = MedicineRepo.findById(id);

        if(user.isEmpty()){
            throw new RuntimeException("User not found");
        }
        return user.get();
    }




    @Autowired
    public MedicineService(MedicineRepo medicineRepo) {
        this.medicineRepo = medicineRepo;
    }

    public List<Long> getMedIDsByMedName(String medName) {
        return medicineRepo.findMedIDByMedName(medName);
    }




}*/


