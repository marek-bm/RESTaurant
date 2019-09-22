package com.mj.restaurant.config;

import com.mj.restaurant.model.Order;
import com.mj.restaurant.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataPreload implements CommandLineRunner {
    @Autowired
    OrdersRepository ordersRepository;

    @Override
    public void run(String... args) throws Exception {
        Order o=new Order();
        o.setOrderedItems("Esspresso Lungo");
        ordersRepository.save(o);
    }
}
