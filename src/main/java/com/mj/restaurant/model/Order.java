package com.mj.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String orderedItems;

    public Order() {
        this.status=OrderStatus.OPEN;
    }

    public Order(String orderedItems) {
        this.orderedItems = orderedItems;
        this.status=OrderStatus.OPEN;

    }
}
