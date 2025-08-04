package com.example.shopping.controller;

import com.example.shopping.common.FileUploadService;
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
import jakarta.annotation.PostConstruct;

/**
 * 管理者商品Controller
 */
@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
@Slf4j
public class AdminProductController {

    private final AdminProductService adminProductService;
    private final FileUploadService fileUploadService;

    @PostConstruct
    public void init() {
        log.debug("AdminProductController initialized");
    }

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
            @RequestParam(value = "t", required = false) String timestamp,
            Model model) {
        log.debug("Admin product list requested: searchName={}, searchCategory={}, page={}",
                searchName, searchCategory, page);

        try {
            Pageable pageable = PageRequest.of(page, 10); // 1ページ10件
            AdminProductListForm productListForm = adminProductService.getProductList(searchName, searchCategory,
                    pageable);

            model.addAttribute("productListForm", productListForm);

            if (timestamp != null) {
                model.addAttribute("cacheBustingTimestamp", timestamp);
            }

            return "admin/product-list";
        } catch (Exception e) {
            log.error("Error in productList: {}", e.getMessage(), e);
            model.addAttribute("productListForm", new AdminProductListForm());
            return "admin/product-list";
        }
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

        try {
            // 既存の商品情報を取得（更新時のみ）
            String oldImagePath = null;
            if (productForm.getId() != null) {
                AdminProductDto existingProduct = adminProductService.getProductById(productForm.getId());
                if (existingProduct != null) {
                    oldImagePath = existingProduct.getImagePath();
                }
            }

            // ファイルアップロード処理
            if (productForm.getImageFile() != null && !productForm.getImageFile().isEmpty()) {
                String uploadedImagePath = fileUploadService.uploadFile(productForm.getImageFile());
                productForm.setImagePath(uploadedImagePath);
                log.debug("画像がアップロードされました: {}", uploadedImagePath);
            }

            AdminProductDto productDto = productForm.toDto();
            log.debug("保存前のDTO - id: {}, name: {}, imagePath: {}",
                    productDto.getId(), productDto.getName(), productDto.getImagePath());
            boolean success = adminProductService.saveProduct(productDto);

            if (success) {
                // 商品保存成功後、古い画像ファイルを削除
                if (oldImagePath != null && !oldImagePath.isEmpty() &&
                        productForm.getImageFile() != null && !productForm.getImageFile().isEmpty()) {
                    fileUploadService.deleteFile(oldImagePath);
                    log.debug("古い画像ファイルを削除しました: {}", oldImagePath);
                }

                if (productForm.isNew()) {
                    redirectAttributes.addFlashAttribute("message", "商品を登録しました");
                } else {
                    redirectAttributes.addFlashAttribute("message", "商品を更新しました");
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "商品の保存に失敗しました");
            }
        } catch (Exception e) {
            log.error("商品保存中にエラーが発生しました: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "商品の保存に失敗しました: " + e.getMessage());
        }

        // キャッシュバスティング用のタイムスタンプを追加
        return "redirect:/admin/products?t=" + System.currentTimeMillis();
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

        try {
            // 商品情報を取得して画像パスを確認
            AdminProductDto productDto = adminProductService.getProductById(productId);
            String imagePath = null;
            if (productDto != null) {
                imagePath = productDto.getImagePath();
            }

            boolean success = adminProductService.deleteProduct(productId);

            if (success) {
                // 商品削除成功後、画像ファイルも削除
                if (imagePath != null && !imagePath.isEmpty()) {
                    fileUploadService.deleteFile(imagePath);
                }
                redirectAttributes.addFlashAttribute("message", "商品を削除しました");
            } else {
                redirectAttributes.addFlashAttribute("error", "商品の削除に失敗しました");
            }
        } catch (Exception e) {
            log.error("商品削除中にエラーが発生しました: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "商品の削除に失敗しました: " + e.getMessage());
        }

        // キャッシュバスティング用のタイムスタンプを追加
        return "redirect:/admin/products?t=" + System.currentTimeMillis();
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