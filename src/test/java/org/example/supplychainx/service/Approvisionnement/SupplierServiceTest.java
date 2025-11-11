package org.example.supplychainx.service.Approvisionnement;

import org.example.supplychainx.DTO.Approvisionnement.SupplierDTO;
import org.example.supplychainx.Mappers.Approvisionnement.SupplierMapper;
import org.example.supplychainx.Model.Approvisionnement.Supplier;
import org.example.supplychainx.Repository.Approvisionnement.SupplierRepository;
import org.example.supplychainx.Service.Approvisionnement.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {
    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierMapper supplierMapper;

    @InjectMocks
    private SupplierService supplierService;

    private Supplier supplier;
    private SupplierDTO supplierDTO;

    @BeforeEach
    void setup() {
        supplier = new Supplier();
        supplier.setIdSupplier(1L);
        supplier.setName("Test Supplier");
        supplier.setContact("supplier@gmail.com");
        supplier.setRating(8.0);
        supplier.setLeadTime(5);

        supplierDTO = new SupplierDTO();
        supplierDTO.setIdSupplier(1L);
        supplierDTO.setName("Test Supplier");
        supplierDTO.setContact("supplier@gmail.com");
        supplierDTO.setRating(8.0);
        supplierDTO.setLeadTime(5);
    }

    // --- Create ---
    @Test
    @DisplayName("Test createSupplier")
    void testCreateSupplier() {
        when(supplierMapper.toEntity(supplierDTO)).thenReturn(supplier);
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);
        when(supplierMapper.toDto(supplier)).thenReturn(supplierDTO);

        SupplierDTO result = supplierService.save(supplierDTO);

        assertNotNull(result);
        assertEquals("Test Supplier", result.getName());
        verify(supplierMapper, times(1)).toEntity(supplierDTO);
        verify(supplierRepository, times(1)).save(any(Supplier.class));
        verify(supplierMapper, times(1)).toDto(supplier);
    }

    @Test
    @DisplayName("Test CreateSupplier - null result")
    void testCreateSupplier_Null() {
        when(supplierMapper.toEntity(supplierDTO)).thenReturn(supplier);
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);
        when(supplierMapper.toDto(supplier)).thenReturn(null);

        SupplierDTO result = supplierService.save(supplierDTO);

        assertNull(result);
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }


    // --- Update ---

    @Test
    @DisplayName("Test updateSupplier")
    void testUpdateSupplier() {
        SupplierDTO updatedDTO = new SupplierDTO();
        updatedDTO.setIdSupplier(1L);
        updatedDTO.setName("Updated Supplier");
        updatedDTO.setContact("updated@gmail.com");
        updatedDTO.setRating(9.0);
        updatedDTO.setLeadTime(3);

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);
        when(supplierMapper.toDto(any(Supplier.class))).thenReturn(updatedDTO);

        SupplierDTO result = supplierService.update(1L, updatedDTO);

        assertNotNull(result);
        assertEquals("Updated Supplier", result.getName());
        verify(supplierRepository, times(1)).findById(1L);
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }

    @Test
    @DisplayName("TestUpdateSupplier - null result")
    void testUpdateSupplier_Null() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);
        when(supplierMapper.toDto(any(Supplier.class))).thenReturn(null);

        SupplierDTO result = supplierService.update(1L, supplierDTO);

        assertNull(result);
        verify(supplierRepository, times(1)).findById(1L);
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }

    @Test
    @DisplayName("TestUpdateSupplier - id not found")
    void testUpdateSupplier_IdNotFound() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            supplierService.update(1L, supplierDTO);
        });

        verify(supplierRepository, times(1)).findById(1L);
        verify(supplierRepository, never()).save(any(Supplier.class));
    }

    // --- Read ---

    @Test
    @DisplayName("Test getSupplierById")
    void testGetSupplierById() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(supplierMapper.toDto(supplier)).thenReturn(supplierDTO);

        SupplierDTO result = supplierService.findById(1L);

        assertNotNull(result);
        assertEquals("Test Supplier", result.getName());
        verify(supplierRepository, times(1)).findById(1L);
        verify(supplierMapper, times(1)).toDto(supplier);
    }


    @Test
    @DisplayName("null testGetSupplierById")
    void testGetSupplierById_Null() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            supplierService.findById(1L);
        });

        verify(supplierRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test getAllSuppliers")
    void testGetAllSuppliers() {
        when(supplierRepository.findAll()).thenReturn(List.of(supplier));
        when(supplierMapper.toDto(supplier)).thenReturn(supplierDTO);

        List<SupplierDTO> result = supplierService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Supplier", result.get(0).getName());
        verify(supplierRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Empty list testGetAllSuppliers")
    void testGetAllSuppliers_EmptyList() {
        when(supplierRepository.findAll()).thenReturn(List.of());

        List<SupplierDTO> result = supplierService.findAll();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(supplierRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findByName testGetSupplierByName")
    void testGetSupplierByName() {
        when(supplierRepository.findByName("Test Supplier")).thenReturn(supplier);

        Supplier result = supplierService.findByName("Test Supplier");

        assertNotNull(result);
        assertEquals("Test Supplier", result.getName());
        verify(supplierRepository, times(1)).findByName("Test Supplier");
    }


    // --- Delete ---

    @Test
    @DisplayName("Test deleteSupplier")
    void testDeleteSupplier() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        doNothing().when(supplierRepository).deleteById(1L);

        supplierService.deleteById(1L);

        verify(supplierRepository, times(1)).findById(1L);
        verify(supplierRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Test deleteSupplier - not found")
    void testDeleteSupplier_NotFound() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> supplierService.deleteById(1L));

        verify(supplierRepository, times(1)).findById(1L);
        verify(supplierRepository, never()).deleteById(any());
    }

}
