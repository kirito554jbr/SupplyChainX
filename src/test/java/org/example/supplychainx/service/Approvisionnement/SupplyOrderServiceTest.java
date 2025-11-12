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

    // Lambda Tests for Save Method
    @Test
    void testSave_SupplierLambda_ThrowsException() {
        SupplyOrderRequest request = new SupplyOrderRequest();
        request.setDate(LocalDate.now());
        request.setSupplierId(999L);

        when(supplierRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> supplyOrderService.save(request));

        assertEquals("Supplier not found.", exception.getMessage());
        verify(supplierRepository, times(1)).findById(999L);
        verify(supplyOrderRepository, never()).save(any());
    }

    @Test
    void testSave_RawMaterialLambda_ThrowsException() {
        SupplyOrderRequest request = new SupplyOrderRequest();
        request.setDate(LocalDate.now());
        request.setSupplierId(1L);

        SupplyOrderRequest.RawMaterialQuantity rmq = new SupplyOrderRequest.RawMaterialQuantity();
        rmq.setRawMaterialId(999L);
        rmq.setQuantity(50);
        request.setRawMaterials(Arrays.asList(rmq));

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(rawMaterialRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> supplyOrderService.save(request));

        assertEquals("Raw material with ID 999 not found.", exception.getMessage());
        verify(rawMaterialRepository, times(1)).findById(999L);
        verify(supplyOrderRepository, never()).save(any());
    }

    @Test
    void testSave_MultipleRawMaterials_LambdaIteration() {
        SupplyOrderRequest request = new SupplyOrderRequest();
        request.setDate(LocalDate.now());
        request.setSupplierId(1L);

        RawMaterial rawMaterial2 = new RawMaterial();
        rawMaterial2.setIdMaterial(2L);
        rawMaterial2.setName("Aluminum");

        SupplyOrderRequest.RawMaterialQuantity rmq1 = new SupplyOrderRequest.RawMaterialQuantity();
        rmq1.setRawMaterialId(1L);
        rmq1.setQuantity(50);

        SupplyOrderRequest.RawMaterialQuantity rmq2 = new SupplyOrderRequest.RawMaterialQuantity();
        rmq2.setRawMaterialId(2L);
        rmq2.setQuantity(75);

        request.setRawMaterials(Arrays.asList(rmq1, rmq2));

        SupplyOrderResponse response = new SupplyOrderResponse();
        response.setId_order(1L);

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));
        when(rawMaterialRepository.findById(2L)).thenReturn(Optional.of(rawMaterial2));
        when(supplyOrderRepository.save(any(SupplyOrder.class))).thenReturn(supplyOrder);
        when(supplyOrderMapper.toResponse(any(SupplyOrder.class))).thenReturn(response);

        SupplyOrderResponse result = supplyOrderService.save(request);

        assertNotNull(result);
        verify(rawMaterialRepository, times(1)).findById(1L);
        verify(rawMaterialRepository, times(1)).findById(2L);
        verify(supplyOrderRepository, times(1)).save(any(SupplyOrder.class));
    }

    // Lambda Tests for Update Method
    @Test
    void testUpdate_OrderNotFoundLambda_ThrowsException() {
        when(supplyOrderMapper.toEntity(supplyOrderDTO)).thenReturn(supplyOrder);
        when(supplyOrderRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> supplyOrderService.update(999L, supplyOrderDTO));

        assertEquals("SupplierOrder not found with id: 999", exception.getMessage());
        verify(supplyOrderRepository, times(1)).findById(999L);
        verify(supplyOrderRepository, never()).save(any());
    }

    @Test
    void testUpdate_WithSupplierName_Success() {
        Supplier newSupplier = new Supplier();
        newSupplier.setIdSupplier(2L);
        newSupplier.setName("Updated Supplier");

        supplyOrderDTO.setSupplierName("Updated Supplier");

        when(supplyOrderMapper.toEntity(supplyOrderDTO)).thenReturn(supplyOrder);
        when(supplyOrderRepository.findById(1L)).thenReturn(Optional.of(supplyOrder));
        when(supplierRepository.findByName("Updated Supplier")).thenReturn(newSupplier);
        when(supplyOrderRepository.save(any(SupplyOrder.class))).thenReturn(supplyOrder);
        when(supplyOrderMapper.toDto(any(SupplyOrder.class))).thenReturn(supplyOrderDTO);

        SupplyOrderDTO result = supplyOrderService.update(1L, supplyOrderDTO);

        assertNotNull(result);
        assertEquals("Updated Supplier", result.getSupplierName());
        verify(supplierRepository, times(1)).findByName("Updated Supplier");
        verify(supplyOrderRepository, times(1)).save(any(SupplyOrder.class));
    }

    // Lambda Tests for Delete Method
    @Test
    void testDelete_OrderNotFoundLambda_ThrowsException() {
        when(supplyOrderRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> supplyOrderService.delete(999L));

        assertEquals("Order not found", exception.getMessage());
        verify(supplyOrderRepository, times(1)).findById(999L);
        verify(supplyOrderRepository, never()).deleteById(any());
    }

    @Test
    void testDelete_PendingOrder_Success() {
        supplyOrder.setStatus(StatusSupply.EN_ATTENTE);
        when(supplyOrderRepository.findById(1L)).thenReturn(Optional.of(supplyOrder));

        supplyOrderService.delete(1L);

        verify(supplyOrderRepository, times(1)).deleteById(1L);
    }



    // Lambda Tests for FindAll Method
    @Test
    void testFindAll_WithMultipleOrders_LambdaMapping() {
        Supplier supplier2 = new Supplier();
        supplier2.setIdSupplier(2L);
        supplier2.setName("Second Supplier");

        SupplyOrder supplyOrder2 = new SupplyOrder();
        supplyOrder2.setIdOrder(2L);
        supplyOrder2.setSupplier(supplier2);
        supplyOrder2.setOrderDate(LocalDate.now().minusDays(1));
        supplyOrder2.setStatus(StatusSupply.RECUE);

        SupplyOrderDTO supplyOrderDTO2 = new SupplyOrderDTO();
        supplyOrderDTO2.setIdOrder(2L);
        supplyOrderDTO2.setSupplierName("Second Supplier");
        supplyOrderDTO2.setOrderDate(LocalDate.now().minusDays(1));
        supplyOrderDTO2.setStatus(StatusSupply.RECUE);

        List<SupplyOrder> orders = Arrays.asList(supplyOrder, supplyOrder2);

        when(supplyOrderRepository.findAll()).thenReturn(orders);
        when(supplyOrderMapper.toDto(supplyOrder)).thenReturn(supplyOrderDTO);
        when(supplyOrderMapper.toDto(supplyOrder2)).thenReturn(supplyOrderDTO2);

        List<SupplyOrderDTO> result = supplyOrderService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Supplier", result.get(0).getSupplierName());
        assertEquals("Second Supplier", result.get(1).getSupplierName());
        verify(supplyOrderRepository, times(1)).findAll();
        verify(supplyOrderMapper, times(1)).toDto(supplyOrder);
        verify(supplyOrderMapper, times(1)).toDto(supplyOrder2);
    }

    @Test
    void testFindAll_WithNullSupplier_LambdaHandling() {
        SupplyOrder orderWithoutSupplier = new SupplyOrder();
        orderWithoutSupplier.setIdOrder(3L);
        orderWithoutSupplier.setSupplier(null);
        orderWithoutSupplier.setOrderDate(LocalDate.now());
        orderWithoutSupplier.setStatus(StatusSupply.EN_ATTENTE);

        SupplyOrderDTO dtoWithoutSupplier = new SupplyOrderDTO();
        dtoWithoutSupplier.setIdOrder(3L);
        dtoWithoutSupplier.setOrderDate(LocalDate.now());
        dtoWithoutSupplier.setStatus(StatusSupply.EN_ATTENTE);

        List<SupplyOrder> orders = Arrays.asList(orderWithoutSupplier);

        when(supplyOrderRepository.findAll()).thenReturn(orders);
        when(supplyOrderMapper.toDto(orderWithoutSupplier)).thenReturn(dtoWithoutSupplier);

        List<SupplyOrderDTO> result = supplyOrderService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.get(0).getSupplierName());
        verify(supplyOrderRepository, times(1)).findAll();
    }

    @Test
    void testFindById_WithNullSupplier() {
        supplyOrder.setSupplier(null);
        when(supplyOrderRepository.findById(1L)).thenReturn(Optional.of(supplyOrder));
        when(supplyOrderMapper.toDto(supplyOrder)).thenReturn(supplyOrderDTO);

        SupplyOrderDTO result = supplyOrderService.findById(1L);

        assertNotNull(result);
        verify(supplyOrderRepository, times(1)).findById(1L);
    }
}