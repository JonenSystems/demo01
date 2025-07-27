package com.example.shopping.common.impl;

import com.example.shopping.common.SessionCart;
import com.example.shopping.common.SessionUtils;
import com.example.shopping.model.dto.UserCartItemDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * カートセッション管理実装クラス
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SessionCartImpl implements SessionCart {

    private final SessionUtils sessionUtils;

    @Override
    public List<UserCartItemDto> getCartItems(HttpSession session) {
        Map<Long, UserCartItemDto> cartMap = sessionUtils.getSessionObject(
                session,
                SessionUtils.SessionKeys.CART_SESSION,
                new ConcurrentHashMap<Long, UserCartItemDto>());
        return new ArrayList<>(cartMap.values());
    }

    @Override
    public void addToCart(Long productId, Integer quantity, HttpSession session) {
        if (productId == null || quantity == null || quantity <= 0) {
            log.warn("Invalid parameters: productId={}, quantity={}", productId, quantity);
            return;
        }

        Map<Long, UserCartItemDto> cartMap = sessionUtils.getSessionObject(
                session,
                SessionUtils.SessionKeys.CART_SESSION,
                new ConcurrentHashMap<Long, UserCartItemDto>());

        UserCartItemDto existingItem = cartMap.get(productId);
        if (existingItem != null) {
            // 既存アイテムの数量を更新
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.calculateSubtotal();
            log.debug("Updated cart item: productId={}, newQuantity={}", productId, existingItem.getQuantity());
        } else {
            // 新規アイテムを追加（商品情報は後でService層で設定）
            UserCartItemDto newItem = new UserCartItemDto();
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            cartMap.put(productId, newItem);
            log.debug("Added new cart item: productId={}, quantity={}", productId, quantity);
        }

        sessionUtils.setSessionObject(session, SessionUtils.SessionKeys.CART_SESSION, cartMap);
    }

    @Override
    public void removeFromCart(Long productId, HttpSession session) {
        if (productId == null) {
            log.warn("ProductId is null");
            return;
        }

        Map<Long, UserCartItemDto> cartMap = sessionUtils.getSessionObject(
                session,
                SessionUtils.SessionKeys.CART_SESSION,
                new ConcurrentHashMap<Long, UserCartItemDto>());

        UserCartItemDto removedItem = cartMap.remove(productId);
        if (removedItem != null) {
            log.debug("Removed cart item: productId={}", productId);
            sessionUtils.setSessionObject(session, SessionUtils.SessionKeys.CART_SESSION, cartMap);
        } else {
            log.warn("Cart item not found for removal: productId={}", productId);
        }
    }

    @Override
    public void clearCart(HttpSession session) {
        sessionUtils.removeSessionObject(session, SessionUtils.SessionKeys.CART_SESSION);
        log.debug("Cart cleared");
    }

    @Override
    public int getCartItemCount(HttpSession session) {
        List<UserCartItemDto> cartItems = getCartItems(session);
        return cartItems.stream()
                .mapToInt(UserCartItemDto::getQuantity)
                .sum();
    }

    @Override
    public BigDecimal getCartTotal(HttpSession session) {
        List<UserCartItemDto> cartItems = getCartItems(session);
        return cartItems.stream()
                .map(UserCartItemDto::getSubtotal)
                .filter(subtotal -> subtotal != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void updateCartItemQuantity(Long productId, Integer quantity, HttpSession session) {
        if (productId == null || quantity == null) {
            log.warn("Invalid parameters: productId={}, quantity={}", productId, quantity);
            return;
        }

        Map<Long, UserCartItemDto> cartMap = sessionUtils.getSessionObject(
                session,
                SessionUtils.SessionKeys.CART_SESSION,
                new ConcurrentHashMap<Long, UserCartItemDto>());

        UserCartItemDto existingItem = cartMap.get(productId);
        if (existingItem != null) {
            if (quantity <= 0) {
                // 数量が0以下の場合は削除
                cartMap.remove(productId);
                log.debug("Removed cart item due to zero quantity: productId={}", productId);
            } else {
                // 数量を更新
                existingItem.setQuantity(quantity);
                existingItem.calculateSubtotal();
                log.debug("Updated cart item quantity: productId={}, newQuantity={}", productId, quantity);
            }
            sessionUtils.setSessionObject(session, SessionUtils.SessionKeys.CART_SESSION, cartMap);
        } else {
            log.warn("Cart item not found for quantity update: productId={}", productId);
        }
    }

    @Override
    public boolean isCartEmpty(HttpSession session) {
        List<UserCartItemDto> cartItems = getCartItems(session);
        return cartItems.isEmpty();
    }
}