package com.example.shopping.controller;

import com.example.shopping.common.SessionCart;
import com.example.shopping.model.dto.UserCustomerDto;
import com.example.shopping.model.form.UserCustomerForm;
import com.example.shopping.service.UserCustomerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

/**
 * ユーザー顧客Controller
 */
@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
@Slf4j
public class UserCustomerController {

    private final UserCustomerService userCustomerService;
    private final SessionCart sessionCart;

    /**
     * 顧客情報入力画面表示
     * 
     * @param session HttpSession
     * @param model   Model
     * @return 顧客情報入力画面
     */
    @GetMapping("/input")
    public String customerInput(HttpSession session, Model model) {
        log.debug("Customer input page requested");

        // カートが空の場合は商品一覧にリダイレクト
        if (sessionCart.getCartItemCount(session) == 0) {
            log.debug("Cart is empty, redirecting to product list");
            return "redirect:/";
        }

        // セッションから顧客情報を取得（既に入力済みの場合）
        UserCustomerDto customerDto = userCustomerService.getCustomerFromSession();
        UserCustomerForm customerForm;

        if (customerDto != null) {
            customerForm = UserCustomerForm.fromDto(customerDto);
        } else {
            customerForm = new UserCustomerForm();
        }

        model.addAttribute("customerForm", customerForm);
        model.addAttribute("cartItemCount", sessionCart.getCartItemCount(session));
        return "customer-input";
    }

    /**
     * 顧客情報確認画面表示
     * 
     * @param customerForm  顧客Form
     * @param bindingResult バリデーション結果
     * @param session       HttpSession
     * @param model         Model
     * @return 顧客情報確認画面
     */
    @PostMapping("/confirm")
    public String customerConfirm(@Valid @ModelAttribute UserCustomerForm customerForm,
            BindingResult bindingResult,
            HttpSession session, Model model) {
        log.debug("Customer confirmation requested: email={}", customerForm.getEmail());

        // カートが空の場合は商品一覧にリダイレクト
        if (sessionCart.getCartItemCount(session) == 0) {
            log.debug("Cart is empty, redirecting to product list");
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            log.warn("Validation errors in customer form");
            model.addAttribute("cartItemCount", sessionCart.getCartItemCount(session));
            return "customer-input";
        }

        // FormをDTOに変換してServiceに渡す
        UserCustomerDto customerDto = customerForm.toDto();
        boolean success = userCustomerService.saveCustomer(customerDto);

        if (success) {
            // セッションに顧客情報を保存
            userCustomerService.saveCustomerToSession(customerDto);
            log.debug("Customer info saved successfully");

            model.addAttribute("customerForm", customerForm);
            model.addAttribute("cartItemCount", sessionCart.getCartItemCount(session));
            return "customer-confirm";
        } else {
            log.warn("Failed to save customer info");
            model.addAttribute("error", "顧客情報の保存に失敗しました");
            model.addAttribute("cartItemCount", sessionCart.getCartItemCount(session));
            return "customer-input";
        }
    }

    /**
     * 顧客情報入力画面に戻る
     * 
     * @param session HttpSession
     * @param model   Model
     * @return 顧客情報入力画面
     */
    @PostMapping("/back")
    public String customerBack(HttpSession session, Model model) {
        log.debug("Back to customer input requested");

        UserCustomerDto customerDto = userCustomerService.getCustomerFromSession();
        UserCustomerForm customerForm;

        if (customerDto != null) {
            customerForm = UserCustomerForm.fromDto(customerDto);
        } else {
            customerForm = new UserCustomerForm();
        }

        model.addAttribute("customerForm", customerForm);
        model.addAttribute("cartItemCount", sessionCart.getCartItemCount(session));
        return "customer-input";
    }
}