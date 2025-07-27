package com.example.shopping.controller;

import com.example.shopping.common.SessionCart;
import com.example.shopping.model.dto.UserCartItemDto;
import com.example.shopping.service.UserCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ユーザーカートController
 */
@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class UserCartController {

    private final UserCartService userCartService;
    private final SessionCart sessionCart;

    /**
     * カート画面表示
     * 
     * @param session HttpSession
     * @param model   Model
     * @return カート画面
     */
    @GetMapping
    public String cart(HttpSession session, Model model) {
        log.debug("Cart page requested");

        // セッションからカートアイテムを取得
        List<UserCartItemDto> cartItems = sessionCart.getCartItems(session);

        // 商品情報を設定
        cartItems = userCartService.enrichCartItemsWithProductInfo(cartItems);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", sessionCart.getCartTotal(session));
        model.addAttribute("cartItemCount", sessionCart.getCartItemCount(session));

        return "user/cart";
    }

    /**
     * カートに商品を追加（Ajax）
     * 
     * @param productId 商品ID
     * @param quantity  数量
     * @param session   HttpSession
     * @return JSONレスポンス
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(@RequestParam Long productId,
            @RequestParam(defaultValue = "1") Integer quantity,
            HttpSession session) {
        log.debug("Adding to cart via Ajax: productId={}, quantity={}", productId, quantity);

        Map<String, Object> response = new HashMap<>();

        // ビジネスロジックの検証
        boolean validationSuccess = userCartService.addToCart(productId, quantity);

        if (validationSuccess) {
            // セッションに追加
            sessionCart.addToCart(productId, quantity, session);

            response.put("success", true);
            response.put("message", "カートに追加しました");
            response.put("cartItemCount", sessionCart.getCartItemCount(session));
            response.put("cartTotal", sessionCart.getCartTotal(session));
        } else {
            response.put("success", false);
            response.put("message", "カートに追加できませんでした");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * カートから商品を削除
     * 
     * @param productId          商品ID
     * @param session            HttpSession
     * @param redirectAttributes RedirectAttributes
     * @return カート画面にリダイレクト
     */
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long productId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("Removing from cart: productId={}", productId);

        // ビジネスロジックの検証
        boolean validationSuccess = userCartService.removeFromCart(productId);

        if (validationSuccess) {
            // セッションから削除
            sessionCart.removeFromCart(productId, session);
            redirectAttributes.addFlashAttribute("message", "商品をカートから削除しました");
        } else {
            redirectAttributes.addFlashAttribute("error", "商品の削除に失敗しました");
        }

        return "redirect:/cart";
    }

    /**
     * カート内の商品数量を更新
     * 
     * @param productId          商品ID
     * @param quantity           新しい数量
     * @param session            HttpSession
     * @param redirectAttributes RedirectAttributes
     * @return カート画面にリダイレクト
     */
    @PostMapping("/update")
    public String updateCartItemQuantity(@RequestParam Long productId,
            @RequestParam Integer quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        log.debug("Updating cart item quantity: productId={}, quantity={}", productId, quantity);

        // ビジネスロジックの検証
        boolean validationSuccess = userCartService.updateCartItemQuantity(productId, quantity);

        if (validationSuccess) {
            // セッションを更新
            sessionCart.updateCartItemQuantity(productId, quantity, session);
            redirectAttributes.addFlashAttribute("message", "カートを更新しました");
        } else {
            redirectAttributes.addFlashAttribute("error", "カートの更新に失敗しました");
        }

        return "redirect:/cart";
    }

    /**
     * カートをクリア
     * 
     * @param session            HttpSession
     * @param redirectAttributes RedirectAttributes
     * @return カート画面にリダイレクト
     */
    @PostMapping("/clear")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        log.debug("Clearing cart");

        // ビジネスロジックの検証
        boolean validationSuccess = userCartService.clearCart();

        if (validationSuccess) {
            // セッションをクリア
            sessionCart.clearCart(session);
            redirectAttributes.addFlashAttribute("message", "カートをクリアしました");
        } else {
            redirectAttributes.addFlashAttribute("error", "カートのクリアに失敗しました");
        }

        return "redirect:/cart";
    }
}