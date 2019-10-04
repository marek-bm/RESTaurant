package com.mj.restaurant.config;

import com.mj.restaurant.model.Order;
import com.mj.restaurant.model.OrderLine;
import com.mj.restaurant.model.OrderStatus;
import com.mj.restaurant.model.Product;
import com.mj.restaurant.repository.OrderLineRepository;
import com.mj.restaurant.repository.OrdersRepository;
import com.mj.restaurant.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataPreload implements CommandLineRunner {
    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderLineRepository orderLineRepository;

    @Override
    public void run(String... args) throws Exception {
        Product p1=new Product("Esspresso Lungo", BigDecimal.valueOf(5.50), 0.22);
        Product p2=new Product("Cafe Latte", BigDecimal.valueOf(9.50), 0.22);
        Product p3=new Product("Latte Macciatto", BigDecimal.valueOf(8.50), 0.22);
        Product p4=new Product("Cappuccino", BigDecimal.valueOf(7.80), 0.22);
        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
        productRepository.save(p4);


        OrderLine l1=new OrderLine(3,p1);
        OrderLine l2=new OrderLine(1,p2);
        OrderLine l3=new OrderLine(2,p3);
        orderLineRepository.save(l1);
        orderLineRepository.save(l2);
        orderLineRepository.save(l3);

        Order o1=new Order();
        o1.addNextLine(l1);
        o1.addNextLine(l2);
        o1.sumTotal();
        ordersRepository.save(o1);

        Order o2=new Order();
        o2.addNextLine(l3);
        o2.setStatus(OrderStatus.IN_PROGRESS);
        o2.sumTotal();
        ordersRepository.save(o2);
    }
}
