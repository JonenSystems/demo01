package com.example.shopping.controller;

import com.example.shopping.model.dto.AdminOrderDto;
import com.example.shopping.model.form.AdminOrderListForm;
import com.example.shopping.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

/**
 * 管理者注文Controller
 */
@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@Slf4j
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    /**
     * 注文一覧画面表示
     * 
     * @param searchOrderNumber  注文番号検索
     * @param searchCustomerName 顧客名検索
     * @param searchStatus       ステータス検索
     * @param page               ページ番号
     * @param model              Model
     * @return 注文一覧画面
     */
    @GetMapping
    public String orderList(@RequestParam(value = "searchOrderNumber", required = false) String searchOrderNumber,
            @RequestParam(value = "searchCustomerName", required = false) String searchCustomerName,
            @RequestParam(value = "searchStatus", required = false) String searchStatus,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {
        log.debug("Admin order list requested: searchOrderNumber={}, searchCustomerName={}, searchStatus={}, page={}",
                searchOrderNumber, searchCustomerName, searchStatus, page);

        Pageable pageable = PageRequest.of(page, 10); // 1ページ10件
        AdminOrderListForm orderListForm = adminOrderService.getOrderList(searchOrderNumber, searchCustomerName,
                searchStatus, pageable);

        // 統計情報を取得
        Map<String, Object> statistics = adminOrderService.getOrderStatistics();

        model.addAttribute("orderListForm", orderListForm);
        model.addAttribute("statistics", statistics);
        return "admin/order-list";
    }

    /**
     * 注文詳細画面表示
     * 
     * @param orderId 注文ID
     * @param model   Model
     * @return 注文詳細画面
     */
    @GetMapping("/{orderId}")
    public String orderDetail(@PathVariable Long orderId, Model model) {
        log.debug("Admin order detail requested: orderId={}", orderId);

        AdminOrderDto orderDto = adminOrderService.getOrderById(orderId);
        if (orderDto == null) {
            log.warn("Order not found: orderId={}", orderId);
            return "redirect:/admin/orders";
        }

        model.addAttribute("orderDto", orderDto);
        model.addAttribute("statusOptions", adminOrderService.getAvailableStatuses());

        return "admin/order-detail";
    }

    /**
     * 注文ステータス更新処理
     * 
     * @param orderId            注文ID
     * @param status             新しいステータス
     * @param redirectAttributes RedirectAttributes
     * @return 注文詳細画面にリダイレクト
     */
    @PostMapping("/{orderId}/status")
    public String updateOrderStatus(@PathVariable Long orderId,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {
        log.debug("Admin order status update requested: orderId={}, status={}", orderId, status);

        boolean success = adminOrderService.updateOrderStatus(orderId, status);

        if (success) {
            redirectAttributes.addFlashAttribute("message", "注文ステータスを更新しました");
        } else {
            redirectAttributes.addFlashAttribute("error", "注文ステータスの更新に失敗しました");
        }

        return "redirect:/admin/orders/" + orderId;
    }
}