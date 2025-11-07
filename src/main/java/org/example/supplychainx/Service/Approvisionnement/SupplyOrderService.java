package org.example.supplychainx.Service.Approvisionnement;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderDTO;
import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderRequest;
import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderResponse;
import org.example.supplychainx.Mappers.Approvisionnement.SupplyOrderMapper;
import org.example.supplychainx.Model.Approvisionnement.*;
import org.example.supplychainx.Repository.Approvisionnement.RawMaterialRepository;
import org.example.supplychainx.Repository.Approvisionnement.SupplierRepository;
import org.example.supplychainx.Repository.Approvisionnement.SupplyOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class SupplyOrderService {

    private final SupplierRepository supplierService;
    private SupplyOrderRepository supplyOrderRepository;
    private SupplyOrderMapper supplyOrderMapper;
    private RawMaterialRepository rawMaterialRepository;

    public SupplyOrderDTO findById(Long id) {
        SupplyOrder supplyOrder = supplyOrderRepository.findById(id).orElse(null);
        SupplyOrderDTO supplyOrderDTO = supplyOrderMapper.toDto(supplyOrder);
        if (supplyOrder.getSupplier() != null) {
            supplyOrderDTO.setSupplierName(supplyOrder.getSupplier().getName());
        }
        return supplyOrderDTO;
    }

    public List<SupplyOrderDTO> findAll() {
        List<SupplyOrder> orders = supplyOrderRepository.findAll();
        List<SupplyOrderDTO> orderDTOS = orders.stream()
                .map(order -> {
                    SupplyOrderDTO dto = supplyOrderMapper.toDto(order);
                    if (order.getSupplier() != null) {
                        dto.setSupplierName(order.getSupplier().getName());
                    }
                    return dto;
                })
                .toList();
        return orderDTOS;
    }



public SupplyOrderResponse save(SupplyOrderRequest request) {

    if (request.getDate().isAfter(LocalDate.now())) {
        throw new IllegalArgumentException("Supply order date cannot be in the future.");
    }

    Supplier supplier = supplierService.findById(request.getSupplierId())
            .orElseThrow(() -> new IllegalArgumentException("Supplier not found."));

    SupplyOrder supplyOrder = new SupplyOrder();
    supplyOrder.setOrderDate(request.getDate());
    supplyOrder.setSupplier(supplier);
    supplyOrder.setStatus(StatusSupply.EN_ATTENTE);

    List<SupplyOrderMaterials> orderRawMaterials = new ArrayList<>();

    for (SupplyOrderRequest.RawMaterialQuantity rmq : request.getRawMaterials()) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(rmq.getRawMaterialId())
                .orElseThrow(() -> new IllegalArgumentException("Raw material with ID " + rmq.getRawMaterialId() + " not found."));

        SupplyOrderMaterials relation = new SupplyOrderMaterials();
        relation.setRawMaterial(rawMaterial);
        relation.setQuantity(rmq.getQuantity());
        relation.setSupplyOrder(supplyOrder);

        orderRawMaterials.add(relation);
    }

    supplyOrder.setSupplyOrderMaterials(orderRawMaterials);
    SupplyOrder supplyOrder1 = supplyOrderRepository.save(supplyOrder);
    return supplyOrderMapper.toResponse(supplyOrder1);

}
    public void delete(Long id) {
        SupplyOrder order = supplyOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == StatusSupply.RECUE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot delete delivered order");
        }

        supplyOrderRepository.deleteById(id);
    }

    public SupplyOrderDTO update(Long id, SupplyOrderDTO supplyOrder) {

        SupplyOrder supplyOrder1 = supplyOrderMapper.toEntity(supplyOrder);
        SupplyOrder existingOrder = supplyOrderRepository.findById(id).orElseThrow(() -> new RuntimeException("SupplierOrder not found with id: " + id));

        Supplier supplier = supplierService.findByName(supplyOrder.getSupplierName());
        if (existingOrder != null) {

            existingOrder.setSupplier(supplier);
//            existingOrder.setMaterial(supplyOrder1.getMaterial());
            existingOrder.setOrderDate(supplyOrder1.getOrderDate());
            existingOrder.setStatus(supplyOrder.getStatus());
            SupplyOrder supplyOrder2 = supplyOrderRepository.save(existingOrder);
            SupplyOrderDTO supplyOrderDTO = supplyOrderMapper.toDto(supplyOrder2);
            supplyOrderDTO.setSupplierName(supplyOrder.getSupplierName());
            return supplyOrderDTO;
        }
        return null;
    }

}
