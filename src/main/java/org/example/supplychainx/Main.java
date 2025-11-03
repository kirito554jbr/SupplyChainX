package org.example.supplychainx;

import org.example.supplychainx.DTO.Approvisionnement.RawMaterialDTO;
import org.example.supplychainx.Service.Approvisionnement.RawMateialService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

//@SpringBootApplication
public class Main {

//    public static void main(String[] args) {
//        SpringApplication.run(Main.class, args);
//    }
//
//    @Bean
//    public CommandLineRunner testRawMaterialService(RawMateialService rawMaterialService) {
//        return args -> {
//            System.out.println("\n=== Testing RawMaterialService ===\n");
//
//            // Test 1: Find all raw materials
//            System.out.println("1. Finding all raw materials:");
//            List<RawMaterialDTO> allMaterials = rawMaterialService.findAllRawMaterials();
//            System.out.println("Found " + allMaterials.size() + " raw materials");
//            allMaterials.forEach(material ->
//                System.out.println("  - " + material.getName() + " (Stock: " + material.getStock() + ")")
//            );
//
//            // Test 2: Create a new raw material
//            System.out.println("\n2. Creating a new raw material:");
//            RawMaterialDTO newMaterial = new RawMaterialDTO();
//            newMaterial.setName("Test Material");
//            newMaterial.setStock(100);
//            newMaterial.setMinStock(20);
//            newMaterial.setUnit("kg");
//
//            try {
//                RawMaterialDTO savedMaterial = rawMaterialService.saveRawMaterial(newMaterial);
//                System.out.println("  Created: " + savedMaterial.getName() + " with ID: " + savedMaterial.getIdRawMaterial());
//
//                // Test 3: Find by ID
//                System.out.println("\n3. Finding material by ID:");
//                RawMaterialDTO foundMaterial = rawMaterialService.findByIdRawMaterial(savedMaterial.getIdRawMaterial());
//                if (foundMaterial != null) {
//                    System.out.println("  Found: " + foundMaterial.getName());
//                } else {
//                    System.out.println("  Material not found");
//                }
//
//                // Test 4: Update material
//                System.out.println("\n4. Updating material:");
//                foundMaterial.setStock(150);
//                RawMaterialDTO updatedMaterial = rawMaterialService.updateRawMaterial(
//                    savedMaterial.getIdRawMaterial(),
//                    foundMaterial
//                );
//                System.out.println("  Updated stock to: " + updatedMaterial.getStock());
//
//                // Test 5: Delete material
//                System.out.println("\n5. Deleting material:");
//                rawMaterialService.deleteRawMaterial(savedMaterial.getIdRawMaterial());
//                System.out.println("  Material deleted successfully");
//
//            } catch (Exception e) {
//                System.out.println("  Error during test: " + e.getMessage());
//            }
//
//            System.out.println("\n=== Test completed ===\n");
//        };
//    }
}
