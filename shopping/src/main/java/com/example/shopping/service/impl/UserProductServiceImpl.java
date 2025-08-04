package com.example.shopping.service.impl;

import com.example.shopping.model.dto.UserProductDto;
import com.example.shopping.model.entity.Product;
import com.example.shopping.repository.UserProductRepository;
import com.example.shopping.service.UserProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ユーザー商品サービス実装
 */
@Service
@RequiredArgsConstructor
public class UserProductServiceImpl implements UserProductService {

    private final UserProductRepository userProductRepository;

    @Override
    public List<UserProductDto> getAllProducts() {
        List<Product> products = userProductRepository.findAvailableProducts();
        return products.stream()
                .map(UserProductDto::fromEntity)
                .toList();
    }

    @Override
    public List<UserProductDto> getProductsByCategory(String category) {
        List<Product> products = userProductRepository.findAvailableProductsByCategory(category);
        return products.stream()
                .map(UserProductDto::fromEntity)
                .toList();
    }
}