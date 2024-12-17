package tn.firas.orderservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.firas.orderservice.entities.OrderLine;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {

    List<OrderLine> findAllByOrderId(Integer orderId);
}
