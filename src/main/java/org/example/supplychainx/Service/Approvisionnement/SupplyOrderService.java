package org.example.supplychainx.Service.Approvisionnement;

import org.example.supplychainx.Model.Approvisionnement.SupplyOrder;
import org.example.supplychainx.Repository.Approvisionnement.SupplyOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplyOrderService {

    @Autowired
    private SupplyOrderRepository supplyOrderRepository;

    public SupplyOrder findById(Long id) {
        return supplyOrderRepository.findById(id).orElse(null);
    }

    public List<SupplyOrder> findAll() {
        return supplyOrderRepository.findAll();
    }

    public SupplyOrder save(SupplyOrder supplyOrder) {
        return supplyOrderRepository.save(supplyOrder);
    }

    public void delete(SupplyOrder supplyOrder) {
        supplyOrderRepository.delete(supplyOrder);
    }

    public SupplyOrder update(SupplyOrder supplyOrder) {
        SupplyOrder existingOrder = findById(supplyOrder.getIdOrder());
        if (existingOrder != null) {
            existingOrder.setSupplier(supplyOrder.getSupplier());
            existingOrder.setMaterial(supplyOrder.getMaterial());
            existingOrder.setOrderDate(supplyOrder.getOrderDate());
            existingOrder.setStatus(supplyOrder.getStatus());
            return supplyOrderRepository.save(existingOrder);
        }
        return null;
    }

}
