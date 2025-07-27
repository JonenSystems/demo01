package com.example.shopping.controller;

import com.example.shopping.service.AdminOrderService;
import com.example.shopping.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 管理者ダッシュボードController
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminOrderService adminOrderService;
    private final AdminProductService adminProductService;

    /**
     * 管理者ダッシュボード画面表示
     * 
     * @param model Model
     * @return 管理者ダッシュボード画面
     */
    @GetMapping
    public String dashboard(Model model) {
        log.debug("Admin dashboard requested");

        // 注文統計情報を取得
        Map<String, Object> orderStatistics = adminOrderService.getOrderStatistics();

        // 在庫不足商品を取得
        var lowStockProducts = adminProductService.getLowStockProducts(10);

        model.addAttribute("orderStatistics", orderStatistics);
        model.addAttribute("lowStockProducts", lowStockProducts);

        return "admin/dashboard";
    }
}