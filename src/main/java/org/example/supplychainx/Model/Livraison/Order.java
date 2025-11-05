package org.example.supplychainx.Model.Livraison;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.supplychainx.Model.Production.Product;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrder;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCustomer")
    private Customer customer;
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    private StatusOrder status;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Delivery> deliveries;
}
