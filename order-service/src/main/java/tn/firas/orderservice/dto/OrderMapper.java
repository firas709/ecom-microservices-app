package tn.firas.orderservice.dto;

import org.springframework.stereotype.Service;
import tn.firas.orderservice.dto.request.OrderRequest;
import tn.firas.orderservice.dto.response.OrderResponse;
import tn.firas.orderservice.entities.Order;

@Service
public class OrderMapper {


    public Order toOrder(OrderRequest request) {
        if (request == null) {
            return null;
        }
        return Order.builder()
                .id(request.id())
                .reference(request.reference())
                .paymentMethod(request.paymentMethod())
                .customerId(request.customerId())
                .build();
    }

    public OrderResponse fromOrder(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getReference(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getCustomerId()
        );
    }
}
