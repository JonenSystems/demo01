package com.example.shopping.controller;

import com.example.shopping.model.dto.AdminCustomerDto;
import com.example.shopping.model.form.AdminCustomerForm;
import com.example.shopping.model.form.AdminCustomerListForm;
import com.example.shopping.service.AdminCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 管理者顧客Controller
 */
@Controller
@RequestMapping("/admin/customers")
@RequiredArgsConstructor
@Slf4j
public class AdminCustomerController {

    private final AdminCustomerService adminCustomerService;

    /**
     * 顧客一覧画面表示
     * 
     * @param searchName  顧客名検索
     * @param searchEmail メールアドレス検索
     * @param page        ページ番号
     * @param model       Model
     * @return 顧客一覧画面
     */
    @GetMapping
    public String customerList(@RequestParam(value = "searchName", required = false) String searchName,
            @RequestParam(value = "searchEmail", required = false) String searchEmail,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {
        log.debug("Admin customer list requested: searchName={}, searchEmail={}, page={}",
                searchName, searchEmail, page);

        Pageable pageable = PageRequest.of(page, 10); // 1ページ10件
        AdminCustomerListForm customerListForm = adminCustomerService.getCustomerList(searchName, searchEmail,
                pageable);

        model.addAttribute("customerListForm", customerListForm);
        return "admin/customer-list";
    }

    /**
     * 顧客新規作成画面表示
     * 
     * @param model Model
     * @return 顧客作成画面
     */
    @GetMapping("/new")
    public String customerNew(Model model) {
        log.debug("Admin customer new page requested");

        AdminCustomerForm customerForm = new AdminCustomerForm();
        model.addAttribute("customerForm", customerForm);

        return "admin/customer-form";
    }

    /**
     * 顧客編集画面表示
     * 
     * @param customerId 顧客ID
     * @param model      Model
     * @return 顧客編集画面
     */
    @GetMapping("/{customerId}/edit")
    public String customerEdit(@PathVariable Long customerId, Model model) {
        log.debug("Admin customer edit page requested: customerId={}", customerId);

        AdminCustomerDto customerDto = adminCustomerService.getCustomerById(customerId);
        if (customerDto == null) {
            log.warn("Customer not found: customerId={}", customerId);
            return "redirect:/admin/customers";
        }

        AdminCustomerForm customerForm = AdminCustomerForm.fromDto(customerDto);
        model.addAttribute("customerForm", customerForm);

        return "admin/customer-form";
    }

    /**
     * 顧客保存処理
     * 
     * @param customerForm       顧客Form
     * @param redirectAttributes RedirectAttributes
     * @return 顧客一覧画面にリダイレクト
     */
    @PostMapping("/save")
    public String customerSave(@ModelAttribute AdminCustomerForm customerForm,
            RedirectAttributes redirectAttributes) {
        log.debug("Admin customer save requested: id={}, name={}, email={}",
                customerForm.getId(), customerForm.getName(), customerForm.getEmail());

        AdminCustomerDto customerDto = customerForm.toDto();
        boolean success = adminCustomerService.saveCustomer(customerDto);

        if (success) {
            if (customerForm.isNew()) {
                redirectAttributes.addFlashAttribute("message", "顧客を登録しました");
            } else {
                redirectAttributes.addFlashAttribute("message", "顧客を更新しました");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "顧客の保存に失敗しました（メールアドレスが重複している可能性があります）");
        }

        return "redirect:/admin/customers";
    }

    /**
     * 顧客削除確認画面表示
     * 
     * @param customerId 顧客ID
     * @param model      Model
     * @return 顧客削除確認画面
     */
    @GetMapping("/{customerId}/delete")
    public String customerDeleteConfirm(@PathVariable Long customerId, Model model) {
        log.debug("Admin customer delete confirmation requested: customerId={}", customerId);

        AdminCustomerDto customerDto = adminCustomerService.getCustomerById(customerId);
        if (customerDto == null) {
            log.warn("Customer not found: customerId={}", customerId);
            return "redirect:/admin/customers";
        }

        AdminCustomerForm customerForm = AdminCustomerForm.fromDto(customerDto);
        model.addAttribute("customerForm", customerForm);
        return "admin/customer-delete-confirm";
    }

    /**
     * 顧客削除処理
     * 
     * @param customerId         顧客ID
     * @param redirectAttributes RedirectAttributes
     * @return 顧客一覧画面にリダイレクト
     */
    @PostMapping("/{customerId}/delete")
    public String customerDelete(@PathVariable Long customerId,
            RedirectAttributes redirectAttributes) {
        log.debug("Admin customer delete requested: customerId={}", customerId);

        boolean success = adminCustomerService.deleteCustomer(customerId);

        if (success) {
            redirectAttributes.addFlashAttribute("message", "顧客を削除しました");
        } else {
            redirectAttributes.addFlashAttribute("error", "顧客の削除に失敗しました");
        }

        return "redirect:/admin/customers";
    }
}