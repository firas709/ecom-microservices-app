package tn.firas.orderservice.kafka;

import tn.firas.orderservice.customer.CustomerResponse;
import tn.firas.orderservice.entities.PaymentMethod;
import tn.firas.orderservice.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation (
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products

) {
}