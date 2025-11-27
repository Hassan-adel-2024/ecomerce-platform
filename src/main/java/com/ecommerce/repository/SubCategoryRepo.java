package com.ecommerce.repository;

import com.ecommerce.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubCategoryRepo extends JpaRepository<SubCategory, Long> {
    boolean existsBySubCategoryName(String subCategoryName);
    List<SubCategory> findByCategoryCategoryId(Long categoryId);
    Optional<SubCategory> findBySubCategoryName(String subCategoryName);
}

