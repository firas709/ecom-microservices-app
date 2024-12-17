package tn.firas.orderservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.firas.orderservice.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
