package tn.firas.productservice.dto;

import org.springframework.stereotype.Service;
import tn.firas.productservice.dto.request.CategoryRequest;
import tn.firas.productservice.dto.response.CategoryResponse;
import tn.firas.productservice.entities.Category;

@Service
public class CategoryMapper {
    public Category toCategory (CategoryRequest categoryAddRequest) {
        return Category.builder()
                .name(categoryAddRequest.categoryName())
                .description(categoryAddRequest.categoryDescription())
                .build();
    }

    public CategoryResponse toCategoryResponse (Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
