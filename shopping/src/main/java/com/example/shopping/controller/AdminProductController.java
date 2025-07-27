package com.example.shopping.controller;

import com.example.shopping.model.dto.AdminProductDto;
import com.example.shopping.model.form.AdminProductForm;
import com.example.shopping.model.form.AdminProductListForm;
import com.example.shopping.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * 管理者商品Controller
 */
@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
@Slf4j
public class AdminProductController {

    private final AdminProductService adminProductService;

    /**
     * 商品一覧画面表示
     * 
     * @param searchName     商品名検索
     * @param searchCategory カテゴリ検索
     * @param page           ページ番号
     * @param model          Model
     * @return 商品一覧画面
     */
    @GetMapping
    public String productList(@RequestParam(value = "searchName", required = false) String searchName,
            @RequestParam(value = "searchCategory", required = false) String searchCategory,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {
        log.debug("Admin product list requested: searchName={}, searchCategory={}, page={}",
                searchName, searchCategory, page);

        Pageable pageable = PageRequest.of(page, 10); // 1ページ10件
        AdminProductListForm productListForm = adminProductService.getProductList(searchName, searchCategory, pageable);

        model.addAttribute("productListForm", productListForm);
        return "admin/product-list";
    }

    /**
     * 商品新規作成画面表示
     * 
     * @param model Model
     * @return 商品作成画面
     */
    @GetMapping("/new")
    public String productNew(Model model) {
        log.debug("Admin product new page requested");

        AdminProductForm productForm = new AdminProductForm();
        model.addAttribute("productForm", productForm);
        model.addAttribute("categories", adminProductService.getAvailableCategories());

        return "admin/product-form";
    }

    /**
     * 商品編集画面表示
     * 
     * @param productId 商品ID
     * @param model     Model
     * @return 商品編集画面
     */
    @GetMapping("/{productId}/edit")
    public String productEdit(@PathVariable Long productId, Model model) {
        log.debug("Admin product edit page requested: productId={}", productId);

        AdminProductDto productDto = adminProductService.getProductById(productId);
        if (productDto == null) {
            log.warn("Product not found: productId={}", productId);
            return "redirect:/admin/products";
        }

        AdminProductForm productForm = AdminProductForm.fromDto(productDto);
        model.addAttribute("productForm", productForm);
        model.addAttribute("categories", adminProductService.getAvailableCategories());

        return "admin/product-form";
    }

    /**
     * 商品保存処理
     * 
     * @param productForm        商品Form
     * @param redirectAttributes RedirectAttributes
     * @return 商品一覧画面にリダイレクト
     */
    @PostMapping("/save")
    public String productSave(@ModelAttribute AdminProductForm productForm,
            RedirectAttributes redirectAttributes) {
        log.debug("Admin product save requested: id={}, name={}",
                productForm.getId(), productForm.getName());

        AdminProductDto productDto = productForm.toDto();
        boolean success = adminProductService.saveProduct(productDto);

        if (success) {
            if (productForm.isNew()) {
                redirectAttributes.addFlashAttribute("message", "商品を登録しました");
            } else {
                redirectAttributes.addFlashAttribute("message", "商品を更新しました");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "商品の保存に失敗しました");
        }

        return "redirect:/admin/products";
    }

    /**
     * 商品削除確認画面表示
     * 
     * @param productId 商品ID
     * @param model     Model
     * @return 商品削除確認画面
     */
    @GetMapping("/{productId}/delete")
    public String productDeleteConfirm(@PathVariable Long productId, Model model) {
        log.debug("Admin product delete confirmation requested: productId={}", productId);

        AdminProductDto productDto = adminProductService.getProductById(productId);
        if (productDto == null) {
            log.warn("Product not found: productId={}", productId);
            return "redirect:/admin/products";
        }

        AdminProductForm productForm = AdminProductForm.fromDto(productDto);
        model.addAttribute("productForm", productForm);
        return "admin/product-delete-confirm";
    }

    /**
     * 商品削除処理
     * 
     * @param productId          商品ID
     * @param redirectAttributes RedirectAttributes
     * @return 商品一覧画面にリダイレクト
     */
    @PostMapping("/{productId}/delete")
    public String productDelete(@PathVariable Long productId,
            RedirectAttributes redirectAttributes) {
        log.debug("Admin product delete requested: productId={}", productId);

        boolean success = adminProductService.deleteProduct(productId);

        if (success) {
            redirectAttributes.addFlashAttribute("message", "商品を削除しました");
        } else {
            redirectAttributes.addFlashAttribute("error", "商品の削除に失敗しました");
        }

        return "redirect:/admin/products";
    }

    /**
     * 在庫不足商品一覧表示
     * 
     * @param threshold 在庫閾値
     * @param model     Model
     * @return 在庫不足商品一覧画面
     */
    @GetMapping("/low-stock")
    public String lowStockProducts(@RequestParam(value = "threshold", defaultValue = "10") Integer threshold,
            Model model) {
        log.debug("Admin low stock products requested: threshold={}", threshold);

        List<AdminProductDto> lowStockProducts = adminProductService
                .getLowStockProducts(threshold);

        model.addAttribute("lowStockProducts", lowStockProducts);
        model.addAttribute("threshold", threshold);

        return "admin/low-stock-products";
    }
}