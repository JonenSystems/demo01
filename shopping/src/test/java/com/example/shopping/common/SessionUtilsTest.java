package com.example.shopping.common;

import com.example.shopping.model.dto.UserCartItemDto;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * セッション操作共通ユーティリティクラスのテスト
 */
@ExtendWith(MockitoExtension.class)
class SessionUtilsTest {

    @Mock
    private HttpSession session;

    private SessionUtils sessionUtils;

    @BeforeEach
    void setUp() {
        sessionUtils = new SessionUtils();
    }

    @Test
    void UT_Common_0001_getSessionObject_正常系() {
        // セッションに存在するオブジェクトが正しく取得されることを確認
        List<UserCartItemDto> existingCartItems = new ArrayList<>();
        UserCartItemDto item = new UserCartItemDto();
        item.setProductId(1L);
        item.setProductName("商品A");
        existingCartItems.add(item);

        when(session.getAttribute("cart")).thenReturn(existingCartItems);

        List<UserCartItemDto> result = sessionUtils.getSessionObject(session, "cart", new ArrayList<>());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getProductId());
        assertEquals("商品A", result.get(0).getProductName());
    }

    @Test
    void UT_Common_0002_getSessionObject_正常系() {
        // セッションに存在しないオブジェクトがデフォルト値で初期化されることを確認
        when(session.getAttribute("nonexistent")).thenReturn(null);

        List<UserCartItemDto> result = sessionUtils.getSessionObject(session, "nonexistent", new ArrayList<>());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(session).setAttribute("nonexistent", new ArrayList<>());
    }

    @Test
    void UT_Common_0003_getSessionObject_異常系() {
        // セッションが無効な場合にデフォルト値が返されることを確認
        List<UserCartItemDto> result = sessionUtils.getSessionObject(null, "cart", new ArrayList<>());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void UT_Common_0004_setSessionObject_正常系() {
        // オブジェクトが正しくセッションに保存されることを確認
        List<UserCartItemDto> cartItems = new ArrayList<>();
        UserCartItemDto item = new UserCartItemDto();
        item.setProductId(1L);
        cartItems.add(item);

        sessionUtils.setSessionObject(session, "cart", cartItems);

        verify(session).setAttribute("cart", cartItems);
    }

    @Test
    void UT_Common_0005_setSessionObject_異常系() {
        // セッションが無効な場合にログが出力されることを確認
        List<UserCartItemDto> cartItems = new ArrayList<>();

        sessionUtils.setSessionObject(null, "cart", cartItems);

        // ログ出力の確認は難しいため、例外が発生しないことを確認
        assertDoesNotThrow(() -> sessionUtils.setSessionObject(null, "cart", cartItems));
    }

    @Test
    void UT_Common_0006_removeSessionObject_正常系() {
        // セッションからオブジェクトが正しく削除されることを確認
        sessionUtils.removeSessionObject(session, "cart");

        verify(session).removeAttribute("cart");
    }

    @Test
    void UT_Common_0007_removeSessionObject_異常系() {
        // セッションが無効な場合にログが出力されることを確認
        sessionUtils.removeSessionObject(null, "cart");

        // ログ出力の確認は難しいため、例外が発生しないことを確認
        assertDoesNotThrow(() -> sessionUtils.removeSessionObject(null, "cart"));
    }

    @Test
    void UT_Common_0008_invalidateSession_正常系() {
        // セッションが正しく無効化されることを確認
        sessionUtils.invalidateSession(session);

        verify(session).invalidate();
    }

    @Test
    void UT_Common_0009_invalidateSession_異常系() {
        // セッションが無効な場合にログが出力されることを確認
        sessionUtils.invalidateSession(null);

        // ログ出力の確認は難しいため、例外が発生しないことを確認
        assertDoesNotThrow(() -> sessionUtils.invalidateSession(null));
    }
}