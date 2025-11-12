package org.example.supplychainx.service.Livraison;

import org.example.supplychainx.DTO.Livraison.DeliveryDTO;
import org.example.supplychainx.Mappers.Livraison.DeliveryMapper;
import org.example.supplychainx.Model.Livraison.Delivery;
import org.example.supplychainx.Model.Livraison.Order;
import org.example.supplychainx.Model.Livraison.StatusDelivery;
import org.example.supplychainx.Repository.Livraison.DeliveryRepository;
import org.example.supplychainx.Repository.Livraison.OrderRepository;
import org.example.supplychainx.Service.Livraison.DeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private DeliveryMapper deliveryMapper;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private DeliveryService deliveryService;

    private Delivery delivery;
    private DeliveryDTO deliveryDTO;
    private Order order;

    @BeforeEach
    void setUp() {
        // Setup Order
        order = new Order();
        order.setIdOrder(1L);

        // Setup Delivery entity
        delivery = new Delivery();
        delivery.setIdDelivery(1L);
        delivery.setOrder(order);
        delivery.setVehicle("Truck-001");
        delivery.setDriver("John Driver");
        delivery.setStatus(StatusDelivery.PLANIFIEE);
        delivery.setDeliveryDate(LocalDate.now().plusDays(2));
        delivery.setCost(250.0);

        // Setup DeliveryDTO
        deliveryDTO = new DeliveryDTO();
        deliveryDTO.setIdDelivery(1L);
        deliveryDTO.setOrder_id(1L);
        deliveryDTO.setVehicle("Truck-001");
        deliveryDTO.setDriver("John Driver");
        deliveryDTO.setStatus(StatusDelivery.PLANIFIEE);
        deliveryDTO.setDeliveryDate(LocalDate.now().plusDays(2));
        deliveryDTO.setCost(250.0);
    }

    @Test
    void testFindAll_Success() {

        Delivery delivery2 = new Delivery();
        delivery2.setIdDelivery(2L);
        delivery2.setVehicle("Van-002");
        delivery2.setDriver("Jane Driver");
        delivery2.setStatus(StatusDelivery.EN_COURS);

        DeliveryDTO deliveryDTO2 = new DeliveryDTO();
        deliveryDTO2.setIdDelivery(2L);
        deliveryDTO2.setVehicle("Van-002");
        deliveryDTO2.setDriver("Jane Driver");
        deliveryDTO2.setStatus(StatusDelivery.EN_COURS);

        List<Delivery> deliveries = Arrays.asList(delivery, delivery2);
        when(deliveryRepository.findAll()).thenReturn(deliveries);
        when(deliveryMapper.toDto(delivery)).thenReturn(deliveryDTO);
        when(deliveryMapper.toDto(delivery2)).thenReturn(deliveryDTO2);


        List<DeliveryDTO> result = deliveryService.findAll();


        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Truck-001", result.get(0).getVehicle());
        assertEquals("Van-002", result.get(1).getVehicle());
        verify(deliveryRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_EmptyList() {

        when(deliveryRepository.findAll()).thenReturn(List.of());


        List<DeliveryDTO> result = deliveryService.findAll();


        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(deliveryRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {

        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(deliveryMapper.toDto(delivery)).thenReturn(deliveryDTO);


        DeliveryDTO result = deliveryService.findById(1L);


        assertNotNull(result);
        assertEquals(1L, result.getIdDelivery());
        assertEquals("Truck-001", result.getVehicle());
        assertEquals("John Driver", result.getDriver());
        assertEquals(StatusDelivery.PLANIFIEE, result.getStatus());
        assertEquals(250.0, result.getCost());
        verify(deliveryRepository, times(1)).findById(1L);
        verify(deliveryMapper, times(1)).toDto(delivery);
    }

    @Test
    void testFindById_NotFound() {

        when(deliveryRepository.findById(999L)).thenReturn(Optional.empty());
        when(deliveryMapper.toDto(null)).thenReturn(null);


        DeliveryDTO result = deliveryService.findById(999L);


        assertNull(result);
        verify(deliveryRepository, times(1)).findById(999L);
    }

    @Test
    void testSave_Success() {

        when(deliveryMapper.toEntity(deliveryDTO)).thenReturn(delivery);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);
        when(deliveryMapper.toDto(delivery)).thenReturn(deliveryDTO);


        DeliveryDTO result = deliveryService.save(deliveryDTO);


        assertNotNull(result);
        assertEquals("Truck-001", result.getVehicle());
        assertEquals("John Driver", result.getDriver());
        assertEquals(StatusDelivery.PLANIFIEE, result.getStatus());
        verify(deliveryMapper, times(1)).toEntity(deliveryDTO);
        verify(orderRepository, times(1)).findById(1L);
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
        verify(deliveryMapper, times(1)).toDto(delivery);
    }

    @Test
    void testSave_OrderNotFound() {

        when(deliveryMapper.toEntity(deliveryDTO)).thenReturn(delivery);
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);
        when(deliveryMapper.toDto(delivery)).thenReturn(deliveryDTO);


        DeliveryDTO result = deliveryService.save(deliveryDTO);


        assertNotNull(result);
        verify(orderRepository, times(1)).findById(1L);
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    @Test
    void testUpdate_Success() {

        DeliveryDTO updatedDTO = new DeliveryDTO();
        updatedDTO.setIdDelivery(1L);
        updatedDTO.setOrder_id(1L);
        updatedDTO.setVehicle("Truck-Updated");
        updatedDTO.setDriver("Updated Driver");
        updatedDTO.setStatus(StatusDelivery.LIVREE);
        updatedDTO.setDeliveryDate(LocalDate.now());
        updatedDTO.setCost(300.0);

        Delivery updatedDelivery = new Delivery();
        updatedDelivery.setIdDelivery(1L);
        updatedDelivery.setVehicle("Truck-Updated");
        updatedDelivery.setDriver("Updated Driver");
        updatedDelivery.setStatus(StatusDelivery.LIVREE);
        updatedDelivery.setCost(300.0);

        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(deliveryMapper.toEntity(updatedDTO)).thenReturn(updatedDelivery);
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(updatedDelivery);
        when(deliveryMapper.toDto(updatedDelivery)).thenReturn(updatedDTO);


        DeliveryDTO result = deliveryService.update(1L, updatedDTO);


        assertNotNull(result);
        assertEquals("Truck-Updated", result.getVehicle());
        assertEquals("Updated Driver", result.getDriver());
        assertEquals(StatusDelivery.LIVREE, result.getStatus());
        assertEquals(300.0, result.getCost());
        verify(deliveryRepository, times(1)).findById(1L);
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    @Test
    void testUpdate_NotFound() {

        when(deliveryRepository.findById(999L)).thenReturn(Optional.empty());


        DeliveryDTO result = deliveryService.update(999L, deliveryDTO);


        assertNull(result);
        verify(deliveryRepository, times(1)).findById(999L);
        verify(deliveryRepository, never()).save(any(Delivery.class));
    }

    @Test
    void testDelete_Success() {

        doNothing().when(deliveryRepository).deleteById(1L);


        deliveryService.delete(1L);


        verify(deliveryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testSave_WithDifferentStatuses() {
        // Test PLANIFIEE
        DeliveryDTO planifiee = new DeliveryDTO();
        planifiee.setStatus(StatusDelivery.PLANIFIEE);
        planifiee.setOrder_id(1L);

        Delivery deliveryPlanifiee = new Delivery();
        deliveryPlanifiee.setStatus(StatusDelivery.PLANIFIEE);

        when(deliveryMapper.toEntity(planifiee)).thenReturn(deliveryPlanifiee);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(deliveryPlanifiee);
        when(deliveryMapper.toDto(deliveryPlanifiee)).thenReturn(planifiee);

        DeliveryDTO result = deliveryService.save(planifiee);

        assertEquals(StatusDelivery.PLANIFIEE, result.getStatus());
    }

    @Test
    void testSave_WithEnCoursStatus() {
        // Test EN_COURS
        DeliveryDTO enCours = new DeliveryDTO();
        enCours.setStatus(StatusDelivery.EN_COURS);
        enCours.setOrder_id(1L);

        Delivery deliveryEnCours = new Delivery();
        deliveryEnCours.setStatus(StatusDelivery.EN_COURS);

        when(deliveryMapper.toEntity(enCours)).thenReturn(deliveryEnCours);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(deliveryEnCours);
        when(deliveryMapper.toDto(deliveryEnCours)).thenReturn(enCours);

        DeliveryDTO result = deliveryService.save(enCours);

        assertEquals(StatusDelivery.EN_COURS, result.getStatus());
    }

    @Test
    void testSave_WithLivreeStatus() {
        // Test LIVREE
        DeliveryDTO livree = new DeliveryDTO();
        livree.setStatus(StatusDelivery.LIVREE);
        livree.setOrder_id(1L);

        Delivery deliveryLivree = new Delivery();
        deliveryLivree.setStatus(StatusDelivery.LIVREE);

        when(deliveryMapper.toEntity(livree)).thenReturn(deliveryLivree);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(deliveryLivree);
        when(deliveryMapper.toDto(deliveryLivree)).thenReturn(livree);

        DeliveryDTO result = deliveryService.save(livree);

        assertEquals(StatusDelivery.LIVREE, result.getStatus());
    }
}
