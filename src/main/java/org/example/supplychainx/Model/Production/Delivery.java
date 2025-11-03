package org.example.supplychainx.Model.Production;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDelivery;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idOrder")
    private Order order;
    private String vehicle;
    private String driver;
    @Enumerated(EnumType.STRING)
    private StatusDelivery status;
    private LocalDate deliveryDate;
    private Double cost;
}
