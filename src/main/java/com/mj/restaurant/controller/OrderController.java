package com.mj.restaurant.controller;

import com.mj.restaurant.model.Order;
import com.mj.restaurant.model.OrderStatus;
import com.mj.restaurant.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping (value = "/order", produces = {MediaType.APPLICATION_JSON_VALUE})
public class OrderController {
    private OrdersRepository ordersRepository;
    private OrderResourceAssembler resourceAssembler;

    @Autowired
    public OrderController(OrdersRepository ordersRepository, OrderResourceAssembler resourceAssembler) {
        this.ordersRepository = ordersRepository;
        this.resourceAssembler = resourceAssembler;
    }

    @GetMapping ("/{id}")
    public Resource<Order> findOrder(@PathVariable long id){
        Order order=Optional.ofNullable(ordersRepository.findById(id).get()).orElseThrow(NoSuchElementException::new);
        return resourceAssembler.toResource(order);
    }

    @GetMapping("/")
    public Resources<Resource<Order>> findAll(){
        List<Order> orders=ordersRepository.findAll();

        List resources=orders.stream()
                .map( x -> resourceAssembler.toResource(x))
                .collect(Collectors.toList());

        return new Resources<Resource<Order>>(resources,
                linkTo(methodOn(OrderController.class).findAll()).withSelfRel());
    }

    @PostMapping(
            value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Resource <Order> saveOrder(@RequestBody Order order){
        ordersRepository.save(order);
        return resourceAssembler.toResource(order);
    }

    @PutMapping(value = "/{id}/progress")
    public Resource<Order> updateOrder(@PathVariable long id){
        Order order=ordersRepository.findById(id).orElseThrow(NoSuchElementException::new);
        order.setStatus(OrderStatus.IN_PROGRESS);
        ordersRepository.save(order);
        return resourceAssembler.toResource(order);
    }

    @PutMapping("/{id}/complete")
    Resource<Order> completeOrder(@PathVariable long id){
        Order order=ordersRepository.findById(id).orElseThrow(NoSuchElementException::new);
        order.setStatus(OrderStatus.COMPLETED);
        ordersRepository.save(order);
        return resourceAssembler.toResource(order);
    }

    @DeleteMapping ("/{id}")
    public void deleteOrderFromDB(long id){
        Order order=ordersRepository.findById(id).orElseThrow(NoSuchElementException::new);
        order.setStatus(OrderStatus.DELETED);
        ordersRepository.save(order);
    }
}
