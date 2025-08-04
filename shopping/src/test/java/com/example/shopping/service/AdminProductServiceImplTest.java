package com.example.shopping.service;

import com.example.shopping.model.dto.AdminProductDto;
import com.example.shopping.model.entity.Product;
import com.example.shopping.model.form.AdminProductListForm;
import com.example.shopping.repository.AdminProductRepository;
import com.example.shopping.service.impl.AdminProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 管理者商品サービスのテストクラス
 */
@ExtendWith(MockitoExtension.class)
class AdminProductServiceImplTest {

    @Mock
    private AdminProductRepository adminProductRepository;

    @InjectMocks
    private AdminProductServiceImpl adminProductService;

    private Product testProduct1;
    private Product testProduct2;
    private Product testProduct3;

    @BeforeEach
    void setUp() {
        // テスト用の商品1
        testProduct1 = new Product();
        testProduct1.setId(1L);
        testProduct1.setName("テスト商品1");
        testProduct1.setDescription("テスト商品1の説明");
        testProduct1.setPrice(new BigDecimal("1000"));
        testProduct1.setStockQuantity(10);
        testProduct1.setCategory("電子機器");
        testProduct1.setCreatedAt(LocalDateTime.now());
        testProduct1.setUpdatedAt(LocalDateTime.now());

        // テスト用の商品2
        testProduct2 = new Product();
        testProduct2.setId(2L);
        testProduct2.setName("テスト商品2");
        testProduct2.setDescription("テスト商品2の説明");
        testProduct2.setPrice(new BigDecimal("2000"));
        testProduct2.setStockQuantity(5);
        testProduct2.setCategory("電子機器");
        testProduct2.setCreatedAt(LocalDateTime.now());
        testProduct2.setUpdatedAt(LocalDateTime.now());

        // テスト用の商品3
        testProduct3 = new Product();
        testProduct3.setId(3L);
        testProduct3.setName("別カテゴリ商品");
        testProduct3.setDescription("別カテゴリ商品の説明");
        testProduct3.setPrice(new BigDecimal("1500"));
        testProduct3.setStockQuantity(15);
        testProduct3.setCategory("衣類");
        testProduct3.setCreatedAt(LocalDateTime.now());
        testProduct3.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void UT_Service_0018_getProductList_正常系() {
        // 検索条件なしで全商品取得
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> productList = Arrays.asList(testProduct1, testProduct2, testProduct3);
        Page<Product> productPage = new PageImpl<>(productList, pageable, 3);

        when(adminProductRepository.findAllByOrderByIdDesc(pageable))
                .thenReturn(productPage);

        AdminProductListForm result = adminProductService.getProductList(null, null, pageable);

        assertNotNull(result);
        assertEquals(3, result.getProducts().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(3, result.getTotalElements());
        assertFalse(result.isHasNext());
        assertFalse(result.isHasPrevious());
        assertEquals(0, result.getCurrentPage());
        assertNull(result.getSearchName());
        assertNull(result.getSearchCategory());
        verify(adminProductRepository).findAllByOrderByIdDesc(pageable);
    }

    @Test
    void UT_Service_0019_getProductList_正常系() {
        // 商品名で検索
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> productList = Arrays.asList(testProduct1, testProduct2);
        Page<Product> productPage = new PageImpl<>(productList, pageable, 2);

        when(adminProductRepository.findByNameContainingIgnoreCaseOrderByIdDesc("テスト商品", pageable))
                .thenReturn(productPage);

        AdminProductListForm result = adminProductService.getProductList("テスト商品", null, pageable);

        assertNotNull(result);
        assertEquals(2, result.getProducts().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getTotalElements());
        assertFalse(result.isHasNext());
        assertFalse(result.isHasPrevious());
        assertEquals(0, result.getCurrentPage());
        assertEquals("テスト商品", result.getSearchName());
        assertNull(result.getSearchCategory());

        // 検索結果の商品名を確認
        List<AdminProductDto> products = result.getProducts();
        assertTrue(products.stream().allMatch(p -> p.getName().contains("テスト商品")));
        verify(adminProductRepository).findByNameContainingIgnoreCaseOrderByIdDesc("テスト商品", pageable);
    }

    @Test
    void UT_Service_0020_getProductList_正常系() {
        // カテゴリで検索
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> productList = Arrays.asList(testProduct1, testProduct2);
        Page<Product> productPage = new PageImpl<>(productList, pageable, 2);

        when(adminProductRepository.findByCategoryOrderByIdDesc("電子機器", pageable))
                .thenReturn(productPage);

        AdminProductListForm result = adminProductService.getProductList(null, "電子機器", pageable);

        assertNotNull(result);
        assertEquals(2, result.getProducts().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getTotalElements());
        assertFalse(result.isHasNext());
        assertFalse(result.isHasPrevious());
        assertEquals(0, result.getCurrentPage());
        assertNull(result.getSearchName());
        assertEquals("電子機器", result.getSearchCategory());

        // 検索結果のカテゴリを確認
        List<AdminProductDto> products = result.getProducts();
        assertTrue(products.stream().allMatch(p -> "電子機器".equals(p.getCategory())));
        verify(adminProductRepository).findByCategoryOrderByIdDesc("電子機器", pageable);
    }

    @Test
    void UT_Service_0021_getProductList_正常系() {
        // 商品名とカテゴリで検索
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> productList = Arrays.asList(testProduct1);
        Page<Product> productPage = new PageImpl<>(productList, pageable, 1);

        when(adminProductRepository.findByNameContainingIgnoreCaseAndCategoryOrderByIdDesc("テスト", "電子機器", pageable))
                .thenReturn(productPage);

        AdminProductListForm result = adminProductService.getProductList("テスト", "電子機器", pageable);

        assertNotNull(result);
        assertEquals(1, result.getProducts().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getTotalElements());
        assertFalse(result.isHasNext());
        assertFalse(result.isHasPrevious());
        assertEquals(0, result.getCurrentPage());
        assertEquals("テスト", result.getSearchName());
        assertEquals("電子機器", result.getSearchCategory());

        // 検索結果の商品名とカテゴリを確認
        List<AdminProductDto> products = result.getProducts();
        assertTrue(products.stream().allMatch(p -> p.getName().contains("テスト") && "電子機器".equals(p.getCategory())));
        verify(adminProductRepository).findByNameContainingIgnoreCaseAndCategoryOrderByIdDesc("テスト", "電子機器", pageable);
    }

    @Test
    void UT_Service_0022_getProductList_正常系() {
        // 空文字の検索条件で検索
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> productList = Arrays.asList(testProduct1, testProduct2, testProduct3);
        Page<Product> productPage = new PageImpl<>(productList, pageable, 3);

        when(adminProductRepository.findAllByOrderByIdDesc(pageable))
                .thenReturn(productPage);

        AdminProductListForm result = adminProductService.getProductList("", "", pageable);

        assertNotNull(result);
        assertEquals(3, result.getProducts().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(3, result.getTotalElements());
        assertFalse(result.isHasNext());
        assertFalse(result.isHasPrevious());
        assertEquals(0, result.getCurrentPage());
        assertEquals("", result.getSearchName());
        assertEquals("", result.getSearchCategory());
        verify(adminProductRepository).findAllByOrderByIdDesc(pageable);
    }

    @Test
    void UT_Service_0023_getProductList_正常系() {
        // 2ページ目の取得
        Pageable pageable = PageRequest.of(1, 5);
        List<Product> productList = Arrays.asList(testProduct3);
        Page<Product> productPage = new PageImpl<>(productList, pageable, 6);

        when(adminProductRepository.findAllByOrderByIdDesc(pageable))
                .thenReturn(productPage);

        AdminProductListForm result = adminProductService.getProductList(null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getProducts().size());
        assertEquals(2, result.getTotalPages());
        assertEquals(6, result.getTotalElements());
        assertFalse(result.isHasNext());
        assertTrue(result.isHasPrevious());
        assertEquals(1, result.getCurrentPage());
        assertNull(result.getSearchName());
        assertNull(result.getSearchCategory());
        verify(adminProductRepository).findAllByOrderByIdDesc(pageable);
    }

    @Test
    void UT_Service_0024_getProductById_正常系() {
        // 存在する商品IDで詳細取得
        when(adminProductRepository.findById(1L))
                .thenReturn(Optional.of(testProduct1));

        AdminProductDto result = adminProductService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("テスト商品1", result.getName());
        assertEquals("テスト商品1の説明", result.getDescription());
        assertEquals(new BigDecimal("1000"), result.getPrice());
        assertEquals(10, result.getStockQuantity());
        assertEquals("電子機器", result.getCategory());
        verify(adminProductRepository).findById(1L);
    }

    @Test
    void UT_Service_0025_getProductById_異常系() {
        // 存在しない商品IDで詳細取得失敗
        when(adminProductRepository.findById(999L))
                .thenReturn(Optional.empty());

        AdminProductDto result = adminProductService.getProductById(999L);

        assertNull(result);
        verify(adminProductRepository).findById(999L);
    }

    @Test
    void UT_Service_0026_getProductById_異常系() {
        // nullの商品IDで詳細取得失敗
        AdminProductDto result = adminProductService.getProductById(null);

        assertNull(result);
        verify(adminProductRepository).findById(null);
    }

    @Test
    void UT_Service_0027_saveProduct_正常系() {
        // 新規商品の保存
        AdminProductDto productDto = new AdminProductDto();
        productDto.setName("新商品");
        productDto.setPrice(new BigDecimal("1000"));
        productDto.setDescription("新商品の説明");
        productDto.setStockQuantity(5);
        productDto.setCategory("食品");

        when(adminProductRepository.save(any(Product.class)))
                .thenReturn(testProduct1);

        boolean result = adminProductService.saveProduct(productDto);

        assertTrue(result);
        verify(adminProductRepository).save(any(Product.class));
    }

    @Test
    void UT_Service_0028_saveProduct_正常系() {
        // 既存商品の更新
        AdminProductDto productDto = new AdminProductDto();
        productDto.setId(1L);
        productDto.setName("更新商品");
        productDto.setPrice(new BigDecimal("2000"));
        productDto.setDescription("更新商品の説明");
        productDto.setStockQuantity(15);
        productDto.setCategory("衣類");

        when(adminProductRepository.save(any(Product.class)))
                .thenReturn(testProduct1);

        boolean result = adminProductService.saveProduct(productDto);

        assertTrue(result);
        verify(adminProductRepository).save(any(Product.class));
    }

    @Test
    void UT_Service_0029_saveProduct_異常系() {
        // 無効な商品データで保存失敗
        // nullの場合はNullPointerExceptionが発生するため、例外を期待
        assertThrows(NullPointerException.class, () -> {
            adminProductService.saveProduct(null);
        });
        verify(adminProductRepository, never()).save(any(Product.class));
    }

    @Test
    void UT_Service_0030_saveProduct_異常系() {
        // 商品名が空で保存失敗
        AdminProductDto productDto = new AdminProductDto();
        productDto.setName("");
        productDto.setPrice(new BigDecimal("1000"));

        when(adminProductRepository.save(any(Product.class)))
                .thenThrow(new RuntimeException("商品名が空です"));

        boolean result = adminProductService.saveProduct(productDto);

        assertFalse(result);
        verify(adminProductRepository).save(any(Product.class));
    }

    @Test
    void UT_Service_0031_saveProduct_異常系() {
        // 価格が負の値で保存失敗
        AdminProductDto productDto = new AdminProductDto();
        productDto.setName("商品");
        productDto.setPrice(new BigDecimal("-100"));

        when(adminProductRepository.save(any(Product.class)))
                .thenThrow(new RuntimeException("価格が負の値です"));

        boolean result = adminProductService.saveProduct(productDto);

        assertFalse(result);
        verify(adminProductRepository).save(any(Product.class));
    }

    @Test
    void UT_Service_0032_deleteProduct_正常系() {
        // 存在する商品の削除
        when(adminProductRepository.existsById(1L))
                .thenReturn(true);
        doNothing().when(adminProductRepository).deleteById(1L);

        boolean result = adminProductService.deleteProduct(1L);

        assertTrue(result);
        verify(adminProductRepository).existsById(1L);
        verify(adminProductRepository).deleteById(1L);
    }

    @Test
    void UT_Service_0033_deleteProduct_異常系() {
        // 存在しない商品の削除失敗
        when(adminProductRepository.existsById(999L))
                .thenReturn(false);

        boolean result = adminProductService.deleteProduct(999L);

        assertFalse(result);
        verify(adminProductRepository).existsById(999L);
        verify(adminProductRepository, never()).deleteById(any());
    }

    @Test
    void UT_Service_0034_deleteProduct_異常系() {
        // nullの商品IDで削除失敗
        when(adminProductRepository.existsById(null))
                .thenReturn(false);

        boolean result = adminProductService.deleteProduct(null);

        assertFalse(result);
        verify(adminProductRepository).existsById(null);
        verify(adminProductRepository, never()).deleteById(any());
    }

    @Test
    void UT_Service_0035_getAvailableCategories_正常系() {
        // 利用可能カテゴリ一覧取得
        List<String> categories = Arrays.asList("電子機器", "衣類");
        when(adminProductRepository.findDistinctCategories())
                .thenReturn(categories);

        List<String> result = adminProductService.getAvailableCategories();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("電子機器"));
        assertTrue(result.contains("衣類"));
        verify(adminProductRepository).findDistinctCategories();
    }

    @Test
    void UT_Service_0041_getAvailableCategories_異常系() {
        // リポジトリがnullを返す場合
        when(adminProductRepository.findDistinctCategories())
                .thenReturn(null);

        List<String> result = adminProductService.getAvailableCategories();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(adminProductRepository).findDistinctCategories();
    }

    @Test
    void UT_Service_0036_getLowStockProducts_正常系() {
        // 在庫不足商品一覧取得
        List<Product> lowStockProducts = Arrays.asList(testProduct1, testProduct2);
        when(adminProductRepository.findByStockQuantityLessThanEqualOrderByIdDesc(10))
                .thenReturn(lowStockProducts);

        List<AdminProductDto> result = adminProductService.getLowStockProducts(10);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getStockQuantity() <= 10));
        verify(adminProductRepository).findByStockQuantityLessThanEqualOrderByIdDesc(10);
    }

    @Test
    void UT_Service_0037_getLowStockProducts_正常系() {
        // 在庫不足商品なし
        when(adminProductRepository.findByStockQuantityLessThanEqualOrderByIdDesc(100))
                .thenReturn(Arrays.asList());

        List<AdminProductDto> result = adminProductService.getLowStockProducts(100);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(adminProductRepository).findByStockQuantityLessThanEqualOrderByIdDesc(100);
    }

    @Test
    void UT_Service_0038_getLowStockProducts_異常系() {
        // 負の閾値で取得失敗
        when(adminProductRepository.findByStockQuantityLessThanEqualOrderByIdDesc(-1))
                .thenReturn(Arrays.asList());

        List<AdminProductDto> result = adminProductService.getLowStockProducts(-1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(adminProductRepository).findByStockQuantityLessThanEqualOrderByIdDesc(-1);
    }

    @Test
    void UT_Service_0039_getLowStockProducts_異常系() {
        // nullの閾値で取得失敗
        when(adminProductRepository.findByStockQuantityLessThanEqualOrderByIdDesc(null))
                .thenReturn(Arrays.asList());

        List<AdminProductDto> result = adminProductService.getLowStockProducts(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(adminProductRepository).findByStockQuantityLessThanEqualOrderByIdDesc(null);
    }

    @Test
    void UT_Service_0040_getLowStockProducts_異常系() {
        // リポジトリがnullを返す場合
        when(adminProductRepository.findByStockQuantityLessThanEqualOrderByIdDesc(10))
                .thenReturn(null);

        List<AdminProductDto> result = adminProductService.getLowStockProducts(10);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(adminProductRepository).findByStockQuantityLessThanEqualOrderByIdDesc(10);
    }
}