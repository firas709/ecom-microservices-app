package tn.firas.productservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.firas.productservice.entities.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
  List<Product> findByNameContainingIgnoreCase(String name);
  List<Product> findByCategoryId(Integer categoryId);

  @Query("""
            SELECT product
            FROM Product product
            """)
  Page<Product> findAllProducts(Pageable pageable);
}