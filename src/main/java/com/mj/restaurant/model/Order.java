package com.mj.restaurant.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @OneToMany
    private List<OrderLine> products;
    private BigDecimal totalSum;

    public Order() {
        this.products = new ArrayList<>();
        this.status=OrderStatus.OPEN;
        this.totalSum= new BigDecimal(0.0);
    }

    public void addNextLine(OrderLine item){
        this.products.add(item);
    }

    public void sumTotal(){
        BigDecimal total=new BigDecimal(0);
        for(OrderLine i : this.products){
            BigDecimal subtotal=i.getSubTotal();
            total=total.add(subtotal);
        }
        this.totalSum=total;
    }


}
