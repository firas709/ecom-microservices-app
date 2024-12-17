package tn.firas.orderservice.dto;

import org.springframework.stereotype.Service;
import tn.firas.orderservice.dto.request.OrderLineRequest;
import tn.firas.orderservice.dto.response.OrderLineResponse;
import tn.firas.orderservice.entities.Order;
import tn.firas.orderservice.entities.OrderLine;

@Service
public class OrderLineMapper {
    public OrderLine toOrderLine(OrderLineRequest request) {
        return OrderLine.builder()
                .id(request.orderId())
                .productId(request.productId())
                .order(
                        Order.builder()
                                .id(request.orderId())
                                .build()
                )
                .quantity(request.quantity())
                .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(
                orderLine.getId(),
                orderLine.getQuantity()
        );
    }
}
