package com.mj.restaurant.controller;

import com.mj.restaurant.model.Product;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class ProductResourceAssembler implements ResourceAssembler<Product, Resource> {

    @Override
    public Resource toResource(Product product) {
//        return new Resource(product, linkTo(methodOn(ProductController.class).loadAll()).withRel("products"));
        return new Resource(product);
    }
}
