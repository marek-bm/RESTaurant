package com.mj.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
public class Product extends ResourceSupport {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long productId;
    private String name;
    private BigDecimal netPrice;
    private BigDecimal grossPrice;
    private double tax;

    public Product(String name, BigDecimal price, double tax) {
        this.name=name;
        this.netPrice=price;
        this.tax=tax;
        this.grossPrice=price.multiply(BigDecimal.valueOf(1+tax));
    }
}
