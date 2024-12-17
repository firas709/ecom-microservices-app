package tn.firas.orderservice.dto.response;

public record OrderLineResponse(
        Integer id,
        double quantity
) { }