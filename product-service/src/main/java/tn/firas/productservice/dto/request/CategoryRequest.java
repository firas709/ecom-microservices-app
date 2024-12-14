package tn.firas.productservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotNull(message = "Category name is required")
        @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
        String categoryName,
        @NotNull(message = "Category IsActive is required")
        Boolean categoryIsActive
) {
}
