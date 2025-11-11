package org.example.supplychainx.service.Approvisionnement;

import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderDTO;
import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderRequest;
import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderResponse;
import org.example.supplychainx.Mappers.Approvisionnement.SupplyOrderMapper;
import org.example.supplychainx.Model.Approvisionnement.*;
import org.example.supplychainx.Repository.Approvisionnement.RawMaterialRepository;
import org.example.supplychainx.Repository.Approvisionnement.SupplierRepository;
import org.example.supplychainx.Repository.Approvisionnement.SupplyOrderRepository;
import org.example.supplychainx.Service.Approvisionnement.SupplyOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplyOrderServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplyOrderRepository supplyOrderRepository;

    @Mock
    private SupplyOrderMapper supplyOrderMapper;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private SupplyOrderService supplyOrderService;

    private Supplier supplier;
    private SupplyOrder supplyOrder;
    private SupplyOrderDTO supplyOrderDTO;
    private RawMaterial rawMaterial;

    @BeforeEach
    void setUp() {
        // Setup Supplier
        supplier = new Supplier();
        supplier.setIdSupplier(1L);
        supplier.setName("Test Supplier");

        // Setup RawMaterial
        rawMaterial = new RawMaterial();
        rawMaterial.setIdMaterial(1L);
        rawMaterial.setName("Steel");

        // Setup SupplyOrder
        supplyOrder = new SupplyOrder();
        supplyOrder.setIdOrder(1L);
        supplyOrder.setSupplier(supplier);
        supplyOrder.setOrderDate(LocalDate.now());
        supplyOrder.setStatus(StatusSupply.EN_ATTENTE);

        // Setup SupplyOrderDTO
        supplyOrderDTO = new SupplyOrderDTO();
        supplyOrderDTO.setIdOrder(1L);
        supplyOrderDTO.setSupplierName("Test Supplier");
        supplyOrderDTO.setOrderDate(LocalDate.now());
        supplyOrderDTO.setStatus(StatusSupply.EN_ATTENTE);
    }

    @Test
    void testFindById_Success() {
        when(supplyOrderRepository.findById(1L)).thenReturn(Optional.of(supplyOrder));
        when(supplyOrderMapper.toDto(supplyOrder)).thenReturn(supplyOrderDTO);

        SupplyOrderDTO result = supplyOrderService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdOrder());
        assertEquals("Test Supplier", result.getSupplierName());
        verify(supplyOrderRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll_Success() {
        List<SupplyOrder> orders = Arrays.asList(supplyOrder);
        when(supplyOrderRepository.findAll()).thenReturn(orders);
        when(supplyOrderMapper.toDto(supplyOrder)).thenReturn(supplyOrderDTO);

        List<SupplyOrderDTO> result = supplyOrderService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(supplyOrderRepository, times(1)).findAll();
    }

    @Test
    void testSave_Success() {
        SupplyOrderRequest request = new SupplyOrderRequest();
        request.setDate(LocalDate.now());
        request.setSupplierId(1L);

        SupplyOrderRequest.RawMaterialQuantity rmq = new SupplyOrderRequest.RawMaterialQuantity();
        rmq.setRawMaterialId(1L);
        rmq.setQuantity(50);
        request.setRawMaterials(Arrays.asList(rmq));

        SupplyOrderResponse response = new SupplyOrderResponse();
        response.setId_order(1L);

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));
        when(supplyOrderRepository.save(any(SupplyOrder.class))).thenReturn(supplyOrder);
        when(supplyOrderMapper.toResponse(any(SupplyOrder.class))).thenReturn(response);

        SupplyOrderResponse result = supplyOrderService.save(request);

        assertNotNull(result);
        assertEquals(1L, result.getId_order());
        verify(supplyOrderRepository, times(1)).save(any(SupplyOrder.class));
    }

    @Test
    void testSave_FutureDate_ThrowsException() {
        SupplyOrderRequest request = new SupplyOrderRequest();
        request.setDate(LocalDate.now().plusDays(1));
        request.setSupplierId(1L);

        assertThrows(IllegalArgumentException.class, () -> supplyOrderService.save(request));
        verify(supplyOrderRepository, never()).save(any());
    }

    @Test
    void testDelete_Success() {
        when(supplyOrderRepository.findById(1L)).thenReturn(Optional.of(supplyOrder));

        supplyOrderService.delete(1L);

        verify(supplyOrderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_DeliveredOrder_ThrowsException() {
        supplyOrder.setStatus(StatusSupply.RECUE);
        when(supplyOrderRepository.findById(1L)).thenReturn(Optional.of(supplyOrder));

        assertThrows(ResponseStatusException.class, () -> supplyOrderService.delete(1L));
        verify(supplyOrderRepository, never()).deleteById(any());
    }

    @Test
    void testUpdate_Success() {
        when(supplyOrderMapper.toEntity(supplyOrderDTO)).thenReturn(supplyOrder);
        when(supplyOrderRepository.findById(1L)).thenReturn(Optional.of(supplyOrder));
        when(supplierRepository.findByName("Test Supplier")).thenReturn(supplier);
        when(supplyOrderRepository.save(any(SupplyOrder.class))).thenReturn(supplyOrder);
        when(supplyOrderMapper.toDto(any(SupplyOrder.class))).thenReturn(supplyOrderDTO);

        SupplyOrderDTO result = supplyOrderService.update(1L, supplyOrderDTO);

        assertNotNull(result);
        verify(supplyOrderRepository, times(1)).save(any(SupplyOrder.class));
    }
}