package org.example.supplychainx.Service.Livraison;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Livraison.CustomerDTO;
import org.example.supplychainx.Mappers.Livraison.CustomerMapper;
import org.example.supplychainx.Model.Livraison.Customer;
import org.example.supplychainx.Repository.Livraison.CustomerRepository;
import org.example.supplychainx.Repository.Livraison.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final OrderRepository orderRepository;

    public CustomerDTO findById(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        return customerMapper.toDto(customer);
    }

    public List<CustomerDTO> findAll() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customerMapper::toDto)
                .toList();
    }

    public CustomerDTO save(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    public CustomerDTO update(Long id, CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findById(id).orElse(null);
        if (existingCustomer != null) {
            Customer customerToUpdate = customerMapper.toEntity(customerDTO);
            customerToUpdate.setIdCustomer(id);
            Customer updatedCustomer = customerRepository.save(customerToUpdate);
            return customerMapper.toDto(updatedCustomer);
        }
        return null;
    }

    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    public CustomerDTO findByName(String name) {
        Customer customer = customerRepository.findByName(name);
        return customerMapper.toDto(customer);
    }
}
