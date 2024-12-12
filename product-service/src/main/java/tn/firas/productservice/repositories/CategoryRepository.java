package tn.firas.productservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.firas.productservice.entities.Category;

import java.awt.print.Book;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Custom query method
    Optional<Category> findByName(String name);

    // Check if category exists by name
    boolean existsByName(String name);

    @Query("""
            SELECT category
            FROM Category category
            """)
    Page<Category> findAllCategories(Pageable pageable);
}