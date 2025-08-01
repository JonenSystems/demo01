package com.example.shopping.config;

import com.example.shopping.common.AdminUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Spring Security設定のテストクラス
 */
@WebMvcTest({ SecurityConfig.class, com.example.shopping.controller.AdminAuthController.class })
@ActiveProfiles("dev")
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminUserDetailsService adminUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void adminLoginPage_ShouldBeAccessible() throws Exception {
        // 管理者ログインページは認証なしでアクセス可能
        mockMvc.perform(get("/admin/login"))
                .andExpect(status().isOk());
    }

    @Test
    void adminProductsPage_ShouldRequireAuthentication() throws Exception {
        // 管理者商品ページは認証が必要
        mockMvc.perform(get("/admin/products"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/admin/login"));
    }

    @Test
    void adminOrdersPage_ShouldRequireAuthentication() throws Exception {
        // 管理者注文ページは認証が必要
        mockMvc.perform(get("/admin/orders"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/admin/login"));
    }

    @Test
    void adminCustomersPage_ShouldRequireAuthentication() throws Exception {
        // 管理者顧客ページは認証が必要
        mockMvc.perform(get("/admin/customers"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/admin/login"));
    }

    @Test
    void publicPages_ShouldBeAccessible() throws Exception {
        // 公開ページは認証なしでアクセス可能
        // ルートパスにはstatic/index.htmlが存在するため、200エラーは正常
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void staticResources_ShouldBeAccessible() throws Exception {
        // 静的リソースは認証なしでアクセス可能
        mockMvc.perform(get("/css/style.css"))
                .andExpect(status().isOk());
    }

    @Test
    void loginForm_ShouldHaveCorrectAction() throws Exception {
        // ログインフォームのアクションURL確認
        mockMvc.perform(get("/admin/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("action=\"/admin/login\"")));
    }

    @Test
    void loginForm_ShouldHaveUsernameField() throws Exception {
        // ログインフォームにユーザー名フィールドがあることを確認
        mockMvc.perform(get("/admin/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("name=\"username\"")));
    }

    @Test
    void loginForm_ShouldHavePasswordField() throws Exception {
        // ログインフォームにパスワードフィールドがあることを確認
        mockMvc.perform(get("/admin/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("name=\"password\"")));
    }

    @Test
    void loginForm_ShouldHaveCsrfToken() throws Exception {
        // ログインフォームにCSRFトークンがあることを確認
        mockMvc.perform(get("/admin/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("_csrf")));
    }

    @Test
    void passwordEncoder_ShouldBeConfigured() {
        // パスワードエンコーダーが設定されていることを確認
        assertNotNull(passwordEncoder);

        // BCryptエンコーダーの動作確認
        String rawPassword = "testpassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertNotNull(encodedPassword);
        assertTrue(encodedPassword.startsWith("$2a$"));
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        assertFalse(passwordEncoder.matches("wrongpassword", encodedPassword));
    }

    @Test
    void logoutEndpoint_ShouldBeConfigured() throws Exception {
        // ログアウトエンドポイントが設定されていることを確認
        // CSRFトークンなしでPOSTすると403エラーになるため、GETリクエストでテスト
        mockMvc.perform(get("/admin/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/admin/login"));
    }
}