package tn.firas.productservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "CRUD operations for products")
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> saveProduct(
            @RequestPart("product") String req,
            @RequestPart("images") List<MultipartFile> images
    ) throws IOException {


            ObjectMapper objectMapper = new ObjectMapper();
            ProductRequest product = objectMapper.readValue(req, ProductRequest.class);


            return ResponseEntity.status(HttpStatus.OK).body(productService.createProduct(product, images));

    }


    @PostMapping(value = "/{product-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> updateProduct(
            @PathVariable("product-id") Integer idProduct,
            @RequestPart("product") String req,
            @RequestPart("images") List<MultipartFile> images
    ) throws Exception {

            ObjectMapper objectMapper = new ObjectMapper();
            ProductRequest product = objectMapper.readValue(req, ProductRequest.class);

            return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(idProduct,product, images));

    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProduct(@PathVariable Integer id)  throws IOException {
        return productService.getProduct(id);
    }



    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Integer id) throws Exception {
        productService.deleteById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ProductResponse> getAllProducts(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return productService.getAllProducts(page,size);
    }


    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAllProductsWithoutPagination() {
        List<ProductResponse> products = productService.findAllProductsWithoutPagination();
        return ResponseEntity.ok(products);
    }


}
