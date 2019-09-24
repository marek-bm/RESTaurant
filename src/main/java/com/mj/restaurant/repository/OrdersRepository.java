package com.mj.restaurant.repository;

import com.mj.restaurant.model.Order;
import com.mj.restaurant.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Min;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStatusEqualsOrStatusEquals(OrderStatus status1, OrderStatus status2 );
    List<Order> findAllByStatus(OrderStatus status);
}
