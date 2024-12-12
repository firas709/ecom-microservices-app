package tn.firas.productservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequest(
        @NotNull(message = "Product name is required")
        String name,
        @NotNull(message = "Product description is required")
        String description,
        @Positive(message = "Available quantity should be positive")
        Double availableQuantity,
        List<String>images,
        @Positive(message = "Price should be positive")
        BigDecimal price,
        Boolean isActive,
        Boolean isSold,
        @NotNull(message = "Product category is required")
        Integer categoryId
) {
}
