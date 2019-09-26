package com.mj.restaurant.controller;

import com.mj.restaurant.model.Order;
import com.mj.restaurant.model.Product;
import com.mj.restaurant.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductResourceAssembler resourceAssembler;

    @GetMapping ("/")
    public Resources<Resource<Product>> loadAll(){
        List<Product> products= Optional.ofNullable(productRepository.findAll()).orElseThrow(NoSuchElementException::new);

        List resources=products.stream()
                .map(x -> resourceAssembler.toResource(x))
                .collect(Collectors.toList());

        return new Resources<Resource<Product>>(resources,
                linkTo(methodOn(ProductController.class).loadAll()).withRel("products"));
    }
}
