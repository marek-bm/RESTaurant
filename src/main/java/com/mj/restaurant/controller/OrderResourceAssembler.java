package com.mj.restaurant.controller;

import com.mj.restaurant.model.Order;
import com.mj.restaurant.model.OrderStatus;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class OrderResourceAssembler implements ResourceAssembler<Order, Resource> {
    @Override
    public Resource toResource(Order order) {

//        if(order.getStatus().equals(OrderStatus.COMPLETED)){
//            return new Resource(order,
//                    linkTo(methodOn(OrderController.class).findOrder(order.getId())).withSelfRel(),
//                    linkTo(methodOn(OrderController.class).findAll()).withRel("orders"));
//        } else {
            return new Resource(order,
                    linkTo(methodOn(OrderController.class).findOrder(order.getId())).withSelfRel(),
                    linkTo(methodOn(OrderController.class).updateOrder(order.getId())).withRel("in-progress"),
                    linkTo(methodOn(OrderController.class).completeOrder(order.getId())).withRel("completed"),
                    linkTo(methodOn(OrderController.class).findAll()).withRel("orders"));
//        }
    }
}
