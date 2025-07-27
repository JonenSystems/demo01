package com.example.shopping.service.impl;

import com.example.shopping.model.dto.AdminProductDto;
import com.example.shopping.model.entity.Product;
import com.example.shopping.model.form.AdminProductListForm;
import com.example.shopping.repository.AdminProductRepository;
import com.example.shopping.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理者商品Service実装クラス
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminProductServiceImpl implements AdminProductService {

    private final AdminProductRepository adminProductRepository;

    @Override
    public AdminProductListForm getProductList(String searchName, String searchCategory, Pageable pageable) {
        log.debug("Getting product list: searchName={}, searchCategory={}, page={}",
                searchName, searchCategory, pageable.getPageNumber());

        Page<Product> productPage;

        if (searchName != null && !searchName.trim().isEmpty() &&
                searchCategory != null && !searchCategory.trim().isEmpty()) {
            // 商品名とカテゴリで検索
            productPage = adminProductRepository.findByNameContainingIgnoreCaseAndCategoryOrderByIdDesc(
                    searchName.trim(), searchCategory.trim(), pageable);
        } else if (searchName != null && !searchName.trim().isEmpty()) {
            // 商品名で検索
            productPage = adminProductRepository.findByNameContainingIgnoreCaseOrderByIdDesc(
                    searchName.trim(), pageable);
        } else if (searchCategory != null && !searchCategory.trim().isEmpty()) {
            // カテゴリで検索
            productPage = adminProductRepository.findByCategoryOrderByIdDesc(
                    searchCategory.trim(), pageable);
        } else {
            // 全商品取得
            productPage = adminProductRepository.findAllByOrderByIdDesc(pageable);
        }

        List<AdminProductDto> productDtos = productPage.getContent().stream()
                .map(AdminProductDto::fromEntity)
                .collect(Collectors.toList());

        AdminProductListForm form = AdminProductListForm.fromDtoList(productDtos);
        form.setSearchName(searchName);
        form.setSearchCategory(searchCategory);
        form.setCategories(getAvailableCategories());
        form.setCurrentPage(productPage.getNumber());
        form.setTotalPages(productPage.getTotalPages());
        form.setTotalElements(productPage.getTotalElements());
        form.setHasNext(productPage.hasNext());
        form.setHasPrevious(productPage.hasPrevious());

        return form;
    }

    @Override
    public AdminProductDto getProductById(Long productId) {
        log.debug("Getting product by id: {}", productId);
        Product product = adminProductRepository.findById(productId).orElse(null);
        return AdminProductDto.fromEntity(product);
    }

    @Override
    @Transactional
    public boolean saveProduct(AdminProductDto productDto) {
        log.debug("Saving product: id={}, name={}", productDto.getId(), productDto.getName());

        try {
            Product product = productDto.toEntity();
            Product savedProduct = adminProductRepository.save(product);
            log.debug("Product saved successfully: id={}", savedProduct.getId());
            return true;
        } catch (Exception e) {
            log.error("Error saving product", e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteProduct(Long productId) {
        log.debug("Deleting product: id={}", productId);

        try {
            if (adminProductRepository.existsById(productId)) {
                adminProductRepository.deleteById(productId);
                log.debug("Product deleted successfully: id={}", productId);
                return true;
            } else {
                log.warn("Product not found for deletion: id={}", productId);
                return false;
            }
        } catch (Exception e) {
            log.error("Error deleting product", e);
            return false;
        }
    }

    @Override
    public List<String> getAvailableCategories() {
        log.debug("Getting available categories");
        return adminProductRepository.findAll().stream()
                .map(Product::getCategory)
                .filter(category -> category != null && !category.trim().isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminProductDto> getLowStockProducts(Integer threshold) {
        log.debug("Getting low stock products: threshold={}", threshold);
        return adminProductRepository.findByStockQuantityLessThanEqualOrderByIdDesc(threshold)
                .stream()
                .map(AdminProductDto::fromEntity)
                .collect(Collectors.toList());
    }
}