package tn.firas.productservice.dto;

import org.springframework.stereotype.Service;
import tn.firas.productservice.dto.request.ProductRequest;
import tn.firas.productservice.dto.response.CategoryResponse;
import tn.firas.productservice.dto.response.ProductResponse;
import tn.firas.productservice.entities.Category;
import tn.firas.productservice.entities.Product;

@Service
public class ProductMapper {
    public Product toProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        Category category = new Category();
        category.setId(request.categoryId());
        product.setCategory(category);
        product.setIsActive(request.isActive());
        product.setIsSold(request.isSold());
        product.setAvailableQuantity(request.availableQuantity());
      return product;
    }

    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getAvailableQuantity(),
                product.getImages(),
                product.getPrice(),
                new CategoryResponse(
                        product.getCategory().getId(),
                        product.getCategory().getName(),
                        product.getCategory().getDescription()
                ),
                product.getIsSold(),
                product.getIsActive()
        );

    }




}
