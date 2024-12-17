package tn.firas.orderservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import tn.firas.orderservice.entities.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.math.BigDecimal;

@JsonInclude(Include.NON_EMPTY)
public record OrderResponse(
        Integer id,
        String reference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String customerId
) {

}
