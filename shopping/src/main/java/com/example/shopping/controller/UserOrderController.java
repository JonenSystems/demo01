package com.example.shopping.controller;

import com.example.shopping.common.SessionCart;
import com.example.shopping.common.SessionCustomer;
import com.example.shopping.common.SessionOrder;
import com.example.shopping.model.dto.UserOrderDto;
import com.example.shopping.model.dto.UserCartItemDto;
import com.example.shopping.model.dto.UserCustomerDto;
import com.example.shopping.model.form.UserCustomerForm;
import com.example.shopping.model.form.UserOrderConfirmForm;
import com.example.shopping.service.UserCartService;
import com.example.shopping.service.UserOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * ユーザー注文Controller
 */
@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class UserOrderController {

    private final UserOrderService userOrderService;
    private final UserCartService userCartService;
    private final SessionCart sessionCart;
    private final SessionCustomer sessionCustomer;
    private final SessionOrder sessionOrder;

    /**
     * 注文確認画面表示
     * 
     * @param session HttpSession
     * @param model   Model
     * @return 注文確認画面
     */
    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        log.debug("Checkout page requested");

        // カートが空の場合は商品一覧にリダイレクト
        if (sessionCart.isCartEmpty(session)) {
            log.warn("Cart is empty, redirecting to product list");
            return "redirect:/";
        }

        // セッションからカートアイテムと顧客情報を取得
        List<UserCartItemDto> cartItems = sessionCart.getCartItems(session);
        cartItems = userCartService.enrichCartItemsWithProductInfo(cartItems);

        UserCustomerForm customerForm = sessionCustomer.getCustomerForm(session);
        UserCustomerDto customerDto = customerForm != null ? customerForm.toDto() : null;

        // 注文確認データを作成
        UserOrderDto orderDto = userOrderService.createOrderConfirmData(cartItems, customerDto);

        // orderDtoがnullの場合は空のフォームを作成
        UserOrderConfirmForm orderConfirmForm = new UserOrderConfirmForm();
        if (orderDto != null) {
            orderConfirmForm.setCartItems(orderDto.getOrderItems());
            orderConfirmForm.setTotalAmount(orderDto.getTotalAmount());
            orderConfirmForm.setCustomer(orderDto.getCustomer());
        } else {
            orderConfirmForm.setCartItems(cartItems);
            orderConfirmForm.setTotalAmount(cartItems.stream()
                    .map(UserCartItemDto::getSubtotal)
                    .filter(subtotal -> subtotal != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        model.addAttribute("orderConfirmForm", orderConfirmForm);
        model.addAttribute("cartItemCount", sessionCart.getCartItemCount(session));

        return "user/order-confirm";
    }

    /**
     * 顧客情報入力画面表示
     * 
     * @param session HttpSession
     * @param model   Model
     * @return 顧客情報入力画面
     */
    @GetMapping("/customer-input")
    public String customerInput(HttpSession session, Model model) {
        log.debug("Customer input page requested");

        // カートが空の場合は商品一覧にリダイレクト
        if (sessionCart.isCartEmpty(session)) {
            log.warn("Cart is empty, redirecting to product list");
            return "redirect:/";
        }

        UserCustomerForm customerForm = new UserCustomerForm();
        model.addAttribute("customerForm", customerForm);
        model.addAttribute("cartItemCount", sessionCart.getCartItemCount(session));

        return "user/customer-input";
    }

    /**
     * 顧客情報保存処理
     * 
     * @param customerForm  顧客情報Form
     * @param bindingResult バリデーション結果
     * @param session       HttpSession
     * @param model         Model
     * @return 注文確認画面にリダイレクト
     */
    @PostMapping("/customer-input")
    public String saveCustomerInfo(@Valid @ModelAttribute UserCustomerForm customerForm,
            BindingResult bindingResult,
            HttpSession session,
            Model model) {
        log.debug("Customer info save requested: name={}, email={}",
                customerForm.getName(), customerForm.getEmail());

        if (bindingResult.hasErrors()) {
            log.warn("Validation errors in customer form");
            model.addAttribute("cartItemCount", sessionCart.getCartItemCount(session));
            return "user/customer-input";
        }

        // FormをDTOに変換してServiceに渡す
        UserCustomerDto customerDto = customerForm.toDto();
        boolean success = userOrderService.saveCustomerInfo(customerDto);

        if (success) {
            // セッションに顧客情報を保存
            sessionCustomer.setCustomerForm(customerForm, session);
            log.debug("Customer info saved successfully");
            return "redirect:/order/checkout";
        } else {
            log.warn("Failed to save customer info");
            model.addAttribute("error", "顧客情報の保存に失敗しました");
            model.addAttribute("cartItemCount", sessionCart.getCartItemCount(session));
            return "user/customer-input";
        }
    }

    /**
     * 注文確定処理
     * 
     * @param session            HttpSession
     * @param redirectAttributes RedirectAttributes
     * @return 注文完了画面にリダイレクト
     */
    @PostMapping("/confirm")
    public String confirmOrder(HttpSession session, RedirectAttributes redirectAttributes) {
        log.debug("Order confirmation requested");

        // カートが空の場合は商品一覧にリダイレクト
        if (sessionCart.isCartEmpty(session)) {
            log.warn("Cart is empty, redirecting to product list");
            return "redirect:/";
        }

        // セッションからカートアイテムと顧客情報を取得
        List<UserCartItemDto> cartItems = sessionCart.getCartItems(session);
        cartItems = userCartService.enrichCartItemsWithProductInfo(cartItems);

        UserCustomerForm customerForm = sessionCustomer.getCustomerForm(session);
        log.debug("Customer form from session: {}", customerForm != null ? "found" : "null");
        if (customerForm == null) {
            log.warn("Customer info not found, redirecting to customer input");
            return "redirect:/order/customer-input";
        }

        // FormをDTOに変換してServiceに渡す
        UserCustomerDto customerDto = customerForm.toDto();
        log.debug("Saving order to database: cartItems={}, customer={}",
                cartItems.size(), customerDto.getName());
        UserOrderDto orderDto = userOrderService.saveOrderToDatabase(cartItems, customerDto);

        log.debug("Order save result: {}", orderDto != null ? "success" : "failed");
        if (orderDto != null) {
            // 注文情報をセッションに保存
            sessionOrder.setOrder(orderDto, session);

            // カートをクリア
            sessionCart.clearCart(session);

            log.debug("Order confirmed successfully: orderNumber={}", orderDto.getOrderNumber());
            redirectAttributes.addFlashAttribute("orderNumber", orderDto.getOrderNumber());
            log.debug("Redirecting to order complete page with orderNumber: {}", orderDto.getOrderNumber());
            return "redirect:/order/complete";
        } else {
            log.warn("Failed to confirm order");
            redirectAttributes.addFlashAttribute("error", "注文の確定に失敗しました");
            return "redirect:/order/checkout";
        }
    }

    /**
     * 注文完了画面表示
     * 
     * @param model Model
     * @return 注文完了画面
     */
    @GetMapping("/complete")
    public String orderComplete(Model model) {
        // FlashAttributeから注文番号を取得
        Object orderNumberObj = model.getAttribute("orderNumber");
        String orderNumber = orderNumberObj != null ? orderNumberObj.toString() : null;
        log.debug("Order complete page requested: orderNumber={}", orderNumber);

        if (orderNumber == null || orderNumber.trim().isEmpty()) {
            log.warn("Order number is missing, redirecting to product list");
            return "redirect:/";
        }

        UserOrderDto orderDto = userOrderService.getOrderByOrderNumber(orderNumber);
        log.debug("Order lookup result: {}", orderDto != null ? "found" : "not found");
        if (orderDto == null) {
            log.warn("Order not found: orderNumber={}, redirecting to product list", orderNumber);
            return "redirect:/";
        }

        model.addAttribute("orderDto", orderDto);
        log.debug("Order complete page rendered successfully");
        return "user/order-complete";
    }
}