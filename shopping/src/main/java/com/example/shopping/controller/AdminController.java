package com.example.shopping.controller;

import com.example.shopping.service.AdminOrderService;
import com.example.shopping.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
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

        try {
            log.debug("Step 1: Getting order statistics");
            Map<String, Object> orderStatistics = adminOrderService.getOrderStatistics();

            log.debug("Step 2: Getting low stock products");
            var lowStockProducts = adminProductService.getLowStockProducts(10);

            log.debug("Step 3: Adding attributes to model");
            model.addAttribute("orderStatistics", orderStatistics);
            model.addAttribute("lowStockProducts", lowStockProducts);

            log.debug("Step 4: Returning dashboard view");
            return "admin/dashboard";
        } catch (Exception e) {
            log.error("Error in dashboard: {}", e.getMessage(), e);
            log.error("Exception type: {}", e.getClass().getName());
            log.error("Stack trace:", e);
            model.addAttribute("orderStatistics", new HashMap<>());
            model.addAttribute("lowStockProducts", new ArrayList<>());
            return "admin/dashboard";
        }
    }
}