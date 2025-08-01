package com.example.shopping.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.example.shopping.common.AdminUserDetailsService;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 管理者認証コントローラーのテストクラス
 */
@WebMvcTest(AdminAuthController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
@ActiveProfiles("dev")
class AdminAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminUserDetailsService adminUserDetailsService;

    @Test
    void showLoginForm_ShouldDisplayLoginPage() throws Exception {
        // ログイン画面の表示テスト
        mockMvc.perform(get("/admin/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/login"))
                .andExpect(model().attributeExists("loginForm"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("管理者ログイン")));
    }

    @Test
    void showLoginForm_WithErrorParameter_ShouldDisplayErrorMessage() throws Exception {
        // エラーパラメータ付きでのログイン画面表示テスト
        mockMvc.perform(get("/admin/login")
                .param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/login"))
                .andExpect(model().attribute("errorMessage", "ユーザー名またはパスワードが正しくありません。"))
                .andExpect(model().attributeExists("loginForm"));
    }

    @Test
    void showLoginForm_WithLogoutParameter_ShouldDisplaySuccessMessage() throws Exception {
        // ログアウトパラメータ付きでのログイン画面表示テスト
        mockMvc.perform(get("/admin/login")
                .param("logout", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/login"))
                .andExpect(model().attribute("successMessage", "ログアウトしました。"))
                .andExpect(model().attributeExists("loginForm"));
    }

    @Test
    void showLoginForm_WithExpiredParameter_ShouldDisplayExpiredMessage() throws Exception {
        // セッション期限切れパラメータ付きでのログイン画面表示テスト
        mockMvc.perform(get("/admin/login")
                .param("expired", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/login"))
                .andExpect(model().attribute("errorMessage", "セッションが期限切れです。再度ログインしてください。"))
                .andExpect(model().attributeExists("loginForm"));
    }

    @Test
    void showLoginForm_WithoutParameters_ShouldNotHaveMessages() throws Exception {
        // パラメータなしでのログイン画面表示テスト
        mockMvc.perform(get("/admin/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/login"))
                .andExpect(model().attributeDoesNotExist("errorMessage"))
                .andExpect(model().attributeDoesNotExist("successMessage"))
                .andExpect(model().attributeExists("loginForm"));
    }

    @Test
    void showLoginForm_ShouldHaveCorrectFormStructure() throws Exception {
        // フォーム構造の確認テスト
        mockMvc.perform(get("/admin/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("username")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("password")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("ログイン")));
    }
}