package tn.firas.productservice.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        Integer id,
        String name,
        String description,
        Double availableQuantity,
        List<String> images,
        BigDecimal price,
        CategoryResponse category,
        Boolean isActive,
        Boolean isSold

) {
}
