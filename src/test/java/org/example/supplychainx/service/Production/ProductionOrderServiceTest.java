package org.example.supplychainx.service.Production;

import org.example.supplychainx.DTO.Production.ProductResponseDTO;
import org.example.supplychainx.DTO.Production.ProductionOrderDTO;
import org.example.supplychainx.DTO.Production.ProductionOrderResponseDTO;
import org.example.supplychainx.Mappers.Production.ProductionOrderMapper;
import org.example.supplychainx.Model.Production.Product;
import org.example.supplychainx.Model.Production.ProductionOrder;
import org.example.supplychainx.Model.Production.StatusProduction;
import org.example.supplychainx.Repository.Production.ProductRepository;
import org.example.supplychainx.Repository.Production.ProductionOrderRepository;
import org.example.supplychainx.Service.Production.ProductionOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductionOrderServiceTest {

    @Mock
    private ProductionOrderRepository productionOrderRepository;

    @Mock
    private ProductionOrderMapper productionOrderMapper;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductionOrderService productionOrderService;

    private ProductionOrder productionOrder;
    private ProductionOrderDTO productionOrderDTO;
    private ProductionOrderResponseDTO productionOrderResponseDTO;
    private Product product;
    private ProductResponseDTO productResponseDTO;

    @BeforeEach
    void setUp() {
        // Setup Product
        product = new Product();
        product.setIdProduct(1L);
        product.setName("Test Product");
        product.setProductionTime(10);
        product.setCost(100.0);
        product.setStock(50);

        // Setup ProductResponseDTO
        productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setIdProduct(1L);
        productResponseDTO.setName("Test Product");
        productResponseDTO.setProductionTime(10);
        productResponseDTO.setCost(100.0);
        productResponseDTO.setStock(50);

        // Setup ProductionOrder
        productionOrder = new ProductionOrder();
        productionOrder.setIdProductionOrder(1L);
        productionOrder.setProduct(product);
        productionOrder.setQuantity(100);
        productionOrder.setStatus(StatusProduction.EN_ATTENTE);
        productionOrder.setStartDate(LocalDate.now());
        productionOrder.setEndDate(LocalDate.now().plusDays(10));

        // Setup ProductionOrderDTO
        productionOrderDTO = new ProductionOrderDTO(
                1L,
                1L,
                100,
                StatusProduction.EN_ATTENTE,
                LocalDate.now(),
                LocalDate.now().plusDays(10)
        );

        // Setup ProductionOrderResponseDTO
        productionOrderResponseDTO = new ProductionOrderResponseDTO(
                1L,
                100,
                StatusProduction.EN_ATTENTE,
                LocalDate.now(),
                LocalDate.now().plusDays(10),
                productResponseDTO
        );
    }

    // Tests for findAllProductionOrders
    @Test
    void testFindAllProductionOrders_Success() {
        List<ProductionOrder> orders = Arrays.asList(productionOrder);
        when(productionOrderRepository.findAll()).thenReturn(orders);
        when(productionOrderMapper.toResponseDto(productionOrder)).thenReturn(productionOrderResponseDTO);

        List<ProductionOrderResponseDTO> result = productionOrderService.findAllProductionOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getIdProductionOrder());
        assertEquals(100, result.get(0).getQuantity());
        assertEquals(StatusProduction.EN_ATTENTE, result.get(0).getStatus());
        verify(productionOrderRepository, times(1)).findAll();
        verify(productionOrderMapper, times(1)).toResponseDto(productionOrder);
    }

    @Test
    void testFindAllProductionOrders_EmptyList() {
        when(productionOrderRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProductionOrderResponseDTO> result = productionOrderService.findAllProductionOrders();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productionOrderRepository, times(1)).findAll();
        verify(productionOrderMapper, never()).toResponseDto(any());
    }

    @Test
    void testFindAllProductionOrders_MultipleOrders_LambdaMapping() {
        ProductionOrder order2 = new ProductionOrder();
        order2.setIdProductionOrder(2L);
        order2.setProduct(product);
        order2.setQuantity(200);
        order2.setStatus(StatusProduction.EN_PRODUCTION);
        order2.setStartDate(LocalDate.now());
        order2.setEndDate(LocalDate.now().plusDays(5));

        ProductionOrderResponseDTO responseDTO2 = new ProductionOrderResponseDTO(
                2L,
                200,
                StatusProduction.EN_PRODUCTION,
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                productResponseDTO
        );

        List<ProductionOrder> orders = Arrays.asList(productionOrder, order2);
        when(productionOrderRepository.findAll()).thenReturn(orders);
        when(productionOrderMapper.toResponseDto(productionOrder)).thenReturn(productionOrderResponseDTO);
        when(productionOrderMapper.toResponseDto(order2)).thenReturn(responseDTO2);

        List<ProductionOrderResponseDTO> result = productionOrderService.findAllProductionOrders();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getIdProductionOrder());
        assertEquals(2L, result.get(1).getIdProductionOrder());
        assertEquals(StatusProduction.EN_ATTENTE, result.get(0).getStatus());
        assertEquals(StatusProduction.EN_PRODUCTION, result.get(1).getStatus());
        verify(productionOrderRepository, times(1)).findAll();
        verify(productionOrderMapper, times(2)).toResponseDto(any(ProductionOrder.class));
    }

    @Test
    void testFindAllProductionOrders_DifferentStatuses_LambdaMapping() {
        ProductionOrder order2 = new ProductionOrder();
        order2.setIdProductionOrder(2L);
        order2.setStatus(StatusProduction.TERMINE);
        order2.setQuantity(150);

        ProductionOrder order3 = new ProductionOrder();
        order3.setIdProductionOrder(3L);
        order3.setStatus(StatusProduction.BLOQUE);
        order3.setQuantity(50);

        ProductionOrderResponseDTO responseDTO2 = new ProductionOrderResponseDTO(
                2L, 150, StatusProduction.TERMINE, LocalDate.now(), LocalDate.now(), productResponseDTO
        );

        ProductionOrderResponseDTO responseDTO3 = new ProductionOrderResponseDTO(
                3L, 50, StatusProduction.BLOQUE, LocalDate.now(), LocalDate.now(), productResponseDTO
        );

        List<ProductionOrder> orders = Arrays.asList(productionOrder, order2, order3);
        when(productionOrderRepository.findAll()).thenReturn(orders);
        when(productionOrderMapper.toResponseDto(productionOrder)).thenReturn(productionOrderResponseDTO);
        when(productionOrderMapper.toResponseDto(order2)).thenReturn(responseDTO2);
        when(productionOrderMapper.toResponseDto(order3)).thenReturn(responseDTO3);

        List<ProductionOrderResponseDTO> result = productionOrderService.findAllProductionOrders();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(StatusProduction.EN_ATTENTE, result.get(0).getStatus());
        assertEquals(StatusProduction.TERMINE, result.get(1).getStatus());
        assertEquals(StatusProduction.BLOQUE, result.get(2).getStatus());
        verify(productionOrderMapper, times(3)).toResponseDto(any(ProductionOrder.class));
    }

    // Tests for findProductionOrderById
    @Test
    void testFindProductionOrderById_Success() {
        when(productionOrderRepository.findById(1L)).thenReturn(Optional.of(productionOrder));
        when(productionOrderMapper.toResponseDto(productionOrder)).thenReturn(productionOrderResponseDTO);

        ProductionOrderResponseDTO result = productionOrderService.findProductionOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdProductionOrder());
        assertEquals(100, result.getQuantity());
        assertEquals(StatusProduction.EN_ATTENTE, result.getStatus());
        assertNotNull(result.getProduct());
        assertEquals("Test Product", result.getProduct().getName());
        verify(productionOrderRepository, times(1)).findById(1L);
        verify(productionOrderMapper, times(1)).toResponseDto(productionOrder);
    }

    @Test
    void testFindProductionOrderById_NotFound() {
        when(productionOrderRepository.findById(999L)).thenReturn(Optional.empty());
        when(productionOrderMapper.toResponseDto(null)).thenReturn(null);

        ProductionOrderResponseDTO result = productionOrderService.findProductionOrderById(999L);

        assertNull(result);
        verify(productionOrderRepository, times(1)).findById(999L);
        verify(productionOrderMapper, times(1)).toResponseDto(null);
    }

    @Test
    void testFindProductionOrderById_WithDifferentStatuses() {
        productionOrder.setStatus(StatusProduction.TERMINE);
        productionOrderResponseDTO.setStatus(StatusProduction.TERMINE);

        when(productionOrderRepository.findById(1L)).thenReturn(Optional.of(productionOrder));
        when(productionOrderMapper.toResponseDto(productionOrder)).thenReturn(productionOrderResponseDTO);

        ProductionOrderResponseDTO result = productionOrderService.findProductionOrderById(1L);

        assertNotNull(result);
        assertEquals(StatusProduction.TERMINE, result.getStatus());
        verify(productionOrderRepository, times(1)).findById(1L);
    }

    // Tests for createProductionOrder
    @Test
    void testCreateProductionOrder_Success() {
        when(productionOrderMapper.toEntity(productionOrderDTO)).thenReturn(productionOrder);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenReturn(productionOrder);
        when(productionOrderMapper.toResponseDto(productionOrder)).thenReturn(productionOrderResponseDTO);

        ProductionOrderResponseDTO result = productionOrderService.createProductionOrder(productionOrderDTO);

        assertNotNull(result);
        assertEquals(1L, result.getIdProductionOrder());
        assertEquals(100, result.getQuantity());
        assertEquals(StatusProduction.EN_ATTENTE, result.getStatus());
        verify(productionOrderMapper, times(1)).toEntity(productionOrderDTO);
        verify(productRepository, times(1)).findById(1L);
        verify(productionOrderRepository, times(1)).save(any(ProductionOrder.class));
        verify(productionOrderMapper, times(1)).toResponseDto(productionOrder);
    }

    @Test
    void testCreateProductionOrder_ProductNotFound() {
        ProductionOrderDTO dto = new ProductionOrderDTO(
                1L, 999L, 100, StatusProduction.EN_ATTENTE, LocalDate.now(), LocalDate.now().plusDays(10)
        );

        when(productionOrderMapper.toEntity(dto)).thenReturn(productionOrder);
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenReturn(productionOrder);
        when(productionOrderMapper.toResponseDto(productionOrder)).thenReturn(productionOrderResponseDTO);

        ProductionOrderResponseDTO result = productionOrderService.createProductionOrder(dto);

        assertNotNull(result);
        verify(productRepository, times(1)).findById(999L);
        verify(productionOrderRepository, times(1)).save(any(ProductionOrder.class));
    }

    @Test
    void testCreateProductionOrder_WithDifferentStatus() {
        ProductionOrderDTO dto = new ProductionOrderDTO(
                null, 1L, 150, StatusProduction.EN_PRODUCTION, LocalDate.now(), LocalDate.now().plusDays(7)
        );

        ProductionOrder order = new ProductionOrder();
        order.setQuantity(150);
        order.setStatus(StatusProduction.EN_PRODUCTION);

        ProductionOrderResponseDTO response = new ProductionOrderResponseDTO(
                null, 150, StatusProduction.EN_PRODUCTION, LocalDate.now(), LocalDate.now().plusDays(7), productResponseDTO
        );

        when(productionOrderMapper.toEntity(dto)).thenReturn(order);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenReturn(order);
        when(productionOrderMapper.toResponseDto(order)).thenReturn(response);

        ProductionOrderResponseDTO result = productionOrderService.createProductionOrder(dto);

        assertNotNull(result);
        assertEquals(150, result.getQuantity());
        assertEquals(StatusProduction.EN_PRODUCTION, result.getStatus());
        verify(productionOrderRepository, times(1)).save(any(ProductionOrder.class));
    }

    @Test
    void testCreateProductionOrder_ProductSetCorrectly() {
        when(productionOrderMapper.toEntity(productionOrderDTO)).thenReturn(productionOrder);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenAnswer(invocation -> {
            ProductionOrder saved = invocation.getArgument(0);
            assertNotNull(saved.getProduct());
            assertEquals(1L, saved.getProduct().getIdProduct());
            assertEquals("Test Product", saved.getProduct().getName());
            return saved;
        });
        when(productionOrderMapper.toResponseDto(any(ProductionOrder.class))).thenReturn(productionOrderResponseDTO);

        ProductionOrderResponseDTO result = productionOrderService.createProductionOrder(productionOrderDTO);

        assertNotNull(result);
        verify(productionOrderRepository, times(1)).save(any(ProductionOrder.class));
    }

    // Tests for updateProductionOrder
    @Test
    void testUpdateProductionOrder_Success() {
        when(productionOrderRepository.findById(1L)).thenReturn(Optional.of(productionOrder));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productionOrderMapper.toEntity(productionOrderDTO)).thenReturn(productionOrder);
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenReturn(productionOrder);
        when(productionOrderMapper.toResponseDto(productionOrder)).thenReturn(productionOrderResponseDTO);

        ProductionOrderResponseDTO result = productionOrderService.updateProductionOrder(1L, productionOrderDTO);

        assertNotNull(result);
        assertEquals(1L, result.getIdProductionOrder());
        assertEquals(100, result.getQuantity());
        verify(productionOrderRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(productionOrderRepository, times(1)).save(any(ProductionOrder.class));
        verify(productionOrderMapper, times(1)).toResponseDto(productionOrder);
    }

    @Test
    void testUpdateProductionOrder_NotFound() {
        when(productionOrderRepository.findById(999L)).thenReturn(Optional.empty());

        ProductionOrderResponseDTO result = productionOrderService.updateProductionOrder(999L, productionOrderDTO);

        assertNull(result);
        verify(productionOrderRepository, times(1)).findById(999L);
        verify(productionOrderRepository, never()).save(any());
        verify(productionOrderMapper, never()).toResponseDto(any());
    }

    @Test
    void testUpdateProductionOrder_ProductNotFound() {
        ProductionOrderDTO dto = new ProductionOrderDTO(
                1L, 999L, 100, StatusProduction.EN_ATTENTE, LocalDate.now(), LocalDate.now().plusDays(10)
        );

        when(productionOrderRepository.findById(1L)).thenReturn(Optional.of(productionOrder));
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        when(productionOrderMapper.toEntity(dto)).thenReturn(productionOrder);
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenReturn(productionOrder);
        when(productionOrderMapper.toResponseDto(productionOrder)).thenReturn(productionOrderResponseDTO);

        ProductionOrderResponseDTO result = productionOrderService.updateProductionOrder(1L, dto);

        assertNotNull(result);
        verify(productRepository, times(1)).findById(999L);
        verify(productionOrderRepository, times(1)).save(any(ProductionOrder.class));
    }

    @Test
    void testUpdateProductionOrder_StatusChanged() {
        ProductionOrderDTO updatedDTO = new ProductionOrderDTO(
                1L, 1L, 100, StatusProduction.TERMINE, LocalDate.now(), LocalDate.now().plusDays(10)
        );

        ProductionOrder updatedOrder = new ProductionOrder();
        updatedOrder.setIdProductionOrder(1L);
        updatedOrder.setStatus(StatusProduction.TERMINE);
        updatedOrder.setQuantity(100);

        ProductionOrderResponseDTO updatedResponse = new ProductionOrderResponseDTO(
                1L, 100, StatusProduction.TERMINE, LocalDate.now(), LocalDate.now().plusDays(10), productResponseDTO
        );

        when(productionOrderRepository.findById(1L)).thenReturn(Optional.of(productionOrder));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productionOrderMapper.toEntity(updatedDTO)).thenReturn(updatedOrder);
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenReturn(updatedOrder);
        when(productionOrderMapper.toResponseDto(updatedOrder)).thenReturn(updatedResponse);

        ProductionOrderResponseDTO result = productionOrderService.updateProductionOrder(1L, updatedDTO);

        assertNotNull(result);
        assertEquals(StatusProduction.TERMINE, result.getStatus());
        verify(productionOrderRepository, times(1)).save(any(ProductionOrder.class));
    }

    @Test
    void testUpdateProductionOrder_IdPreserved() {
        when(productionOrderRepository.findById(1L)).thenReturn(Optional.of(productionOrder));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productionOrderMapper.toEntity(productionOrderDTO)).thenReturn(productionOrder);
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenAnswer(invocation -> {
            ProductionOrder saved = invocation.getArgument(0);
            assertEquals(1L, saved.getIdProductionOrder());
            return saved;
        });
        when(productionOrderMapper.toResponseDto(any(ProductionOrder.class))).thenReturn(productionOrderResponseDTO);

        ProductionOrderResponseDTO result = productionOrderService.updateProductionOrder(1L, productionOrderDTO);

        assertNotNull(result);
        verify(productionOrderRepository, times(1)).save(any(ProductionOrder.class));
    }

    @Test
    void testUpdateProductionOrder_QuantityChanged() {
        ProductionOrderDTO updatedDTO = new ProductionOrderDTO(
                1L, 1L, 250, StatusProduction.EN_ATTENTE, LocalDate.now(), LocalDate.now().plusDays(10)
        );

        ProductionOrder updatedOrder = new ProductionOrder();
        updatedOrder.setIdProductionOrder(1L);
        updatedOrder.setQuantity(250);
        updatedOrder.setProduct(product);

        ProductionOrderResponseDTO updatedResponse = new ProductionOrderResponseDTO(
                1L, 250, StatusProduction.EN_ATTENTE, LocalDate.now(), LocalDate.now().plusDays(10), productResponseDTO
        );

        when(productionOrderRepository.findById(1L)).thenReturn(Optional.of(productionOrder));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productionOrderMapper.toEntity(updatedDTO)).thenReturn(updatedOrder);
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenReturn(updatedOrder);
        when(productionOrderMapper.toResponseDto(updatedOrder)).thenReturn(updatedResponse);

        ProductionOrderResponseDTO result = productionOrderService.updateProductionOrder(1L, updatedDTO);

        assertNotNull(result);
        assertEquals(250, result.getQuantity());
        verify(productionOrderRepository, times(1)).save(any(ProductionOrder.class));
    }

    @Test
    void testUpdateProductionOrder_ProductAndIdSet() {
        when(productionOrderRepository.findById(1L)).thenReturn(Optional.of(productionOrder));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productionOrderMapper.toEntity(productionOrderDTO)).thenReturn(productionOrder);
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenAnswer(invocation -> {
            ProductionOrder saved = invocation.getArgument(0);
            assertEquals(1L, saved.getIdProductionOrder());
            assertNotNull(saved.getProduct());
            assertEquals(1L, saved.getProduct().getIdProduct());
            return saved;
        });
        when(productionOrderMapper.toResponseDto(any(ProductionOrder.class))).thenReturn(productionOrderResponseDTO);

        ProductionOrderResponseDTO result = productionOrderService.updateProductionOrder(1L, productionOrderDTO);

        assertNotNull(result);
        verify(productionOrderRepository, times(1)).save(any(ProductionOrder.class));
    }

    // Tests for deleteProductionOrder
    @Test
    void testDeleteProductionOrder_Success() {
        doNothing().when(productionOrderRepository).deleteById(1L);

        productionOrderService.deleteProductionOrder(1L);

        verify(productionOrderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProductionOrder_NonExistentId() {
        doNothing().when(productionOrderRepository).deleteById(999L);

        productionOrderService.deleteProductionOrder(999L);

        verify(productionOrderRepository, times(1)).deleteById(999L);
    }

    @Test
    void testDeleteProductionOrder_MultipleDeletes() {
        doNothing().when(productionOrderRepository).deleteById(anyLong());

        productionOrderService.deleteProductionOrder(1L);
        productionOrderService.deleteProductionOrder(2L);
        productionOrderService.deleteProductionOrder(3L);

        verify(productionOrderRepository, times(1)).deleteById(1L);
        verify(productionOrderRepository, times(1)).deleteById(2L);
        verify(productionOrderRepository, times(1)).deleteById(3L);
        verify(productionOrderRepository, times(3)).deleteById(anyLong());
    }

    // Edge case tests
    @Test
    void testFindAllProductionOrders_StreamMapping_PreservesOrder() {
        ProductionOrder order1 = new ProductionOrder();
        order1.setIdProductionOrder(1L);
        order1.setQuantity(100);

        ProductionOrder order2 = new ProductionOrder();
        order2.setIdProductionOrder(2L);
        order2.setQuantity(200);

        ProductionOrder order3 = new ProductionOrder();
        order3.setIdProductionOrder(3L);
        order3.setQuantity(300);

        ProductionOrderResponseDTO response1 = new ProductionOrderResponseDTO(
                1L, 100, StatusProduction.EN_ATTENTE, LocalDate.now(), LocalDate.now(), productResponseDTO
        );

        ProductionOrderResponseDTO response2 = new ProductionOrderResponseDTO(
                2L, 200, StatusProduction.EN_PRODUCTION, LocalDate.now(), LocalDate.now(), productResponseDTO
        );

        ProductionOrderResponseDTO response3 = new ProductionOrderResponseDTO(
                3L, 300, StatusProduction.TERMINE, LocalDate.now(), LocalDate.now(), productResponseDTO
        );

        List<ProductionOrder> orders = Arrays.asList(order1, order2, order3);
        when(productionOrderRepository.findAll()).thenReturn(orders);
        when(productionOrderMapper.toResponseDto(order1)).thenReturn(response1);
        when(productionOrderMapper.toResponseDto(order2)).thenReturn(response2);
        when(productionOrderMapper.toResponseDto(order3)).thenReturn(response3);

        List<ProductionOrderResponseDTO> result = productionOrderService.findAllProductionOrders();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1L, result.get(0).getIdProductionOrder());
        assertEquals(2L, result.get(1).getIdProductionOrder());
        assertEquals(3L, result.get(2).getIdProductionOrder());
        assertEquals(100, result.get(0).getQuantity());
        assertEquals(200, result.get(1).getQuantity());
        assertEquals(300, result.get(2).getQuantity());
    }

    @Test
    void testCreateProductionOrder_WithNullProduct() {
        when(productionOrderMapper.toEntity(productionOrderDTO)).thenReturn(productionOrder);
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenAnswer(invocation -> {
            ProductionOrder saved = invocation.getArgument(0);
            assertNull(saved.getProduct());
            return saved;
        });
        when(productionOrderMapper.toResponseDto(any(ProductionOrder.class))).thenReturn(productionOrderResponseDTO);

        ProductionOrderResponseDTO result = productionOrderService.createProductionOrder(productionOrderDTO);

        assertNotNull(result);
        verify(productionOrderRepository, times(1)).save(any(ProductionOrder.class));
    }
}
