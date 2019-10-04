package com.mj.restaurant.controller;

import com.mj.restaurant.model.OrderLine;
import com.mj.restaurant.repository.OrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = "/lines")
public class OrderLineController {
//
//    @Autowired
//    private OrderLineRepository orderLineRepository;

//    @PostMapping ("/")
//    public void saveOrderLine(OrderLine orderLine){
//        orderLineRepository.save(orderLine);
//    }
}
