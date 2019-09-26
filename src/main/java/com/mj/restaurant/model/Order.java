package com.mj.restaurant.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @OneToOne
    private Product product;

    public Order() {
        this.status=OrderStatus.OPEN;
    }

    public Order(Product product) {
        this.product = product;
        this.status=OrderStatus.OPEN;

    }
}
