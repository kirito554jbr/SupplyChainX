package org.example.supplychainx.Service.Approvisionnement;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderDTO;
import org.example.supplychainx.Mappers.Approvisionnement.SupplyOrderMapper;
import org.example.supplychainx.Model.Approvisionnement.Supplier;
import org.example.supplychainx.Model.Approvisionnement.SupplyOrder;
import org.example.supplychainx.Repository.Approvisionnement.SupplyOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SupplyOrderService {

    private final SupplierService supplierService;
    private SupplyOrderRepository supplyOrderRepository;
    private SupplyOrderMapper supplyOrderMapper;

    public SupplyOrderDTO findById(Long id) {
        SupplyOrder supplyOrder = supplyOrderRepository.findById(id).orElse(null);
        SupplyOrderDTO supplyOrderDTO = supplyOrderMapper.toDto(supplyOrder);
        return supplyOrderDTO;
    }

    public List<SupplyOrderDTO> findAll() {
        List<SupplyOrder> orders = supplyOrderRepository.findAll();
        List<SupplyOrderDTO> orderDTOS = orders.stream()
                .map(order -> supplyOrderMapper.toDto(order))
                .toList();
        return orderDTOS;
    }

    public SupplyOrderDTO save(SupplyOrderDTO supplyOrder) {
        SupplyOrder supplyOrderEntity = supplyOrderMapper.toEntity(supplyOrder);

        Supplier supplier = supplierService.findByName(supplyOrder.getSupplierName());



        supplyOrderEntity.setSupplier(supplier);
        SupplyOrder result = supplyOrderRepository.save(supplyOrderEntity);
        return supplyOrderMapper.toDto(result);
    }

    public void delete(SupplyOrder supplyOrder) {
        supplyOrderRepository.delete(supplyOrder);
    }

    public SupplyOrderDTO update(Long id, SupplyOrderDTO supplyOrder) {

        SupplyOrder supplyOrder1 = supplyOrderMapper.toEntity(supplyOrder);
        SupplyOrder existingOrder = supplyOrderRepository.findById(id).orElseThrow(() -> new RuntimeException("SupplierOrder not found with id: " + id));

        if (existingOrder != null) {

            existingOrder.setSupplier(supplyOrder1.getSupplier());
//            existingOrder.setMaterial(supplyOrder1.getMaterial());
            existingOrder.setOrderDate(supplyOrder1.getOrderDate());
            existingOrder.setStatus(supplyOrder.getStatus());
            SupplyOrder supplyOrder2 = supplyOrderRepository.save(existingOrder);
            return supplyOrderMapper.toDto(supplyOrder2);
        }
        return null;
    }

}
