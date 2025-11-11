package org.example.supplychainx.service.Approvisionnement;

import org.example.supplychainx.DTO.Approvisionnement.RawMaterialDTO;
import org.example.supplychainx.DTO.Approvisionnement.SupplierDTO;
import org.example.supplychainx.Mappers.Approvisionnement.RawMaterialMapper;
import org.example.supplychainx.Mappers.Approvisionnement.SupplierMapper;
import org.example.supplychainx.Model.Approvisionnement.RawMaterial;
import org.example.supplychainx.Model.Approvisionnement.Supplier;
import org.example.supplychainx.Repository.Approvisionnement.RawMaterialRepository;
import org.example.supplychainx.Service.Approvisionnement.RawMateialService;
import org.example.supplychainx.Service.Approvisionnement.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RawMaterialServiceTest {

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @Mock
    private RawMaterialMapper rawMaterialMapper;

    @Mock
    private SupplierService supplierService;

    @Mock
    private SupplierMapper supplierMapper;

    @InjectMocks
    private RawMateialService rawMaterialService;

    private RawMaterial rawMaterial;
    private RawMaterialDTO rawMaterialDTO;
    private Supplier supplier;
    private SupplierDTO supplierDTO;

    @BeforeEach
    void setUp() {
        // Setup supplier
        supplier = new Supplier();
        supplier.setIdSupplier(1L);
        supplier.setName("Test Supplier");
        supplier.setContact("test@supplier.com");
        supplier.setLeadTime(5);
        supplier.setRating(4.5);

        supplierDTO = new SupplierDTO();
        supplierDTO.setIdSupplier(1L);
        supplierDTO.setName("Test Supplier");
        supplierDTO.setContact("test@supplier.com");
        supplierDTO.setLeadTime(5);
        supplierDTO.setRating(4.5);

        // Setup raw material
        rawMaterial = new RawMaterial();
        rawMaterial.setIdMaterial(1L);
        rawMaterial.setName("Steel");
        rawMaterial.setStock(100);
        rawMaterial.setMinStock(50);
        rawMaterial.setUnit("kg");
        rawMaterial.setSuppliers(new ArrayList<>(Arrays.asList(supplier)));

        rawMaterialDTO = new RawMaterialDTO();
        rawMaterialDTO.setIdRawMaterial(1L);
        rawMaterialDTO.setName("Steel");
        rawMaterialDTO.setStock(100);
        rawMaterialDTO.setMinStock(50);
        rawMaterialDTO.setUnit("kg");
        rawMaterialDTO.setSuppliers(Arrays.asList("Test Supplier"));
    }

    @Test
    void testFindByIdRawMaterial_Success() {
        // Arrange
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));
        when(rawMaterialMapper.toDto(rawMaterial)).thenReturn(rawMaterialDTO);

        // Act
        RawMaterialDTO result = rawMaterialService.findByIdRawMaterial(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Steel", result.getName());
        assertEquals(100, result.getStock());
        verify(rawMaterialRepository, times(1)).findById(1L);
        verify(rawMaterialMapper, times(1)).toDto(rawMaterial);
    }

    @Test
    void testFindByIdRawMaterial_NotFound() {
        // Arrange
        when(rawMaterialRepository.findById(999L)).thenReturn(Optional.empty());
        when(rawMaterialMapper.toDto(null)).thenReturn(null);

        // Act
        RawMaterialDTO result = rawMaterialService.findByIdRawMaterial(999L);

        // Assert
        assertNull(result);
        verify(rawMaterialRepository, times(1)).findById(999L);
    }

    @Test
    void testFindAllRawMaterials_Success() {
        // Arrange
        List<RawMaterial> materials = Arrays.asList(rawMaterial);
        when(rawMaterialRepository.findAll()).thenReturn(materials);
        when(rawMaterialMapper.toDto(rawMaterial)).thenReturn(rawMaterialDTO);

        // Act
        List<RawMaterialDTO> result = rawMaterialService.findAllRawMaterials();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Steel", result.get(0).getName());
        verify(rawMaterialRepository, times(1)).findAll();
    }

    @Test
    void testFindAllRawMaterials_EmptyList() {
        // Arrange
        when(rawMaterialRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<RawMaterialDTO> result = rawMaterialService.findAllRawMaterials();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(rawMaterialRepository, times(1)).findAll();
    }

    @Test
    void testSaveRawMaterial_Success() {
        // Arrange
        when(rawMaterialMapper.toEntity(rawMaterialDTO)).thenReturn(rawMaterial);
        when(supplierService.findByName("Test Supplier")).thenReturn(supplier);
        when(rawMaterialRepository.save(any(RawMaterial.class))).thenReturn(rawMaterial);
        when(rawMaterialMapper.toDto(rawMaterial)).thenReturn(rawMaterialDTO);

        // Act
        RawMaterialDTO result = rawMaterialService.saveRawMaterial(rawMaterialDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Steel", result.getName());
        verify(supplierService, times(1)).findByName("Test Supplier");
        verify(rawMaterialRepository, times(1)).save(any(RawMaterial.class));
    }

    @Test
    void testSaveRawMaterial_SupplierNotFound() {
        // Arrange
        when(rawMaterialMapper.toEntity(rawMaterialDTO)).thenReturn(rawMaterial);
        when(supplierService.findByName("Test Supplier")).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            rawMaterialService.saveRawMaterial(rawMaterialDTO);
        });

        assertEquals("Supplier not found: Test Supplier", exception.getMessage());
        verify(supplierService, times(1)).findByName("Test Supplier");
        verify(rawMaterialRepository, never()).save(any(RawMaterial.class));
    }

    @Test
    void testDeleteRawMaterial_Success() {
        // Arrange
        doNothing().when(rawMaterialRepository).deleteById(1L);

        // Act
        rawMaterialService.deleteRawMaterial(1L);

        // Assert
        verify(rawMaterialRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateRawMaterial_Success() {
        // Arrange
        RawMaterialDTO updateDTO = new RawMaterialDTO();
        updateDTO.setName("Updated Steel");
        updateDTO.setStock(200);
        updateDTO.setMinStock(75);
        updateDTO.setUnit("ton");
        updateDTO.setSuppliers(Arrays.asList("Test Supplier"));

        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));
        when(supplierService.findByName("Test Supplier")).thenReturn(supplier);
        when(rawMaterialRepository.save(any(RawMaterial.class))).thenReturn(rawMaterial);
        when(rawMaterialMapper.toDto(any(RawMaterial.class))).thenReturn(updateDTO);

        // Act
        RawMaterialDTO result = rawMaterialService.updateRawMaterial(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Steel", result.getName());
        assertEquals(200, result.getStock());
        verify(rawMaterialRepository, times(1)).findById(1L);
        verify(rawMaterialRepository, times(1)).save(any(RawMaterial.class));
    }

    @Test
    void testDeleteOneSupplierFromRawMaterial_Success() {
        // Arrange
        Supplier supplier2 = new Supplier();
        supplier2.setIdSupplier(2L);
        supplier2.setName("Supplier 2");

        rawMaterial.setSuppliers(new ArrayList<>(Arrays.asList(supplier, supplier2)));

        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));
        when(rawMaterialRepository.save(any(RawMaterial.class))).thenReturn(rawMaterial);

        // Act
        rawMaterialService.deleteOneSupplierFromRawMaterial(1L, 1L);

        // Assert
        verify(rawMaterialRepository, times(1)).findById(1L);
        verify(rawMaterialRepository, times(1)).save(any(RawMaterial.class));
    }

    @Test
    void testDeleteOneSupplierFromRawMaterial_MaterialNotFound() {
        // Arrange
        when(rawMaterialRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        rawMaterialService.deleteOneSupplierFromRawMaterial(999L, 1L);

        // Assert
        verify(rawMaterialRepository, times(1)).findById(999L);
        verify(rawMaterialRepository, never()).save(any(RawMaterial.class));
    }

    @Test
    void testAddSupplierToRawMaterial_Success() {
        // Arrange
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));
        when(supplierService.findById(2L)).thenReturn(supplierDTO);
        when(supplierMapper.toEntity(supplierDTO)).thenReturn(supplier);
        when(rawMaterialRepository.save(any(RawMaterial.class))).thenReturn(rawMaterial);

        // Act
        rawMaterialService.addSupplierToRawMaterial(1L, 2L);

        // Assert
        verify(rawMaterialRepository, times(1)).findById(1L);
        verify(supplierService, times(1)).findById(2L);
        verify(rawMaterialRepository, times(1)).save(any(RawMaterial.class));
    }

    @Test
    void testAddSupplierToRawMaterial_WithNullSupplierList() {
        // Arrange
        rawMaterial.setSuppliers(null);
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));
        when(supplierService.findById(2L)).thenReturn(supplierDTO);
        when(supplierMapper.toEntity(supplierDTO)).thenReturn(supplier);
        when(rawMaterialRepository.save(any(RawMaterial.class))).thenReturn(rawMaterial);

        // Act
        rawMaterialService.addSupplierToRawMaterial(1L, 2L);

        // Assert
        verify(rawMaterialRepository, times(1)).findById(1L);
        verify(supplierService, times(1)).findById(2L);
        verify(rawMaterialRepository, times(1)).save(any(RawMaterial.class));
    }

    @Test
    void testAddSupplierToRawMaterial_MaterialNotFound() {
        // Arrange
        when(rawMaterialRepository.findById(999L)).thenReturn(Optional.empty());
        when(supplierService.findById(2L)).thenReturn(supplierDTO);

        // Act
        rawMaterialService.addSupplierToRawMaterial(999L, 2L);

        // Assert
        verify(rawMaterialRepository, times(1)).findById(999L);
        verify(rawMaterialRepository, never()).save(any(RawMaterial.class));
    }

    @Test
    void testFindByName_Success() {
        // Arrange
        when(rawMaterialRepository.findByName("Steel")).thenReturn(rawMaterial);

        // Act
        RawMaterial result = rawMaterialService.findByName("Steel");

        // Assert
        assertNotNull(result);
        assertEquals("Steel", result.getName());
        verify(rawMaterialRepository, times(1)).findByName("Steel");
    }

    @Test
    void testFindEntityById_Success() {
        // Arrange
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));

        // Act
        RawMaterial result = rawMaterialService.findEntityById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdMaterial());
        assertEquals("Steel", result.getName());
        verify(rawMaterialRepository, times(1)).findById(1L);
    }

    @Test
    void testFindEntityById_NotFound() {
        // Arrange
        when(rawMaterialRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            rawMaterialService.findEntityById(999L);
        });

        assertEquals("RawMaterial not found with id: 999", exception.getMessage());
        verify(rawMaterialRepository, times(1)).findById(999L);
    }

    @Test
    void testGetRawMaterialsWithLowStock_Success() {
        // Arrange
        RawMaterial lowStockMaterial = new RawMaterial();
        lowStockMaterial.setIdMaterial(2L);
        lowStockMaterial.setName("Aluminum");
        lowStockMaterial.setStock(30);
        lowStockMaterial.setMinStock(50);

        RawMaterialDTO lowStockDTO = new RawMaterialDTO();
        lowStockDTO.setIdRawMaterial(2L);
        lowStockDTO.setName("Aluminum");
        lowStockDTO.setStock(30);
        lowStockDTO.setMinStock(50);

        List<RawMaterial> allMaterials = Arrays.asList(rawMaterial, lowStockMaterial);

        when(rawMaterialRepository.findAll()).thenReturn(allMaterials);
        when(rawMaterialMapper.toDto(lowStockMaterial)).thenReturn(lowStockDTO);

        // Act
        List<RawMaterialDTO> result = rawMaterialService.getRawMaterialsWithLowStock();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Aluminum", result.get(0).getName());
        assertEquals(30, result.get(0).getStock());
        assertTrue(result.get(0).getStock() < result.get(0).getMinStock());
        verify(rawMaterialRepository, times(1)).findAll();
    }

    @Test
    void testGetRawMaterialsWithLowStock_NoLowStockMaterials() {
        // Arrange
        List<RawMaterial> allMaterials = Arrays.asList(rawMaterial);
        when(rawMaterialRepository.findAll()).thenReturn(allMaterials);

        // Act
        List<RawMaterialDTO> result = rawMaterialService.getRawMaterialsWithLowStock();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(rawMaterialRepository, times(1)).findAll();
    }
}
