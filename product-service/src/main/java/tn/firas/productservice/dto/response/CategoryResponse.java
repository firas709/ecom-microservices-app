package tn.firas.productservice.dto.response;

import java.time.LocalDateTime;

public record CategoryResponse(
        Integer categoryId,
        String categoryName,
        Boolean categoryIsActive,
        LocalDateTime createdDate

) {

}
