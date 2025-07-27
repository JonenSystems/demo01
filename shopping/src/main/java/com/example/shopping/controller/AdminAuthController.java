package com.example.shopping.controller;

import com.example.shopping.model.form.AdminLoginForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 管理者認証コントローラー
 */
@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    /**
     * ログイン画面を表示する
     * 
     * @param model   モデル
     * @param error   エラーパラメータ
     * @param logout  ログアウトパラメータ
     * @param expired セッション期限切れパラメータ
     * @return ログイン画面のビュー名
     */
    @GetMapping("/login")
    public String showLoginForm(Model model,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "expired", required = false) String expired) {

        // エラーメッセージの設定
        if (error != null) {
            model.addAttribute("errorMessage", "ユーザー名またはパスワードが正しくありません。");
        }

        // ログアウトメッセージの設定
        if (logout != null) {
            model.addAttribute("successMessage", "ログアウトしました。");
        }

        // セッション期限切れメッセージの設定
        if (expired != null) {
            model.addAttribute("errorMessage", "セッションが期限切れです。再度ログインしてください。");
        }

        // ログインフォームの初期化
        model.addAttribute("loginForm", new AdminLoginForm());

        return "admin/login";
    }
}