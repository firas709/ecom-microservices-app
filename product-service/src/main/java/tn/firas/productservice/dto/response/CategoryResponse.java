package tn.firas.productservice.dto.response;

public record CategoryResponse(
        Integer categoryId,
        String categoryName,
        String categoryDescription
) {
}
