package tn.firas.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.firas.productservice.common.PageResponse;
import tn.firas.productservice.dto.request.ProductRequest;
import tn.firas.productservice.dto.response.ProductResponse;
import tn.firas.productservice.services.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "CRUD operations for products")
public class ProductController {

    private final ProductService productService;

    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> saveProduct(
            @RequestPart("product") String req,
            @RequestPart("images") List<MultipartFile> images
    ) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductRequest product = objectMapper.readValue(req, ProductRequest.class);
            String response = productService.createProduct(product, images);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception error) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.getMessage());
        }
    }


    @PutMapping(value = "/{product-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateProduct(
            @PathVariable("product-id") Integer idProduct,
            @RequestPart("product") String req,
            @RequestPart("images") List<MultipartFile> images
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductRequest product = objectMapper.readValue(req, ProductRequest.class);
            String response = productService.updateProduct(idProduct,product, images);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception error) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProduct(@PathVariable Integer id) {
        return productService.getProduct(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ProductResponse> getAllProducts(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return productService.getAllProducts(page,size);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Integer id) throws Exception {
        productService.deleteById(id);
    }


}
