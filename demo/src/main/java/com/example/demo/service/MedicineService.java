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

    public Medicine getMedicineByMedID(Long medID) {
        return medicineRepo.findByMedID(medID);
    }

    // Save or update Medicine
    public Medicine saveMedicine(Medicine medicine) {
        return medicineRepo.save(medicine);
    }

    public List<Long> getMedIDsByMedName(String medName) {
        return medicineRepo.findMedIDByMedName(medName);
    }



    public List<Medicine> searchMedicines(String query) {
        return medicineRepo.findByMedNameContainingIgnoreCaseOrMedTypeContainingIgnoreCaseOrManufacturerContainingIgnoreCase(query, query, query);
    }

    public long countAllMedicine() {
        return medicineRepo.count();
    }

}
