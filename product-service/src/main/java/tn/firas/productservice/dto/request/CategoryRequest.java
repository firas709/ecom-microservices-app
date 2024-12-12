package tn.firas.productservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotNull(message = "Category name is required")
        @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
        String categoryName,
        @NotNull(message = "Category description is required")
        @Size(min = 5, max = 255, message = "Category description must be between 5 and 255 characters")
        String categoryDescription
) {
}
