package com.example.shopping.common.impl;

import com.example.shopping.common.SessionUtils;
import com.example.shopping.model.dto.UserCartItemDto;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * カートセッション管理実装クラスのテスト
 */
@ExtendWith(MockitoExtension.class)
class SessionCartImplTest {

    @Mock
    private HttpSession session;

    @Mock
    private SessionUtils sessionUtils;

    private SessionCartImpl sessionCart;

    @BeforeEach
    void setUp() {
        sessionCart = new SessionCartImpl(sessionUtils);
    }

    @Test
    void UT_Common_0010_getCartItems_正常系() {
        // カート内の商品一覧が正しく取得されることを確認
        ConcurrentHashMap<Long, UserCartItemDto> cartMap = new ConcurrentHashMap<>();
        UserCartItemDto item1 = new UserCartItemDto();
        item1.setProductId(1L);
        item1.setProductName("商品A");
        item1.setQuantity(2);
        cartMap.put(1L, item1);

        UserCartItemDto item2 = new UserCartItemDto();
        item2.setProductId(2L);
        item2.setProductName("商品B");
        item2.setQuantity(1);
        cartMap.put(2L, item2);

        when(sessionUtils.getSessionObject(eq(session), eq(SessionUtils.SessionKeys.CART_SESSION), any()))
                .thenReturn(cartMap);

        List<UserCartItemDto> result = sessionCart.getCartItems(session);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("商品A", result.get(0).getProductName());
        assertEquals("商品B", result.get(1).getProductName());
    }

    @Test
    void UT_Common_0011_getCartItems_正常系() {
        // カートが空の場合に空のリストが返されることを確認
        when(sessionUtils.getSessionObject(eq(session), eq(SessionUtils.SessionKeys.CART_SESSION), any()))
                .thenReturn(new ConcurrentHashMap<>());

        List<UserCartItemDto> result = sessionCart.getCartItems(session);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void UT_Common_0012_addToCart_正常系() {
        // 商品が正しくカートに追加されることを確認
        ConcurrentHashMap<Long, UserCartItemDto> cartMap = new ConcurrentHashMap<>();
        when(sessionUtils.getSessionObject(eq(session), eq(SessionUtils.SessionKeys.CART_SESSION), any()))
                .thenReturn(cartMap);

        sessionCart.addToCart(1L, 2, session);

        verify(sessionUtils).setSessionObject(eq(session), eq(SessionUtils.SessionKeys.CART_SESSION), any());
        assertEquals(1, cartMap.size());
        assertNotNull(cartMap.get(1L));
        assertEquals(2, cartMap.get(1L).getQuantity());
    }

    @Test
    void UT_Common_0013_addToCart_異常系() {
        // 無効な数量でログが出力されることを確認
        sessionCart.addToCart(1L, 0, session);

        verify(sessionUtils, never()).setSessionObject(any(), any(), any());
    }

    @Test
    void UT_Common_0014_removeFromCart_正常系() {
        // 商品が正しくカートから削除されることを確認
        ConcurrentHashMap<Long, UserCartItemDto> cartMap = new ConcurrentHashMap<>();
        UserCartItemDto item = new UserCartItemDto();
        item.setProductId(1L);
        item.setProductName("商品A");
        cartMap.put(1L, item);

        when(sessionUtils.getSessionObject(eq(session), eq(SessionUtils.SessionKeys.CART_SESSION), any()))
                .thenReturn(cartMap);

        sessionCart.removeFromCart(1L, session);

        verify(sessionUtils).setSessionObject(eq(session), eq(SessionUtils.SessionKeys.CART_SESSION), any());
        assertTrue(cartMap.isEmpty());
    }

    @Test
    void UT_Common_0015_clearCart_正常系() {
        // カートが正しくクリアされることを確認
        sessionCart.clearCart(session);

        verify(sessionUtils).removeSessionObject(session, SessionUtils.SessionKeys.CART_SESSION);
    }

    @Test
    void UT_Common_0016_getCartItemCount_正常系() {
        // カート内の商品数が正しく取得されることを確認
        ConcurrentHashMap<Long, UserCartItemDto> cartMap = new ConcurrentHashMap<>();
        UserCartItemDto item1 = new UserCartItemDto();
        item1.setProductId(1L);
        item1.setQuantity(2);
        cartMap.put(1L, item1);

        UserCartItemDto item2 = new UserCartItemDto();
        item2.setProductId(2L);
        item2.setQuantity(1);
        cartMap.put(2L, item2);

        when(sessionUtils.getSessionObject(eq(session), eq(SessionUtils.SessionKeys.CART_SESSION), any()))
                .thenReturn(cartMap);

        int result = sessionCart.getCartItemCount(session);

        assertEquals(3, result);
    }

    @Test
    void UT_Common_0017_getCartItemCount_正常系() {
        // カートが空の場合に0が返されることを確認
        when(sessionUtils.getSessionObject(eq(session), eq(SessionUtils.SessionKeys.CART_SESSION), any()))
                .thenReturn(new ConcurrentHashMap<>());

        int result = sessionCart.getCartItemCount(session);

        assertEquals(0, result);
    }

    @Test
    void UT_Common_0018_getCartTotal_正常系() {
        // カート内の合計金額が正しく取得されることを確認
        ConcurrentHashMap<Long, UserCartItemDto> cartMap = new ConcurrentHashMap<>();
        UserCartItemDto item1 = new UserCartItemDto();
        item1.setProductId(1L);
        item1.setUnitPrice(new BigDecimal("1000"));
        item1.setQuantity(2);
        item1.setSubtotal(new BigDecimal("2000"));
        cartMap.put(1L, item1);

        UserCartItemDto item2 = new UserCartItemDto();
        item2.setProductId(2L);
        item2.setUnitPrice(new BigDecimal("1500"));
        item2.setQuantity(2);
        item2.setSubtotal(new BigDecimal("3000"));
        cartMap.put(2L, item2);

        when(sessionUtils.getSessionObject(eq(session), eq(SessionUtils.SessionKeys.CART_SESSION), any()))
                .thenReturn(cartMap);

        BigDecimal result = sessionCart.getCartTotal(session);

        assertEquals(new BigDecimal("5000"), result);
    }

    @Test
    void UT_Common_0019_getCartTotal_正常系() {
        // カートが空の場合に0が返されることを確認
        when(sessionUtils.getSessionObject(eq(session), eq(SessionUtils.SessionKeys.CART_SESSION), any()))
                .thenReturn(new ConcurrentHashMap<>());

        BigDecimal result = sessionCart.getCartTotal(session);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void UT_Common_0020_updateCartItemQuantity_正常系() {
        // 商品の数量が正しく更新されることを確認
        ConcurrentHashMap<Long, UserCartItemDto> cartMap = new ConcurrentHashMap<>();
        UserCartItemDto item = new UserCartItemDto();
        item.setProductId(1L);
        item.setQuantity(2);
        cartMap.put(1L, item);

        when(sessionUtils.getSessionObject(eq(session), eq(SessionUtils.SessionKeys.CART_SESSION), any()))
                .thenReturn(cartMap);

        sessionCart.updateCartItemQuantity(1L, 5, session);

        verify(sessionUtils).setSessionObject(eq(session), eq(SessionUtils.SessionKeys.CART_SESSION), any());
        assertEquals(5, cartMap.get(1L).getQuantity());
    }

    @Test
    void UT_Common_0021_updateCartItemQuantity_異常系() {
        // 無効な数量でログが出力されることを確認
        ConcurrentHashMap<Long, UserCartItemDto> cartMap = new ConcurrentHashMap<>();
        when(sessionUtils.getSessionObject(eq(session), eq(SessionUtils.SessionKeys.CART_SESSION), any()))
                .thenReturn(cartMap);

        sessionCart.updateCartItemQuantity(1L, -1, session);

        verify(sessionUtils, never()).setSessionObject(any(), any(), any());
    }

    @Test
    void UT_Common_0022_isCartEmpty_正常系() {
        // カートが空の場合にtrueが返されることを確認
        when(sessionUtils.getSessionObject(eq(session), eq(SessionUtils.SessionKeys.CART_SESSION), any()))
                .thenReturn(new ConcurrentHashMap<>());

        boolean result = sessionCart.isCartEmpty(session);

        assertTrue(result);
    }

    @Test
    void UT_Common_0023_isCartEmpty_正常系() {
        // カートに商品がある場合にfalseが返されることを確認
        ConcurrentHashMap<Long, UserCartItemDto> cartMap = new ConcurrentHashMap<>();
        UserCartItemDto item = new UserCartItemDto();
        item.setProductId(1L);
        cartMap.put(1L, item);

        when(sessionUtils.getSessionObject(eq(session), eq(SessionUtils.SessionKeys.CART_SESSION), any()))
                .thenReturn(cartMap);

        boolean result = sessionCart.isCartEmpty(session);

        assertFalse(result);
    }
}