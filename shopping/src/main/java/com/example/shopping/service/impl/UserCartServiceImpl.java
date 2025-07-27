package com.example.shopping.service.impl;

import com.example.shopping.model.dto.UserCartItemDto;
import com.example.shopping.model.entity.Product;
import com.example.shopping.repository.UserCartRepository;
import com.example.shopping.service.UserCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * ユーザーカートService実装クラス
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserCartServiceImpl implements UserCartService {

    private final UserCartRepository userCartRepository;

    @Override
    public List<UserCartItemDto> getCartItems() {
        log.debug("Getting cart items");
        // セッション操作はController層で行うため、ここでは商品情報の設定のみ
        return null; // Controller層で実装
    }

    @Override
    public boolean addToCart(Long productId, Integer quantity) {
        log.debug("Adding to cart: productId={}, quantity={}", productId, quantity);

        if (productId == null || quantity == null || quantity <= 0) {
            log.warn("Invalid parameters: productId={}, quantity={}", productId, quantity);
            return false;
        }

        // 商品の存在確認と在庫チェック
        Optional<Product> productOpt = userCartRepository.findByIdAndStockQuantityGreaterThan(productId, 0);
        if (productOpt.isEmpty()) {
            log.warn("Product not found or out of stock: productId={}", productId);
            return false;
        }

        Product product = productOpt.get();
        if (product.getStockQuantity() < quantity) {
            log.warn("Insufficient stock: productId={}, requested={}, available={}",
                    productId, quantity, product.getStockQuantity());
            return false;
        }

        log.debug("Product validation successful: productId={}, quantity={}", productId, quantity);
        return true;
    }

    @Override
    public boolean removeFromCart(Long productId) {
        log.debug("Removing from cart: productId={}", productId);

        if (productId == null) {
            log.warn("Invalid productId: null");
            return false;
        }

        log.debug("Cart item removal validation successful: productId={}", productId);
        return true;
    }

    @Override
    public boolean updateCartItemQuantity(Long productId, Integer quantity) {
        log.debug("Updating cart item quantity: productId={}, quantity={}", productId, quantity);

        if (productId == null || quantity == null) {
            log.warn("Invalid parameters: productId={}, quantity={}", productId, quantity);
            return false;
        }

        if (quantity <= 0) {
            log.debug("Quantity is zero or negative, will remove item: productId={}", productId);
            return true;
        }

        // 在庫チェック
        Optional<Product> productOpt = userCartRepository.findByIdAndStockQuantityGreaterThan(productId, 0);
        if (productOpt.isEmpty()) {
            log.warn("Product not found or out of stock: productId={}", productId);
            return false;
        }

        Product product = productOpt.get();
        if (product.getStockQuantity() < quantity) {
            log.warn("Insufficient stock: productId={}, requested={}, available={}",
                    productId, quantity, product.getStockQuantity());
            return false;
        }

        log.debug("Cart item quantity update validation successful: productId={}, quantity={}", productId, quantity);
        return true;
    }

    @Override
    public boolean clearCart() {
        log.debug("Clearing cart");
        return true;
    }

    @Override
    public int getCartItemCount() {
        log.debug("Getting cart item count");
        return 0; // Controller層で実装
    }

    @Override
    public BigDecimal getCartTotal() {
        log.debug("Getting cart total");
        return BigDecimal.ZERO; // Controller層で実装
    }

    @Override
    public boolean isCartEmpty() {
        log.debug("Checking if cart is empty");
        return true; // Controller層で実装
    }

    /**
     * カートアイテムに商品情報を設定
     * 
     * @param cartItems カートアイテムリスト
     * @return 商品情報が設定されたカートアイテムリスト
     */
    public List<UserCartItemDto> enrichCartItemsWithProductInfo(List<UserCartItemDto> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return cartItems;
        }

        for (UserCartItemDto item : cartItems) {
            Optional<Product> productOpt = userCartRepository.findByIdAndStockQuantityGreaterThan(item.getProductId(),
                    0);
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                item.setProductName(product.getName());
                item.setProductImagePath(product.getImagePath());
                item.setUnitPrice(product.getPrice());
                item.calculateSubtotal();
            }
        }

        return cartItems;
    }
}