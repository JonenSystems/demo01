package com.example.shopping.controller;

import com.example.shopping.common.SessionCart;
import com.example.shopping.model.dto.UserProductDto;
import com.example.shopping.model.form.UserProductListForm;
import com.example.shopping.service.UserProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

import java.util.List;

/**
 * ユーザー商品Controller
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class UserProductController {

    private final UserProductService userProductService;
    private final SessionCart sessionCart;

    /**
     * 商品一覧画面表示
     * 
     * @param category カテゴリ（オプション）
     * @param model    Model
     * @return 商品一覧画面
     */
    @GetMapping({ "/", "/products" })
    public String productList(@RequestParam(value = "category", required = false) String category,
            Model model, HttpSession session) {
        log.debug("Product list requested with category: {}", category);

        List<UserProductDto> productDtos;
        if (category != null && !category.trim().isEmpty()) {
            productDtos = userProductService.getProductsByCategory(category);
        } else {
            productDtos = userProductService.getAllProducts();
        }

        // DTOリストからFormを作成
        UserProductListForm productListForm = new UserProductListForm();
        productListForm.setProducts(productDtos);

        // カート情報を追加
        model.addAttribute("cartItemCount", sessionCart.getCartItemCount(session));
        model.addAttribute("productListForm", productListForm);
        return "product-list";
    }

    /**
     * カテゴリ別商品一覧画面表示
     * 
     * @param category カテゴリ
     * @param model    Model
     * @return 商品一覧画面
     */
    @GetMapping("/products/category/{category}")
    public String productListByCategory(@PathVariable String category, Model model, HttpSession session) {
        log.debug("Product list by category requested: {}", category);

        List<UserProductDto> productDtos = userProductService.getProductsByCategory(category);

        // DTOリストからFormを作成
        UserProductListForm productListForm = new UserProductListForm();
        productListForm.setProducts(productDtos);
        productListForm.setSelectedCategory(category);

        // カート情報を追加
        model.addAttribute("cartItemCount", sessionCart.getCartItemCount(session));
        model.addAttribute("productListForm", productListForm);
        return "product-list";
    }
}