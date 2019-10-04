package com.mj.restaurant.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int quantity;
    private BigDecimal subTotal;
    @OneToOne
    private Product product;

    public OrderLine() {
    }

    public OrderLine(int quantity, Product product) {
        this.quantity = quantity;
        this.subTotal = product.getGrossPrice().multiply(BigDecimal.valueOf(quantity));
        this.product = product;
    }
}
